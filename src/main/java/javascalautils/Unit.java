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

import java.io.Serializable;

/**
 * Represents the type <i>scala.Unit</i>. <br>
 * The usage of <i>Unit</i> is for situations where one returns types such as {@link Try} or {@link javascalautils.Future Future} that are not meant to hold a
 * value. <br>
 * E.g.
 * 
 * <pre>
 * </code>
 * Try&lt;Unit&gt; deleteSynchronous(String id)
 * Future&lt;Unit&gt; deleteAsynchronous(String id)
 * </code>
 * </pre>
 * 
 * The example illustrates deleting something but the only thing we care of is if the operation was successful.<br>
 * This becomes very apparent with the asynchronous method. In that case we only care if the operation is finished and successful.<br>
 * One can of course return dummy objects/values but providing Unit is more concise as it clearly marks a non-value.
 * 
 * @author Peter Nerg
 * @since 1.6
 */
public final class Unit implements Serializable {
    private static final long serialVersionUID = 75L;
}
