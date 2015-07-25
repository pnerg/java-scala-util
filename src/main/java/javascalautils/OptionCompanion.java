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
 * Acts as a Scala type companion object for {@link Option}/{@link Some}/{@link None}. <br>
 * The primary purpose is to get the Scala feel of instantiating classes. <br>
 * In Scala you can define a companion object for a class, acting as a static reference/singleton for that class allowing you do define factory methods.<br>
 * One use case is to define methods with the same name as the class and let these methods invoke the constructor thus creating a nice way to create instances
 * without using the word "new". <br>
 * This can be achieved in java by statically importing a method and then using it. <br>
 * The limitation is that classes may not have method with the same name as the class itself hence new companion classes have to be created. <br>
 * To be able to use it in a neat concise way one needs to statically import the method. <blockquote>
 * 
 * <pre>
 * import static javascalautils.OptionCompanion.Option;
 * import static javascalautils.OptionCompanion.Some;
 * import static javascalautils.OptionCompanion.None;
 * 
 * Option&lt;String&gt; option = Option("Life is full of options");
 * Option&lt;String&gt; some = Some("Some is never None");
 * Option&lt;String&gt; none = None();
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Peter Nerg
 * @since 1.3
 */
public final class OptionCompanion {

    private OptionCompanion() {
    }

    /**
     * Creates an instance of Option. <br>
     * If a <code>null</code> value is provided then {@link None} is returned, else {@link Some} containing the provided value.<br>
     * Best used in conjunction with statically importing this method. <blockquote>
     * 
     * <pre>
     * import static javascalautils.OptionCompanion.Option;
     * 
     * Option&lt;String&gt; option = Option("Life is full of options");
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the value this {@link Option} represents
     * @param value
     *            The value this Option shall represent
     * @return The Option representing the provided value
     * @see Option#apply(Object)
     */
    public static <T> Option<T> Option(T value) {
        return Option.apply(value);
    }

    /**
     * Creates an instance of {@link Some}. <br>
     * Best used in conjunction with statically importing this method. <blockquote>
     * 
     * <pre>
     * import static javascalautils.OptionCompanion.Some;
     * 
     * Option&lt;String&gt; some = Some("Some is never None");
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the value this {@link Some} represents
     * @param value
     *            The value represented by this Some
     * @return The Some representing the value
     * @see Some#Some(Object)
     */
    public static <T> Some<T> Some(T value) {
        return new Some<>(value);
    }

    /**
     * Returns an instance of {@link None}. <br>
     * In practice returns the singleton instance for {@link None}, never creates new instances.<br>
     * Best used in conjunction with statically importing this method. <blockquote>
     * 
     * <pre>
     * import static javascalautils.OptionCompanion.None;
     * 
     * Option&lt;String&gt; none = None();
     * </pre>
     * 
     * </blockquote>
     * 
     * @param <T>
     *            The type for the value this {@link None} represents
     * 
     * @return The singleton instance for {@link None}
     * @see Option#None()
     * @see Option#empty()
     * @see Option#DEFAULT_NONE
     */
    @SuppressWarnings("unchecked")
    public static <T> None<T> None() {
        return Option.DEFAULT_NONE;
    }

}
