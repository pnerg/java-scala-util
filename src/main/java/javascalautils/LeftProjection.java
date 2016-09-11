/**
 *  Copyright 2015 Peter Nerg
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

import static javascalautils.EitherCompanion.Left;

/**
 * This is a left-biased wrapper for an instance of {@link Either}.
 * 
 * @author Peter Nerg
 * @since 1.1
 */
public class LeftProjection<L, R> extends Projection<L> implements Iterable<L>, Serializable {
    private static final long serialVersionUID = 4251047373391313192L;
    private final Either<L, R> either;

    /**
     * Creates an instance of the projection.
     * 
     * @param either
     *            The instance to wrap.
     * @since 1.1
     */
    LeftProjection(Either<L, R> either) {
        this.either = either;
    }

    /**
     * Returns <code>false</code> if {@link Right} or returns the result of applying the predicate to the {@link Left} value.
     * 
     * @param predicate
     *            The predicate to apply
     * @return If this is a {@link Left} and the predicate matches the value
     * @since 1.1
     */
    public boolean exists(Predicate<L> predicate) {
        return either.isLeft() && predicate.test(orNull());
    }

    /**
     * Returns the value if this is a {@link Left} else the value provided by the supplier.
     * 
     * @param supplier
     *            The supplier
     * @return The value
     * @since 1.1
     */
    @Override
    public L getOrElse(Supplier<L> supplier) {
        return either.fold(v -> v, v -> supplier.get());
    }

    /**
     * Returns a {@link Some} wrapping the {@link Either} if it's a {@link Left} and the value of the {@link Left} matches the predicate, else {@link None} .
     * 
     * @param predicate
     *            The predicate to apply
     * @return The resulting Option of the filter operation
     * @since 1.1
     */
    public Option<Either<L, R>> filter(Predicate<L> predicate) {
        return Option.apply(exists(predicate) ? either : null);
    }

    /**
     * Returns <code>true</code> if {@link Right} or returns the result of applying the predicate to the {@link Left} value.
     * 
     * @param predicate
     *            The predicate to apply
     * @return If it is a match
     * @since 1.1
     */
    public boolean forAll(Predicate<L> predicate) {
        return !either.isLeft() || predicate.test(orNull());
    }

    /**
     * If this projection contains a {@link Left} then a new {@link Left} is returned containing the value from the original {@link Right} mapped via the
     * provided function, else the contained Either is returned as is.
     * 
     * @param <T>
     *            The type to return as the new {@link Left}
     * @param function
     *            The function
     * @return Mapped Either
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public <T> Either<T, R> map(Function<L, T> function) {
        return either.isLeft() ? Left(function.apply(get())) : (Either<T, R>) either;
    }

    /**
     * If this projection contains a {@link Left} then a new {@link Left} is returned containing the value from the original {@link Right} mapped via the
     * provided function, else the contained Either is returned as is.
     * 
     * @param <T>
     *            The type to return as the new {@link Left}
     * @param function
     *            The function
     * @return Mapped Either
     * @since 1.2
     */
    @SuppressWarnings("unchecked")
    public <T> Either<T, R> flatMap(Function<L, Left<T, R>> function) {
        return either.isLeft() ? function.apply(get()) : (Either<T, R>) either;
    }

}
