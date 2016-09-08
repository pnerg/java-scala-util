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
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The <i>Try</i> type represents a computation that may either result in an exception, or return a successfully computed value. <br>
 * Typical use case is situations where parallel computation takes place resulting in more than one response where it's possible that one or more computations
 * fail. <br>
 * Though it might not be desirable to raise an exception for failed computations hence the Try acts a place holder for a response that is either failed or
 * successful. <br>
 * Instances of <i>Try</i>, are either an instance of {@link Success} or {@link Failure}. <br>
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
 * To make it even more concise we can create <i>Try</i> instances just by using {@link TryCompanion#Try(ThrowableFunction0) Try(ThrowableFunction0)}. <br>
 * This however requires you to statically import the proper methods from the {@link TryCompanion companion class} related to <i>Try</i>.
 * 
 * <blockquote>
 * 
 * <pre>
 * import static javascalautils.TryCompanion.Try;
 * 
 * Try&lt;SomeData&gt; getSomeData(SomeInput input) {
 *     // readUserFromDB throws if no user exists
 *     Try(() -&gt; readUserFromDB(input));
 * }
 * </pre>
 * 
 * </blockquote>
 * 
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
     * @since 1.0
     */
    static <T> Try<T> apply(T value) {
        return value instanceof Throwable ? new Failure<>((Throwable) value) : new Success<>(value);
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
            return new Failure<>(ex);
        }
    }

    /**
     * Returns <code>true</code> if the <i>Try</i> is a {@link Failure}, <code>false</code> otherwise.
     * 
     * @return If the Try is a {@link Failure}
     * @since 1.0
     */
    default boolean isFailure() {
        return !isSuccess();
    }

    /**
     * Returns <code>true</code> if the <i>Try</i> is a {@link Success}, <code>false</code> otherwise.
     * 
     * @return If the Try is a {@link Success}
     * @since 1.0
     */
    boolean isSuccess();

    /**
     * Returns the value from this {@link Success} or the value provided by the supplier if this is a {@link Failure}.
     * 
     * @param supplier
     *            The supplier to return the value in case of a {@link Failure}
     * @return The value from the Try or the supplier
     * @since 1.0
     */
    T getOrElse(Supplier<T> supplier);

    /**
     * Returns the value if it is a {@link Success}, else <code>null</code>.
     * 
     * @return The value of the Try or <code>null</code>.
     * @since 1.0
     */
    default T orNull() {
        return getOrElse(() -> null);
    }

    /**
     * Returns this <i>Try</i> if it's a {@link Success} or the value provided by the supplier if this is a {@link Failure}.
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
     * @since 1.0
     */
    T get() throws Throwable;

    /**
     * Completes this <i>Try</i> with an exception wrapped in a {@link Success}. <br>
     * The exception is either the exception that the <i>Try</i> failed with (if a {@link Failure}) or an 'UnsupportedOperationException'.
     * 
     * @return The value of the {@link Failure} in a {@link Success}
     * @since 1.0
     */
    Try<Throwable> failed();

    /**
     * Applies the predicate to the value of the {@link Try} and either returns the Try if the predicate matched or a {@link Failure}. <br>
     * One of the three outcomes are applicable:
     * <ul>
     * <li>Instance is {@link Success} and predicate matches -&gt; return <i>this</i></li>
     * <li>Instance is {@link Success} and predicate does not match -&gt; return {@link Failure}</li>
     * <li>Instance is {@link Failure} -&gt; return <i>this</i></li>
     * <li></li>
     * </ul>
     * 
     * @param filter
     *            The filter to apply
     * @return The try matching the filter, either <i>this</i> if matching or a {@link Failure} in case no match
     * @since 1.4
     */
    Try<T> filter(Predicate<T> filter);

    /**
     * Returns the Try's value in an {@link Iterator} if it is a {@link Success}, or an empty {@link Iterator} if it is Failure. <br>
     * 
     * @return The iterator for the Try
     * @since 1.0
     */
    Iterator<T> iterator();

    /**
     * Maps the given function to the value from this {@link Success} or returns <i>this</i> if this is a {@link Failure}. <br>
     * This allows for mapping a {@link Try} containing some type to some completely different type. <br>
     * The example converts a {@link Try} of type String to Integer.<blockquote>
     * 
     * <pre>
     * Try&lt;String&gt; t = ...
     * Try&lt;Integer&gt; t2 = t.map(v -&gt; v.length);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <R>
     *            The type for the return value from the function
     * @param function
     *            The function to use
     * @return The Option containing the mapped value
     * @since 1.0
     */
    <R> Try<R> map(ThrowableFunction1<T, R> function);

    /**
     * Maps the given function to the value from this {@link Success} or returns <i>this</i> if this is a {@link Failure}.
     * 
     * @param <R>
     *            The type for the return value from the function
     * @param function
     *            The function to use
     * @return The Option containing the mapped value
     * @since 1.2
     */
    <R> Try<R> flatMap(ThrowableFunction1<T, Try<R>> function);

    /**
     * Creates a new {@link Try} that in case <i>this</i> {@link Try} is a {@link Failure} will apply the function to recover to a {@link Success}. <br>
     * Should <i>this</i> be a {@link Success} then <i>this</i> is returned, i.e. no new instance is created. <br>
     * This is a kind of {@link #map(ThrowableFunction1)} for failures only.<br>
     * E.g.<br>
     * In case of <i>t</i> being successful then that value is passed on to <i>recovered</i>, in case of failure then the recover function kicks in and returns
     * the message from the throwable.
     * 
     * <blockquote>
     * 
     * <pre>
     * Try&lt;String&gt; t = ...
     * Try&lt;String&gt; recovered = t.recover(t -&gt; t.getMessage());
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * This statement will be <code>Success(3)</code> <blockquote>Try&lt;Integer&gt; t = Try(() -&gt; 9/3).recover(t -&gt; 0)</blockquote> <br>
     * Whilst this statement will be <code>Success(0)</code> as it is division-by-zero <blockquote>Try&lt;Integer&gt; t = Try(() -&gt; 9/0).recover(t -&gt;
     * 0)</blockquote>
     * 
     * 
     * @param function
     *            The function to apply in case of a {@link Failure}
     * @return The recovered Try
     * @since 1.4
     */
    Try<T> recover(ThrowableFunction1<Throwable, T> function);

    /**
     * Creates a new {@link Try} that in case <i>this</i> {@link Try} is a {@link Failure} will apply the function to recover the {@link Try} rendered by the
     * function. <br>
     * Should <i>this</i> be a {@link Success} the value is propagated as-is. <br>
     * This is a kind of {@link #map(ThrowableFunction1)} for failures only.<br>
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
    Try<T> recoverWith(ThrowableFunction1<Throwable, Try<T>> function);

    /**
     * Returns the Try's value in a Stream if it is a {@link Success}, or an empty Stream if it is a {@link Failure}.
     * 
     * @return The stream for the Try
     * @since 1.0
     */
    Stream<T> stream();

    /**
     * Returns this {@link Try} as an {@link Option}. <br>
     * If it is a {@link Success} then the value is wrapped in {@link Some} else {@link None} is returned. <br>
     * Should the {@link Success} contain a <code>null</code> value the result will be {@link None} as <code>null</code> values are per definition
     * <i>none/nothing</i>.
     * 
     * @return The {@link Option} representing this Try
     * @since 1.0
     */
    default Option<T> asOption() {
        return Option.apply(orNull());
    }
}
