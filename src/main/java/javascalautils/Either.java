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

import java.util.function.Function;

/**
 * Represents a value of one of two possible types. <br>
 * Instances of {@link Either} are either an instance of {@link Left} or {@link Right}. <br>
 * This is what is called a disjoint union, meaning that the Either will never contain both types only one of them. <br>
 * This is the biggest difference from a tuple that would contain both values. <br>
 * Examples of creating an {@link Either} of the type {@link Right}. <br>
 * 
 * <pre>
 * {@code
 *     Either<InputStream, String>; either = new Right<>("Right is not Left");
 * }
 * </pre>
 * 
 * In contrast to {@link Try} and {@link Option} the {@link Either} cannot directly be used as a collection (i.e iterate over it). <br>
 * This is due to that {@link Either} is unbiased as to which of {@link Left}, {@link Right} it represents. <br>
 * To get access to the data represented by the {@link Either} you as a developer have to decide to work with either the {@link Left} or {@link Right} side. <br>
 * <br>
 * Consider the {@link Either} ({@link Right}) instance exemplified above. <br>
 * To get hold of the data it represents you need to decide which side (Left, Right) to work with. <br>
 * Easiest way is to first decide which side the instance represents. <br>
 * The methods {@link #isLeft()} and {@link #isRight()} will help you decide which side is represented. <br>
 * Invoking either {@link #left()} or {@link #right()} will yield a biased projection for that side. <br>
 * 
 * <pre>
 * {
 *     &#064;code
 *     RightProjection&lt;InputStream, String&gt; projection = either.right();
 * }
 * </pre>
 * 
 * @author Peter Nerg
 * @since 1.1
 * @param <L>
 *            The type for the {@link Left} side
 * @param <R>
 *            The type for the {@link Right} side
 */
public interface Either<L, R> {
    /**
     * Returns<code>true</code> if this instance is {@link Right}, else <code>false</code>.
     * 
     * @return <code>true</code> if this instance is {@link Right}.
     */
    boolean isRight();

    /**
     * Returns<code>true</code> if this instance is {@link Left}, else <code>false</code>
     * 
     * @return <code>true</code> if this instance is {@link Left}.
     */
    default boolean isLeft() {
        return !isRight();
    }

    /**
     * Applies <code>func_left</code> if this is a {@link Left} or <code>func_right</code> if this is a {@link Right}
     * 
     * @param <T>
     *            The type to return from <code>func_left/func_right</code>
     * @param func_left
     *            The function to apply in case this is a {@link Left}
     * @param func_right
     *            The function to apply in case this is a {@link Right}
     * @return The result from applying either func_left or func_right
     */
    <T> T fold(Function<L, T> func_left, Function<R, T> func_right);

    /**
     * Returns a {@link RightProjection} for this instance.
     * 
     * @return The projection
     */
    default RightProjection<L, R> right() {
        return new RightProjection<>(this);
    }

    /**
     * Returns a {@link LeftProjection} for this instance.
     * 
     * @return The projection
     */
    default LeftProjection<L, R> left() {
        return new LeftProjection<>(this);
    }

    /**
     * If this is a {@link Left}, then return the left value in {@link Right} or vice versa.
     * 
     * @return The swapped version of this instance
     */
    Either<R, L> swap();
}
