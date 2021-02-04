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

import java.util.NoSuchElementException;

/**
 * Test the class {@link RightProjection}
 *
 * @author Peter Nerg
 */
public class TestRightProjection_WithLeft extends BaseAssert {

  private final Either<Object, String> either = new Left<>(new Object());
  private final RightProjection<Object, String> projection = new RightProjection<>(either);

  @Test
  public void asOption() {
    assertFalse(projection.asOption().isDefined());
  }

  @Test(expected = NoSuchElementException.class)
  public void get() {
    projection.get();
  }

  @Test
  public void orNull() {
    assertNull(projection.orNull());
  }

  @Test
  public void iterator() {
    assertFalse(projection.iterator().hasNext());
  }

  @Test
  public void stream() {
    assertEquals(0, projection.stream().count());
  }

  @Test
  public void exists() {
    // provide predicate that always returns true
    assertFalse(projection.exists(v -> true));
  }

  @Test
  public void getOrElse() {
    assertEquals("DEFAULT", projection.getOrElse(() -> "DEFAULT"));
  }

  @Test
  public void filter() {
    // provide predicate that always returns true
    assertFalse(projection.filter(v -> true).isDefined());
  }

  @Test
  public void forAll() {
    // no matter what predicate, always return true
    assertTrue(projection.forAll(v -> true));
    assertTrue(projection.forAll(v -> false));
  }

  @Test
  public void map() {
    // mapping won't matter as it's a Left and will not be mapped
    assertFalse(projection.map(v -> v).isRight());
  }

  @Test
  public void flatMap() {
    // mapping won't matter as it's a Left and will not be mapped
    assertFalse(projection.flatMap(v -> new Right<>(v)).isRight());
  }
}
