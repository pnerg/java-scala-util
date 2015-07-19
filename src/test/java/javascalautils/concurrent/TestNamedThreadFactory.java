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
package javascalautils.concurrent;

import java.io.IOException;
import java.util.concurrent.ThreadFactory;

import javascalautils.BaseAssert;

import org.junit.Test;

/**
 * Test the class {@link NamedSequenceThreadFactory}.
 * 
 * @author Peter Nerg
 */
public class TestNamedThreadFactory extends BaseAssert {

    private final ThreadFactory factory = new NamedSequenceThreadFactory("TestNamedThreadFactory");

    @Test
    public void newThread() throws IOException {
        Thread t = factory.newThread(() -> System.currentTimeMillis());
        assertNotNull(t);
        assertEquals("TestNamedThreadFactory-1", t.getName());
    }
}
