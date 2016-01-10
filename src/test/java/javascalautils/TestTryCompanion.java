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

import static javascalautils.TryCompanion.Failure;
import static javascalautils.TryCompanion.Success;
import static javascalautils.TryCompanion.Try;

import org.junit.Test;

/**
 * Test the class {@link TryCompanion}
 * 
 * @author Peter Nerg
 *
 */
public class TestTryCompanion extends BaseAssert {

    @Test
    public void createInstance() throws ReflectiveOperationException {
        assertPrivateConstructor(TryCompanion.class);
    }

    @Test
    public void try_withThrowableFunction0_success() throws Throwable {
        Try<Integer> t = Try(() -> 9 / 3);
        assertEquals(3, t.get().intValue());
    }

    @Test
    public void try_withThrowableFunction0_failure() throws Throwable {
        Try<Integer> t = Try(() -> 9 / 0);
        assertTrue(t.isFailure());
    }
    
    @Test
    public void try_withVoidFunction0_success() throws Throwable {
        Try<Unit> t = Try(() -> {
        	String s = "does nothing";
        	s.length();
        });
        assertEquals(Unit.Instance, t.get());
    }

    @Test
    public void try_withVoidFunction0_failure() throws Throwable {
        Try<Unit> t = Try(() -> {
        	//pointless function, but has no return type and will fail here
        	@SuppressWarnings("unused")
			int val = 9/0;
        });
        assertTrue(t.isFailure());
    }
    
    @Test
    public void success() throws Throwable {
        Try<Integer> t = Success(69);
        assertEquals(69, t.get().intValue());
    }

    @Test(expected = DummyException.class)
    public void failure() throws Throwable {
        Try<Integer> t = Failure(new DummyException());
        t.get();
    }
}
