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
import java.util.function.Supplier;

/**
 * Represents the 'successful' implementation of {@link Try}.
 * 
 * @author Peter Nerg
 * @since 1.0
 * @param <T>
 *            The type of the value represented by this instance
 */
public final class Success<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = -5588727176438040926L;
    private final T value;

    /**
     * Creates a 'successful' instance with the provided value.<br>
     * Null values are allowed.
     * 
     * @param value
     *            The value represented by this instance
     */
    public Success(T value) {
        this.value = value;
    }

    /**
     * Always returns <code>true</code>.
     */
    @Override
    public boolean isSuccess() {
        return true;
    }

    /**
     * Always returns the value provided in the constructor.
     */
    @Override
    public T getOrElse(Supplier<T> s) {
        return value;
    }

    /**
     * Always returns this.
     */
    @Override
    public Try<T> orElse(Supplier<Try<T>> s) {
        return this;
    }

    /**
     * Always returns the value provided in the constructor.
     */
    @Override
    public T get() {
        return value;
    }

    /**
     * Always returns a {@link Failure} with an {@link UnsupportedOperationException}.
     */
    @Override
    public Try<Throwable> failed() {
        return Try.apply(new UnsupportedOperationException("Success.failed"));
    }

    /**
     * Applies the value to the function and returns the Try representing the mapped value.
     */
    @Override
    public <R> Try<R> map(Function<T, R> function) {
        return Try.apply(function.apply(value));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Success:" + (value != null ? value.toString() : "NULL VALUE");
    }
}
