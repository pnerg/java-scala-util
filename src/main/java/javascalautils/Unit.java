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
 * The usage of <i>Unit</i> is for situations where one returns types such as {@link Try} or {@link javascalautils.concurrent.Future Future} that are not meant
 * to hold a value. <br>
 * E.g.
 * 
 * <pre>
 * <code>
 * Try&lt;Unit&gt; deleteSynchronous(String id)
 * Future&lt;Unit&gt; deleteAsynchronous(String id)
 * </code>
 * </pre>
 * 
 * The example illustrates deleting something but the only thing we care of is if the operation was successful.<br>
 * This becomes very apparent with the asynchronous method. In that case we only care if the operation is finished and successful.<br>
 * One can of course return dummy objects/values but providing Unit is more concise as it clearly marks a non-value. <br>
 * Instead of creating new instances it is recommended to use the {@link #Instance singleton} instance. 
 * 
 * @author Peter Nerg
 * @since 1.6
 */
public final class Unit implements Serializable {
	
	/** 
	 * Static singleton representing the Unit.
	 * @since 1.7
	 */
	public static final Unit Instance = new Unit();
	
    private static final long serialVersionUID = 75L;

    /**
     * Always returns <tt>69</tt>.
	 * @since 1.7
	 * @return Always <tt>69</tt>
     */
    @Override
    public int hashCode() {
    	return 69;
    }

    /**
     * Simply compares the provided object to see if it is an instance of {@link Unit}.
     * @param obj The object to compare to 
	 * @since 1.7
     */
    @Override
    public boolean equals(Object obj) {
    	if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		return getClass() == obj.getClass();
    }

    /**
     * Always returns the string <tt>Unit</tt>
     * @return The string <tt>Unit</tt>
	 * @since 1.7
     */
    @Override
    public String toString() {
    	return "Unit";
    }
}
