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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Executor service used to execute work in asynchronous fashion. <br>
 * In contrast to the {@link java.util.concurrent.Executor} service provided by the Java SDK this returns non-blocking {@link Future} instances.
 * 
 * @author Peter Nerg
 * @since 1.2
 */
public interface Executor {

    /**
     * Executes the provided {@link Executable} sometime in the future.
     * 
     * @param <T>
     *            The type the Future is expected to deliver
     * @param executable
     *            The executable to execute
     * @return The future acting as a place holder for a response-to-be
     */
    <T> Future<T> execute(Executable<T> executable);

    /**
     * Executes the provided {@link Callable} sometime in the future.
     * 
     * @param <T>
     *            The type the Future is expected to deliver
     * @param callable
     *            The callable to execute
     * @return The future acting as a place holder for a response-to-be
     */
    <T> Future<T> execute(Callable<T> callable);

    /**
     * Executes the list of provided {@link Executable} sometime in the future.
     * 
     * @param <T>
     *            The type the Future is expected to deliver
     * @param executables
     *            The list of executables to execute
     * @return List of futures acting as a place holder for a response-to-be
     */
    <T> List<Future<T>> executeAll(@SuppressWarnings("unchecked") Executable<T>... executables);

    /**
     * Executes the list of provided {@link Callable} sometime in the future.
     * 
     * @param <T>
     *            The type the Future is expected to deliver
     * @param callables
     *            The list of callables to execute
     * @return List of futures acting as a place holder for a response-to-be
     */
    <T> List<Future<T>> executeAll(@SuppressWarnings("unchecked") Callable<T>... callables);

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted. Invocation has no additional effect if
     * already shut down.
     * 
     */
    void shutdown();

    /**
     * Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs, or the current thread is interrupted, whichever happens
     * first.
     * 
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return <tt>true</tt> if this executor terminated and <tt>false</tt> if the timeout elapsed before termination
     * @throws InterruptedException
     *             if interrupted while waiting
     */
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
}
