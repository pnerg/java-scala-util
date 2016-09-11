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

import javascalautils.BaseAssert;
import javascalautils.Try;
import org.junit.Test;

/**
 * Test the class {@link ReflectionUtil}.
 * 
 * @author Peter Nerg
 *
 */
public class TestReflectionUtil extends BaseAssert {

    @Test
    public void createInstance() throws ReflectiveOperationException {
        assertPrivateConstructor(ReflectionUtil.class);
    }

    @Test
    public void newInstance_success() throws Throwable {
        Try<TestReflectionUtil> t = ReflectionUtil.newInstance(TestReflectionUtil.class.getName());
        assertTrue(t.isSuccess());
        assertEquals(TestReflectionUtil.class.getName(), t.get().getClass().getName());
    }

    @Test
    public void newInstance_failure() {
        assertTrue(ReflectionUtil.newInstance("com.foo.NoSuchClass").isFailure());
    }

}
