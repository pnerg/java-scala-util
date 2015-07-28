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
 * Allows for creating a custom {@link Executor} for {@link Executors#getDefault()}. <br>
 * This is done by creating a class that implements this interface and then set the fully qualified class name in the system property
 * <i>javascalautils.concurrent.executorprovider</i>. <br>
 * The implementing class <b>must</b>:
 * <ul>
 * <li>be declared public</li>
 * <li>have a public (default) non-argument constructor</li>
 * </ul>
 * 
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
