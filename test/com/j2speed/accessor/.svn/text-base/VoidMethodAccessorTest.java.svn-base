/**
 * Copyright © 2007 J2Speed. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.j2speed.accessor;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @version trunk
 * @since 0.1
 * @author Alessandro Nistico
 */
public class VoidMethodAccessorTest {

  private VoidMethodAccessor throwingMethod;

  @Before
  public void setUp() {
    throwingMethod = new VoidMethodAccessor("throwingMethod", new TestObject());
  }

  @After
  public void tearDown() {
    throwingMethod = null;
  }

  /**
   * Test method for
   * {@link com.j2speed.accessor.AbstractMethodAccessor#invokeBase(java.lang.Object[])}.
   */
  @Test
  public void testInvoke() {
    new VoidMethodAccessor("nonThrowingMethod", new TestObject()).invoke();
    try {
      throwingMethod.invoke();
      fail("Expected test exception");
    }
    catch (RuntimeException e) {
      Throwable th = e.getCause();
      if (th instanceof TestException) {
        // OK
      } else {
        throw e;
      }
    }
    try {
      throwingMethod.invoke("wrong");
      fail("Expected test exception");
    }
    catch (IllegalArgumentException e) {
      // OK
    }
  }
}