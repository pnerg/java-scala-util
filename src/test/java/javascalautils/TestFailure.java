/**
 * Copyright 2015 Peter Nerg
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javascalautils;

import org.junit.Test;

/**
 * Test the class {@link Failure}
 * 
 * @author Peter Nerg
 */
public class TestFailure extends BaseAssert {

    private final Exception exception = new Exception("Error, terror!");
    private final Try<String> t = new Failure<>(exception);

    @Test(expected = IllegalArgumentException.class)
    public void create_withNullValue() {
        new Failure<Object>(null);
    }

    @Test
    public void isSuccess() {
        assertFalse(t.isSuccess());
    }

    @Test
    public void isFailure() {
        assertTrue(t.isFailure());
    }

    @Test(expected = Exception.class)
    public void get() throws Throwable {
        t.get();
    }

    @Test
    public void getOrElse() {
        String defaultText = "Default since we're testing a failure";
        assertEquals(defaultText, t.getOrElse(() -> defaultText));
    }

    @Test
    public void orElse() {
        Success<String> defaultVal = new Success<String>("Default since we're testing a failure");
        assertEquals(defaultVal, t.orElse(() -> defaultVal));
    }

    @Test
    public void failed() throws Throwable {
        Try<Throwable> result = t.failed();
        assertTrue(result.isSuccess());
        assertEquals(exception, result.get());
    }

    @Test
    public void filter() {
        // the filter doesn't matter as we're anyways a failure
        assertEquals(t, t.filter(s -> true));
    }

    @Test
    public void toString_withValue() {
        assertNotNull(t.toString());
    }

    @Test
    public void asOption() {
        assertFalse(t.asOption().isDefined());
    }

    @Test
    public void map() {
        // mapping a failure only returns itself anyways
        assertTrue(t.map(v -> null).isFailure());
    }

    @Test
    public void flatMap() {
        // mapping a failure only returns itself anyways
        assertTrue(t.flatMap(v -> null).isFailure());
    }

    @Test
    public void recover() throws Throwable {
        // should recover to contain the exception message
        assertEquals(exception.getMessage(), t.recover(ex -> ex.getMessage()).get());
    }

    @Test
    public void recoverWith() throws Throwable {
        // should recover to contain the exception message
        assertEquals(exception.getMessage(), t.recoverWith(ex -> new Success<>(ex.getMessage())).get());
    }
}
