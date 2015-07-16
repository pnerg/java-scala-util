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

import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Test the class {@link LeftProjection}
 * 
 * @author Peter Nerg
 */
public class TestLeftProjection_WithRight extends BaseAssert {

    private final Either<String, Object> either = new Right<>(new Object());
    private final LeftProjection<String, Object> projection = new LeftProjection<>(either);

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
        // mapping won't matter as it's a Right and will not be mapped
        assertFalse(projection.map(v -> v).isLeft());
    }
}
