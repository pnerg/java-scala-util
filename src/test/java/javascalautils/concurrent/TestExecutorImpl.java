/**
 * Copyright 2015 Peter Nerg
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javascalautils.concurrent;

import javascalautils.BaseAssert;
import javascalautils.DummyException;
import org.junit.After;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Test the class {@link ExecutorImpl}.
 *
 * @author name Peter Nerg
 */
public class TestExecutorImpl extends BaseAssert {
  /** The string message/response used for testing. */
  private static final String Expected_Message = "Peter is da king";

  // providing an implicit executor that in practice runs the runnable in the invoking thread
  private final ExecutorImpl executor = new ExecutorImpl(r -> r.run());

  @After
  public void after() throws InterruptedException {
    executor.shutdown();
    assertTrue(executor.awaitTermination(666, TimeUnit.MILLISECONDS));
  }

  @Test
  public void execute_executable() throws InterruptedException, Throwable {
    Future<String> future = executor.execute(p -> p.success(Expected_Message));
    assertEquals(Expected_Message, future.result(5, TimeUnit.MILLISECONDS));
  }

  @Test(expected = IllegalStateException.class)
  public void execute_executableNoResponse() throws InterruptedException, Throwable {
    // the executable will not respond causing it to be completed with IllegalStateException
    Future<String> future = executor.execute(p -> doNothing());
    future.result(5, TimeUnit.MILLISECONDS);
  }

  @Test(expected = DummyException.class)
  public void execute_executableWithException() throws InterruptedException, Throwable {
    // the executable will throw a DummyException
    Future<String> future = executor.execute(p -> throwDummyException());
    future.result(5, TimeUnit.MILLISECONDS);
  }

  @Test
  public void execute_callable() throws InterruptedException, Throwable {
    Future<String> future = executor.execute(() -> Expected_Message);
    assertEquals(Expected_Message, future.result(5, TimeUnit.MILLISECONDS));
  }

  @Test(expected = DummyException.class)
  public void execute_callableWithException() throws InterruptedException, Throwable {
    Future<String> future = executor.execute(() -> throwDummyException());
    future.result(5, TimeUnit.MILLISECONDS);
  }

  @Test(expected = RejectedExecutionException.class)
  public void execute_rejectedExecution() throws InterruptedException, Throwable {
    // creates an executor that always fails on execution
    ExecutorImpl localExecutor =
        new ExecutorImpl(r -> throwException(new RejectedExecutionException("Simulated error")));

    // The actual executable doesn't matter as we won't get there anyways
    Future<String> future = localExecutor.execute(p -> doNothing());
    future.result(5, TimeUnit.MILLISECONDS);
  }

  @Test
  public void executeAll_executable() throws InterruptedException, Throwable {
    // assert both responses
    List<Future<String>> result =
        executor.executeAll(p -> p.success(Expected_Message), p -> p.success(Expected_Message));
    assertCollection(result, 2);

    for (Future<String> future : result) {
      assertEquals(Expected_Message, future.result(5, TimeUnit.MILLISECONDS));
    }
  }

  @Test
  public void executeAll_callable() throws InterruptedException, Throwable {
    // assert both responses
    List<Future<String>> result =
        executor.executeAll(() -> Expected_Message, () -> Expected_Message);
    assertCollection(result, 2);
    for (Future<String> future : result) {
      assertEquals(Expected_Message, future.result(5, TimeUnit.MILLISECONDS));
    }
  }
}
