/**
 * Copyright 2015 Peter Nerg
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javascalautils.concurrent;

import static javascalautils.Option.None;
import static javascalautils.OptionCompanion.Some;
import static javascalautils.TryCompanion.Failure;
import static javascalautils.TryCompanion.Success;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javascalautils.Option;
import javascalautils.Try;
import javascalautils.Validator;

/**
 * The future implementation.
 * 
 * @author Peter Nerg
 * @since 1.2
 */
final class FutureImpl<T> implements Future<T> {
    /** Will contain the result once {@link #complete(Try)} is invoked. */
    private Option<Try<T>> response = None();

    /** The success/failure/complete handlers set by the user. */
    private final List<EventHandler> eventHandlers = new ArrayList<>();

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#isCompleted()
     */
    @Override
    public boolean isCompleted() {
        return response.isDefined();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#value()
     */
    @Override
    public Option<Try<T>> value() {
        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#onFailure(java.util.function.Consumer)
     */
    @Override
    public void onFailure(Consumer<Throwable> consumer) {
        Validator.requireNonNull(consumer, "Null is not a valid consumer");
        // register a complete handler and ignore any Success responses
        onComplete(result -> {
            // transform Failure to a Success with the Throwable
            // should it be a Success it will be transformed into a Failure and forEach will do nothing
            result.failed().forEach(consumer);
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#onSuccess(java.util.function.Consumer)
     */
    @Override
    public void onSuccess(Consumer<T> consumer) {
        Validator.requireNonNull(consumer, "Null is not a valid consumer");
        // register a complete handler and ignore any Failure responses
        onComplete(result -> {
            // should it be a Failure the forEach will do nothing
            result.forEach(consumer);
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#onComplete(java.util.function.Consumer)
     */
    @Override
    public void onComplete(Consumer<Try<T>> c) {
        Validator.requireNonNull(c, "Null is not a valid consumer");
        eventHandlers.add(new EventHandler(c));
        // invoke all new handlers in case this Future is already completed
        notifyHandlers();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#forEach(java.util.function.Consumer)
     */
    @Override
    public void forEach(Consumer<T> c) {
        onSuccess(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#map(java.util.function.Function)
     */
    @Override
    public <R> Future<R> map(Function<T, R> function) {
        // the onFailure function just passed the error as-is without transformation
        return transform(function, t -> t);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#flatMap(java.util.function.Function)
     */
    @Override
    public <R> Future<R> flatMap(Function<T, Future<R>> function) {
        Validator.requireNonNull(function, "Null is not a valid function");
        // Create new future expected to hold the value of the mapped type
        FutureImpl<R> future = new FutureImpl<>();

        // install success handler that will map the result before applying it
        onSuccess(value -> {
            // use the provided function to create a mapped future
            Future<R> mapped = function.apply(value);
            // install success/failure handlers on the mapped future to bridge between this instance
            // and the one created a few lines above
            mapped.onComplete(t -> future.complete(t));
        });

        // install failure handler that just passes the result through
        onFailure(t -> future.failure(t));

        return future;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#filter(java.util.function.Predicate)
     */
    @Override
    public Future<T> filter(Predicate<T> predicate) {
        Validator.requireNonNull(predicate, "Null is not a valid predicate");
        // Create new future expected to hold the value of the mapped type
        FutureImpl<T> future = new FutureImpl<>();
        // install success handler that will filter the result before applying it
        onSuccess(value -> {
            if (predicate.test(value)) {
                future.success(value);
            } else {
                future.failure(new NoSuchElementException("The predicate failed on value [" + value + "]"));
            }
        });
        // install failure handler that just passes the result through
        onFailure(t -> future.failure(t));
        return future;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#transform(java.util.function.Function, java.util.function.Function)
     */
    @Override
    public <R> Future<R> transform(Function<T, R> onSuccess, Function<Throwable, Throwable> onFailure) {
        Validator.requireNonNull(onSuccess, "Null is not a valid function");
        Validator.requireNonNull(onFailure, "Null is not a valid function");
        // Create new future expected to hold the value of the mapped type
        FutureImpl<R> future = new FutureImpl<>();
        // install success handler that will map the result before applying it
        onSuccess(value -> future.success(onSuccess.apply(value)));
        // install failure handler that will map the error before applying it
        onFailure(t -> future.failure(onFailure.apply(t)));
        return future;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#recover(java.util.function.Function)
     */
    @Override
    public Future<T> recover(Function<Throwable, T> recoverFunction) {
        Validator.requireNonNull(recoverFunction, "Null is not a valid function");
        // Create new future expected to hold the value of the mapped type
        FutureImpl<T> future = new FutureImpl<>();

        // installs a handler that will automatically recover the result/Try should it be needed
        onComplete(t -> future.complete(t.recover(recoverFunction)));

        return future;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javascalautils.concurrent.Future#result(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public T result(long duration, TimeUnit timeUnit) throws Throwable, TimeoutException {
        Validator.requireNonNull(timeUnit, "Null is not a valid time unit");
        CountDownLatch latch = new CountDownLatch(1);

        // install a handler that releases the count down latch when notified with a result.
        // the actual result/response is of no interest as we anyways have access to it internally in this class/instance.
        onComplete(t -> latch.countDown());

        // block for either the time to pass or the Future gets completed
        if (!latch.await(duration, timeUnit)) {
            throw new TimeoutException("Timeout waiting for Future to complete");
        }

        // The response is now set to Some
        // return the value of the response (Try), should it be a Failure the exception is raised
        return response.get().get();
    }

    /**
     * Returns a String representation of the instance.
     * 
     * @since 1.2
     */
    @Override
    public String toString() {
        return "Future:" + response;
    }

    /**
     * Completes this Future with a result. <br>
     * Invoked by the Promise owning this instance.
     * 
     * @param result
     */
    void complete(Try<T> result) {
        // save the result
        this.response = Some(result);

        // notify all potential handlers
        notifyHandlers();
    }

    /**
     * Internal report success to this future. <br>
     *
     * @param value
     *            The response value
     */
    private void success(T value) {
        complete(Success(value));
    }

    /**
     * Internal report failure to this future. <br>
     *
     * @param throwable
     *            The failure Throwable
     */
    private void failure(Throwable throwable) {
        complete(Failure(throwable));
    }

    /**
     * Invoke all handlers with the value of this Future. <br>
     * The response may or may not exist at this point. <br>
     * A filter is applied to make sure we only notify handlers that have not been notified before.
     * 
     * @param value
     *            The value/result to notify
     */
    private <R> void notifyHandlers() {
        // the response may or may not exist at this point
        // The filter is to make sure we only respond/notify once
        response.forEach(t -> eventHandlers.stream().filter(h -> !h.notified()).forEach(h -> h.notify(t)));
    }

    /**
     * Internal holder for the success/failure/complete handlers provided by the user. <br>
     * Used primarily to keep track on if a particular handler already has been notified. <br>
     * This is to make sure the same handler won't be notified more than once.
     * 
     * @author Peter Nerg
     *
     * @param <R>
     */
    private final class EventHandler {
        private final AtomicBoolean notified = new AtomicBoolean(false);
        private final Consumer<Try<T>> consumer;

        private EventHandler(Consumer<Try<T>> consumer) {
            this.consumer = consumer;
        }

        /**
         * If this event handler already has been notified.
         * 
         * @return
         */
        private boolean notified() {
            return notified.get();
        }

        /**
         * Notifies the response to the handler.
         * 
         * @param response
         */
        private void notify(Try<T> response) {
            if (notified.compareAndSet(false, true)) {
                consumer.accept(response);
            }
        }
    }
}
