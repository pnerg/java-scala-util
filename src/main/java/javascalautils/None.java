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

import static javascalautils.EitherCompanion.Left;
import static javascalautils.EitherCompanion.Right;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents an empty {@link Option}. <br>
 * The {@link None} is a replacement for <code>null</code> values representing a non-existing value. <br>
 * One can consider {@link None} as a collection of size 0. <br>
 * Instances of None should not be created directly, rather use the factory methods provided on {@link Option}.
 * <ul>
 * <li>{@link Option#empty()}</li>
 * <li>{@link Option#None()}</li>
 * </ul>
 * Since {@link None} anyways cannot represent a value it is preferable to use it as a singleton saving unnecessary instance creation. <br>
 * For examples on usage refer to the documentation for {@link Option}.
 * 
 * @author Peter Nerg
 * @since 1.0
 * @param <T>
 *            The type of the value represented by this instance
 */
public final class None<T> implements Option<T>, Serializable {

    private static final long serialVersionUID = -5169653193196761412L;

    /**
     * Always throws {@link NoSuchElementException} since None cannot per definition hold any value.
     */
    @Override
    public T get() {
        throw new NoSuchElementException("None cannot hold values");
    }

    /**
     * Always the value provided by the supplier.
     */
    public T getOrElse(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * Always returns <code>false</code>. <br>
     * I.e. the predicate is never used as {@link None} represents nothing/no value.
     */
    public boolean exists(Predicate<T> predicate) {
        return false;
    }

    /**
     * Always returns <code>this</code>. <br>
     * I.e. the function is never used as {@link None} represents nothing/no value.
     */
    @SuppressWarnings("unchecked")
    public <R> Option<R> map(Function<T, R> function) {
        return (Option<R>) this;
    }

    /**
     * Always returns <code>this</code>. <br>
     * I.e. the function is never used as {@link None} represents nothing/no value.
     * 
     * @since 1.2
     */
    @Override
    public <R> Option<R> flatMap(Function<T, Option<R>> function) {
        // uses the Map method as it anyways produces the same result
        return map(null);
    }

    /**
     * Always the value provided by the supplier.
     * 
     * @return The value provided by the supplier
     */
    public Option<T> orElse(Supplier<Option<T>> supplier) {
        return supplier.get();
    }

    /**
     * Always returns an empty stream.
     * 
     * @return Empty stream
     */
    public Stream<T> stream() {
        return Stream.empty();
    }

    /**
     * Returns a {@link Right} containing the value from the provided supplier.
     * 
     * @since 1.4
     */
    @Override
    public <R> Either<T, R> toLeft(Supplier<R> right) {
        return Right(right.get());
    }

    /**
     * Returns a {@link Left} containing the value from the provided supplier.
     * 
     * @since 1.4
     */
    @Override
    public <L> Either<L, T> toRight(Supplier<L> left) {
        return Left(left.get());
    }

    /**
     * Returns <code>true</code> if other is a {@link None} as {@link None} is stateless comparing it to some other None is therefore always the same,
     * <code>false</code> otherwise.
     * 
     * @param other
     *            The other object to compare to
     * @return <code>true</code> if other is {@link None}, <code>false</code> otherwise
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof None;
    }

    /**
     * Always returns <code>0</code> as None is stateless and has no value.
     * 
     * @return 0
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Returns a String representation of the instance.
     */
    @Override
    public String toString() {
        return "None";
    }
}
