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

import static javascalautils.EitherCompanion.Left;

/**
 * Represents the {@link Right} side of an {@link Either}.
 *
 * @author Peter Nerg
 * @since 1.1
 * @param <L> The type for the {@link Left} side (not used for this class)
 * @param <R> The type for the {@link Right} side
 */
public final class Right<L, R> implements Either<L, R>, Serializable {

  private static final long serialVersionUID = -2119790544433508346L;
  private final R value;

  /**
   * Creates an instance wrapping the provide value.
   *
   * @param value The value wrapped by this instance
   * @since 1.1
   */
  public Right(R value) {
    this.value = value;
  }

  /**
   * Applies the provided <code>func_right</code> to the wrapped value and returns the result.
   *
   * @since 1.1
   */
  @Override
  public <T> T fold(Function<L, T> func_left, Function<R, T> func_right) {
    return func_right.apply(value);
  }

  /**
   * Always returns <code>true</code>.
   *
   * @since 1.1
   */
  @Override
  public boolean isRight() {
    return true;
  }

  /**
   * Returns a {@link Left} containing the value for this instance.
   *
   * @since 1.1
   */
  @Override
  public Either<R, L> swap() {
    return Left(value);
  }

  /**
   * Returns a String representation of the instance.
   *
   * @since 1.1
   */
  @Override
  public String toString() {
    return String.format("Right(%s)", value);
  }
}
