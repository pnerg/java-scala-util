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
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents the 'failed' implementation of {@link Try}.
 * 
 * @author Peter Nerg
 * @since 1.0
 */
public final class Failure<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = -3153874073796965428L;
    private final Throwable throwable;

    public Failure(Throwable throwable) {
        Objects.requireNonNull(throwable, "Null values are not allowed for Failure");
        this.throwable = throwable;
    }

    /**
     * Always returns <code>false</code>.
     */
    @Override
    public boolean isSuccess() {
        return false;
    }

    /**
     * Always returns the provided default value.
     */
    @Override
    public T getOrElse(Supplier<T> s) {
        return s.get();
    }

    /**
     * Always returns the provided default value.
     */
    @Override
    public Try<T> orElse(Supplier<Try<T>> s) {
        return s.get();
    }

    /**
     * Always throws the {@link Throwable} provided in the constructor.
     */
    @Override
    public T get() throws Throwable {
        throw throwable;
    }

    /**
     * Always returns a {@link Success} with the {@link Throwable} provided in the constructor.
     */
    @Override
    public Try<Throwable> failed() {
        return new Success<Throwable>(throwable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Failure:" + throwable.getMessage();
    }
}
