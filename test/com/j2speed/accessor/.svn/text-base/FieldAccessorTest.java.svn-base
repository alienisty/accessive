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
 * Unit test case for class FieldAccessor.
 * 
 * @version trunk
 * @since 0.1
 * @author Alessandro Nistico
 */
public class FieldAccessorTest {

  private FieldAccessor<TestObject, Integer> aPrivate;

  @Before
  public void setUp() throws Exception {
    aPrivate = new FieldAccessor<TestObject, Integer>("aPrivate", TestObject.class);
  }

  @After
  public void tearDown() throws Exception {
    aPrivate = null;
  }

  @Test(expected = RuntimeException.class)
  public void testFieldAccessor1() {
    new FieldAccessor<Object, Void>("missing", Object.class);
  }

  @Test(expected = NullPointerException.class)
  public void testFieldAccessor2() {
    new FieldAccessor<Object, Void>("missing", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFieldAccessor3() {
    new FieldAccessor<TestObject, Void>("aSuperStaticPrivate", TestObject.class);
    fail("The field should be static: FieldAccessor only accept non static fields");
  }

  /**
   * Test method for {@link com.j2speed.accessor.FieldAccessor#get()}.
   */
  @Test
  public void testGet() {
    TestObject toTest = new TestObject();
    assertEquals(26071973, aPrivate.get(toTest).intValue());
  }

  /**
   * Test method for
   * {@link com.j2speed.accessor.FieldAccessor#set(java.lang.Object)}.
   */
  @Test
  public void testSet() {
    TestObject toTest = new TestObject();
    int newValue = 26072007;
    aPrivate.set(toTest, Integer.valueOf(newValue));
    assertEquals(newValue, aPrivate.get(toTest).intValue());
    try {
      aPrivate.set(toTest, null);
    }
    catch (IllegalArgumentException e) {
      // ok
    }
  }
}
