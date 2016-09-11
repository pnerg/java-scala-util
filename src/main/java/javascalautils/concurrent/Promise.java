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

import javascalautils.Failure;
import javascalautils.Success;
import javascalautils.Try;

/**
 * The Promise is the promise to deliver a value at some time in the future. <br>
 * This is the handle the actual computation side of of the job uses. <br>
 * Once a job is finished it will report the outcome to the Promise which in turn relays it to the Future the client/application is monitoring.<br>
 * A promise can be fulfilled either by invoking success or failure but not both. <br>
 * Nor can {@link #success(Object) success}/{@link #failure(Throwable) failure} be invoked twice. <br>
 * The principle is that a {@link Promise} will deliver exactly one <i>successful</i> or <i>failure</i> response. <br>
 * The successful response is of any type whilst the failure is expected to be of type (or subclass of) {@link Throwable}. <br>
 * <br>
 * Together with a {@link Future} this allows a safe publication of asynchronously calculated results into another thread. <br>
 * The basic principle is to first create a {@link Promise}. <br>
 * <blockquote>Promise&#60;String&#62; promise = Promise.apply();</blockquote> Using that instance one can get hold of the {@link Future} that is the container
 * for the value-to-be. <br>
 * <blockquote>Future&#60;String&#62; future = promise.future();</blockquote> To complete the Promise and by extension completing the Future one can use any of
 * several methods:
 * <ul>
 * <li>{@link #success(Object)}</li>
 * <li>{@link #failure(Throwable)}</li>
 * <li>{@link #complete(Try)}</li>
 * <li>{@link #completeWith(Future)}</li>
 * </ul>
 * E.g. <blockquote>promise.success("Peter was here");</blockquote> Note that only <u>one</u> of the methods may be invoked as the Promise can only be fulfilled
 * once. <br>
 * The above methods come with a variant <i>tryNNN</i> which allows for multiple invocations without raising an exception. <br>
 * Though still only the first invocation/completion counts.
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
     *            The type this Promise will produce as result
     * @return The Promise instance
     * @since 1.2
     */
    static <T> Promise<T> apply() {
        return new PromiseImpl<>();
    }

    /**
     * Get a {@link Future} that will hold the value once this Promise is completed. <br>
     * Each {@link Promise} is connected to a single {@link Future}, invoking this method multiple times will always return the same {@link Future} instance.
     * 
     * @return A Future that will hold the value once this Promise is completed.
     * @since 1.2
     */
    Future<T> future();

    /**
     * Check if the {@link Promise} have been completed, with a value or an exception.
     * 
     * @return <code>true</code> if the {@link Promise} has been completed. <code>false</code> otherwise.
     * @since 1.2
     */
    boolean isCompleted();

    /**
     * Completes the {@link Promise} with either a {@link Success} or a {@link Failure}.
     * 
     * @param result
     *            The result to complete with.
     * @throws IllegalStateException
     *             Thrown if the Promise is already completed.
     * @since 1.3
     */
    void complete(Try<T> result);

    /**
     * Completes the {@link Promise} with the value from the provided {@link Future} once that is completed.
     * 
     * @param future
     *            The future whose value will complete this Promise
     * @throws IllegalStateException
     *             Thrown if the Promise is already completed.
     * @since 1.3
     */
    void completeWith(Future<T> future);

    /**
     * Completes the {@link Promise} with a value.
     * 
     * @param result
     *            The value to complete with.
     * @throws IllegalStateException
     *             Thrown if the Promise is already completed.
     * @since 1.2
     */
    void success(T result);

    /**
     * Completes the {@link Promise} with an exception.
     * 
     * @param throwable
     *            The Throwable to complete with.
     * @throws IllegalStateException
     *             Thrown if the Promise is already completed.
     * @since 1.2
     */
    void failure(Throwable throwable);

    /**
     * Tries to complete the {@link Promise} with either a {@link Success} or a {@link Failure}. <br>
     * Contrary to the {@link #complete(Try)} method this does not throw an exception in case the Promise is already completed.
     * 
     * @param result
     *            The result to complete with.
     * @return <code>true</code> if the Promise was not completed before, <code>false</code> otherwise
     * @since 1.3
     */
    boolean tryComplete(Try<T> result);

    /**
     * Tries to complete this {@link Promise} with the value from the provided {@link Future} once that is completed. <br>
     * Contrary to the {@link #complete(Try)} method this does not throw an exception in case the Promise is already completed.
     * 
     * @param future
     *            The future whose value will complete this Promise
     * @return <code>true</code> if the Promise was not completed before, <code>false</code> otherwise
     * @since 1.3
     */
    boolean tryCompleteWith(Future<T> future);

    /**
     * Tries to complete the {@link Promise} with a value. <br>
     * Contrary to the {@link #success(Object)} method this does not throw an exception in case the Promise is already completed.
     * 
     * @param result
     *            The value to complete with.
     * @return <code>true</code> if the Promise was not completed before, <code>false</code> otherwise
     * @since 1.3
     */
    boolean trySuccess(T result);

    /**
     * Tries to complete the {@link Promise} with an exception. <br>
     * Contrary to the {@link #failure(Throwable)} method this does not throw an exception in case the Promise is already completed.
     * 
     * @param throwable
     *            The Throwable to complete with.
     * @return <code>true</code> if the Promise was not completed before, <code>false</code> otherwise
     * @since 1.3
     */
    boolean tryFailure(Throwable throwable);
}
