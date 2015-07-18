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
 * A Promise that can be completed once with a value or failed once with an exception. <br>
 * Together with a {@link Future} this allows a safe publication of asynchronously calculated results into another thread.
 * 
 * @author Peter Nerg
 * @since 1.2
 * @param <T>
 *            The type this Promise will produce as result
 */
public interface Promise<T> {

    /**
     * Creates an instance of Promise. <br>
     * E.g. <code>Promise&#60;String&#62; p = Promise.apply();</code>
     * 
     * @param <T>
     *            The type the Promise is expected to deliver
     * @return The Promise instance
     */
    public static <T> Promise<T> apply() {
        return new PromiseImpl<T>();
    }

    /**
     * Get a {@link Future} that will hold the value once this Promise is completed. <br>
     * Each {@link Promise} is connected to a single {@link Future}, invoking this method multiple times will always return the same {@link Future} instance.
     * 
     * @return A Future that will hold the value once this Promise is completed.
     */
    Future<T> future();

    /**
     * Check if the {@link Promise} have been completed, with a value or an exception.
     * 
     * @return <code>true</code> if the {@link Promise} has been completed. <code>false</code> otherwise.
     */
    boolean isCompleted();

    /**
     * Completes the {@link Promise} with a value.
     * 
     * @param object
     *            The value to complete with.
     * @throws IllegalStateException
     *             Thrown if the Promise is already completed.
     * 
     */
    void success(T object);

    /**
     * Completes the {@link Promise} with an exception.
     * 
     * @param throwable
     *            The Throwable to complete with.
     * @throws IllegalStateException
     *             Thrown if the Promise is already completed.
     */
    void failure(Throwable throwable);

}