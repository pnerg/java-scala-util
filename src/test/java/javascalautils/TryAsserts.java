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
 *
 * @author Peter Nerg.
 * @since 1.10
 */
public interface TryAsserts {

  /**
   * Asserts that the provided Try is a Success.
   *
   * @param t The Try to assert
   */
  default void assertIsSuccess(Try<?> t) {
    Assert.assertTrue("The Try [" + t + "] is not a Success", t.isSuccess());
  }

  /**
   * Asserts that the provided Try is a Failure.
   *
   * @param t The Try to assert
   */
  default void assertIsFailure(Try<?> t) {
    Assert.assertTrue("The Try [" + t + "] is not a Failure", t.isFailure());
  }
}
