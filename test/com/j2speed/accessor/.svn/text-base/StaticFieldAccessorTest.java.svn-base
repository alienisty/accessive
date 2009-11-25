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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class StaticFieldAccessorTest {

  private StaticFieldAccessor<TestObject, Integer> aStaticPrivate;

  /**
   * {@inheritDoc}
   */
  @Before
  public void setUp() throws Exception {
    aStaticPrivate = new StaticFieldAccessor<TestObject, Integer>("aStaticPrivate", TestObject.class);
  }

  @Test(expected = RuntimeException.class)
  public void testStaticFieldAccessor() {
    new StaticFieldAccessor<Object, Void>("missing", Object.class);
  }

  @Test(expected = NullPointerException.class)
  public void testStaticFieldAccessor2() {
    new StaticFieldAccessor<Object, Void>("missing", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStaticFieldAccessor3() {
    new StaticFieldAccessor<TestObject, Void>("aPrivate", TestObject.class);
    fail("The field should not be static: StaticFieldAccessor only accepts static fields");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStaticFieldAccessor4() {
    new StaticFieldAccessor<TestObject, Void>("aSuperStaticPrivate", TestObject.class);
    fail("The field should not be declared in the specified class: StaticFieldAccessor only accepts static fields declared in the specified class");
  }

  /**
   * Test method for {@link com.j2speed.accessor.FieldAccessor#get()}.
   */
  @Test
  public void testGet() {
    assertEquals(27022008, aStaticPrivate.get().intValue());
  }

  /**
   * Test method for
   * {@link com.j2speed.accessor.FieldAccessor#set(java.lang.Object)}.
   */
  @Test
  public void testSet() {
    int newValue = 26072007;

    aStaticPrivate.set(Integer.valueOf(newValue));
    assertEquals(newValue, aStaticPrivate.get().intValue());
    try {
      aStaticPrivate.set(null);
    }
    catch (IllegalArgumentException e) {
      // ok
    }
  }

}
