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
package javascalautils.concurrent;

import static javascalautils.TryCompanion.Try;

import javascalautils.Failure;
import javascalautils.Success;
import javascalautils.Try;

/**
 * Class with utilities for reflective operations. <br>
 * This class is not intended for public usage, it's a library internal helper. <br>
 *
 * @author Peter Nerg
 * @since 1.4
 */
final class ReflectionUtil {

  /** Inhibitive constructor. */
  private ReflectionUtil() {}

  /**
   * Attempts to create an instance of the provided class. <br>
   * This expects the class to be public and have a public (default) constructor with no arguments.
   *
   * @param <T> The expected to be returned by this method
   * @param className The name of the class
   * @return The result, either {@link Success Success(T)} with the instance or {@link Failure
   *     Failure(Throwable)} in case of failure
   * @since 1.4
   */
  @SuppressWarnings("unchecked")
  static <T> Try<T> newInstance(String className) {
    return Try(() -> (T) Class.forName(className).newInstance());
  }
}
