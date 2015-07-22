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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Abstract base class for the projections used by {@link Either}.
 * 
 * @author Peter Nerg
 * @since 1.1
 */
abstract class Projection<T> implements Iterable<T> {

    /**
     * Returns the value of the Right/Left in {@link Some} side in case the projection matches the side, else {@link None}. <br>
     * That is:<br>
     * If this is a {@link RightProjection} with a {@link Right} <br>
     * or<br>
     * If this is a {@link LeftProjection} with a {@link Left}. <br>
     * Then then return {@link Some} containing the value of the {@link Left}/{@link Right}. <br>
     * Any other case, return {@link None}.
     * 
     * @return The Option
     */
    public Option<T> asOption() {
        return Option.apply(orNull());
    }

    /**
     * Returns the value of the Right/Left side in case the projection matches the side, else throw {@link NoSuchElementException}. <br>
     * That is:<br>
     * If this is a {@link RightProjection} with a {@link Right} <br>
     * or<br>
     * If this is a {@link LeftProjection} with a {@link Left}. <br>
     * Then return the value of the {@link Left}/{@link Right}. <br>
     * Any other case, throw {@link NoSuchElementException}.
     * 
     * @return The value
     */
    public T get() {
        return asOption().get();
    }

    /**
     * Returns the iterator of the Right/Left side in case the projection matches the side. <br>
     * That is:<br>
     * If this is a {@link RightProjection} with a {@link Right} <br>
     * or<br>
     * If this is a {@link LeftProjection} with a {@link Left}. <br>
     * Then return an iterator with the value for the {@link Left}/{@link Right}. <br>
     * Else return an empty iterator
     * 
     * @return The iterator
     */
    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    /**
     * Returns the stream of the Right/Left side in case the projection matches the side. <br>
     * That is:<br>
     * If this is a {@link RightProjection} with a {@link Right} <br>
     * or<br>
     * If this is a {@link LeftProjection} with a {@link Left}. <br>
     * Then return a stream with the value for the {@link Left}/{@link Right}. <br>
     * Else return an empty stream
     * 
     * @return The stream
     */
    public Stream<T> stream() {
        return asOption().stream();
    }

    /**
     * Returns the value of this projection if it is of the correct type else <code>null</code>. <br>
     * That is:<br>
     * If this is a {@link RightProjection} with a {@link Right} <br>
     * or<br>
     * If this is a {@link LeftProjection} with a {@link Left}. <br>
     * Then return the value of the {@link Left}/{@link Right}. <br>
     * Any other case, return <code>null</code>. <br>
     * In an essence this is a specialized version of {@link #getOrElse(Supplier)} where the supplier returns <code>null</code>. <br>
     * <code>getOrElse(() -&#62; null)</code>
     * 
     * @return The value or <code>null</code>
     */
    public T orNull() {
        return getOrElse(() -> null);
    }

    /**
     * Returns the value of this projection if it is of the correct type else else the value provided by the supplier. <br>
     * That is:<br>
     * If this is a {@link RightProjection} with a {@link Right} <br>
     * or<br>
     * If this is a {@link LeftProjection} with a {@link Left}. <br>
     * Then return the value of the {@link Left}/{@link Right}. <br>
     * Any other case, return the value provided by the supplier.
     * 
     * @param supplier
     *            The supplier to use if else becomes used
     * @return The value or <code>null</code>
     */
    public abstract T getOrElse(Supplier<T> supplier);
}
