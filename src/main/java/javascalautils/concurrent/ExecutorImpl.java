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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import static javascalautils.concurrent.PromiseCompanion.Promise;

/**
 * Implements the executor interface. <br>
 * Internally uses a Java concurrent executor to provide the thread execution mechanism.
 *
 * @author Peter Nerg
 * @since 1.2
 */
final class ExecutorImpl implements Executor {

  /** The internal Java concurrent executor to perform the actual work. */
  private final java.util.concurrent.Executor executor;

  /**
   * Creates an instance using the provided SDK {@link java.util.concurrent.Executor} for thread
   * management.
   *
   * @param executor The underlying executor to use
   * @since 1.2
   */
  ExecutorImpl(java.util.concurrent.Executor executor) {
    this.executor = executor;
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Executor#execute(javascalautils.concurrent.Executable)
   */
  @Override
  public <T> Future<T> execute(final Executable<T> executable) {
    final Promise<T> promise = Promise();
    return execute(
        promise,
        () -> {
          try {
            executable.execute(promise);
            // if the implementation didn't respond to the Promise we mark it as a failure.
            if (!promise.isCompleted()) {
              promise.failure(new IllegalStateException("No response was provided by the Promise"));
            }
          } catch (Exception ex) {
            promise.failure(ex);
          }
        });
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Executor#execute(java.util.concurrent.Callable)
   */
  @Override
  public <T> Future<T> execute(final Callable<T> callable) {
    final Promise<T> promise = Promise();
    return execute(
        promise,
        () -> {
          try {
            promise.success(callable.call());
          } catch (Exception ex) {
            promise.failure(ex);
          }
        });
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Executor#executeAll(javascalautils.concurrent.Executable<T>[])
   */
  @Override
  public <T> List<Future<T>> executeAll(
      @SuppressWarnings("unchecked") Executable<T>... executables) {
    List<Future<T>> responses = new ArrayList<>();
    for (Executable<T> executable : executables) {
      responses.add(execute(executable));
    }
    return responses;
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Executor#executeAll(java.util.concurrent.Callable<T>[])
   */
  @Override
  public <T> List<Future<T>> executeAll(@SuppressWarnings("unchecked") Callable<T>... callables) {
    List<Future<T>> responses = new ArrayList<>();
    for (Callable<T> callable : callables) {
      responses.add(execute(callable));
    }
    return responses;
  }

  /**
   * Executes the provided {@link Runnable} on the internal executor. <br>
   * Any issues related to the internal executor are reported to the provided {@link Promise}
   *
   * @param promise The promise to fulfill once the work is finished
   * @param runnable The runnable to execute in the executor
   * @return The Future holding the response-to-be
   */
  private <T> Future<T> execute(Promise<T> promise, Runnable runnable) {
    try {
      executor.execute(runnable);
    } catch (RejectedExecutionException ex) {
      // could be rejected due to resource starvation, report a failure
      promise.failure(ex);
    }
    return promise.future();
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Executor#shutdown()
   */
  @Override
  public void shutdown() {
    if (executor instanceof ExecutorService) {
      ((ExecutorService) executor).shutdown();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Executor#awaitTermination(long, java.util.concurrent.TimeUnit)
   */
  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    if (executor instanceof ExecutorService) {
      return ((ExecutorService) executor).awaitTermination(timeout, unit);
    }
    return true;
  }
}
