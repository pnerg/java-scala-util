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

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents an empty {@link Option}.
 *
 * @param <T>
 * @author Peter Nerg
 * @since 1.0
 */
public final class None<T> implements Option<T> {
    /**
     * Always throws {@link NoSuchElementException} since None cannot hold any value.
     */
    @Override
    public T get() {
        throw new NoSuchElementException("None cannot hold values");
    }

    /**
     * Always the value provided by the supplier.
     * 
     * @return
     */
    public T getOrElse(Supplier<T> s) {
        return s.get();
    }

    /**
     * Always returns <code>false</code>.
     */
    public boolean exists(Predicate<T> p) {
        return false;
    }

    /**
     * Always returns <code>this</code>.
     */
    @SuppressWarnings("unchecked")
    public <R> Option<R> map(Function<T, R> f) {
        return (Option<R>) this;
    }

    /**
     * Always the value provided by the supplier.
     */
    public Option<T> orElse(Supplier<Option<T>> s) {
        return s.get();
    }

    /**
     * Always returns an empty stream.
     */
    public Stream<T> stream() {
        return Stream.empty();
    }

    /**
     * Always returns <code>true</code> as None is stateless comparing it to some other None is therefore always the same.
     * 
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof None;
    }

    /**
     * Always returns <code>0</code> as None is stateless and has no value.
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
