/**
 * Copyright 2015 Peter Nerg
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javascalautils.concurrent;

import static javascalautils.TryCompanion.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javascalautils.*;

/**
 * A Future that will hold the result of an asynchronous computation. <br>
 * The Future is paired with an instance of {@link Promise} which is also the way to get hold of a
 * Future. <br>
 * One can see a Future as a holder for a value-to-be, not yet available but accessible sometime in
 * the future. <br>
 * The preferred way to get hold of the value-to-be is to register a listener on any of the provided
 * listener types since this allows for asynchronous non-blocking operations.
 *
 * <ul>
 *   <li>{@link #onComplete(Consumer)}
 *   <li>{@link #onSuccess(Consumer)}
 *   <li>{@link #onFailure(Consumer)}
 * </ul>
 *
 * It is possible to register multiple listeners to several/same listener type. <br>
 * One can register a listener both before and after a Future has been completed. <br>
 * Should the Future already be completed when the listener is added it is fired immediately. <br>
 * The guarantee is that a listener is only fired once. <br>
 * <br>
 * All operations on this class are non-blocking, i.e. allowing for proper asynchronous/non-blocking
 * programming patterns. <br>
 * With one exception, for situations where it is necessary to maintain synchronous/blocking
 * behavior the method {@link #result(long, TimeUnit)} is provided. <br>
 * <br>
 * For real short and consistent programming one can use the {@link #apply(ThrowableFunction0)}
 * method to provide a function that will be executed in the future.<br>
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
 * @author Peter Nerg
 * @since 1.2
 * @param <T> The type this Future will hold as result
 */
public interface Future<T> {

  /**
   * Allows for easy creation of asynchronous computations that will be executed in the future. <br>
   * The method will use the {@link Executors#getDefault()} method to get hold of the default {@link
   * Executor} to use for executing the provided job. <br>
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
   * @param <T> The type for the Future
   * @param function The function to render either the value <i>T</i> or raise an exception.
   * @return The future that will hold the result provided by the function
   * @since 1.3
   */
  static <T> Future<T> apply(ThrowableFunction0<T> function) {
    return apply(function, Executors.getDefault());
  }

  /**
   * Allows for easy creation of asynchronous computations that will be executed in the future. <br>
   * The method will use the provided {@link Executor} for executing the provided job. <br>
   * Simple examples:
   *
   * <blockquote>
   *
   * <pre>
   * Future&lt;Integer&gt; resultSuccess = Future.apply(() -&gt; 9 / 3, someExecutor); // The Future will at some point contain: Success(3)
   * Future&lt;Integer&gt; resultFailure = Future.apply(() -&gt; 9 / 0, someExecutor); // The Future will at some point contain: Failure(ArithmeticException)
   * </pre>
   *
   * </blockquote>
   *
   * @param <T> The type for the Future
   * @param function The function to render either the value <i>T</i> or raise an exception.
   * @param executor The executor to use to compute/execute the Future holding the provided function
   * @return The future that will hold the result provided by the function
   * @since 1.4
   */
  static <T> Future<T> apply(ThrowableFunction0<T> function, Executor executor) {
    return executor.execute(promise -> promise.complete(Try(function)));
  }

  /**
   * Creates a failed Future with the provided Throwable.
   *
   * @param <T> The type for the Future
   * @param throwable The throwable to complete the Future with.
   * @return The completed Future holding the provided Throwable
   * @since 1.5
   */
  static <T> Future<T> failed(Throwable throwable) {
    return fromTry(Failure(throwable));
  }

  /**
   * Creates a successful Future with the provided value.
   *
   * @param <T> The type for the Future
   * @param value The value to complete the Future with.
   * @return The completed Future holding the provided value
   * @since 1.5
   */
  static <T> Future<T> successful(T value) {
    return fromTry(Success(value));
  }

  /**
   * Creates a completed Future with the provided Try. <br>
   * The Future can therefore be either {@link Success} or {@link Failure}.
   *
   * @param <T> The type for the Future
   * @param result The {@link Success}/{@link Failure} to complete the Future with.
   * @return The completed Future holding the provided result
   * @since 1.5
   */
  static <T> Future<T> fromTry(Try<T> result) {
    return new FutureImpl<T>().complete(result);
  }

  /**
   * Turns a Stream of Futures into a single Future containing a Stream with all the results from
   * the Futures. <br>
   * Allows for easy management of multiple Futures. <br>
   * Note, should any Future in the Stream fail the entire sequence fails. <br>
   * An empty input Stream will result in a Future containing an empty result Stream. <br>
   *
   * @param <T> The type for the Stream in the resulting Future
   * @param stream The Stream with Futures
   * @return A single Future containing the Stream of results
   * @since 1.5
   */
  static <T> Future<Stream<T>> sequence(Stream<Future<T>> stream) {
    return traverse(stream, f -> f);
  }

  /**
   * Takes a Stream of values and applies the provided function to them in parallel resulting in a
   * Future containing a Stream with the mapped values. <br>
   * Can be used to run a mapping operation in parallel e.g.:
   *
   * <blockquote>
   *
   * <pre>
   * import static javascalautils.FutureCompanion.Future;
   *
   * Stream&lt;String&gt; stream = ...; // Stream with strings
   * Future&lt;Stream&lt;Integer&gt;&gt; future = Future.traverse(stream, v -&gt; Future(() -&gt; v.length()));
   * </pre>
   *
   * </blockquote>
   *
   * @param <T> The type for the input Stream
   * @param <R> The type for the Stream in the resulting Future
   * @param stream The Stream with values
   * @param function The function to be applied to all values of the Stream
   * @return A single Future containing the Stream of results
   * @since 1.5
   */
  static <T, R> Future<Stream<R>> traverse(Stream<T> stream, Function<T, Future<R>> function) {
    // map all Future<T> to Future<Stream<T>>
    Stream<Future<Stream<R>>> mappedStream =
        stream.map(v -> function.apply(v)).map(f -> f.map(v -> Stream.of(v)));

    // create the initial (complete) Future used by the reduction
    // the Future is completed with an empty stream
    // this is used as the base for the reduction, it will also be the result in case the input
    // stream was empty
    Future<Stream<R>> initial = successful(Stream.empty());

    // now it's a simple reduction of the stream
    // for each found Future<Stream<T>> we perform a flatMap with a map of the left/right Future
    // creating a new single Future
    return mappedStream.reduce(
        initial, (f1, f2) -> f1.flatMap(f1v -> f2.map(f2v -> Stream.concat(f1v, f2v))));
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
   *
   * <ul>
   *   <li>The future has not been completed -&#62; {@link None} is returned
   *   <li>The execution of the Future was successful -&#62; {@link Some} with a {@link Success}
   *       containing the value of the executed job
   *   <li>The execution failed and an exception was reported -&#62; {@link Some} with a {@link
   *       Failure} containing the Throwable
   * </ul>
   *
   * @return An {@link Option} with the result.
   */
  Option<Try<T>> value();

  /**
   * Register a handler to be invoked if the Future gets completed with an exception. <br>
   * If the Future has already been completed the notification will happen in the current thread.
   * <br>
   * Multiple handlers can be registered, without any guarantee of notification order. <br>
   * Each individual event handler will only be invoked once. <br>
   *
   * @param failureHandler Consumer to invoke.
   */
  void onFailure(Consumer<Throwable> failureHandler);

  /**
   * Register a handler to be invoked if the Future gets completed with a value. <br>
   * If the Future has already been completed the notification will happen in the current thread.
   * <br>
   * Multiple handlers can be registered, without any guarantee of notification order. <br>
   * Each individual event handler will only be invoked once. <br>
   *
   * @param successHandler Consumer to invoke.
   */
  void onSuccess(Consumer<T> successHandler);

  /**
   * Register a handler to be invoked if the Future gets completed with a value or a failure. <br>
   * If the Future has already been completed the notification will happen in the current thread.
   * <br>
   * Multiple handlers can be registered, without any guarantee of notification order. <br>
   * Each individual event handler will only be invoked once. <br>
   *
   * @param completeHandler Consumer to invoke.
   */
  void onComplete(Consumer<Try<T>> completeHandler);

  /**
   * Asynchronously processes the value in the Future once it is available. <br>
   * Only successful Futures are reported to the consumer. <br>
   * This is pretty much the same as {@link #onSuccess(Consumer)} but is here for completion keeping
   * a consistent look and feel.
   *
   * @param consumer The consumer to digest the result
   */
  void forEach(Consumer<T> consumer);

  /**
   * Creates a new {@link Future} that will hold the mapped successful value of this instance once
   * it is completed. <br>
   * Unsuccessful Futures will not be mapped, they are kept unsuccessful as they are.
   *
   * @param <R> The type for the value held by the mapped future
   * @param function The function to apply
   * @return The mapped Future
   */
  <R> Future<R> map(ThrowableFunction1<T, R> function);

  /**
   * Creates a new {@link Future} that will hold the mapped successful value of this instance once
   * it is completed. <br>
   * Unsuccessful Futures will not be mapped, they are kept unsuccessful as they are.
   *
   * @param <R> The type for the value held by the mapped future
   * @param function The function to apply
   * @return The mapped Future
   */
  <R> Future<R> flatMap(ThrowableFunction1<T, Future<R>> function);

  /**
   * Creates a new {@link Future} that will filter the successful value of this instance once it is
   * completed. <br>
   * The possible outcomes are:
   *
   * <ul>
   *   <li>This Future is successful -&#62; predicate matches -&#62; The filtered future is
   *       completed with the value.
   *   <li>This Future is successful -&#62; predicate fails -&#62; The filtered future is failed
   *       with NoSuchElementException.
   *   <li>This Future is failure -&#62; The failure is passed on to the filtered Future.
   * </ul>
   *
   * @param predicate The predicate to apply
   * @return The filtered Future
   */
  Future<T> filter(Predicate<T> predicate);

  /**
   * Creates a new {@link Future} that will hold the transformed successful value of this instance
   * once it is completed. <br>
   * Successful futures are transformed with the <i>onSuccess</i> function and unsuccessful/failure
   * futures are transformed with <i>onFailure</i>.
   *
   * @param <R> The type for the value held by the mapped future
   * @param onSuccess The function to apply on a 'successful' result
   * @param onFailure The function to apply on a 'failure' result
   * @return The mapped Future
   * @since 1.3
   */
  <R> Future<R> transform(
      ThrowableFunction1<T, R> onSuccess, ThrowableFunction1<Throwable, Throwable> onFailure);

  /**
   * Creates a new {@link Future} that in case <i>this</i> {@link Future} is a 'failure' will apply
   * the function to recover the 'failure' to a 'success'. <br>
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
   * In case of 'future' being successful then that value is passed on to 'recovered', in case of
   * failure then the recover function kicks in and returns the message from the throwable.
   *
   * @param recoverFunction The function to apply in case of a 'failure'
   * @return The recovered Future
   * @since 1.3
   */
  Future<T> recover(ThrowableFunction1<Throwable, T> recoverFunction);

  /**
   * Blocks and waits for this Future to complete. <br>
   * Returns the result of a successful Future or throws the exception in case of a failure. <br>
   * The methods blocks for at most the provided duration. <br>
   * If the Future is already completed the method returns immediately.
   *
   * @param duration The duration to block
   * @param timeUnit The unit for the duration
   * @return The result in case successful
   * @throws Throwable The error reported in case of a failure
   * @throws TimeoutException In case the waiting time is passed
   */
  T result(long duration, TimeUnit timeUnit) throws Throwable, TimeoutException;

  /**
   * Blocks and waits for this Future to complete. <br>
   * Returns the result of a successful Future or throws the exception in case of a failure. <br>
   * The methods blocks for at most the provided duration. <br>
   * If the Future is already completed the method returns immediately.
   *
   * @param duration The duration to block
   * @return The result in case successful
   * @throws Throwable The error reported in case of a failure
   * @throws TimeoutException In case the waiting time is passed
   * @since 1.8
   */
  default T result(Duration duration) throws Throwable, TimeoutException {
    Validator.requireNonNull(duration, "Null is not a valid Duration");
    return result(duration.toMillis(), TimeUnit.MILLISECONDS);
  }

  /**
   * Blocks and waits for this Future to complete. <br>
   * As opposed to {@link #result(long, TimeUnit) result} this method will not return the value or
   * throw the exception of the Future. <br>
   * The method will upon completion returns <i>this</i>. <br>
   * The purpose of the method is to provide a blocking mechanism waiting for the Future to
   * complete. <br>
   * Any action on the Future's result is then left to the developer to manage.
   *
   * @param duration The duration to block
   * @param timeUnit The unit for the duration
   * @return <i>this</i> if the Future completes within the specified time
   * @throws TimeoutException In case the waiting time is passed
   * @throws InterruptedException In case the thread gets interrupted during the wait
   * @since 1.8
   */
  Future<T> ready(long duration, TimeUnit timeUnit) throws TimeoutException, InterruptedException;

  /**
   * Blocks and waits for this Future to complete. <br>
   * As opposed to {@link #result(long, TimeUnit) result} this method will not return the value or
   * throw the exception of the Future. <br>
   * The method will upon completion returns <i>this</i>. <br>
   * The purpose of the method is to provide a blocking mechanism waiting for the Future to
   * complete. <br>
   * Any action on the Future's result is then left to the developer to manage.
   *
   * @param duration The duration to block
   * @return <i>this</i> if the Future completes within the specified time
   * @throws TimeoutException In case the waiting time is passed
   * @throws InterruptedException In case the thread gets interrupted during the wait
   * @since 1.8
   */
  default Future<T> ready(Duration duration) throws TimeoutException, InterruptedException {
    Validator.requireNonNull(duration, "Null is not a valid Duration");
    return ready(duration.toMillis(), TimeUnit.MILLISECONDS);
  }
}
