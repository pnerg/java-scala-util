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
import javascalautils.Success;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Test the class {@link PromiseImpl}
 * 
 * @author Peter Nerg
 */
public class TestPromiseImpl extends BaseAssert {
    private final Promise<String> promise = Promise.apply();

    @Test
    public void future() {
        assertNotNull(promise.future());
    }

    @Test
    public void isCompleted_notCompleted() {
        assertFalse(promise.isCompleted());
    }

    /**
     * Test a {@link Promise#success(Object)} invocation
     */
    @Test
    public void success_once() {
        promise.success("Things went well, wohooo!");
        assertTrue(promise.isCompleted());
    }

    /**
     * Test two {@link Promise#success(Object)} invocations, should render an exception on the second.
     */
    @Test(expected = IllegalStateException.class)
    public void success_twice() {
        success_once();
        promise.success("Ooops a second response, bummer");
    }

    /**
     * Test a {@link Promise#failure(throwable)} invocation
     */
    @Test
    public void failure_once() {
        promise.failure(new Exception("What a sad day!"));
        assertTrue(promise.isCompleted());
    }

    /**
     * Test two {@link Promise#failure(Throwable)} invocations, should render an exception on the second.
     */
    @Test(expected = IllegalStateException.class)
    public void failure_twice() {
        failure_once();
        promise.failure(new Exception("Scheit, another exception"));
    }

    /**
     * Test a {@link Promise#success(Object)} invocation, followed by a {@link Promise#failure(Throwable)} invocation, should render an exception on the second
     */
    @Test(expected = IllegalStateException.class)
    public void success_thenFailure() {
        success_once();
        failure_once();
    }

    /**
     * Test a {@link Promise#failure(throwable)} invocation, followed by a {@link Promise#success(Object)} invocation, should render an exception on the second
     */
    @Test(expected = IllegalStateException.class)
    public void failure_thenSuccess() {
        failure_once();
        success_once();
    }

    /**
     * Test a {@link Promise#complete(javascalautils.Try)} invocation
     */
    @Test
    public void complete_once() {
        promise.complete(new Success<>("wohooo!"));
        assertTrue(promise.isCompleted());
    }

    /**
     * Test two {@link Promise#complete(javascalautils.Try)} invocations, should render an exception on the second.
     */
    @Test(expected = IllegalStateException.class)
    public void complete_twice() {
        complete_once();
        promise.complete(new Success<>("Ooops a second response, bummer"));
    }

    @Test
    public void completeWith_once() throws TimeoutException, Throwable {
        FutureImpl<String> future = new FutureImpl<>();
        promise.completeWith(future);
        future.complete(new Success<>("Amazing stuff this!"));
        assertTrue(promise.isCompleted());
        assertEquals("Amazing stuff this!", promise.future().result(1, TimeUnit.SECONDS));
    }

    @Test(expected = IllegalStateException.class)
    public void completeWith_twice() throws TimeoutException, Throwable {
        completeWith_once();
        promise.completeWith(new FutureImpl<>());
    }

    @Test
    public void tryComplete_once() {
        assertTrue(promise.tryComplete(new Success<>("wohooo!")));
        assertTrue(promise.isCompleted());
    }

    @Test
    public void tryComplete_twice() {
        tryComplete_once();
        assertFalse(promise.tryComplete(new Success<>("Ooops a second response, bummer")));
    }

    @Test
    public void trySuccess_once() {
        assertTrue(promise.trySuccess("wohooo!"));
        assertTrue(promise.isCompleted());
    }

    @Test
    public void trySuccess_twice() {
        trySuccess_once();
        assertFalse(promise.trySuccess("Ooops a second response, bummer"));
    }

    @Test
    public void tryFailure_once() {
        assertTrue(promise.tryFailure(new Exception("What a sad day!")));
        assertTrue(promise.isCompleted());
    }

    @Test
    public void tryFailure_twice() {
        tryFailure_once();
        assertFalse(promise.tryFailure(new Exception("Scheit, another exception")));
    }

    @Test
    public void future_listenerThrowsExceptionOnComplete() {
    	promise.future().onSuccess(s -> {
    		//simulates an application that misbehaves and throws an exception when in the event/result listener
    		throw new NullPointerException("Null sucks!!!");
    	});
    	
    	//should yield nothing to us even though the result listener throws an exception
    	promise.success("Yippikaye!!!");
    }
    
    @Test
    public void t_toString() {
        assertNotNull(promise.toString());
    }
}
