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

import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Represents an empty {@link Iterable}. <br>
 * Used by containers representing empty collections.
 *
 * @author Peter Nerg
 * @since 1.4
 */
class EmptyContainer<T> implements Iterable<T> {

  /**
   * Always returns an empty iterator.
   *
   * @return An empty iterator
   * @since 1.0
   */
  @Override
  public final Iterator<T> iterator() {
    return Collections.emptyIterator();
  }

  /**
   * Always returns an empty stream.
   *
   * @return An empty stream
   * @since 1.0
   */
  public final Stream<T> stream() {
    return Stream.empty();
  }
}
