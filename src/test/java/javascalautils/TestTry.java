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
 * Test the class {@link Try}.
 *
 * @author Peter Nerg
 */
public class TestTry extends BaseAssert {

  @Test
  public void apply_withNonException() {
    assertTrue(Try.apply("Peter is nice").isSuccess());
  }

  @Test
  public void apply_withNull() {
    assertTrue(Try.apply(null).isSuccess());
  }

  @Test
  public void apply_withException() {
    assertTrue(Try.apply(new Exception("Error, terror!")).isFailure());
  }

  @Test
  public void apply_checkedEx_success() {
    assertTrue(Try.apply(() -> 9 / 3).isSuccess());
  }

  @Test
  public void apply_checkedEx_failure() {
    assertTrue(Try.apply(() -> 9 / 0).isFailure());
  }

  @Test
  public void iterator_success() {
    assertTrue(Try.apply(new Object()).iterator().hasNext());
  }

  @Test
  public void iterator_success_null() {
    assertTrue(Try.apply(null).iterator().hasNext());
  }

  @Test
  public void iterator_failure() {
    assertFalse(Try.apply(new Exception("Kaboom!")).iterator().hasNext());
  }

  @Test
  public void stream_success() {
    assertEquals(1, Try.apply(1234).stream().count());
  }

  @Test
  public void stream_success_null() {
    assertEquals(1, Try.apply(null).stream().count());
  }

  @Test
  public void stream_failure() {
    assertEquals(0, Try.apply(new Exception("Crash!")).stream().count());
  }
}
