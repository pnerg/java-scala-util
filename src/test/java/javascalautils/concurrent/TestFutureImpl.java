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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import javascalautils.BaseAssert;

import org.junit.Test;

/**
 * Test the class {@link FutureImpl}.
 * 
 * @author Peter Nerg
 *
 */
public class TestFutureImpl extends BaseAssert {
    private final FutureImpl<String> future = new FutureImpl<>();

    @Test
    public void isCompleted_notCompleted() {
        assertFalse(future.isCompleted());
    }

    @Test
    public void value_notCompleted() {
        assertFalse(future.value().isDefined());
    }

    /**
     * Simulates a success response before the success listener has been installed
     * 
     * @throws Throwable
     */
    @Test
    public void success_listenerAddedAfter() throws Throwable {
        String response = "Peter is in da house!!!";

        // simulate success response
        future.success(response);
        assertTrue(future.isCompleted());
        assertEquals(response, future.value().get().get());

        // apply a success handler and assert it immediately gets notified with the response
        AtomicBoolean gotEvent = new AtomicBoolean(false);
        future.onSuccess(s -> {
            assertEquals(response, s);
            gotEvent.set(true);
        });
        // assert that the success handler got a response
        assertTrue(gotEvent.get());
    }

    /**
     * Simulates a success response after the success listener has been installed
     * 
     * @throws Throwable
     */
    @Test
    public void success_listenerAddedBefore() throws Throwable {
        String response = "Peter is in da house!!!";

        // apply a success handler
        AtomicBoolean gotEvent = new AtomicBoolean(false);
        future.onSuccess(s -> {
            assertEquals(response, s);
            gotEvent.set(true);
        });

        // simulate success response
        future.success(response);
        assertTrue(future.isCompleted());
        assertEquals(response, future.value().get().get());

        // assert that the success handler got a response
        assertTrue(gotEvent.get());
    }

    /**
     * Simulates a success response before/after the success listener has been installed.<br>
     * That is
     * 
     * @throws Throwable
     */
    @Test
    public void success_listenerAddedBeforeAndAfter() throws Throwable {
        String response = "Peter is in da house!!!";

        SuccessHandler successHandlerBefore = new SuccessHandler();
        SuccessHandler successHandlerAfter = new SuccessHandler();

        // apply a success handler
        future.onSuccess(successHandlerBefore);

        // simulate success response
        future.success(response);
        assertTrue(future.isCompleted());
        assertEquals(response, future.value().get().get());

        // assert that the success handler1 got a response
        successHandlerBefore.assertResponse(response);

        // apply a second success handler
        future.onSuccess(successHandlerAfter);

        // assert that the second listener got a response
        successHandlerAfter.assertResponse(response);

        // assert the first listener still only got a single response
        successHandlerBefore.assertEventCount();
    }

    /**
     * Simulates a failure response after the success listener has been installed
     * 
     * @throws Throwable
     */
    @Test
    public void failure_listenerAddedAfter() throws Throwable {
        Exception error = new Exception("Error, terror!!!");

        // simulate failure response
        future.failure(error);
        assertTrue(future.isCompleted());
        assertEquals(error, future.value().get().failed().get());

        // apply a success handler and assert it immediately gets notified with the response
        AtomicBoolean gotEvent = new AtomicBoolean(false);
        future.onFailure(s -> {
            assertEquals(error, s);
            gotEvent.set(true);
        });
        // assert that the failure handler got a response
        assertTrue(gotEvent.get());
    }

    /**
     * Simulates a failure response before the success listener has been installed
     * 
     * @throws Throwable
     */
    @Test
    public void failure_listenerAddedBefore() throws Throwable {
        Exception error = new Exception("Error, terror!!!");

        // apply a success handler and assert it immediately gets notified with the response
        AtomicBoolean gotEvent = new AtomicBoolean(false);
        future.onFailure(s -> {
            assertEquals(error, s);
            gotEvent.set(true);
        });

        // simulate failure response
        future.failure(error);
        assertTrue(future.isCompleted());
        assertEquals(error, future.value().get().failed().get());

        // assert that the failure handler got a response
        assertTrue(gotEvent.get());
    }

    @Test
    public void success_withCompleteHandler() throws Throwable {
        String response = "Peter is in da house!!!";

        // apply a success handler
        AtomicBoolean gotEvent = new AtomicBoolean(false);
        future.onComplete(t -> {
            assertEquals(response, t.orNull());
            gotEvent.set(true);
        });

        // simulate success response
        future.success(response);
        assertTrue(future.isCompleted());
        assertEquals(response, future.value().get().get());

        // assert that the success handler got a response
        assertTrue(gotEvent.get());

    }

    /**
     * Simulates a success response after forEach has been invoked
     * 
     * @throws Throwable
     */
    @Test
    public void forEach() throws Throwable {
        String response = "Peter is in da house!!!";

        // apply a success handler
        AtomicBoolean gotEvent = new AtomicBoolean(false);
        future.forEach(s -> {
            assertEquals(response, s);
            gotEvent.set(true);
        });

        // simulate success response
        future.success(response);
        assertTrue(future.isCompleted());
        assertEquals(response, future.value().get().get());

        // assert that the success handler got a response
        assertTrue(gotEvent.get());
    }

    /**
     * Simulates a success response after forEach has been invoked
     * 
     * @throws Throwable
     */
    @Test
    public void map() throws Throwable {
        String response = "Peter is in da house!!!";

        // map the future to one that counts the length of the response
        Future<Integer> mapped = future.map(s -> s.length());

        // apply a success handler to the mapped future
        AtomicBoolean gotEvent = new AtomicBoolean(false);
        mapped.forEach(i -> {
            assertEquals(response.length(), i.intValue());
            gotEvent.set(true);
        });

        // simulate success response
        future.success(response);

        assertTrue(mapped.isCompleted());
        assertEquals(response.length(), mapped.value().get().get().intValue());

        // assert that the success handler got a response
        assertTrue(gotEvent.get());
    }

    private static final class SuccessHandler implements Consumer<String> {
        private final AtomicInteger eventCounter = new AtomicInteger();
        private String response;

        /*
         * (non-Javadoc)
         * 
         * @see java.util.function.Consumer#accept(java.lang.Object)
         */
        @Override
        public void accept(String response) {
            this.response = response;
            eventCounter.incrementAndGet();
        }

        private void assertEventCount() {
            assertEquals("Expected exactly one response", 1, eventCounter.get());
        }

        private void assertResponse(String expected) {
            assertEventCount();
            assertEquals(expected, response);
        }

    }
}
