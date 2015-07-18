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

import java.util.function.Consumer;

import javascalautils.Failure;
import javascalautils.None;
import javascalautils.Option;
import javascalautils.Some;
import javascalautils.Success;
import javascalautils.Try;

/**
 * A Future that holds the result of a computation executed asynchronously.
 * 
 * @author Peter Nerg
 * @since 1.2
 * @param <T>
 *            The type this Future will hold as result
 */
public interface Future<T> {

    /**
     * Check if this Future is completed, with a value or an exception.
     * 
     * @return <code>true</code> if completed, <code>false</code> otherwise.
     */
    boolean isCompleted();

    /**
     * The value of the future. <br>
     * There are three possible outcomes:
     * <ul>
     * <li>The future has not been completed -> {@link None} is returned</li>
     * <li>The execution of the Future was successful -> {@link Some} with a {@link Success} containing the value of the executed job</li>
     * <li>The execution failed and an exception was reported -> {@link Some} with a {@link Failure} containing the Throwable</li>
     * </ul>
     * 
     * @return An Option with the result.
     */
    Option<Try<T>> value();

    /**
     * Register a handler to be invoked if the Future gets completed with an exception. <br>
     * If the Future has already been completed the invocation will happen in the current thread. <br>
     * The Handler will only be invoked once. <br>
     * 
     * @param failureHandler
     *            Consumer to invoke.
     */
    void onFailure(Consumer<Throwable> failureHandler);

    /**
     * Register a handler to be invoked if the Future gets completed with a value. <br>
     * If the Future has already been completed the invocation will happen in the current thread. <br>
     * The Handler will only be invoked once. <br>
     * 
     * @param successHandler
     *            Consumer to invoke.
     */
    void onSuccess(Consumer<T> successHandler);

}
