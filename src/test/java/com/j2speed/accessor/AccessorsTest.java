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

import static com.j2speed.accessor.Accessors.accessClass;
import static com.j2speed.accessor.Accessors.accessField;
import static com.j2speed.accessor.Accessors.accessMethod;
import static com.j2speed.accessor.Accessors.accessStaticField;
import static com.j2speed.accessor.Accessors.accessVoidMethod;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Unit test case for class Accessors.
 * 
 * @version trunk
 * @since trunk
 * @author Alessandro Nistico
 */
public class AccessorsTest {

  /**
   * Test method for
   * {@link com.j2speed.accessor.Accessors#accessField(java.lang.String, java.lang.Class)}.
   */
  @Test
  public void testAccessField() {
    FieldAccessor<TestObject, Integer> aPrivate = accessField("aPrivate", TestObject.class);
    assertNotNull(aPrivate);
  }

  /**
   * Test method for
   * {@link com.j2speed.accessor.Accessors#accessStaticField(java.lang.String, java.lang.Class)}.
   */
  @Test
  public void testAccessStaticField() {
    StaticFieldAccessor<TestObject, Integer> aStaticPrivate = accessStaticField("aStaticPrivate", TestObject.class);
    assertNotNull(aStaticPrivate);
  }

  /**
   * Test method for {@link com.j2speed.accessor.Accessors#accessMethod(java.lang.String,
   * java.lang.Object, java.lang.Class<?>[])}.
   */
  @Test
  public void testAccessMethod() {
    TestObject test = new TestObject();
    MethodAccessor<Integer> getPrivate = accessMethod("getPrivate", test, new Class[0]);
    assertNotNull(getPrivate);
  }

  /**
   * Test method for {@link com.j2speed.accessor.Accessors#accessClass(String)}.
   */
  @Test
  public void testAccessClassString() {
    ClassAccessor access = accessClass("com.j2speed.accessor.separate.PackagePrivateObject");
    assertNotNull(access);
  }

  /**
   * Test method for {@link com.j2speed.accessor.Accessors#accessClass(Class, String)}.
   */
  @Test
  public void testAccessClassClassString() {
    ClassAccessor accessor = accessClass("com.j2speed.accessor.separate.PackagePrivateObject");
    Object object = accessor.constructor(String.class).newInstance("Alex");
    ClassAccessor innerAccessor = accessClass(object.getClass(), "Inner");
    assertNotNull(innerAccessor);
  }

  /**
   * Test method for {@link com.j2speed.accessor.Accessors#accessClass(String, ClassLoader)}.
   */
  @Test
  public void testAccessClassStringClassloader() {
    ClassAccessor access = accessClass("com.j2speed.accessor.separate.PackagePrivateObject", getClass().getClassLoader());
    assertNotNull(access);
  }

  /**
   * Test method for {@link com.j2speed.accessor.Accessors#accessVoidMethod(java.lang.String,
   * java.lang.Object, java.lang.Class<?>[])}.
   */
  @Test
  public void testAccessClassClassStringClassloader() {
    ClassAccessor accessor = accessClass("com.j2speed.accessor.separate.PackagePrivateObject", getClass().getClassLoader());
    Object object = accessor.constructor(String.class).newInstance("Alex");
    ClassAccessor innerAccessor = accessClass(object.getClass(), "Inner", getClass().getClassLoader());
    assertNotNull(innerAccessor);
  }

  /**
   * Test method for {@link com.j2speed.accessor.Accessors#accessVoidMethod(java.lang.String,
   * java.lang.Object, java.lang.Class<?>[])}.
   */
  @Test
  public void testAccessVoidMethod() {
    VoidMethodAccessor throwingMethod = accessVoidMethod("throwingMethod", new TestObject());
    assertNotNull(throwingMethod);
  }
}
