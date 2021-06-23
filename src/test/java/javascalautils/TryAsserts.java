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

import org.junit.Assert;

/**
 * Utility asserts related to the {@link javascalautils.Try} class.
 *
 * @author Peter Nerg.
 * @since 1.10
 */
public interface TryAsserts {

  /**
   * Asserts that the provided Try is a Success.
   *
   * @param t The Try to assert
   */
  default void assertIsSuccess(Try<?> t) {
    Assert.assertTrue("The Try [" + t + "] is not a Success", t.isSuccess());
  }

  /**
   * Asserts that the provided Try is a Failure.
   *
   * @param t The Try to assert
   */
  default void assertIsFailure(Try<?> t) {
    Assert.assertTrue("The Try [" + t + "] is not a Failure", t.isFailure());
  }
}
