/**
 * Copyright 2015 Peter Nerg
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javascalautils;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static javascalautils.EitherCompanion.Right;

/**
 * This is a right-biased wrapper for an instance of {@link Either}.
 *
 * @author Peter Nerg
 * @since 1.1
 */
public class RightProjection<L, R> extends Projection<R> implements Serializable {
  private static final long serialVersionUID = 4251047373391313192L;
  private final Either<L, R> either;

  /**
   * Creates an instance of the projection.
   *
   * @param either The instance to wrap.
   * @since 1.1
   */
  RightProjection(Either<L, R> either) {
    this.either = either;
  }

  /**
   * Returns <code>false</code> if {@link Left} or returns the result of applying the predicate to
   * the {@link Right} value.
   *
   * @param predicate The predicate to apply
   * @return If this is a {@link Right} and the predicate matches the value
   * @since 1.1
   */
  public boolean exists(Predicate<R> predicate) {
    return either.isRight() && predicate.test(orNull());
  }

  /**
   * Returns the value if this is a {@link Right} else the value provided by the supplier.
   *
   * @param supplier The supplier
   * @return The value
   * @since 1.1
   */
  @Override
  public R getOrElse(Supplier<R> supplier) {
    return either.fold(v -> supplier.get(), v -> v);
  }

  /**
   * Returns a {@link Some} wrapping the {@link Either} if it's a {@link Right} and the value of the
   * {@link Right} matches the predicate, else {@link None} .
   *
   * @param predicate The predicate to apply
   * @return The resulting Option of the filter operation
   * @since 1.1
   */
  public Option<Either<L, R>> filter(Predicate<R> predicate) {
    return Option.apply(exists(predicate) ? either : null);
  }

  /**
   * Returns <code>true</code> if {@link Left} or returns the result of applying the predicate to
   * the {@link Right} value.
   *
   * @param predicate The predicate to apply
   * @return If it is a match
   * @since 1.1
   */
  public boolean forAll(Predicate<R> predicate) {
    return !either.isRight() || predicate.test(orNull());
  }

  /**
   * If this projection contains a {@link Right} then a new {@link Right} is returned containing the
   * value from the original {@link Right} mapped via the provided function, else the contained
   * Either is returned as is.
   *
   * @param <T> The type to return as the new {@link Right}
   * @param function The function
   * @return Mapped Either
   * @since 1.1
   */
  @SuppressWarnings("unchecked")
  public <T> Either<L, T> map(Function<R, T> function) {
    return either.isRight() ? Right(function.apply(get())) : (Either<L, T>) either;
  }

  /**
   * If this projection contains a {@link Right} then a new {@link Right} is returned containing the
   * value from the original {@link Right} mapped via the provided function, else the contained
   * Either is returned as is.
   *
   * @param <T> The type to return as the new {@link Right}
   * @param function The function
   * @return Mapped Either
   * @since 1.2
   */
  @SuppressWarnings("unchecked")
  public <T> Either<L, T> flatMap(Function<R, Right<L, T>> function) {
    return either.isRight() ? function.apply(get()) : (Either<L, T>) either;
  }
}
