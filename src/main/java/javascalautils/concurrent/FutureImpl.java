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

import java.util.concurrent.atomic.AtomicBoolean;
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
    /** If we have responded to either the success or failure handler. Used to make sure we only report the response once. */
    private final AtomicBoolean responded = new AtomicBoolean(false);

    /**
     * Contains either a {@link Success} as a result of {@link #success(Object)} or a {@link Failure} as a result of {@link #failure(Throwable)}
     */
    private Option<Try<T>> response = None();

    /** The failure handler set by the user. */
    private Option<Consumer<Throwable>> failureHandler = None();

    /** The success handler set by the user. */
    private Option<Consumer<T>> successHandler = None();

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
        this.failureHandler = Option.apply(c);
        response.filter(t -> t.isFailure()).map(t -> t.failed().orNull()).forEach(t -> invokeFailureHandler(t));
    }

    @Override
    public void onSuccess(Consumer<T> c) {
        this.successHandler = Option.apply(c);
        response.filter(t -> t.isSuccess()).map(t -> t.orNull()).forEach(r -> invokeSucessHandler(r));
    }

    /**
     * Used to report a success to this future.
     * 
     * @param value
     *            The response value
     */
    void success(final T value) {
        this.response = Option.apply(new Success<>(value));
        this.invokeSucessHandler(value);
    }

    /**
     * Used to report a failure to this future.
     * 
     * @param throwable
     *            The failure Throwable
     */
    void failure(Throwable throwable) {
        this.response = Option.apply(new Failure<>(throwable));
        this.invokeFailureHandler(throwable);
    }

    /**
     * Report the stored {@link #value} to the stored {@link #successHandler}.<br>
     * This will only report once irrespective on how many times the method is invoked.
     */
    private void invokeSucessHandler(T response) {
        successHandler.filter(c -> responded.compareAndSet(false, true)).forEach(c -> c.accept(response));
    }

    /**
     * Report the stored {@link #throwable} to the stored {@link #failureHandler}.<br>
     * This will only report once irrespective on how many times the method is invoked.
     */
    private void invokeFailureHandler(Throwable throwable) {
        failureHandler.filter(c -> responded.compareAndSet(false, true)).forEach(c -> c.accept(throwable));
    }
}
