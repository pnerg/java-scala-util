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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javascalautils.Failure;
import javascalautils.Option;
import javascalautils.Success;
import javascalautils.Try;

/**
 * The future implementation.
 * 
 * @author Peter Nerg
 * @since 1.2
 */
final class FutureImpl<T> implements Future<T> {
    /**
     * Contains either a {@link Success} as a result of {@link #success(Object)} or a {@link Failure} as a result of {@link #failure(Throwable)}
     */
    private Option<Try<T>> response = None();

    /** The success handlers set by the user. */
    private final List<EventHandler<T>> successHandlers = new ArrayList<>();

    /** The failure handlers set by the user. */
    private final List<EventHandler<Throwable>> failureHandlers = new ArrayList<>();

    @Override
    public boolean isCompleted() {
        return response.isDefined();
    }

    @Override
    public Option<Try<T>> value() {
        return response;
    }

    @Override
    public void onFailure(Consumer<Throwable> c) {
        failureHandlers.add(new EventHandler<>(c));
        response.filter(Try::isFailure).map(Try::failed).map(Try::orNull).forEach(t -> invokeFailureHandlers(t));
    }

    @Override
    public void onSuccess(Consumer<T> c) {
        successHandlers.add(new EventHandler<>(c));
        response.filter(Try::isSuccess).map(Try::orNull).forEach(r -> invokeSucessHandlers(r));
    }

    /**
     * Used to report a success to this future.
     * 
     * @param value
     *            The response value
     */
    void success(final T value) {
        this.response = Option.apply(new Success<>(value));
        this.invokeSucessHandlers(value);
    }

    /**
     * Used to report a failure to this future.
     * 
     * @param throwable
     *            The failure Throwable
     */
    void failure(Throwable throwable) {
        this.response = Option.apply(new Failure<>(throwable));
        this.invokeFailureHandlers(throwable);
    }

    /**
     * Report the stored {@link #value} to the stored {@link #successHandler}.<br>
     * This will only report once irrespective on how many times the method is invoked.
     * 
     * @param response
     *            The response to report
     */
    private void invokeSucessHandlers(T response) {
        // The filter is to make sure we only respond/notify once
        successHandlers.stream().filter(h -> !h.notified()).forEach(h -> h.notify(response));
    }

    /**
     * Report the stored {@link #throwable} to the stored {@link #failureHandler}.<br>
     * This will only report once irrespective on how many times the method is invoked.
     * 
     * @param throwable
     *            The throwable to report
     */
    private void invokeFailureHandlers(Throwable throwable) {
        // The filter is to make sure we only respond/notify once
        failureHandlers.stream().filter(h -> !h.notified()).forEach(h -> h.notify(throwable));
    }

    /**
     * Internal holder for the success/failure handlers provided by the user.<br>
     * Used primarily to keep track on if a particular handler already has been notified. <br>
     * This is to make sure the same handler won't be notified more than once.
     * 
     * @author Peter Nerg
     *
     * @param <R>
     */
    private static final class EventHandler<R> {
        private final Consumer<R> consumer;
        private boolean notified = false;

        private EventHandler(Consumer<R> consumer) {
            this.consumer = consumer;
        }

        /**
         * If this event handler already has been notified.
         * 
         * @return
         */
        private boolean notified() {
            return notified;
        }

        /**
         * Notifies the response to the handler.
         * 
         * @param response
         */
        private void notify(R response) {
            notified = true;
            consumer.accept(response);
        }
    }
}
