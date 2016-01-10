/**
 *  Copyright 2015 Peter Nerg
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
import javascalautils.ThrowableFunction0;
import javascalautils.Unit;
import javascalautils.VoidFunction0;

/**
 * Acts as a Scala type companion object for {@link Future}. <br>
 * The primary purpose is to get the Scala feel of instantiating classes. <br>
 * In Scala you can define a companion object for a class, acting as a static reference/singleton for that class allowing you do define factory methods.<br>
 * One use case is to define methods with the same name as the class and let these methods invoke the constructor thus creating a nice way to create instances
 * without using the word "new". <br>
 * This can be achieved in java by statically importing a method and then using it. <br>
 * The limitation is that classes may not have method with the same name as the class itself hence new companion classes have to be created. <br>
 * To be able to use it in a neat concise way one needs to statically import the method. <blockquote>
 * 
 * <pre>
 * import static javascalautils.FutureCompanion.Future;
 * 
 * Future&lt;Integer&gt; resultSuccess = Future(() -&gt; 9 / 3); // The Future will at some point contain: Success(3)
 * Future&lt;Integer&gt; resultFailure = Future(() -&gt; 9 / 0); // The Future will at some point contain: Failure(ArithmeticException)
 * 
 * //code that performs a side effect but has not return type can be written like this
 * Future&lt;Unit&gt; t = Future(() -&gt; {
 *    database.delete(someId);
 * });
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Peter Nerg
 * @since 1.3
 */
public final class FutureCompanion {
    private FutureCompanion() {
    }

    /**
     * Allows for easy creation of asynchronous computations that will be executed in the future. <br>
     * The purpose is to allow the user to invoke a side-effecting function that may succeed/fail but has now return type. <br>
     * E.g. deleting something from a database may not have an interesting return type. One is only interested of the outcome, {@link Success}/{@link Failure}. <br>
     * The method relays the execution to {@link Future#apply(ThrowableFunction0)}. <br>
     * Best used in conjunction with statically importing this method.
     * 
     * <blockquote>
     * <pre>
     * import static javascalautils.concurrent.FutureCompanion.Future;
     * 
     * Future&lt;Unit&gt; t = Future(() -&gt; {
     *    database.delete(someId);
     * });
     * </pre>
     * </blockquote>
     * 
     * @param function
     *            The function to render either the value <i>T</i> or raise an exception.
     * @return The future that will hold either {@link Unit} or an exception.
     * @since 1.9
     * @see Future#apply(ThrowableFunction0)
     */
    public static Future<Unit> Future(VoidFunction0 function) {
    	return Future(() -> {
    		function.apply();
    		return Unit.Instance;
    	});
    }
    
    /**
     * Allows for easy creation of asynchronous computations that will be executed in the future. <br>
     * The purpose is to allow the user to invoke a side-effecting function that may succeed/fail but has now return type. <br>
     * E.g. deleting something from a database may not have an interesting return type. One is only interested of the outcome, {@link Success}/{@link Failure}. <br>
     * The method relays the execution to {@link Future#apply(ThrowableFunction0)}. <br>
     * Best used in conjunction with statically importing this method.
     * 
     * <blockquote>
     * <pre>
     * import static javascalautils.concurrent.FutureCompanion.Future;
     * 
     * Future&lt;Unit&gt; t = Future(() -&gt; {
     *    database.delete(someId);
     * }, someExecutor);
     * </pre>
     * </blockquote>
     * 
     * @param function
     *            The function to render either the value <i>T</i> or raise an exception.
     * @param executor
     *            The executor to use to compute/execute the Future holding the provided function
     * @return The future that will hold either {@link Unit} or an exception.
     * @since 1.9
     * @see Future#apply(ThrowableFunction0)
     */
    public static Future<Unit> Future(VoidFunction0 function, Executor executor) {
    	return Future(() -> {
    		function.apply();
    		return Unit.Instance;
    	}, executor);
    }
    
    /**
     * Allows for easy creation of asynchronous computations that will be executed in the future. <br>
     * The method relays the execution to {@link Future#apply(ThrowableFunction0)}. <br>
     * Best used in conjunction with statically importing this method.
     * 
     * <blockquote>
     * 
     * <pre>
     * import static javascalautils.concurrent.FutureCompanion.Future;
     * 
     * Future&lt;Integer&gt; resultSuccess = Future(() -&gt; 9 / 3); // The Future will at some point contain: Success(3)
     * Future&lt;Integer&gt; resultFailure = Future(() -&gt; 9 / 0); // The Future will at some point contain: Failure(ArithmeticException)
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the Future
     * @param function
     *            The function to render either the value <i>T</i> or raise an exception.
     * @return The future that will hold the result provided by the function
     * @since 1.3
     * @see Future#apply(ThrowableFunction0)
     */
    public static <T> Future<T> Future(ThrowableFunction0<T> function) {
        return Future.apply(function);
    }

    /**
     * Allows for easy creation of asynchronous computations that will be executed in the future. <br>
     * The method relays the execution to {@link Future#apply(ThrowableFunction0)}. <br>
     * Best used in conjunction with statically importing this method.
     * 
     * <blockquote>
     * 
     * <pre>
     * import static javascalautils.concurrent.FutureCompanion.Future;
     * 
     * Future&lt;Integer&gt; resultSuccess = Future(() -&gt; 9 / 3, someExecutor); // The Future will at some point contain: Success(3)
     * Future&lt;Integer&gt; resultFailure = Future(() -&gt; 9 / 0, someExecutor); // The Future will at some point contain: Failure(ArithmeticException)
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the Future
     * @param function
     *            The function to render either the value <i>T</i> or raise an exception.
     * @param executor
     *            The executor to use to compute/execute the Future holding the provided function
     * @return The future that will hold the result provided by the function
     * @since 1.4
     * @see Future#apply(ThrowableFunction0, Executor)
     */
    public static <T> Future<T> Future(ThrowableFunction0<T> function, Executor executor) {
        return Future.apply(function, executor);
    }

    /**
     * Allows for converting a blocking JDK {@link java.util.concurrent.Future Future} into a non-blocking future. <br>
     * This will use the default executor to wait/block on the provided future.
     * 
     * @param <T>
     *            The type for the Future
     * @param future
     *            The blocking future
     * @return The future that will hold the result provided by the future
     * @since 1.5
     */
    public static <T> Future<T> Future(java.util.concurrent.Future<T> future) {
        return Future.apply(() -> future.get());
    }

    /**
     * Allows for converting a blocking JDK {@link java.util.concurrent.Future Future} into a non-blocking future.
     * 
     * @param <T>
     *            The type for the Future
     * @param future
     *            The blocking future
     * @param executor
     *            The executor to use to compute/execute the Future holding the provided function
     * @return The future that will hold the result provided by the future
     * @since 1.5
     */
    public static <T> Future<T> Future(java.util.concurrent.Future<T> future, Executor executor) {
        return Future.apply(() -> future.get(), executor);
    }
}
