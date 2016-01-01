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

import org.junit.Test;

/**
 * Test the class {@link Unit}
 * 
 * @author Peter Nerg
 */
public class TestUnit extends BaseAssert {
	private final Unit unit = new Unit();
	
    @Test
    public void equals_withNewUnit() {
    	assertTrue(unit.equals(new Unit()));
    }

    @Test
    public void equals_withSingletonUnit() {
    	assertTrue(unit.equals(Unit.Instance));
    }

    @Test
    public void equals_withSelf() {
    	assertTrue(unit.equals(unit));
    }
    
    @Test
    public void equals_withNull() {
    	assertFalse(unit.equals(null));
    }

    @Test
    public void equals_withNonUnit() {
    	assertFalse(unit.equals(this));
    }

    @Test
    public void hashCode_t() {
    	assertEquals(69, unit.hashCode());
    }
    
    @Test
    public void toString_t() {
    	assertEquals("Unit", unit.toString());
    }
}
