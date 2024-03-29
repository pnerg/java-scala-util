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

import static javascalautils.EitherCompanion.Left;
import static javascalautils.EitherCompanion.Right;

import org.junit.Test;

/**
 * Test the class {@link EitherCompanion}
 *
 * @author Peter Nerg
 */
public class TestEitherCompanion extends BaseAssert {

  @Test
  public void createInstance() throws ReflectiveOperationException {
    assertPrivateConstructor(EitherCompanion.class);
  }

  @Test
  public void left() {
    Either<Integer, String> either = Left(69);
    assertTrue(either.isLeft());
  }

  @Test
  public void right() {
    Either<Integer, String> either = Right("Right is not Left");
    assertTrue(either.isRight());
  }
}
