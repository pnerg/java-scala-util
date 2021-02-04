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

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Represents an {@link Iterable} containing exactly one item. <br>
 * Used by containers representing single item collections.
 *
 * @author Peter Nerg
 * @since 1.4
 */
abstract class SingleItemContainer<T> implements Iterable<T> {

  /**
   * Returns an iterator containing the single value of this instance.
   *
   * @return An iterator containing the single value
   * @since 1.0
   */
  @Override
  public final Iterator<T> iterator() {
    return stream().iterator();
  }

  /**
   * Returns a stream of size one containing the value of this instance.
   *
   * @return A stream containing the single value
   * @since 1.0
   */
  public final Stream<T> stream() {
    return Stream.of(get());
  }

  /**
   * Get the value this instance represents.
   *
   * @return The single value of this instance
   */
  abstract T get();
}
