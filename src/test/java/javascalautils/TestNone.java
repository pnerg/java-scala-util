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

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Test the class {@link None}.
 * 
 * @author Peter Nerg
 */
public class TestNone extends BaseAssert {
    private final Option<String> option = new None<String>();

    @Test
    public void contains() {
        assertFalse(option.contains("Will anyways always be false"));
    }

    @Test
    public void count() {
        assertEquals(0, option.count());
    }

    @Test
    public void exists() {
        assertFalse(option.exists(s -> true));
    }

    @Test
    public void filter() {
        // should still be option
        Option<String> o = option.filter(s -> true);
        assertFalse(o.isDefined());
    }

    @Test
    public void filterNot() {
        // should still be option
        Option<String> o = option.filterNot(s -> false);
        assertFalse(o.isDefined());
    }

    @Test
    public void forall() {
        assertFalse(option.forall(value -> true));
    }

    @Test
    public void foreach() {
        ArrayList<String> list = new ArrayList<>();
        option.forEach(s -> list.add(s));
        assertTrue(list.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void get() {
        option.get();
    }

    @Test
    public void getOrElse() {
        assertEquals("NO-VALUE", option.getOrElse(() -> "NO-VALUE"));
    }

    @Test
    public void isDefined() {
        assertFalse(option.isDefined());
    }

    @Test
    public void iterator() {
        assertFalse(option.iterator().hasNext());
    }

    @Test
    public void map() {
        assertFalse(option.map(v -> 666).isDefined());
    }

    @Test
    public void isEmpty() {
        assertTrue(option.isEmpty());
    }

    @Test
    public void orElse() {
        Option<String> orelse = option.orElse(() -> Option.apply("peter"));
        assertEquals("peter", orelse.get());
    }

    @Test
    public void stream() {
        assertEquals(0, option.stream().count());
    }

    @Test
    public void orNull() {
        assertNull(option.orNull());
    }

    @Test
    public void asOptional() {
        assertFalse(option.asOptional().isPresent());
    }
}
