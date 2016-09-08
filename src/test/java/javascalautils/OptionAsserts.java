/* 
 * Created : 9/8/16
 * 
 * Copyright (c) 2016 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package javascalautils;

import org.junit.Assert;

/**
 * Utility asserts related to the {@link javascalautils.Try} class.
 * @author Peter Nerg.
 * @since 1.10
 */
public interface OptionAsserts {

    /**
     * Asserts that the provided Option is defined.
     * @param o The option to assert
     */
    default void assertIsDefined(Option<?> o) {
        Assert.assertTrue("The Option was not defined", o.isDefined());
    }

    /**
     * Asserts that the provided Option is defined.
     * @param o The option to assert
     */
    default void assertIsNotDefined(Option<?> o) {
        Assert.assertFalse("The Option ["+o+"] was defined", o.isDefined());
    }
}
