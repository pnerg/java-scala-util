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

import java.util.stream.Stream;

/**
 * Test the class {@link Success}
 *
 * @author Peter Nerg
 */
public class TestSuccess extends BaseAssert implements TryAsserts {

  private final String message = "Peter Rulez";
  private final Try<String> t = new Success<>(message);

  @Test
  public void isSuccess() {
    assertTrue(t.isSuccess());
  }

  @Test
  public void isFailure() {
    assertFalse(t.isFailure());
  }

  @Test
  public void get() throws Throwable {
    assertEquals(message, t.get());
  }

  @Test
  public void getOrElse() {
    assertEquals(message, t.getOrElse(() -> "Ignore as we're successful"));
  }

  @Test
  public void orElse() {
    assertEquals(t, t.orElse(() -> new Success<String>("Ignore as we're successful")));
  }

  @Test
  public void failed() {
    assertIsFailure(t.failed());
  }

  @Test
  public void filter_match() {
    // always true filter -> this
    assertIsSuccess(t.filter(s -> true));
  }

  @Test
  public void filter_nomatch() {
    // always false filter -> Failure
    assertIsFailure(t.filter(s -> false));
  }

  @Test
  public void toString_withValue() {
    assertNotNull(t.toString());
  }

  @Test
  public void toString_withNullValue() {
    assertNotNull(new Success<Object>(null).toString());
  }

  @Test
  public void asOption() {
    assertTrue(t.asOption().isDefined());
  }

  @Test
  public void map() {
    assertEquals(message.length(), t.map(v -> v.length()).orNull().intValue());
  }

  @Test
  public void map_failure() {
    assertIsFailure(
        t.map(
            v -> {
              throw new Exception("Oh no!!!");
            }));
  }

  @Test
  public void flatMap() {
    assertEquals(message.toUpperCase(), t.flatMap(v -> new Success<>(v.toUpperCase())).orNull());
  }

  @Test
  public void flatMap_failure() {
    assertIsFailure(
        t.flatMap(
            v -> {
              throw new Exception("Oh no!!!");
            }));
  }

  @Test
  public void recover() {
    // should be exactly the same instance
    assertEquals(t, t.recover(ex -> ex.toString()));
  }

  @Test
  public void recoverWith() {
    // should be exactly the same instance
    assertEquals(t, t.recoverWith(ex -> new Success<>(ex.toString())));
  }

  @Test
  public void stream() {
    assertEquals(1, t.stream().count());
  }

  @Test
  public void stream_ofNull() {
    Stream<String> stream = new Success<String>(null).stream();
    assertEquals(1, stream.count());
  }

  @Test
  public void t_toString() {
    System.out.println(t);
    assertNotNull(t.toString());
  }
}
