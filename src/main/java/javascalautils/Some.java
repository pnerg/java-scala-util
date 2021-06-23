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

import static javascalautils.EitherCompanion.Left;
import static javascalautils.EitherCompanion.Right;

/**
 * Represents an {@link Option} holding a value. <br>
 * The instance of {@link Some} is guaranteed to keep a non-null value object. <br>
 * Null values are not allowed as it implies an instance of {@link None}. <br>
 * One can consider {@link Some} as a collection of size 1. <br>
 * <br>
 * Either use the implementation directly: <br>
 *
 * <blockquote>
 *
 * Option&#60;String&#62; option = new Some&#60;&#62;("Peter is Great!");
 *
 * </blockquote>
 *
 * <br>
 * or use the factory/apply method from Option: <br>
 *
 * <blockquote>
 *
 * Option&#60;String&#62; option = Option.apply("Peter is Great!");
 *
 * </blockquote>
 *
 * For examples on usage refer to the documentation for {@link Option} .
 *
 * @author Peter Nerg
 * @since 1.0
 * @param <T> The type of the value represented by this instance
 */
public final class Some<T> extends SingleItemContainer<T> implements Option<T>, Serializable {
  private static final long serialVersionUID = -17186529545151493L;
  private final T value;

  /**
   * Creates an instance for the provided value. <br>
   * Null objects are not allowed and will render an exception.
   *
   * @param value The value represented by this Some
   * @since 1.0
   */
  public Some(T value) {
    this.value = Validator.requireNonNull(value, "Null values are not allowed for Some");
  }

  /**
   * Returns <code>true</code> if the predicate matches the value.
   *
   * @since 1.0
   */
  @Override
  public boolean exists(Predicate<T> p) {
    return p.test(value);
  }

  /**
   * Always returns the value for this instance. <br>
   * Guaranteed to return a non-null value.
   *
   * @since 1.0
   */
  @Override
  public T get() {
    return value;
  }

  /**
   * Always returns the value for this instance.
   *
   * @since 1.0
   */
  @Override
  public T getOrElse(Supplier<T> s) {
    return value;
  }

  /**
   * Returns an {@link Option} containing the value of applying the given function to the current
   * value.
   *
   * @since 1.0
   */
  @Override
  public <R> Option<R> map(ThrowableFunction1<T, R> f) {
    return flatMap(v -> Option.apply(f.apply(v)));
  }

  /**
   * Returns an Option consisting of the result of applying the given function to the current value.
   *
   * @since 1.2
   */
  @Override
  public <R> Option<R> flatMap(ThrowableFunction1<T, Option<R>> function) {
    try {
      return function.apply(value);
    } catch (Throwable ex) {
      throw new BrokenFunctionException("Caught exception while applying function", ex);
    }
  }

  /**
   * Always returns <i>this</i>.
   *
   * @since 1.0
   */
  @Override
  public Option<T> orElse(Supplier<Option<T>> s) {
    return this;
  }

  /**
   * Returns a {@link Left} containing the value of this instance.
   *
   * @since 1.4
   */
  @Override
  public <R> Either<T, R> toLeft(Supplier<R> right) {
    return Left(value);
  }

  /**
   * Returns a {@link Right} containing the value of this instance.
   *
   * @since 1.4
   */
  @Override
  public <L> Either<L, T> toRight(Supplier<L> left) {
    return Right(value);
  }

  /**
   * Returns <code>true</code> if the other object is {@link Some} containing a value that equals
   * the value of this {@link Some}, else <code>false</code>.
   *
   * @since 1.0
   */
  @Override
  public boolean equals(Object other) {
    // get is safe to invoke as it must be a Some and shall therefore always have a value
    return (other instanceof Some) && ((Option<?>) other).exists(o -> o.equals(value));
  }

  /**
   * Returns the hashCode based on the value of this instance.
   *
   * @since 1.0
   */
  @Override
  public int hashCode() {
    // no need for null checks on the value, as it per definition cannot be null
    return 31 + value.hashCode();
  }

  /**
   * Returns a String representation of the instance.
   *
   * @since 1.0
   */
  @Override
  public String toString() {
    return String.format("Some(%s)", value);
  }
}
