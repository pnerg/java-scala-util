/**
 * Copyright 2015 Peter Nerg
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javascalautils;

/**
 * Acts as a Scala type companion object for {@link Left}/{@link Right}. <br>
 * The primary purpose is to get the Scala feel of instantiating classes. <br>
 * In Scala you can define a companion object for a class, acting as a static reference/singleton
 * for that class allowing you do define factory methods.<br>
 * One use case is to define methods with the same name as the class and let these methods invoke
 * the constructor thus creating a nice way to create instances without using the word "new". <br>
 * This can be achieved in java by statically importing a method and then using it. <br>
 * The limitation is that classes may not have method with the same name as the class itself hence
 * new companion classes have to be created. <br>
 * To be able to use it in a neat concise way one needs to statically import the method.
 *
 * <blockquote>
 *
 * <pre>
 * import static javascalautils.EitherCompanion.Left;
 * import static javascalautils.EitherCompanion.Right;
 *
 * Either&lt;InputStream,String&gt; left = Left(new FileInputStream("foo.bar"));
 * Either&lt;InputStream,String&gt; right = Right("Right is not Left");
 * </pre>
 *
 * </blockquote>
 *
 * @author Peter Nerg
 * @since 1.3
 */
public final class EitherCompanion {

  private EitherCompanion() {}

  /**
   * Creates an instance of {@link Left}. <br>
   * Best used in conjunction with statically importing this method.
   *
   * <blockquote>
   *
   * <pre>
   * import static javascalautils.EitherCompanion.Left;
   *
   * Either&lt;InputStream,String&gt; left = Left(new FileInputStream("foo.bar"));
   * </pre>
   *
   * </blockquote>
   *
   * @param <L> The type for the {@link Left} side (not used for this method)
   * @param <R> The type for the {@link Right} side
   * @param value The value represented by this Left
   * @return The Left representing the value
   * @see Left#Left(Object)
   * @since 1.3
   */
  public static <L, R> Left<L, R> Left(L value) {
    return new Left<>(value);
  }

  /**
   * Creates an instance of {@link Right}. <br>
   * Best used in conjunction with statically importing this method.
   *
   * <blockquote>
   *
   * <pre>
   * import static javascalautils.EitherCompanion.Right;
   *
   * Either&lt;InputStream,String&gt; right = Right("Right is not Left");
   * </pre>
   *
   * </blockquote>
   *
   * @param <L> The type for the {@link Left} side (not used for this method)
   * @param <R> The type for the {@link Right} side
   * @param value The value represented by this Right
   * @return The Right representing the value
   * @see Right#Right(Object)
   * @since 1.3
   */
  public static <L, R> Right<L, R> Right(R value) {
    return new Right<>(value);
  }
}
