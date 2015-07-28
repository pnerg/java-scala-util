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

/**
 * A task that returns either a successful or failed result. <br>
 * Implementors define a single method with called <tt>execute</tt>. <br>
 * The <tt>Executable</tt> interface is similar to {@link java.lang.Runnable} and {@link java.util.concurrent.Callable}, in that both are designed for classes
 * whose instances are potentially executed by another thread. <br>
 * However this class allows the user to respond with either {@link Promise#success(Object) success} or {@link Promise#failure(Throwable) failure}.
 * 
 * @see Executor
 * @author Peter Nerg
 * @since 1.2
 * @param <T>
 *            The type this executable is expected to work on
 */
@FunctionalInterface
public interface Executable<T> {

    /**
     * Execute the job. <br>
     * Any response needs to be reported via either {@link Promise#success(Object) Promise,success(T)} or {@link Promise#failure(Throwable)}. <br>
     * When this method has finished it is imperative that the implementation has reported either failure/success. If not the framework will automatically
     * report the execution as a failure due to a missing response.
     * 
     * @param promise
     *            The promise to report the response to
     * @since 1.2
     */
    void execute(Promise<T> promise);

}
