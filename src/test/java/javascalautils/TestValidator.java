/**
 *  Copyright 2015 Peter Nerg
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
 * Test the class {@link Validator}
 * 
 * @author Peter Nerg
 */
public class TestValidator extends BaseAssert {
    private final Object validObject = new Object();

    @Test
    public void createInstance() throws ReflectiveOperationException {
        assertPrivateConstructor(Validator.class);
    }

    @Test
    public void requireNonNull_success() {
        assertEquals(validObject, Validator.requireNonNull(validObject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void requireNonNull_fail() {
        Validator.requireNonNull(null);
    }

    @Test
    public void requireNonNull_withCustomMsg_success() {
        assertEquals(validObject, Validator.requireNonNull(validObject, "whatever-never-used"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void requireNonNull_withCustomMsg_fail() {
        Validator.requireNonNull(null, "Not a valid argument");
    }
}
