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

import static javascalautils.TryCompanion.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javascalautils.Try;
import javascalautils.Validator;

/**
 * The Promise implementation. <br>
 * Provides a reusable implementation for safely publishing results from a Promise into a Future.
 *
 * @author Peter Nerg
 * @since 1.2
 */
final class PromiseImpl<T> implements Promise<T> {

  private final FutureImpl<T> future = new FutureImpl<>();
  private final AtomicBoolean completed = new AtomicBoolean(false);

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#future()
   */
  @Override
  public Future<T> future() {
    return future;
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#isCompleted()
   */
  @Override
  public boolean isCompleted() {
    return completed.get();
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#success(java.lang.Object)
   */
  @Override
  public void success(T object) {
    complete(Success(object));
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#failure(java.lang.Throwable)
   */
  @Override
  public void failure(Throwable throwable) {
    complete(Failure(throwable));
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#complete(javascalautils.Try)
   */
  @Override
  public void complete(Try<T> result) {
    if (!tryComplete(result)) {
      throw new IllegalStateException("Attempt to complete an already completed Promise");
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#completeWith(javascalautils.concurrent.Future)
   */
  @Override
  public void completeWith(Future<T> result) {
    if (!tryCompleteWith(result)) {
      throw new IllegalStateException("Attempt to complete an already completed Promise");
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#tryComplete(javascalautils.Try)
   */
  @Override
  public boolean tryComplete(Try<T> result) {
    Validator.requireNonNull(result, "Must provide a valid result");
    return tryComplete(future -> future.complete(result));
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#tryCompleteWith(javascalautils.concurrent.Future)
   */
  @Override
  public boolean tryCompleteWith(Future<T> result) {
    return tryComplete(future -> result.onComplete(response -> future.complete(response)));
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#trySuccess(java.lang.Object)
   */
  @Override
  public boolean trySuccess(T result) {
    return tryComplete(Success(result));
  }

  /*
   * (non-Javadoc)
   *
   * @see javascalautils.concurrent.Promise#tryFailure(java.lang.Throwable)
   */
  @Override
  public boolean tryFailure(Throwable throwable) {
    return tryComplete(Failure(throwable));
  }

  /**
   * Returns a String representation of the instance.
   *
   * @since 1.2
   */
  @Override
  public String toString() {
    return "Promise:" + future;
  }

  /**
   * Internal try complete method that takes a consumer to apply the Future this Promise holds. <br>
   * Performs a check if this Promise already has been fulfilled or not.
   *
   * @param c The consumer
   * @return true if completed, false if already completed
   */
  private boolean tryComplete(Consumer<FutureImpl<T>> c) {
    if (completed.compareAndSet(false, true)) {
      // This Try is only here to manage any unforeseen exception raised by the Future event
      // listener
      // See issue#41
      Try(() -> c.accept(future));
      return true;
    }
    return false;
  }
}
