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

import static javascalautils.TryCompanion.Try;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents the <i>successful</i> implementation of {@link Try}. <br>
 * Acts as a carrier for the result of a successful computation. <br>
 * For examples on usage refer to the documentation for {@link Try}.
 *
 * @author Peter Nerg
 * @since 1.0
 * @param <T> The type of the value represented by this instance
 */
public final class Success<T> extends SingleItemContainer<T> implements Try<T>, Serializable {

  private static final long serialVersionUID = -5588727176438040926L;
  private final T value;

  /**
   * Creates a 'successful' instance with the provided value. <br>
   * Null values are allowed.
   *
   * @param value The value represented by this instance
   * @since 1.0
   */
  public Success(T value) {
    this.value = value;
  }

  /**
   * Always returns <code>true</code>.
   *
   * @since 1.0
   */
  @Override
  public boolean isSuccess() {
    return true;
  }

  /**
   * Returns the value provided in the constructor. <br>
   * I.e. the provided supplier is never use.
   *
   * @since 1.0
   */
  @Override
  public T getOrElse(Supplier<T> supplier) {
    return value;
  }

  /**
   * Always returns <i>this</i>. <br>
   * I.e. the provided supplier is never use.
   *
   * @since 1.0
   */
  @Override
  public Try<T> orElse(Supplier<Try<T>> supplier) {
    return this;
  }

  /**
   * Returns the value held by this instance. <br>
   * Never throws an exception as this is a <i>success</i>
   *
   * @since 1.0
   */
  @Override
  public T get() {
    return value;
  }

  /**
   * Returns a {@link Failure} with an {@link UnsupportedOperationException}. <br>
   * Since this is a <i>success</i> there is no <i>failure</i> to map to a <i>success</i> i.e. this
   * is a illegal operation.
   *
   * @since 1.0
   */
  @Override
  public Try<Throwable> failed() {
    return new Failure<>(new UnsupportedOperationException("Cannot make a Failure of a Success"));
  }

  /**
   * Applies the value to the function and returns the {@link Try} representing the mapped value.
   *
   * @since 1.0
   */
  @Override
  public <R> Try<R> map(ThrowableFunction1<T, R> function) {
    return Try(() -> function.apply(value));
  }

  /**
   * Applies the value to the function and returns the {@link Try} generated by the function.
   *
   * @since 1.2
   */
  @Override
  public <R> Try<R> flatMap(ThrowableFunction1<T, Try<R>> function) {
    try {
      return function.apply(value);
    } catch (Throwable t) {
      return new Failure<>(t);
    }
  }

  /**
   * Applies the predicate to the value of this instance, if it matches <i>this</i> is returned else
   * a {@link Failure}.
   *
   * @since 1.4
   */
  @Override
  public Try<T> filter(Predicate<T> predicate) {
    return predicate.test(value)
        ? this
        : new Failure<>(
            new NoSuchElementException("Predicate did not match value, now empty result"));
  }

  /**
   * Always returns <i>this</i> . <br>
   * As per definition this is a <i>success</i> and will not need to be recovered.
   *
   * @since 1.4
   */
  @Override
  public Try<T> recover(ThrowableFunction1<Throwable, T> function) {
    return this;
  }

  /**
   * Always returns <i>this</i> . <br>
   * As per definition this is a <i>success</i> and will not need to be recovered.
   *
   * @since 1.4
   */
  @Override
  public Try<T> recoverWith(ThrowableFunction1<Throwable, Try<T>> function) {
    return this;
  }

  /**
   * Returns a String representation of the instance.
   *
   * @since 1.0
   */
  @Override
  public String toString() {
    return String.format("Success(%s)", value != null ? value.toString() : "NULL VALUE");
  }
}
