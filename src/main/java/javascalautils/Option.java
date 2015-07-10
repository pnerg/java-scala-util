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
 *
 * @param <T>
 * @since 1.0
 */
public interface Option<T> extends Iterable<T> {

    /** This is a singleton {@link None} since it anyways cannot represent a state. */
    @SuppressWarnings("rawtypes")
    public static final None DEFAULT_NONE = new None();

    /**
     * Creates an instance of Option.<br>
     * If a <code>null</code> value is provided then {@link None} is returned, else {@link Some} containing the provided value.
     * 
     * @param value
     * @return
     */
    public static <T> Option<T> apply(T value) {
        return value != null ? new Some<T>(value) : empty();
    }

    /**
     * Creates an empty Option.<br>
     * In practice this returns a static default singleton as it anyways cannot represent a value/state.
     * 
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Option<T> empty() {
        return DEFAULT_NONE;
    }

    /**
     * If the Option contains the provided Object.
     * 
     * @param t
     * @return
     */
    public default boolean contains(final T t) {
        return exists(value -> value.equals(t));
    }

    /**
     * Returns the count which means <code>1</code> for nonempty Option's and <code>0</code> for empty.
     * 
     * @return
     */
    public default int count() {
        // map will either return Some(1) or None upon which the else(0) will be returned.
        return map(value -> 1).getOrElse(() -> 0);
    }

    /**
     * Returns <code>true</code> if this option is nonempty and the predicate p returns <code>true</code> when applied to this Option's value.
     * 
     * @param p
     * @return
     */
    public boolean exists(Predicate<T> p);

    /**
     * Returns this Option if it is nonempty and applying the predicate p to this Option's value returns <code>true</code>.
     * 
     * @param p
     * @return
     */
    public default Option<T> filter(Predicate<T> p) {
        return exists(p) ? this : empty();
    }

    /**
     * Returns this Option if it is nonempty and applying the predicate p to this Option's value returns <code>false</code>.
     * 
     * @param p
     * @return
     */
    public default Option<T> filterNot(final Predicate<T> p) {
        // filter not is in practice just negating the result of the provided predicate
        return filter(value -> !p.test(value));
    }

    /**
     * Returns <code>true</code> if the Option is nonempty and the predicate holds <code>true</code>, else <code>false</code>.<br>
     * In an essence exactly the same as {@link #exists(Predicate)}.
     * 
     * @param p
     * @return
     */
    public default boolean forall(Predicate<T> p) {
        return exists(p);
    }

    /**
     * Returns this Option's value if such exists, else {@link NoSuchElementException} is raised.
     * 
     * @return
     */
    public T get();

    /**
     * Returns this Option's value if such exists, else the value provided by the supplier.
     * 
     * @return
     */
    public T getOrElse(Supplier<T> s);

    /**
     * Returns <code>true</code> if the option is an instance of {@link Some}, <code>false</code> otherwise.
     * 
     * @return
     */
    public default boolean isDefined() {
        return exists(t -> true);
    }

    /**
     * Returns <code>true</code> if the option is an instance of {@link None}, <code>false</code> otherwise
     * 
     * @return
     */
    public default boolean isEmpty() {
        return !isDefined();
    }

    /**
     * Returns the Option's value in an {@link Iterator} if it is nonempty, or an empty {@link Iterator} if it is empty.
     * 
     * @return
     */
    @Override
    public default Iterator<T> iterator() {
        return stream().iterator();
    }

    /**
     * Returns an Option consisting of the result of applying the given function to the current {@link Some}. <br>
     * Applying map to {@link None} will always yield {@link None}.
     * 
     * @param f
     * @return
     */
    public <R> Option<R> map(Function<T, R> f);

    /**
     * Returns this Option if it is nonempty, otherwise return the result of provided by the supplier.
     * 
     * @param s
     * @return
     */
    public Option<T> orElse(Supplier<Option<T>> s);

    /**
     * Returns the Option's value if it is nonempty, or <code>null</code> if it is empty.
     * 
     * @param s
     * @return
     */
    public default T orNull() {
        return getOrElse(() -> null);
    }

    /**
     * Returns the Option's value in a Stream if it is nonempty, or an empty Stream if it is empty.
     * 
     * @return
     */
    public Stream<T> stream();

    /**
     * Converts this {@link Option} to a corresponding {@link Optional}.
     * 
     * @return
     */
    public default Optional<T> asOptional() {
        return Optional.ofNullable(orNull());
    }

    /**
     * Converts the {@link Optional} to a corresponding {@link Option}.
     * 
     * @param o
     * @return
     */
    public static <T> Option<T> ofOptional(Optional<T> o) {
        return apply(o.orElse(null));
    }

}
