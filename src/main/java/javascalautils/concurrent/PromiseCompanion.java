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

/**
 * Acts as a Scala type companion object for {@link Promise}. <br>
 * The primary purpose is to get the Scala feel of instantiating classes. <br>
 * In Scala you can define a companion object for a class, acting as a static reference/singleton for that class allowing you do define factory methods.<br>
 * One use case is to define methods with the same name as the class and let these methods invoke the constructor thus creating a nice way to create instances
 * without using the word "new". <br>
 * This can be achieved in java by statically importing a method and then using it. <br>
 * The limitation is that classes may not have method with the same name as the class itself hence new companion classes have to be created. <br>
 * To be able to use it in a neat concise way one needs to statically import the method. <blockquote>
 * 
 * <pre>
 * import static javascalautils.PromiseCompanion.Promise;
 * 
 * Promise&lt;String&gt; promise = Promise();
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Peter Nerg
 * @since 1.3
 */
public final class PromiseCompanion {
    private PromiseCompanion() {
    }

    /**
     * Creates an instance of Promise. <br>
     * Best used in conjunction with statically importing this method.
     * 
     * <blockquote>
     * 
     * <pre>
     * import static javascalautils.concurrent.FutureCompanion.Promise;
     * 
     * Promise&#60;String&#62; promise = Promise();
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type the Promise is expected to deliver
     * @return The Promise instance
     * @since 1.3
     */
    public static <T> Promise<T> Promise() {
        return Promise.apply();
    }

}
