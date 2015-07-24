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

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents an {@link Option} holding a value. <br>
 * The instance of {@link Some} is guaranteed to keep a non-null value object. <br>
 * Null values are not allowed as it implies an instance of {@link None}. <br>
 * One can consider {@link Some} as a collection of size 1. <br>
 * <br>
 * Either use the implementation directly: <br>
 * <blockquote>Option&#60;String&#62; option = new Some&#60;&#62;("Peter is Great!");</blockquote> <br>
 * or use the factory/apply method from Option: <br>
 * <blockquote>Option&#60;String&#62; option = Option.apply("Peter is Great!");</blockquote>
 * 
 * @author Peter Nerg
 * @since 1.0
 * @param <T>
 *            The type of the value represented by this instance
 */
public final class Some<T> implements Option<T>, Serializable {
    private static final long serialVersionUID = -17186529545151493L;
    private final T value;

    /**
     * Creates an instance for the provided value. <br>
     * Null objects are not allowed and will render an exception.
     * 
     * @param value
     *            The value represented by this Some
     */
    public Some(T value) {
        this.value = Validator.requireNonNull(value, "Null values are not allowed for Some");
    }

    /**
     * Returns <code>true</code> if the predicate matches the value.
     */
    @Override
    public boolean exists(Predicate<T> p) {
        return p.test(value);
    }

    /**
     * Always returns the value.
     */
    @Override
    public T get() {
        return value;
    }

    /**
     * Always returns the value.
     */
    @Override
    public T getOrElse(Supplier<T> s) {
        return value;
    }

    /**
     * Returns an Option consisting of the result of applying the given function to the current value.
     */
    @Override
    public <R> Option<R> map(Function<T, R> f) {
        return Option.apply(f.apply(value));
    }

    /**
     * Returns an Option consisting of the result of applying the given function to the current value.
     */
    @Override
    public <R> Option<R> flatMap(Function<T, Option<R>> function) {
        return function.apply(value);
    }

    /**
     * Always returns <code>this</code>.
     */
    @Override
    public Option<T> orElse(Supplier<Option<T>> s) {
        return this;
    }

    /**
     * Returns a stream of size one containing the value of this instance.
     */
    @Override
    public Stream<T> stream() {
        return Stream.of(value);
    }

    /**
     * Returns <code>true</code> if the other object is {@link Some} containing a value that equals the value of this {@link Some}, else <code>false</code>.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object other) {
        return (other instanceof Some) ? value.equals(((Some) other).orNull()) : false;
    }

    /**
     * Returns the hashCode based on the value of this {@link Some}.
     */
    @Override
    public int hashCode() {
        // no need for null checks on the value, as it per definition cannot be null
        return 31 + value.hashCode();
    }

    /**
     * Returns a String representation of the instance.
     */
    @Override
    public String toString() {
        return "Some:" + value;
    }
}