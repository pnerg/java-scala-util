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

import java.util.Iterator;

import org.junit.Test;

/**
 * Test the class {@link RightProjection}
 * 
 * @author Peter Nerg
 */
public class TestRightProjection_WithRight extends BaseAssert {

    private final String TEXT = "Right is not Left";
    private final Either<Object, String> either = new Right<>(TEXT);
    private final RightProjection<Object, String> projection = new RightProjection<>(either);

    @Test
    public void asOption() {
        Option<String> option = projection.asOption();
        assertTrue(option.isDefined());
        assertEquals(TEXT, option.get());
    }

    @Test
    public void get() {
        assertEquals(TEXT, projection.get());
    }

    @Test
    public void orNull() {
        assertEquals(TEXT, projection.orNull());
    }

    @Test
    public void iterator() {
        Iterator<String> iterator = projection.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(TEXT, iterator.next());
    }

    @Test
    public void stream() {
        assertEquals(1, projection.stream().count());
    }

    @Test
    public void exists() {
        // provide predicate that always returns true
        assertTrue(projection.exists(v -> true));
    }

    @Test
    public void getOrElse() {
        assertEquals(TEXT, projection.getOrElse(() -> "DEFAULT"));
    }

    @Test
    public void filter() {
        // provide predicate that always returns true
        assertTrue(projection.filter(v -> true).isDefined());
    }

    @Test
    public void forAll() {
        // provide predicate that always returns true
        assertTrue(projection.forAll(v -> true));
        // provide predicate that always returns false
        assertFalse(projection.forAll(v -> false));
    }

    @Test
    public void map() {
        Either<Object, Integer> mapped = projection.map(v -> v.length());
        assertTrue(mapped.isRight());
        assertEquals(TEXT.length(), (int) mapped.right().get());
    }

    @Test
    public void flatMap() {
        Either<Object, Integer> mapped = projection.flatMap(v -> new Right<>(v.length()));
        assertTrue(mapped.isRight());
        assertEquals(TEXT.length(), (int) mapped.right().get());
    }
}
