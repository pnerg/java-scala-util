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

import org.junit.Test;

/**
 * Test the class {@link Right}
 *
 * @author Peter Nerg
 */
public class TestRight extends BaseAssert {
  private final String TXT = "Right is not Left";
  private final Either<Object, String> either = new Right<>(TXT);

  @Test
  public void isRight() {
    assertTrue(either.isRight());
  }

  @Test
  public void fold() {
    // left function is anyways not used
    assertEquals(TXT.toUpperCase(), either.fold(null, v -> v.toUpperCase()));
  }

  @Test
  public void swap() {
    assertTrue(either.swap().isLeft());
  }

  @Test
  public void t_toString() {
    assertNotNull(either.toString());
  }
}
