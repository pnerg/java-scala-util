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

import javascalautils.concurrent.Future;

/**
 * A function that takes no arguments and returns a value of type <i>T</i>. <br>
 * The difference with this interface and {@link Function} is that it allows for raising checked exceptions. <br>
 * Primary use case is to create concise lambda expressions such as the {@link Try#apply(ThrowableFunction0)} and {@link Future#apply(ThrowableFunction0)}
 * 
 * 
 * @author Peter Nerg
 * @since 1.3
 * @param <T>
 *            The type this function will return
 */
@FunctionalInterface
public interface ThrowableFunction0<T> {
    /**
     * Applies this function returning a result.
     *
     * @return The function result
     * @throws Throwable
     *             Any error that may occur when applying the function
     * @since 1.3
     */
    T apply() throws Throwable;
}
