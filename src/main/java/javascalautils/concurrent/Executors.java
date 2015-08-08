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

import javascalautils.ReflectionUtil;
import javascalautils.Try;

/**
 * Factory for creating {@link Executor} instances.
 * 
 * @author Peter Nerg
 * @since 1.2
 */
public final class Executors {

    /** The default executor. */
    private static final Executor DefaultExecutor = createDefaultExecutor();

    /**
     * Inhibitive constructor.
     */
    private Executors() {
    }

    /**
     * Get the default thread executor. <br>
     * Unless specified the default one is created from {@link #createCachedThreadPoolExecutor(ThreadFactory)}.
     * 
     * @return The default thread executor
     * @since 1.3
     */
    public static Executor getDefault() {
        return DefaultExecutor;
    }

    /**
     * Creates an executor that uses the {@link java.util.concurrent.Executors#newCachedThreadPool(ThreadFactory)}.
     * 
     * @param threadFactory
     *            The thread factory to use.
     * @return An instance of this Executor
     * @since 1.2
     */
    public static Executor createCachedThreadPoolExecutor(ThreadFactory threadFactory) {
        return create(java.util.concurrent.Executors.newCachedThreadPool(threadFactory));
    }

    /**
     * Creates an executor that uses the {@link java.util.concurrent.Executors#newFixedThreadPool(int,ThreadFactory)}
     * 
     * @param threads
     *            The amount of threads to allow in the thread pool
     * @param threadFactory
     *            The thread factory to use.
     * @return An instance of this Executor
     * @since 1.2
     */
    public static Executor createFixedThreadPoolExecutor(int threads, ThreadFactory threadFactory) {
        return create(java.util.concurrent.Executors.newFixedThreadPool(threads, threadFactory));
    }

    /**
     * Creates an executor that uses the provided Java concurrent executor for the actual job processing
     * 
     * @param threadPool
     *            The java.util.Concurrent executor to use as thread pool
     * @return An instance of this Executor
     * @since 1.2
     */
    public static Executor create(java.util.concurrent.Executor threadPool) {
        return new ExecutorImpl(threadPool);
    }

    /**
     * Creates the default {@link Executor} instance. <br>
     * Either by getting the provider class from the system property or using the default.
     * 
     * @return
     */
    private static Executor createDefaultExecutor() {
        Try<Executor> t = ReflectionUtil.newInstance(System.getProperty("javascalautils.concurrent.executorprovider"));
        return t.getOrElse(() -> createCachedThreadPoolExecutor(new NamedSequenceThreadFactory("Executors-Default")));
    }
}
