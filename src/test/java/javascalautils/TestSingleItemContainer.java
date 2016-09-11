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

import java.util.Iterator;

/**
 * Test the class {@link SingleItemContainer}
 * 
 * @author Peter Nerg
 *
 */
public class TestSingleItemContainer extends BaseAssert {
    private final String TEST_VALUE = "Peter was here - " + System.currentTimeMillis();
    private final SingleItemContainer<String> container = new SingleItemContainer<String>() {
        @Override
        String get() {
            return TEST_VALUE;
        }
    };

    @Test
    public void iterator() {
        Iterator<String> iterator = container.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(TEST_VALUE, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void stream() {
        assertEquals(1, container.stream().count());
        assertEquals(TEST_VALUE, container.stream().iterator().next());
    }
}
