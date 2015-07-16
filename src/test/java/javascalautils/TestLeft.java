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
 * Test the class {@link Left}
 * 
 * @author Peter Nerg
 *
 */
public class TestLeft extends BaseAssert {
    private final String TXT = "Left is not Right";
    private final Either<String, Object> either = new Left<>(TXT);

    @Test
    public void isRight() {
        assertFalse(either.isRight());
    }

    @Test
    public void fold() {
        // right function is anyways not used
        assertEquals(TXT.toUpperCase(), either.fold(v -> v.toUpperCase(), null));
    }

    @Test
    public void swap() {
        assertTrue(either.swap().isRight());
    }

    @Test
    public void t_toString() {
        assertNotNull(either.toString());
    }
}
