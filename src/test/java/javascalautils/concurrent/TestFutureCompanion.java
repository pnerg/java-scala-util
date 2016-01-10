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

import static javascalautils.concurrent.FutureCompanion.Future;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import javascalautils.BaseAssert;
import javascalautils.Unit;

/**
 * Test the class {@link FutureCompanion}
 * 
 * @author Peter Nerg
 */
public class TestFutureCompanion extends BaseAssert {

    private static final Duration duration = Duration.ofSeconds(1);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Executor executor = new ExecutorImpl(r -> r.run());
    
    @Test
    public void createInstance() throws ReflectiveOperationException {
        assertPrivateConstructor(FutureCompanion.class);
    }

    @Test
    public void future_withTrowableFunction0_success() throws TimeoutException, Throwable {
        Future<Integer> future = Future(() -> 9 / 3);
        assertEquals(3, future.result(duration).intValue());
    }

    @Test
    public void future_withTrowableFunction0AndExecutor_success() throws TimeoutException, Throwable {
        Future<Integer> future = FutureCompanion.Future(() -> 9 / 3, executor);
        assertEquals(3, future.result(duration).intValue());
    }

    @Test
    public void future_withVoidFunction0_success() throws TimeoutException, Throwable {
        Future<Unit> future = Future(() -> {
        	@SuppressWarnings("unused")
			int x = 9 / 3;
        });
        assertEquals(Unit.Instance, future.result(duration));
    }

    @Test
    public void future_withVoidFunction0AndExecutor_success() throws TimeoutException, Throwable {
        Future<Unit> future = Future(() -> {
        	@SuppressWarnings("unused")
			int x = 9 / 3;
        }, executor);
        assertEquals(Unit.Instance, future.result(duration));
    }
    
    @Test
    public void future_ofJDKFuture() throws TimeoutException, Throwable {
        java.util.concurrent.Future<Integer> jfuture = executorService.submit(() -> 9 / 3);
        Future<Integer> future = FutureCompanion.Future(jfuture);
        assertEquals(3, future.result(duration).intValue());
    }

    @Test
    public void future_ofJDKFuture_withExector() throws TimeoutException, Throwable {
        java.util.concurrent.Future<Integer> jfuture = executorService.submit(() -> 9 / 3);
        Future<Integer> future = FutureCompanion.Future(jfuture, executor);
        assertEquals(3, future.result(duration).intValue());
    }
}
