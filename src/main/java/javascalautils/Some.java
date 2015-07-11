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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents an {@link Option} holding a value.
 * 
 * @author Peter Nerg
 *
 * @param <T>
 * @since 1.0
 */
public final class Some<T> implements Option<T> {
    private final T value;

    /**
     * Creates an instance for the provided value. <br>
     * Null objects are not allowed and will render an exception.
     * 
     * @param value
     */
    public Some(T value) {
        this.value = Objects.requireNonNull(value, "Null values are not allowed for Some");
    }

    /**
     * Returns <code>true</code> if the predicate matches the value.
     */
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
    public T getOrElse(Supplier<T> s) {
        return get();
    }

    /**
     * Returns an Option consisting of the result of applying the given function to the current value.
     */
    public <R> Option<R> map(Function<T, R> f) {
        return Option.apply(f.apply(get()));
    }

    /**
     * Always returns <code>this</code>.
     */
    public Option<T> orElse(Supplier<Option<T>> s) {
        return this;
    }

    /**
     * Returns a stream of size one containing the value.
     */
    public Stream<T> stream() {
        return Stream.of(get());
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Some:" + value.toString();
    }
}