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
    /** Value used in this test case to populate {@link Some}. */
    private final String TEXT_VALUE = "Peter Rulez!-" + System.nanoTime();

    private final Option<String> option = new Some<String>(TEXT_VALUE);

    @Test(expected = IllegalArgumentException.class)
    public void create_withNullValue() {
        new Some<Object>(null);
    }

    @Test
    public void contains_true() {
        assertTrue(option.contains(TEXT_VALUE));
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
        assertEquals(TEXT_VALUE, option.get());
    }

    @Test
    public void getOrElse() {
        assertEquals(TEXT_VALUE, option.getOrElse(() -> "NO-VALUE"));
    }

    @Test
    public void isDefined() {
        assertTrue(option.isDefined());
    }

    @Test
    public void iterator() {
        Iterator<String> iterator = option.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(TEXT_VALUE, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void map() {
        Option<Integer> mapped = option.map(v -> v.length());
        assertTrue(mapped.isDefined());
        assertEquals(TEXT_VALUE.length(), mapped.get().intValue());
    }

    @Test
    public void flatMap() {
        Option<Integer> mapped = option.flatMap(v -> Option.apply(v.length()));
        assertTrue(mapped.isDefined());
        assertEquals(TEXT_VALUE.length(), mapped.get().intValue());
    }

    @Test
    public void isEmpty() {
        assertFalse(option.isEmpty());
    }

    @Test
    public void orElse() {
        Option<String> orelse = option.orElse(() -> Option.apply("WON'T MATTER"));
        assertEquals(TEXT_VALUE, orelse.get());
    }

    @Test
    public void stream() {
        assertEquals(1, option.stream().count());
    }

    @Test
    public void toLeft() {
        Either<String, String> either = option.toLeft(() -> "right");
        assertTrue(either.isLeft());
        assertEquals(TEXT_VALUE, either.left().get());
    }

    @Test
    public void orNull() {
        assertEquals(TEXT_VALUE, option.orNull());
    }

    @Test
    public void asOptional() {
        assertTrue(option.asOptional().isPresent());
    }

    @Test
    public void equals_true() {
        assertTrue(option.equals(new Some<String>(TEXT_VALUE)));
    }

    @Test
    public void equals_false() {
        assertFalse(option.equals(new Some<String>("NOT-THE-SAME-VALUE")));
    }

    @Test
    public void equals_false2() {
        assertFalse(option.equals(new None<String>()));
    }

    @Test
    public void t_hashCode() {
        assertEquals(31 + TEXT_VALUE.hashCode(), option.hashCode());
    }

    @Test
    public void t_toString() {
        assertNotNull(option.toString());
    }

}
