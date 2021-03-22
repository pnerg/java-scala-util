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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread factory for providing meaningful names to the created threads. <br>
 * All threads created through this factory will be given a unique identifier based on a sequence
 * counter. <br>
 * E.g. providing the name <code>MyThread</code> will yield threads called <code>MyThread-<i>n</i>
 * </code>, where <i>n</i> is the sequence counter for created threads. <br>
 * The counter starts at 1, thus the naming series will be <i>threadName-[1-n]</i>
 *
 * @author Peter Nerg
 * @since 1.2
 */
public final class NamedSequenceThreadFactory implements ThreadFactory {
  /** Counter for creating unique thread names. */
  private final AtomicLong counter = new AtomicLong(1);

  /** The name of the threads. */
  private final String threadName;

  /**
   * Create the factory.
   *
   * @param threadName The name of the threads to be created.
   * @since 1.2
   */
  public NamedSequenceThreadFactory(String threadName) {
    this.threadName = threadName;
  }

  /**
   * Creates a new thread using the provided name and sequence counter. <br>
   * E.g. <i>threadName-nnn</i>
   *
   * @since 1.2
   */
  @Override
  public Thread newThread(Runnable runnable) {
    return new Thread(runnable, threadName + "-" + counter.getAndIncrement());
  }
}
