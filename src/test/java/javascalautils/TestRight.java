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
 * Test the class {@link Right}
 * 
 * @author Peter Nerg
 *
 */
public class TestRight extends BaseAssert {
    private final Either<Object, String> either = new Right<>("Right is not Left");

    @Test
    public void isRight() {
        assertTrue(either.isRight());
    }

    @Test
    public void fold() {
        // left function is anyways not used
        String mapped = either.fold(null, v -> v.toUpperCase());
        assertEquals("RIGHT IS NOT LEFT", mapped);
    }

    @Test
    public void t_toString() {
        assertNotNull(either.toString());
    }
}
