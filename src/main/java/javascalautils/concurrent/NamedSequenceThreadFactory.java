/**
 * Copyright 2015 Peter Nerg
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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread factory for providing meaningful names to the created threads. <br>
 * All threads created through this factory will be given a unique identifier based on a sequence counter. <br>
 * E.g. providing the name <tt>MyThread</tt> will yield threads called <tt>MyThread-<i>n</i></tt>, where n is the sequence counter for created threads.
 * 
 * @author Peter Nerg
 */
public final class NamedSequenceThreadFactory implements ThreadFactory {
    /** Counter for creating unique thread names. */
    private final AtomicLong counter = new AtomicLong(1);

    /** The name of the threads. */
    private final String threadName;

    /**
     * Create the factory.
     * 
     * @param threadName
     *            The name of the threads to be created.
     */
    public NamedSequenceThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    /**
     * Creates a new thread using the provided name and sequence counter.
     */
    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, threadName + "-" + counter.getAndIncrement());
    }

}
