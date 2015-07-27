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
package javascalautils;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The 'Try' type represents a computation that may either result in an exception, or return a successfully computed value. <br>
 * Typical use case is situations where parallel computation takes place resulting in more than one response where it's possible that one or more computations
 * fail. <br>
 * Though it might not be desirable to raise an exception for failed computations hence the Try acts a place holder for a response that is either failed or
 * successful. <br>
 * Instances of 'Try', are either an instance of {@link Success} or {@link Failure}. <br>
 * Example of usage: <br>
 * 
 * <blockquote>
 * 
 * <pre>
 * Try&lt;SomeData&gt; getSomeData(SomeInput input) {
 *     try {
 *         // readUserFromDB throws if no user exists
 *         SomeData data = readUserFromDB(input);
 *         return new Success&lt;&gt;(data);
 *     } catch (SomeException ex) {
 *         return new Failure&lt;&gt;(ex);
 *     }
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * A more elaborate way is to provide a {@link ThrowableFunction0 checked function} provided to the {@link #apply(ThrowableFunction0)} method. <br>
 * 
 * <blockquote>
 * 
 * <pre>
 * Try&lt;Integer&gt; resultSuccess = Try.apply(() -&gt; 9 / 3); // results in Success(3)
 * Try&lt;Integer&gt; resultFailure = Try.apply(() -&gt; 9 / 0); // results in Failure(ArithmeticException)
 * </pre>
 * 
 * </blockquote>
 * 
 * Or let us re-write the first example to use the {@link #apply(ThrowableFunction0)} method. <br>
 * 
 * <blockquote>
 * 
 * <pre>
 * Try&lt;SomeData&gt; getSomeData(SomeInput input) {
 *     // readUserFromDB throws if no user exists
 *     Try.apply(() -&gt; readUserFromDB(input));
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Peter Nerg
 * @since 1.0
 * @param <T>
 *            The type of the value represented by this Try
 */
public interface Try<T> extends Iterable<T> {

    /**
     * Creates an instance of Try. <br>
     * If a <code>null</code> or non-throwable value is provided then {@link Success} is returned containing the value, else {@link Failure} containing the
     * provided throwable.
     * 
     * @param <T>
     *            The type for the Try
     * @param value
     *            The value for this to create a Try
     * @return The Try instance
     */
    static <T> Try<T> apply(T value) {
        return value instanceof Throwable ? new Failure<T>((Throwable) value) : new Success<T>(value);
    }

    /**
     * Creates an instance of Try wrapping the result of the provided function. <br>
     * If the function results in a value then {@link Success} with the value is returned. <br>
     * In case the function raises an exception then {@link Failure} is returned containing that exception. <br>
     * If <code>null</code> is provided as argument then the {@link #apply(Object)} is invoked. <br>
     * Example simple division by zero results in an exception.
     * 
     * <blockquote>
     * 
     * <pre>
     * Try&lt;Integer&gt; resultSuccess = Try.apply(() -&gt; 9 / 3); // results in Success(3)
     * Try&lt;Integer&gt; resultFailure = Try.apply(() -&gt; 9 / 0); // results in Failure(ArithmeticException)
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the Try
     * @param function
     *            The function to render either the value <i>T</i> or raise an exception.
     * @return The resulting Try instance wrapping what the function resulted in
     * @since 1.3
     */
    static <T> Try<T> apply(ThrowableFunction0<T> function) {
        if (function == null) {
            return apply((T) null);
        }
        try {
            return new Success<>(function.apply());
        } catch (Throwable ex) {
            return new Failure<T>(ex);
        }
    }

    /**
     * Returns <code>true</code> if the 'Try' is a {@link Failure}, <code>false</code> otherwise.
     * 
     * @return If the Try is a {@link Failure}
     */
    default boolean isFailure() {
        return !isSuccess();
    }

    /**
     * Returns <code>true</code> if the 'Try' is a {@link Success}, <code>false</code> otherwise.
     * 
     * @return If the Try is a {@link Success}
     */
    boolean isSuccess();

    /**
     * Returns the value from this {@link Success} or the value provided by the supplier if this is a {@link Failure}.
     * 
     * @param supplier
     *            The supplier to return the value in case of a {@link Failure}
     * @return The value from the Try or the supplier
     */
    T getOrElse(Supplier<T> supplier);

    /**
     * Returns the value if it is a {@link Success}, else <code>null</code>.
     * 
     * @return The value of the Try or <code>null</code>.
     */
    default T orNull() {
        return getOrElse(() -> null);
    }

    /**
     * Returns this 'Try' if it's a {@link Success} or the value provided by the supplier if this is a {@link Failure}.
     * 
     * @param supplier
     *            The supplier to return the value in case of a {@link Failure}
     * @return This try or the value from the supplier
     */
    Try<T> orElse(Supplier<Try<T>> supplier);

    /**
     * Returns the value from this {@link Success} or throws the exception if this is a {@link Failure}.
     * 
     * @return The value of the {@link Success}
     * @throws Throwable
     *             The Throwable in case of a {@link Failure}
     */
    T get() throws Throwable;

    /**
     * Completes this 'Try' with an exception wrapped in a {@link Success}. <br>
     * The exception is either the exception that the 'Try' failed with (if a {@link Failure}) or an 'UnsupportedOperationException'.
     * 
     * @return The value of the {@link Failure} in a {@link Success}
     */
    Try<Throwable> failed();

    /**
     * Returns the Try's value in an {@link Iterator} if it is a {@link Success}, or an empty {@link Iterator} if it is Failure. <br>
     * Should it be a {@link Success} containing a <code>null</code> value then the iterator will also be empty.
     * 
     * @return The iterator for the Try
     */
    default Iterator<T> iterator() {
        return stream().iterator();
    }

    /**
     * Maps the given function to the value from this {@link Success} or returns <code>this</code> if this is a {@link Failure}.
     * 
     * @param <R>
     *            The type for the return value from the function
     * @param function
     *            The function to use
     * @return The Option containing the mapped value
     */
    <R> Try<R> map(Function<T, R> function);

    /**
     * Maps the given function to the value from this {@link Success} or returns <code>this</code> if this is a {@link Failure}.
     * 
     * @param <R>
     *            The type for the return value from the function
     * @param function
     *            The function to use
     * @return The Option containing the mapped value
     * @since 1.2
     */
    <R> Try<R> flatMap(Function<T, Try<R>> function);

    /**
     * Creates a new {@link Try} that in case <i>this</i> {@link Try} is a {@link Failure} will apply the function to recover to a {@link Success}. <br>
     * Should <i>this</i> be a {@link Success} the value is propagated as-is. <br>
     * This is a kind of {@link #map(Function)} for failures only.<br>
     * E.g.
     * 
     * <blockquote>
     * 
     * <pre>
     * Try&lt;String&gt; t = ...
     * Try&lt;String&gt; recovered = t.recover(t -&gt; t.getMessage());
     * </pre>
     * 
     * </blockquote>
     * 
     * In case of <i>t</i> being successful then that value is passed on to <i>recovered</i>, in case of failure then the recover function kicks in and returns
     * the message from the throwable.
     * 
     * 
     * @param function
     *            The function to apply in case of a {@link Failure}
     * @return The recovered Try
     * @since 1.4
     */
    Try<T> recover(Function<Throwable, T> function);

    /**
     * Creates a new {@link Try} that in case <i>this</i> {@link Try} is a {@link Failure} will apply the function to recover the {@link Try} rendered by the
     * function. <br>
     * Should <i>this</i> be a {@link Success} the value is propagated as-is. <br>
     * This is a kind of {@link #map(Function)} for failures only.<br>
     * E.g.
     * 
     * <blockquote>
     * 
     * <pre>
     * Try&lt;String&gt; t = ...
     * Try&lt;String&gt; recovered = t.recover(t -&gt; new Success&lt;&gt;(t.getMessage()));
     * </pre>
     * 
     * </blockquote>
     * 
     * In case of <i>t</i> being successful then that value is passed on to <i>recovered</i>, in case of failure then the recover function kicks in and returns
     * the a {@link Success} with message from the throwable.
     * 
     * 
     * @param function
     *            The function to apply in case of a {@link Failure}
     * @return The recovered Try
     * @since 1.4
     */
    Try<T> recoverWith(Function<Throwable, Try<T>> function);

    /**
     * Returns the Try's value in a Stream if it is a {@link Success}, or an empty Stream if it is a {@link Failure}. <br>
     * Should it be a {@link Success} containing a <code>null</code> value then the stream will also be empty.
     * 
     * @return The stream for the Try
     */
    default Stream<T> stream() {
        return asOption().stream();
    }

    /**
     * Returns this {@link Try} as an {@link Option}. <br>
     * If it is a {@link Success} then the value is wrapped in {@link Some} else {@link None} is returned. <br>
     * Should the {@link Success} contain a <code>null</code> value the result will be {@link None} as <code>null</code> values are per definition
     * <i>none/nothing</i>.
     * 
     * @return The {@link Option} representing this Try
     */
    default Option<T> asOption() {
        return Option.apply(orNull());
    }
}
