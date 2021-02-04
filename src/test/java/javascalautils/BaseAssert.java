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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Locale;

/**
 * Base test class.
 *
 * @author Peter Nerg
 */
public class BaseAssert extends Assert {

  static {
    // Configure language for proper logging outputs
    Locale.setDefault(Locale.US);
    System.setProperty("user.country", Locale.US.getCountry());
    System.setProperty("user.language", Locale.US.getLanguage());
    System.setProperty("user.variant", Locale.US.getVariant());
  }

  @BeforeClass
  public static final void setTempDirectoryToTarget() {
    System.setProperty("java.io.tmpdir", "target/");
  }

  @AfterClass
  public static final void resetTempDirectoryToTarget() {
    System.clearProperty("java.io.tmpdir");
  }

  /**
   * Asserts that the provided class has a private default (non-argument) constructor. <br>
   * This is a stupid workaround to please the coverage tools that otherwise whine about not
   * covering private constructors.
   *
   * @param clazz
   * @throws NoSuchMethodException
   * @throws SecurityException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public static <T extends Object> void assertPrivateConstructor(Class<T> clazz)
      throws ReflectiveOperationException {
    Constructor<T> constructor = clazz.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    try {
      constructor.setAccessible(true);
      constructor.newInstance();
    } finally {
      constructor.setAccessible(false);
    }
  }

  /**
   * Assert that the provided arrays do not contain the same data.
   *
   * @param expected
   * @param actual
   */
  public static void assertNotEquals(byte[] expected, byte[] actual) {
    if (expected == null && actual == null) {
      return;
    }

    // different size, can not be the same data
    if (expected.length != actual.length) {
      return;
    }

    // check each position in the arrays, return on the first found non-match
    for (int i = 0; i < actual.length; i++) {
      if (actual[i] != expected[i]) {
        return;
      }
    }
    fail("The expected and the actual array are the same");
  }

  /**
   * Assert that the provided arrays contain the same data.
   *
   * @param expected
   * @param actual
   */
  public static void assertEquals(byte[] expected, byte[] actual) {
    if (expected == null && actual == null) {
      return;
    }

    assertEquals("The length of the arrays do not match", expected.length, actual.length);
    for (int i = 0; i < actual.length; i++) {
      assertEquals("The data on index [" + i + "] does not match", expected[i], actual[i]);
    }
  }

  /**
   * Assert that the provided arrays contain the same data.
   *
   * @param expected
   * @param actual
   */
  public static void assertEquals(char[] expected, char[] actual) {
    if (expected == null && actual == null) {
      return;
    }

    assertEquals("The length of the arrays do not match", expected.length, actual.length);
    for (int i = 0; i < actual.length; i++) {
      assertEquals("The data on index [" + i + "] does not match", expected[i], actual[i]);
    }
  }

  /**
   * Assert that the provided arrays contain the same data.
   *
   * @param expected
   * @param actual
   */
  public static void assertEquals(int[] expected, int[] actual) {
    if (expected == null && actual == null) {
      return;
    }

    assertEquals("The length of the arrays do not match", expected.length, actual.length);
    for (int i = 0; i < actual.length; i++) {
      assertEquals("The data on index [" + i + "] does not match", expected[i], actual[i]);
    }
  }

  /**
   * Assert that a collection is empty.
   *
   * @param collection
   * @param expectedSize
   */
  public static void assertIsEmpty(Collection<?> collection) {
    assertNotNull(collection);
    assertTrue(collection.isEmpty());
  }

  /**
   * Assert a collection.
   *
   * @param collection
   * @param expectedSize
   */
  public static void assertCollection(Collection<?> collection, int expectedSize) {
    assertNotNull(collection);
    assertEquals(expectedSize, collection.size());
  }

  /**
   * Method that does...nothing. <br>
   * Used in test cases concerning executables that don't fulfill their Promise.
   */
  public static void doNothing() {}

  /**
   * Dummy method to raise a {@link DummyException}. <br>
   * This is used since one cannot raise exceptions directly from Lambda expressions.
   *
   * @return Never ever, since it always raises a DummyException
   */
  public static <T> T throwDummyException() {
    return throwException(new DummyException());
  }

  /**
   * Throws the provided exception. <br>
   * This is used since one cannot raise exceptions directly from Lambda expressions.
   *
   * @param ex The exception to throw
   * @return Never ever, since it always raises a DummyException
   */
  public static <T> T throwException(RuntimeException ex) {
    throw ex;
  }

  /**
   * Method used for testing to either raise a {@link DummyException} or return the provided value.
   *
   * @param arg
   * @return
   */
  public static final String throwIfNull(String arg) {
    if (arg == null) {
      throwDummyException();
    }
    return arg;
  }
}
