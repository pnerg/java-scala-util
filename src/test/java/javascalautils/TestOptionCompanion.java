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

import static javascalautils.OptionCompanion.None;
import static javascalautils.OptionCompanion.Option;
import static javascalautils.OptionCompanion.Some;

import org.junit.Test;

/**
 * Test the class {@link OptionCompanion}.
 * 
 * @author Peter Nerg
 *
 */
public class TestOptionCompanion extends BaseAssert {

    @Test
    public void createInstance() throws ReflectiveOperationException {
        assertPrivateConstructor(OptionCompanion.class);
    }

    @Test
    public void option() {
        Option<String> option = Option("Life is full of options");
        assertEquals("Life is full of options", option.get());
    }

    @Test
    public void some() {
        Option<String> option = Some("Some is never none");
        assertEquals("Some is never none", option.get());
    }

    @Test
    public void none() {
        Option<String> option = None();
        assertFalse(option.isDefined());
    }

}
