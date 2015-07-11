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

import java.util.function.Supplier;

/**
 * The 'Try' type represents a computation that may either result in an exception, or return a successfully computed value.<br>
 * Typical use case is situations where parallel computation takes place resulting in more than one response where it's possible that one or more computations
 * fail. <br>
 * Though it might not be desirable to raise an exception for failed computations hence the Try acts a place holder for a response that is either failed or
 * successful.<br>
 * Instances of 'Try', are either an instance of {@link Success} or {@link Failure}.
 * 
 * @author Peter Nerg
 * @since 1.0
 */
public interface Try<T> {

    /**
     * Creates an instance of Try.<br>
     * If a <code>null</code> or non-throwable value is provided then {@link Success} is returned containing the value, else {@link Failure} containing the
     * provided throwable.
     * 
     * @param value
     * @return
     */
    static <T> Try<T> apply(T value) {
        return value instanceof Throwable ? new Failure<>((Throwable) value) : new Success<>(value);
    }

    /**
     * Returns <code>true</code> if the 'Try' is a {@link Failure}, <code>false</code> otherwise.
     */
    default boolean isFailure() {
        return !isSuccess();
    }

    /**
     * Returns <code>true</code> if the 'Try' is a {@link Success}, <code>false</code> otherwise.
     */
    boolean isSuccess();

    /**
     * Returns the value from this {@link Success} or the value provided by the supplier if this is a {@link Failure}.
     */
    T getOrElse(Supplier<T> s);

    /**
     * Returns this 'Try' if it's a {@link Success} or the given 'default' argument if this is a {@link Failure}.
     */
    default Try<T> orElse(Try<T> defaultVal) {
        return orElse(() -> defaultVal);
    }

    /**
     * Returns the value if it is a {@link Success}, else <code>null</code>.
     * 
     * @return
     */
    default T orNull() {
        return getOrElse(() -> null);
    }

    /**
     * Returns this 'Try' if it's a {@link Success} or the value provided by the supplier if this is a {@link Failure}.
     */
    Try<T> orElse(Supplier<Try<T>> s);

    /**
     * Returns the value from this {@link Success} or throws the exception if this is a {@link Failure}.
     */
    T get() throws Throwable;

    /**
     * Completes this 'Try' with an exception wrapped in a {@link Success}.<br>
     * The exception is either the exception that the 'Try' failed with (if a {@link Failure}) or an 'UnsupportedOperationException'.
     */
    Try<Throwable> failed();

    /**
     * Returns this {@link Try} as an {@link Option}. <br>
     * If it is a {@link Success} then the value is wrapped in {@link Some} else {@link None} is returned. <br>
     * Should the {@link Success} contain a <code>null</code> value the result will be {@link None}.
     * 
     * @return
     */
    default Option<T> asOption() {
        return Option.apply(orNull());
    }
}
