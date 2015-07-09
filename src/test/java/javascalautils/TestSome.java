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
import java.util.Iterator;

import org.junit.Test;

/**
 * Test the class {@link Some}
 * 
 * @author Peter Nerg (epknerg)
 */
public class TestSome extends BaseAssert {
    private final Option<String> option = new Some<String>("Peter Rulez!");

    @Test
    public void contains_true() {
        assertTrue(option.contains("Peter Rulez!"));
    }

    @Test
    public void contains_false() {
        assertFalse(option.contains("Whatever"));
    }

    @Test
    public void count() {
        assertEquals(1, option.count());
    }

    @Test
    public void exists() {
        assertTrue(option.exists(s -> true));
    }

    @Test
    public void filter() {
        // should still be option
        Option<String> o = option.filter(s -> true);
        assertTrue(o.isDefined());
    }

    @Test
    public void filter_nomatch() {
        // should still be option
        Option<String> o = option.filter(s -> false);
        assertFalse(o.isDefined());
    }

    @Test
    public void filterNot() {
        // should still be option
        Option<String> o = option.filterNot(s -> false);
        assertTrue(o.isDefined());
    }

    @Test
    public void filterNot_nomatch() {
        // should still be option
        Option<String> o = option.filterNot(s -> true);
        assertFalse(o.isDefined());
    }

    @Test
    public void forall() {
        assertTrue(option.forall(value -> true));
    }

    @Test
    public void foreach() {
        ArrayList<String> list = new ArrayList<>();
        option.forEach(s -> list.add(s));
        assertCollection(list, 1);
    }

    @Test
    public void get() {
        assertEquals("Peter Rulez!", option.get());
    }

    @Test
    public void getOrElse() {
        assertEquals("Peter Rulez!", option.getOrElse(() -> "NO-VALUE"));
    }

    @Test
    public void isDefined() {
        assertTrue(option.isDefined());
    }

    @Test
    public void iterator() {
        Iterator<String> iterator = option.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("Peter Rulez!", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void map() {
        assertTrue(option.map(v -> 666).isDefined());
    }

    @Test
    public void isEmpty() {
        assertFalse(option.isEmpty());
    }

    @Test
    public void orElse() {
        Option<String> orelse = option.orElse(() -> Option.apply("peter"));
        assertEquals("Peter Rulez!", orelse.get());
    }

    @Test
    public void stream() {
        assertEquals(1, option.stream().count());
    }

    @Test
    public void orNull() {
        assertEquals("Peter Rulez!", option.orNull());
    }

    @Test
    public void asOptional() {
        assertTrue(option.asOptional().isPresent());
    }
}
