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
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static javascalautils.EitherCompanion.Left;
import static javascalautils.EitherCompanion.Right;

/**
 * Represents an empty {@link Option}. <br>
 * The {@link None} is a replacement for <code>null</code> values representing a non-existing value.
 * <br>
 * One can consider {@link None} as a collection of size 0. <br>
 * Instances of None should not be created directly, rather use the factory methods provided on
 * {@link Option}.
 *
 * <ul>
 *   <li>{@link Option#empty()}
 *   <li>{@link Option#None()}
 * </ul>
 *
 * Since {@link None} anyways cannot represent a value it is preferable to use it as a singleton
 * saving unnecessary instance creation. <br>
 * For examples on usage refer to the documentation for {@link Option}.
 *
 * @author Peter Nerg
 * @since 1.0
 * @param <T> The type of the value represented by this instance
 */
public final class None<T> extends EmptyContainer<T> implements Option<T>, Serializable {

  private static final long serialVersionUID = -5169653193196761412L;

  /**
   * Always throws {@link NoSuchElementException} since None cannot per definition hold any value.
   *
   * @since 1.0
   */
  @Override
  public T get() {
    throw new NoSuchElementException("None cannot hold values");
  }

  /**
   * Always the value provided by the supplier.
   *
   * @since 1.0
   */
  public T getOrElse(Supplier<T> supplier) {
    return supplier.get();
  }

  /**
   * Always returns <code>false</code>. <br>
   * I.e. the predicate is never used as {@link None} represents nothing/no value.
   *
   * @since 1.0
   */
  public boolean exists(Predicate<T> predicate) {
    return false;
  }

  /**
   * Always returns <code>this</code>. <br>
   * I.e. the function is never used as {@link None} represents nothing/no value.
   *
   * @since 1.0
   */
  @SuppressWarnings("unchecked")
  public <R> Option<R> map(ThrowableFunction1<T, R> function) {
    return (Option<R>) this;
  }

  /**
   * Always returns <code>this</code>. <br>
   * I.e. the function is never used as {@link None} represents nothing/no value.
   *
   * @since 1.2
   * @since 1.0
   */
  @Override
  public <R> Option<R> flatMap(ThrowableFunction1<T, Option<R>> function) {
    // uses the Map method as it anyways produces the same result
    return map(null);
  }

  /**
   * Always the value provided by the supplier.
   *
   * @return The value provided by the supplier
   * @since 1.0
   */
  public Option<T> orElse(Supplier<Option<T>> supplier) {
    return supplier.get();
  }

  /**
   * Returns a {@link Right} containing the value from the provided supplier.
   *
   * @since 1.4
   * @since 1.0
   */
  @Override
  public <R> Either<T, R> toLeft(Supplier<R> right) {
    return Right(right.get());
  }

  /**
   * Returns a {@link Left} containing the value from the provided supplier.
   *
   * @since 1.4
   * @since 1.0
   */
  @Override
  public <L> Either<L, T> toRight(Supplier<L> left) {
    return Left(left.get());
  }

  /**
   * Returns <code>true</code> if other is a {@link None} as {@link None} is stateless comparing it
   * to some other None is therefore always the same, <code>false</code> otherwise.
   *
   * @param other The other object to compare to
   * @return <code>true</code> if other is {@link None}, <code>false</code> otherwise
   * @since 1.0
   */
  @Override
  public boolean equals(Object other) {
    return other instanceof None;
  }

  /**
   * Always returns <code>0</code> as None is stateless and has no value.
   *
   * @return 0
   * @since 1.0
   */
  @Override
  public int hashCode() {
    return 0;
  }

  /**
   * Returns a String representation of the instance.
   *
   * @since 1.0
   */
  @Override
  public String toString() {
    return "None";
  }
}
