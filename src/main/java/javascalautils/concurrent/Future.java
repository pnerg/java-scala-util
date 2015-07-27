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
import javascalautils.ThrowableFunction0;
import javascalautils.Try;

/**
 * A Future that will hold the result of an asynchronous computation. <br>
 * The Future is paired with an instance of {@link Promise} which is also the way to get hold of a Future. <br>
 * One can see a Future as a holder for a value-to-be, not yet available but accessible sometime in the future. <br>
 * The preferred way to get hold of the value-to-be is to register a listener on any of the provided listener types since this allows for asynchronous
 * non-blocking operations.
 * <ul>
 * <li>{@link #onComplete(Consumer)}</li>
 * <li>{@link #onSuccess(Consumer)}</li>
 * <li>{@link #onFailure(Consumer)}</li>
 * </ul>
 * It is possible to register multiple listeners to several/same listener type. <br>
 * One can register a listener both before and after a Future has been completed. <br>
 * Should the Future already be completed when the listener is added it is fired immediately. <br>
 * The guarantee is that a listener is only fired once. <br>
 * <br>
 * All operations on this class are non-blocking, i.e. allowing for proper asynchronous/non-blocking programming patterns. <br>
 * With one exception, for situations where it is necessary to maintain synchronous/blocking behavior the method {@link #result(long, TimeUnit)} is provided. <br>
 * <br>
 * For real short and consistent programming one can use the {@link #apply(ThrowableFunction0)} method to provide a function that will be executed in the
 * future.<br>
 * The result of the function will be reported to the Future returned by the method.
 * 
 * <blockquote>
 * 
 * <pre>
 * Future&lt;Integer&gt; resultSuccess = Future.apply(() -&gt; 9 / 3); // The Future will at some point contain: Success(3)
 * Future&lt;Integer&gt; resultFailure = Future.apply(() -&gt; 9 / 0); // The Future will at some point contain: Failure(ArithmeticException)
 * </pre>
 * 
 * </blockquote>
 * 
 * 
 * @author Peter Nerg
 * @since 1.2
 * @param <T>
 *            The type this Future will hold as result
 */
public interface Future<T> {

    /**
     * Allows for easy creation of asynchronous computations that will be executed in the future. <br>
     * The method will use the {@link Executors#getDefault()} method to get hold of the default {@link Executor} to use for executing the provided job. <br>
     * Simple examples:
     * 
     * <blockquote>
     * 
     * <pre>
     * Future&lt;Integer&gt; resultSuccess = Future.apply(() -&gt; 9 / 3); // The Future will at some point contain: Success(3)
     * Future&lt;Integer&gt; resultFailure = Future.apply(() -&gt; 9 / 0); // The Future will at some point contain: Failure(ArithmeticException)
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the Future
     * @param function
     *            The function to render either the value <i>T</i> or raise an exception.
     * @return The future that will hold the result provided by the function
     * @since 1.3
     */
    static <T> Future<T> apply(ThrowableFunction0<T> function) {
        return Executors.getDefault().execute(promise -> {
            try {
                promise.success(function.apply());
            } catch (Throwable t) {
                promise.failure(t);
            }
        });
    }

    /**
     * Check if this Future is completed, with a value or an exception.
     * 
     * @return <code>true</code> if completed, <code>false</code> otherwise.
     */
    boolean isCompleted();

    /**
     * The current (completed or not) value of the future. <br>
     * This is a non-blocking method, it will return the current state/value of the Future. <br>
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
     * If the Future has already been completed the notification will happen in the current thread. <br>
     * Multiple handlers can be registered, without any guarantee of notification order. <br>
     * Each individual event handler will only be invoked once. <br>
     * 
     * @param failureHandler
     *            Consumer to invoke.
     */
    void onFailure(Consumer<Throwable> failureHandler);

    /**
     * Register a handler to be invoked if the Future gets completed with a value. <br>
     * If the Future has already been completed the notification will happen in the current thread. <br>
     * Multiple handlers can be registered, without any guarantee of notification order. <br>
     * Each individual event handler will only be invoked once. <br>
     * 
     * @param successHandler
     *            Consumer to invoke.
     */
    void onSuccess(Consumer<T> successHandler);

    /**
     * Register a handler to be invoked if the Future gets completed with a value or a failure. <br>
     * If the Future has already been completed the notification will happen in the current thread. <br>
     * Multiple handlers can be registered, without any guarantee of notification order. <br>
     * Each individual event handler will only be invoked once. <br>
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
     * Creates a new {@link Future} that will hold the mapped successful value of this instance once it is completed. <br>
     * Unsuccessful Futures will not be mapped, they are kept unsuccessful as they are.
     * 
     * @param <R>
     *            The type for the value held by the mapped future
     * @param function
     *            The function to apply
     * @return The mapped Future
     */
    <R> Future<R> flatMap(Function<T, Future<R>> function);

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
     * Creates a new {@link Future} that will hold the mapped successful value of this instance once it is completed. <br>
     * Successful futures are mapped with the <i>onSuccess</i> function and unsuccessful/failure futures are mapped with <i>onFailure</i>.
     * 
     * @param <R>
     *            The type for the value held by the mapped future
     * @param onSuccess
     *            The function to apply on a 'successful' result
     * @param onFailure
     *            The function to apply on a 'failure' result
     * @return The mapped Future
     * @since 1.3
     */
    <R> Future<R> transform(Function<T, R> onSuccess, Function<Throwable, Throwable> onFailure);

    /**
     * Creates a new {@link Future} that in case <i>this</i> {@link Future} is a 'failure' will apply the function to recover the 'failure' to a 'success'. <br>
     * Should <i>this</i> {@link Future} be a 'success' the value is propagated as-is. <br>
     * E.g.
     * 
     * <blockquote>
     * 
     * <pre>
     * Future&lt;String&gt; future = ...
     * Future&lt;String&gt; recovered = future.recover(t -&gt; t.getMessage());
     * </pre>
     * 
     * </blockquote>
     * 
     * In case of 'future' being successful then that value is passed on to 'recovered', in case of failure then the recover function kicks in and returns the
     * message from the throwable.
     * 
     * 
     * @param recoverFunction
     *            The function to apply in case of a 'failure'
     * @return The recovered Future
     * @since 1.3
     */
    Future<T> recover(Function<Throwable, T> recoverFunction);

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
