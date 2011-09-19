/**
 * Copyright (c) 2007-2011 J2Speed. All rights reserved.
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
public class MethodAccessorTest {

  private MethodAccessor<Integer> getPrivate;

  private MethodAccessor<Integer> setPrivate;

  @Before
  public void setUp() throws Exception {
    TestObject test = new TestObject();
    getPrivate = new MethodAccessor<Integer>("getPrivate", test, new Class[0]);
    setPrivate = new MethodAccessor<Integer>("setPrivate", test, new Class[] { int.class });
  }

  @After
  public void tearDown() throws Exception {
    getPrivate = null;
    setPrivate = null;
  }

  @Test(expected = RuntimeException.class)
  public void testMethodAccessor1() {
    new MethodAccessor<Void>("missing", new Object());
  }

  @Test(expected = RuntimeException.class)
  public void testMethodAccessor2() {
    new MethodAccessor<Void>("missing", new TestObject());
  }

  @Test(expected = NullPointerException.class)
  public void testMethodAccessor3() {
    new MethodAccessor<Void>("missing", null);
  }

  /**
   * Test method for
   * {@link com.j2speed.accessor.AbstractMethodAccessor#invokeBase(java.lang.Object[])}.
   */
  @Test
  public void testInvoke() {
    int expected = 26071973;
    assertEquals(expected, getPrivate.invoke().intValue());

    int newValue = 26072007;
    assertEquals(expected, setPrivate.invoke(Integer.valueOf(newValue)).intValue());
    assertEquals(newValue, getPrivate.invoke().intValue());
  }

  @Test
  public void testInvokeFactoryMethod() {
    TestObject test = new TestObject();
    getPrivate = MethodAccessor.make("getPrivate", test, new Class[0]);
    setPrivate = MethodAccessor.make("setPrivate", test, new Class[] { int.class });
    int expected = 26071973;
    assertEquals(expected, getPrivate.invoke().intValue());

    int newValue = 26072007;
    assertEquals(expected, setPrivate.invoke(Integer.valueOf(newValue)).intValue());
    assertEquals(newValue, getPrivate.invoke().intValue());
  }

}
