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

import java.util.Objects;

/**
 * Used internally to validate parameters
 * 
 * @author Peter Nerg
 * @since 1.3
 */
public final class Validator {
    /**
     * Checks that the specified object reference is not {@code null}. <br>
     * Essentially this is very much the same as {@link Objects#requireNonNull(Object)} but throws an {@link IllegalArgumentException} as opposed to the
     * {@link NullPointerException} the SDK class throws.
     * 
     * @param <T>
     *            The type of the object
     * @param obj
     *            The object to be validated
     * @return The provided object in case it was non-null
     * @throws IllegalArgumentException
     *             If the provided object was <code>null</code>
     */
    public static <T> T requireNonNull(T obj) {
        return requireNonNull(obj, "Failed validation, the validated parameter was null");
    }

    /**
     * Checks that the specified object reference is not {@code null} and throws a customized {@link IllegalArgumentException} if it is. <br>
     * Essentially this is very much the same as {@link Objects#requireNonNull(Object, String)} but throws an {@link IllegalArgumentException} as opposed to the
     * {@link NullPointerException} the SDK class throws.
     *
     * @param <T>
     *            The type of the object
     * @param obj
     *            The object to be validated
     * @param message
     *            detail message to be used in the event that a {@code IllegalArgumentException} is thrown
     * @return The provided object in case it was non-null
     * @throws IllegalArgumentException
     *             If the provided object was <code>null</code>
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }
}
