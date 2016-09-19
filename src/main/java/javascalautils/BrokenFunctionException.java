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
 * Used when a function throws an exception which cannot be raised. <br>
 * This exception is then created and wrapped around the original exception.
 * @author Peter Nerg
 * @since 1.11
 */
public final class BrokenFunctionException extends RuntimeException {
    private static final long serialVersionUID = 3534543534523L;
    /**
     * Creates the exception
     * @param message The message
     * @param cause The underlying cause
     */
    public BrokenFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

}
