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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javascalautils.Failure;
import javascalautils.None;
import javascalautils.Option;
import javascalautils.Some;
import javascalautils.Success;
import javascalautils.Try;

/**
 * A Future that holds the result of a computation executed asynchronously.
 * 
 * @author Peter Nerg
 * @since 1.2
 * @param <T>
 *            The type this Future will hold as result
 */
public interface Future<T> {

    /**
     * Check if this Future is completed, with a value or an exception.
     * 
     * @return <code>true</code> if completed, <code>false</code> otherwise.
     */
    boolean isCompleted();

    /**
     * The value of the future. <br>
     * There are three possible outcomes:
     * <ul>
     * <li>The future has not been completed -&#62; {@link None} is returned</li>
     * <li>The execution of the Future was successful -&#62; {@link Some} with a {@link Success} containing the value of the executed job</li>
     * <li>The execution failed and an exception was reported -&#62; {@link Some} with a {@link Failure} containing the Throwable</li>
     * </ul>
     * 
     * @return An {@link Option} with the result.
     */
    Option<Try<T>> value();

    /**
     * Register a handler to be invoked if the Future gets completed with an exception. <br>
     * If the Future has already been completed the invocation will happen in the current thread. <br>
     * Multiple handlers can be registered, without any guarantee of notification order. <br>
     * Each individual Handler will only be invoked once. <br>
     * 
     * @param failureHandler
     *            Consumer to invoke.
     */
    void onFailure(Consumer<Throwable> failureHandler);

    /**
     * Register a handler to be invoked if the Future gets completed with a value. <br>
     * If the Future has already been completed the invocation will happen in the current thread. <br>
     * Multiple handlers can be registered, without any guarantee of notification order. <br>
     * Each individual Handler will only be invoked once. <br>
     * 
     * @param successHandler
     *            Consumer to invoke.
     */
    void onSuccess(Consumer<T> successHandler);

    /**
     * Register a handler to be invoked if the Future gets completed with a value or a failure. <br>
     * If the Future has already been completed the invocation will happen in the current thread. <br>
     * Multiple handlers can be registered, without any guarantee of notification order. <br>
     * Each individual Handler will only be invoked once. <br>
     * 
     * @param completeHandler
     *            Consumer to invoke.
     */
    void onComplete(Consumer<Try<T>> completeHandler);

    /**
     * Asynchronously processes the value in the Future once it is available. <br>
     * Only successful Futures are reported to the consumer. <br>
     * This is pretty much the same as {@link #onSuccess(Consumer)} but is here for completion keeping a consistent look and feel.
     * 
     * @param consumer
     *            The consumer to digest the result
     */
    void forEach(Consumer<T> consumer);

    /**
     * Creates a new {@link Future} that will hold the mapped successful value of this instance once it is completed. <br>
     * Unsuccessful Futures will not be mapped, they are kept unsuccessful as they are.
     * 
     * @param <R>
     *            The type for the value held by the mapped future
     * @param function
     *            The function to apply
     * @return The mapped Future
     */
    <R> Future<R> map(Function<T, R> function);

    /**
     * Creates a new {@link Future} that will filter the successful value of this instance once it is completed. <br>
     * The possible outcomes are:
     * <ul>
     * <li>This Future is successful -&#62; predicate matches -&#62; The filtered future is completed with the value.</li>
     * <li>This Future is successful -&#62; predicate fails -&#62; The filtered future is failed with NoSuchElementException.</li>
     * <li>This Future is failure -&#62; The failure is passed on to the filtered Future.</li>
     * </ul>
     * 
     * @param predicate
     *            The predicate to apply
     * @return The filtered Future
     */
    Future<T> filter(Predicate<T> predicate);

    /**
     * Blocks and waits for this Future to complete. <br>
     * Returns the result of a successful Future or throws the exception in case of a failure. <br>
     * The methods blocks for at most the provided duration. <br>
     * If the Future is already completed the method returns immediately.
     * 
     * @param duration
     *            The duration to block
     * @param timeUnit
     *            The unit for the duration
     * @return The result in case successful
     * @throws Throwable
     *             The error reported in case of a failure
     * @throws TimeoutException
     *             In case the waiting time is passed
     */
    T result(long duration, TimeUnit timeUnit) throws Throwable, TimeoutException;
}
