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
package javascalautils.concurrent;

/**
 * Allows for creating a custom {@link Executor} for {@link Executors#getDefault()}. <br>
 * This is done by creating a class that implements this interface and then set the fully qualified
 * class name in the system property <i>javascalautils.concurrent.executorprovider</i>. <br>
 * The implementing class <u>must</u>:
 *
 * <ul>
 *   <li>be declared public
 *   <li>have a public (default) non-argument constructor
 * </ul>
 *
 * This property must be set <u>before</u> invoking any method in {@link Executors} as the default
 * pool is then created.
 *
 * @author Peter Nerg
 * @since 1.4
 */
public interface ExecutorProvider {
  /**
   * Used to create the default {@link Executor} instance.
   *
   * @return The default {@link Executor} instance
   */
  Executor create();
}
