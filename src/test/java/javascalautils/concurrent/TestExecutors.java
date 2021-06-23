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
import javascalautils.Try;
import javascalautils.TryAsserts;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import static javascalautils.TryCompanion.Try;
/**
 * Test the class {@link Executors}.
 *
 * @author name Peter Nerg
 */
public class TestExecutors extends BaseAssert implements TryAsserts {
  @Test
  public void createInstance() throws ReflectiveOperationException {
    assertPrivateConstructor(Executors.class);
  }

  @Test
  public void createCachedThreadPoolExecutor() {
    Executor executor =
        Executors.createCachedThreadPoolExecutor(
            r -> new Thread(r, "createCachedThreadPoolExecutor"));
    destroyExecutor(executor);
  }

  @Test
  public void createFixedThreadPoolExecutor() {
    Executor executor =
        Executors.createFixedThreadPoolExecutor(
            5, r -> new Thread(r, "createFixedThreadPoolExecutor"));
    destroyExecutor(executor);
  }

  @Test
  public void create() {
    Executor executor = Executors.create(r -> r.run());
    destroyExecutor(executor);
  }

  @Test
  public void createDefaultExecutor_default() {
    Executor executor = Executors.createDefaultExecutor();
    destroyExecutor(executor);
  }

  @Test
  public void createExecutorFromProvider() {
    Try<Executor> t = Executors.createExecutorFromProvider(DummyExecutorProvider.class.getName());
    assertIsSuccess(t);
    t.forEach(TestExecutors::destroyExecutor);
  }

  @Test
  public void createExecutorFromProvider_illegalclass() {
    Try<Executor> t = Executors.createExecutorFromProvider("no-such-class");
    assertIsFailure(t);
  }

  @Test
  public void createExecutorFromProvider_null() {
    Try<Executor> t = Executors.createExecutorFromProvider(null);
    assertIsFailure(t);
  }

  private static void destroyExecutor(Executor executor) {
    Try(
        () -> {
          executor.shutdown();
          executor.awaitTermination(666, TimeUnit.MILLISECONDS);
        });
  }

  static class DummyExecutorProvider implements ExecutorProvider {
    @Override
    public Executor create() {
      return Executors.create(r -> r.run());
    }
  }
}
