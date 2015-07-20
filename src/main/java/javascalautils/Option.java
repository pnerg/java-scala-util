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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents optional values. <br>
 * Instances of Option are either an instance of {@link Some} or {@link None}.<br>
 * {@link Some} holds a non-null value whilst {@link None} holds no value.
 * 
 * @author Peter Nerg
 * @since 1.0
 * @param <T>
 *            The type of the value represented by this instance
 */
public interface Option<T> extends Iterable<T> {

    /**
     * This is a singleton {@link None} since it anyways cannot represent a state.<br>
     * Can also be accessed using {@link #empty()}
     */
    @SuppressWarnings("rawtypes")
    public static final None DEFAULT_NONE = new None();

    /**
     * Creates an instance of Option.<br>
     * If a <code>null</code> value is provided then {@link None} is returned, else {@link Some} containing the provided value.
     * 
     * @param <T>
     *            The type for the value this Option represents
     * @param value
     *            The value this Option shall represent
     * @return The Option representing the provided value
     */
    static <T> Option<T> apply(T value) {
        return value != null ? new Some<T>(value) : empty();
    }

    /**
     * Returns an empty Option.<br>
     * In practice this returns a {@link #DEFAULT_NONE singleton} as it anyways cannot represent a value/state.
     * 
     * @param <T>
     *            The type for the value this Option represents
     * @return The default {@link None} instance
     */
    static <T> Option<T> empty() {
        return None();
    }

    /**
     * Returns an empty Option.<br>
     * This is the same as {@link #empty()} but with the difference it provides a more Scala like feeling if the method is statically imported. <br>
     * One can use <code>None()</code> as if it was a apply method on a companion object in Scala. E.g.
     * 
     * <pre>
     * <code>
     * import static javascalautils.Option.None;
     * 
     * Option&#60;String&#62; opt = None();
     * </code>
     * </pre>
     * 
     * 
     * @param <T>
     *            The type for the value this Option represents
     * @return The default {@link None} instance
     * @since 1.2
     */
    @SuppressWarnings("unchecked")
    static <T> Option<T> None() {
        return DEFAULT_NONE;
    }

    /**
     * Returns <code>true</code> if this is a {@link Some} containing the provided object, else <code>false</code>.
     * 
     * @param other
     *            The other object to compare to
     * @return If this {@link Some} contains the provided object
     */
    default boolean contains(final T other) {
        return exists(value -> value.equals(other));
    }

    /**
     * Returns the count which means <code>1</code> for nonempty Option's and <code>0</code> for empty.
     * 
     * @return The count
     */
    default int count() {
        // map will either return Some(1) or None upon which the else(0) will be returned.
        return map(value -> 1).getOrElse(() -> 0);
    }

    /**
     * Returns <code>true</code> if this option is nonempty and the predicate p returns <code>true</code> when applied to this Option's value.
     * 
     * @param p
     *            The predicate
     * @return If the predicate matches
     */
    boolean exists(Predicate<T> p);

    /**
     * Returns this Option if it is nonempty and applying the predicate p to this Option's value returns <code>true</code>.
     * 
     * @param p
     *            The predicate
     * @return The Option representing the match
     */
    default Option<T> filter(Predicate<T> p) {
        return exists(p) ? this : empty();
    }

    /**
     * Returns this Option if it is nonempty and applying the predicate p to this Option's value returns <code>false</code>.
     * 
     * @param p
     *            The predicate
     * @return The Option representing the match
     */
    default Option<T> filterNot(final Predicate<T> p) {
        // filter not is in practice just negating the result of the provided predicate
        return filter(value -> !p.test(value));
    }

    /**
     * Returns <code>true</code> if the Option is nonempty and the predicate holds <code>true</code>, else <code>false</code>.<br>
     * In an essence exactly the same as {@link #exists(Predicate)}.
     * 
     * @param p
     *            The predicate
     * @return If the predicate matches
     */
    default boolean forall(Predicate<T> p) {
        return exists(p);
    }

    /**
     * Returns this Option's value if such exists, else {@link NoSuchElementException} is raised.
     * 
     * @return The value of the Option
     */
    T get();

    /**
     * Returns this Option's value if such exists, else the value provided by the supplier.
     * 
     * @param supplier
     *            The supplier to use in case this is a {@link None}
     * @return The value of the Option or the value provided by the supplier
     */
    T getOrElse(Supplier<T> supplier);

    /**
     * Returns <code>true</code> if the option is an instance of {@link Some}, <code>false</code> otherwise.
     * 
     * @return If this Option is a {@link Some}
     */
    default boolean isDefined() {
        return exists(t -> true);
    }

    /**
     * Returns <code>true</code> if the option is an instance of {@link None}, <code>false</code> otherwise
     * 
     * @return If this Option is a {@link None}
     */
    default boolean isEmpty() {
        return !isDefined();
    }

    /**
     * Returns the Option's value in an {@link Iterator} if it is nonempty, or an empty {@link Iterator} if it is empty.
     * 
     * @return The iterator for the Option
     */
    @Override
    default Iterator<T> iterator() {
        return stream().iterator();
    }

    /**
     * Returns an Option consisting of the result of applying the given function to the current {@link Some}. <br>
     * Applying map to {@link None} will always yield {@link None}.
     * 
     * @param <R>
     *            The type for the return value from the function
     * @param function
     *            The function to use
     * @return The Option containing the mapped value
     */
    <R> Option<R> map(Function<T, R> function);

    /**
     * Returns an Option consisting of the result of applying the given function to the current {@link Some}. <br>
     * Applying map to {@link None} will always yield {@link None}.
     * 
     * @param <R>
     *            The type for the return value from the function
     * @param function
     *            The function to use
     * @return The Option containing the mapped value
     * @since 1.2
     */
    <R> Option<R> flatMap(Function<T, Option<R>> function);

    /**
     * Returns this Option if it is nonempty, otherwise return the result of provided by the supplier.
     * 
     * @param supplier
     *            The supplier to use in case of {@link None}
     * @return This Option or the one provided by the supplier
     */
    Option<T> orElse(Supplier<Option<T>> supplier);

    /**
     * Returns the Option's value if it is nonempty, or <code>null</code> if it is empty.
     * 
     * @return The value of the Option or <code>null</code> in case of {@link None}
     */
    default T orNull() {
        return getOrElse(() -> null);
    }

    /**
     * Returns the Option's value in a Stream if it is nonempty, or an empty Stream if it is empty.
     * 
     * @return The stream for the Option
     */
    Stream<T> stream();

    /**
     * Converts this {@link Option} to a corresponding {@link Optional}.
     * 
     * @return The Optional instance
     */
    default Optional<T> asOptional() {
        return Optional.ofNullable(orNull());
    }

    /**
     * Converts the {@link Optional} to a corresponding {@link Option}.
     * 
     * @param <T>
     *            The type for the value this Option represents
     * @param optional
     *            The Optional to convert
     * @return The Option for the provided Optional
     */
    static <T> Option<T> ofOptional(Optional<T> optional) {
        return apply(optional.orElse(null));
    }

}
