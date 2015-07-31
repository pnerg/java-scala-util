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

import static javascalautils.TryCompanion.Success;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents the <i>failure</i> implementation of {@link Try}. <br>
 * Acts as a carrier for the result/throwable of a unsuccessful computation. <br>
 * For examples on usage refer to the documentation for {@link Try}.
 * 
 * @author Peter Nerg
 * @since 1.0
 * @param <T>
 *            The type of the exception represented by this instance
 */
public final class Failure<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = -3153874073796965428L;
    private final Throwable throwable;

    /**
     * Creates a Failure for the provided Throwable. <br>
     * Null values are <b>not</b> allowed since a 'failure' needs a reason.
     * 
     * @param throwable
     *            The throwable
     * @since 1.0
     */
    public Failure(Throwable throwable) {
        this.throwable = Validator.requireNonNull(throwable, "Null values are not allowed for Failure");
    }

    /**
     * Always returns <code>false</code>.
     * 
     * @since 1.0
     */
    @Override
    public boolean isSuccess() {
        return false;
    }

    /**
     * Always returns the value provided by the supplier. <br>
     * As per definition this is a failure without any data to return.
     * 
     * @since 1.0
     */
    @Override
    public T getOrElse(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * Always returns the value provided by the supplier. <br>
     * As per definition this is a failure without any data to return.
     * 
     * @since 1.0
     */
    @Override
    public Try<T> orElse(Supplier<Try<T>> supplier) {
        return supplier.get();
    }

    /**
     * Always throws the {@link Throwable} this instance represents. <br>
     * As per definition this is a failure without any data to return.
     * 
     * @since 1.0
     */
    @Override
    public T get() throws Throwable {
        throw throwable;
    }

    /**
     * Returns a {@link Success} with the {@link Throwable} this instance represents.
     * 
     * @since 1.0
     */
    @Override
    public Try<Throwable> failed() {
        return Success(throwable);
    }

    /**
     * Always returns <i>this</i>. <br>
     * As per definition this is a failure without any data to map.
     * 
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public <R> Try<R> map(Function<T, R> function) {
        return (Try<R>) this;
    }

    /**
     * Always returns <i>this</i>. <br>
     * As per definition this is a failure without any data to map.
     * 
     * @since 1.2
     */
    @Override
    public <R> Try<R> flatMap(Function<T, Try<R>> function) {
        return map(null);
    }

    /**
     * Always returns <i>this</i>. <br>
     * As per definition this is a failure without any data to filter.
     * 
     * @since 1.4
     */
    @Override
    public Try<T> filter(Predicate<T> predicate) {
        return this;
    }

    /**
     * Applies the provided function to the Throwable of this {@link Failure} and returns a {@link Success} with the result.
     * 
     * @since 1.4
     */
    @Override
    public Try<T> recover(Function<Throwable, T> function) {
        return Success(function.apply(throwable));
    }

    /**
     * Applies the provided function to the Throwable of this {@link Failure} and returns the result.
     * 
     * @since 1.4
     */
    @Override
    public Try<T> recoverWith(Function<Throwable, Try<T>> function) {
        return function.apply(throwable);
    }

    /**
     * Returns a String representation of the instance.
     * 
     * @since 1.0
     */
    @Override
    public String toString() {
        return "Failure:" + throwable.getMessage();
    }
}
