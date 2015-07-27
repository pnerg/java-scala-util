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
 * Test the class {@link Success}
 * 
 * @author Peter Nerg
 */
public class TestSuccess extends BaseAssert {

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
        Try<Throwable> failed = t.failed();
        assertTrue(failed.isFailure());
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
        assertEquals(message.toUpperCase(), t.map(v -> v.toUpperCase()).orNull());
    }

    @Test
    public void flatMap() {
        assertEquals(message.toUpperCase(), t.flatMap(v -> new Success<>(v.toUpperCase())).orNull());
    }

    @Test
    public void recover() {
        // should be exactly the same instance
        assertEquals(t, t.recover(ex -> ex.toString()));
    }
}
