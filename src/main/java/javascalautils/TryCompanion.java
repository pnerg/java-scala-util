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

package javascalautils;

/**
 * Acts as a Scala type companion object for {@link Try}/{@link Success}/{@link Failure}. <br>
 * The primary purpose is to get the Scala feel of instantiating classes. <br>
 * In Scala you can define a companion object for a class, acting as a static reference/singleton for that class allowing you do define factory methods.<br>
 * One use case is to define methods with the same name as the class and let these methods invoke the constructor thus creating a nice way to create instances
 * without using the word "new". <br>
 * This can be achieved in java by statically importing a method and then using it. <br>
 * The limitation is that classes may not have method with the same name as the class itself hence new companion classes have to be created. <br>
 * To be able to use it in a neat concise way one needs to statically import the method. <blockquote>
 * 
 * <pre>
 * import static javascalautils.TryCompanion.Failure;
 * import static javascalautils.TryCompanion.Success;
 * import static javascalautils.TryCompanion.Try;
 * 
 * Try&lt;Integer&gt; t = Try(() -&gt; 9 / 3);
 * Try&lt;String&gt; ts = Success("Peter was here");
 * Try&lt;String&gt; tf = Failure(new Exception("Bad mojo!"));
 * 
 * //code that performs a side effect but has not return type can be written like this
 * Try&lt;Unit&gt; t = Try(() -&gt; {
 *    database.delete(someId);
 * });
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Peter Nerg
 * @since 1.3
 */
public final class TryCompanion {

    private TryCompanion() {
    }

    /**
     * Creates an instance of {@link Try} with a {@link Unit} return type.  <br>
     * The purpose is to allow the user to invoke a side-effecting function that may succeed/fail but has now return type. <br>
     * E.g. deleting something from a database may not have an interesting return type. One is only interested of the outcome, {@link Success}/{@link Failure}.
     * Best used in conjunction with statically importing this method. 
     * 
     * <blockquote>
     * <pre>
     * import static javascalautils.TryCompanion.Try;
     * 
     * Try&lt;Unit&gt; t = Try(() -&gt; {
     *    database.delete(someId);
     * });
     * </pre>
     * </blockquote>
     * 
     * @param function
     *            The function to render either the value <i>T</i> or raise an exception.
     * @return The resulting Try instance wrapping either {@link Unit} or an exception
     * @since 1.9
     */
    public static Try<Unit> Try(VoidFunction0 function) {
    	return Try.apply(() -> {
    		function.apply();
    		return Unit.Instance;
    	});
    }

    /**
     * Creates an instance of {@link Try} wrapping the result of the provided function. <br>
     * Best used in conjunction with statically importing this method. 
     * 
     * <blockquote>
     * <pre>
     * import static javascalautils.TryCompanion.Try;
     * 
     * Try&lt;Integer&gt; t = Try(() -&gt; 9 / 3);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the Try
     * @param function
     *            The function to render either the value <i>T</i> or raise an exception.
     * @return The resulting Try instance wrapping what the function resulted in
     * @see Try#apply(ThrowableFunction0)
     * @since 1.3
     */
    public static <T> Try<T> Try(ThrowableFunction0<T> function) {
        return Try.apply(function);
    }

    /**
     * Creates an instance of {@link Failure} wrapping the provided throwable. <br>
     * Best used in conjunction with statically importing this method. 
     * 
     * <blockquote>
     * <pre>
     * import static javascalautils.TryCompanion.Failure;
     * 
     * Try&lt;String&gt; tf = Failure(new Exception("Bad mojo!"));
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the Try
     * @param throwable
     *            The throwable to wrap
     * @return The failure instance
     * @see Failure#Failure(Throwable)
     * @since 1.3
     */
    public static <T> Failure<T> Failure(Throwable throwable) {
        return new Failure<>(throwable);
    }

    /**
     * Creates an instance of {@link Success} wrapping the provided value. <br>
     * Best used in conjunction with statically importing this method. 
     * 
     * <blockquote>
     * <pre>
     * import static javascalautils.TryCompanion.Success;
     * 
     * Try&lt;String&gt; ts = Success("Peter was here");
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the Try
     * @param value
     *            The value to wrap
     * @return The failure instance
     * @see Success#Success(Object)
     * @since 1.3
     */
    public static <T> Success<T> Success(T value) {
        return new Success<>(value);
    }

}
