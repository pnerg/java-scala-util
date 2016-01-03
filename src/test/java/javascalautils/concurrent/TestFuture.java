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
import static javascalautils.concurrent.Future.apply;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

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
        Future<Integer> future = apply(() -> 9 / 3);
        assertEquals(3, future.result(1, SECONDS).intValue());
    }

    @Test(expected = ArithmeticException.class)
    public void apply_failure() throws TimeoutException, Throwable {
        Future<Integer> future = apply(() -> 9 / 0);
        future.result(1, SECONDS);
    }

    @Test
    public void apply_withExecutor_success() throws TimeoutException, Throwable {
        Future<Integer> future = apply(() -> 9 / 3, executor);
        assertEquals(3, future.result(1, SECONDS).intValue());
    }

    @Test(expected = ArithmeticException.class)
    public void apply_withExecutor_failure() throws TimeoutException, Throwable {
        Future<Integer> future = apply(() -> 9 / 0, executor);
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

    @Test
    public void sequence_success() throws TimeoutException, Throwable {
        Stream<Future<Integer>> stream = Stream.of(apply(() -> 9 / 3), apply(() -> 12 / 3));

        Future<Stream<Integer>> future = Future.sequence(stream);
        Stream<Integer> result = future.result(1, SECONDS);

        // assert by summing up all the results, 9/3 + 12/3 = 7
        assertEquals(7, result.mapToInt(Integer::intValue).sum());
    }

    @Test
    public void sequence_emptyInput() throws TimeoutException, Throwable {
        Stream<Future<Integer>> stream = Stream.empty();

        Future<Stream<Integer>> future = Future.sequence(stream);
        Stream<Integer> result = future.result(1, SECONDS);

        // resulting stream shall be empty
        assertEquals(0, result.count());
    }

    @Test(expected = ArithmeticException.class)
    public void sequence_failure() throws TimeoutException, Throwable {
        Stream<Future<Integer>> stream = Stream.of(apply(() -> 9 / 3), apply(() -> 12 / 0));

        // the result will fail due to one of the Futures results in division by zero
        Future<Stream<Integer>> future = Future.sequence(stream);
        future.result(1, SECONDS);
    }

    @Test
    public void traverse_success() throws TimeoutException, Throwable {
        Stream<String> stream = Stream.of("Peter", "was", "here");

        Future<Stream<Integer>> future = Future.traverse(stream, v -> apply(() -> v.length()));

        Stream<Integer> result = future.result(1, SECONDS);

        // assert by summing up all the results, 5 + 3 + 4 = 12
        assertEquals(12, result.mapToInt(Integer::intValue).sum());
    }

    @Test
    public void traverse_emptyInput() throws TimeoutException, Throwable {
        Stream<String> stream = Stream.empty();

        Future<Stream<Integer>> future = Future.traverse(stream, v -> apply(() -> v.length()));

        Stream<Integer> result = future.result(1, SECONDS);

        // resulting stream shall be empty
        assertEquals(0, result.count());
    }

    @Test(expected = ArithmeticException.class)
    public void traverse_failure() throws TimeoutException, Throwable {
        Stream<Integer> stream = Stream.of(9, 12, 20);

        // division-by-zero will cause an ArithmeticException
        Future<Stream<Integer>> future = Future.traverse(stream, v -> apply(() -> v / 0));

        future.result(1, SECONDS);
    }
    
    @Test
    public void ready_successful() throws Throwable {
    	String expected = "The Future is here";
    	Future<String> future = Future.successful(expected);
    	Future<String> ready = future.ready(1, TimeUnit.SECONDS);
    	assertEquals(expected, ready.result(Duration.ZERO));
    }

    @Test(expected = TimeoutException.class)
    public void ready_timeOut() throws TimeoutException, InterruptedException {
    	Future<String> future = new FutureImpl<>();
    	future.ready(5, TimeUnit.MILLISECONDS); //will never finish
    }
    
    @Test
    public void result_withDuration() throws TimeoutException, Throwable {
    	String expected = "The future is here";
    	assertEquals(expected, Future.successful(expected).result(Duration.ofMillis(100)));
    }
}
