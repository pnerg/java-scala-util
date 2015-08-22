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

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeoutException;

import javascalautils.BaseAssert;
import javascalautils.DummyException;
import javascalautils.Failure;
import javascalautils.Success;

import org.junit.Test;

/**
 * Test the class {@link Future}.
 * 
 * @author Peter Nerg
 *
 */
public class TestFuture extends BaseAssert {

    private final Executor executor = Executors.create(r -> r.run());

    @Test
    public void apply_success() throws TimeoutException, Throwable {
        Future<Integer> future = Future.apply(() -> 9 / 3);
        assertEquals(3, future.result(1, SECONDS).intValue());
    }

    @Test(expected = ArithmeticException.class)
    public void apply_failure() throws TimeoutException, Throwable {
        Future<Integer> future = Future.apply(() -> 9 / 0);
        future.result(1, SECONDS);
    }

    @Test
    public void apply_withExecutor_success() throws TimeoutException, Throwable {
        Future<Integer> future = Future.apply(() -> 9 / 3, executor);
        assertEquals(3, future.result(1, SECONDS).intValue());
    }

    @Test(expected = ArithmeticException.class)
    public void apply_withExecutor_failure() throws TimeoutException, Throwable {
        Future<Integer> future = Future.apply(() -> 9 / 0, executor);
        future.result(1, SECONDS);
    }

    @Test(expected = DummyException.class)
    public void failed() throws TimeoutException, Throwable {
        Future<String> future = Future.failed(new DummyException());
        future.result(1, SECONDS);
    }

    @Test
    public void successful() throws TimeoutException, Throwable {
        Future<String> future = Future.successful("Peter was here");
        assertEquals("Peter was here", future.result(1, SECONDS));
    }

    @Test
    public void fromTry_successful() throws TimeoutException, Throwable {
        Future<String> future = Future.fromTry(new Success<>("Peter was here"));
        assertEquals("Peter was here", future.result(1, SECONDS));
    }

    @Test(expected = DummyException.class)
    public void fromTry_failure() throws TimeoutException, Throwable {
        Future<String> future = Future.fromTry(new Failure<>(new DummyException()));
        future.result(1, SECONDS);
    }
}
