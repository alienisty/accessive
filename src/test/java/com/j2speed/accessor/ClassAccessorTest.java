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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.Date;
import org.junit.Test;
import com.j2speed.accessor.ClassAccessorTest.AccessPackageProtectedObject.AccessInner;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * Unit test case for class ClassAccessor.
 * 
 * @version trunk
 * @since 1.0
 * @author Alessandro Nistico
 */
public class ClassAccessorTest {

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorStringClassNotFound() {
    ClassAccessor.create("com.j2speed.accessor.ThisClassDoesNotExist");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateAccessorForInterface() {
    ClassAccessor.create(AccessPackageProtectedObject.class.getName());
  }

  @Test
  public void testCreateAccessorString() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    String expectedName = "Alex";
    AccessPackageProtectedObject object = accessor.constructor(String.class).newProxy(AccessPackageProtectedObject.class, expectedName);
    assertTrue(object.isItYou());
    assertSame(expectedName, object.getName());
  }

  @Test
  public void testForInnerStringDotNotation() {
    ClassAccessor innerAccessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject").forInner("Inner.Nested");
    assertNotNull(innerAccessor);
  }

  @Test
  public void testForInnerString() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    Object object = accessor.constructor(String.class).newInstance("Alex");
    ClassAccessor innerAccessor = accessor.forInner("Inner");
    Date expectedDate = new Date();
    AccessInner inner = innerAccessor.constructor(object, Date.class).newProxy(AccessInner.class, expectedDate);
    assertSame(expectedDate, inner.getDate());
  }

  @Test(expected = IllegalStateException.class)
  public void testInnerConstructorWithoutEnclosing() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    accessor.forInner("Inner").constructor(Date.class);
  }

  @Test
  public void testCreateAccessorStringClassLoader() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject", getClass().getClassLoader());
    String expectedName = "Alex";
    AccessPackageProtectedObject object = accessor.constructor(String.class).newProxy(AccessPackageProtectedObject.class, expectedName);
    assertTrue(object.isItYou());
    assertSame(expectedName, object.getName());
  }

  @Test
  public void testCreateAccessorClassOfQString() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    Object object = accessor.constructor(String.class).newInstance("Alex");
    ClassAccessor innerAccessor = ClassAccessor.create(object.getClass(), "Inner");
    Date expectedDate = new Date();
    AccessInner inner = innerAccessor.constructor(object, Date.class).newProxy(AccessInner.class, expectedDate);
    assertSame(expectedDate, inner.getDate());
  }

  @Test
  public void testCreateAccessorClassOfQStringDotNotation() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    ClassAccessor innerAccessor = ClassAccessor.create(accessor.getAccessedClass(), "Inner.Nested");
    assertNotNull(innerAccessor);
  }

  @Test
  public void testCreateAccessorClassOfQStringClassLoader() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject", getClass().getClassLoader());
    Object object = accessor.constructor(String.class).newInstance("Alex");
    ClassAccessor innerAccessor = ClassAccessor.create(object.getClass(), "Inner", getClass().getClassLoader());
    Date expectedDate = new Date();
    AccessInner inner = innerAccessor.constructor(object, Date.class).newProxy(AccessInner.class, expectedDate);
    assertSame(expectedDate, inner.getDate());
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorClassOfQArrayNotExisting() {
    ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject").constructor(Date.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorClassOfQArrayWrongParameterValue() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    accessor.constructor(String.class).newInstance(new Date());
  }

  @Test
  public void testConstructorClassOfQArray() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    assertNotNull(accessor.constructor(String.class));
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorObjectClassOfQArrayNotExisting() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject", getClass().getClassLoader());
    Object object = accessor.constructor(String.class).newInstance("Alex");
    ClassAccessor innerAccessor = ClassAccessor.create(object.getClass(), "Inner", getClass().getClassLoader());
    innerAccessor.constructor(object, String.class);
  }

  @Test(expected = IllegalStateException.class)
  public void testConstructorObjectClassOfQArrayNotInner() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    accessor.constructor(new Object(), String.class);
  }

  @Test(expected = NullPointerException.class)
  @SuppressWarnings(value = "NP")
  public void testConstructorObjectClassOfQArrayNullEnclosing() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    ClassAccessor innerAccessor = accessor.forInner("Inner");
    innerAccessor.constructor((Object) null, Date.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorObjectClassOfQArrayMisnatchingEnclosing() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    ClassAccessor innerAccessor = accessor.forInner("Inner");
    innerAccessor.constructor(new Object(), Date.class);
  }

  @Test(expected = Throwable.class)
  public void testConstructorObjectClassOfQArrayThrowsThrowable() throws Throwable {
    ClassAccessor innerAccessor = ClassAccessor.create(TestObject.class, "InnerThrowsThrowable");
    try {
      innerAccessor.constructor(new TestObject()).newInstance();
    } catch (RuntimeException e) {
      throw e.getCause();
    }
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorObjectClassOfQArrayThrowsRuntime() {
    ClassAccessor innerAccessor = ClassAccessor.create(TestObject.class, "InnerThrowsRuntimeException");
    innerAccessor.constructor(new TestObject()).newInstance();
  }

  @Test(expected = Error.class)
  public void testConstructorObjectClassOfQArrayThrowsError() {
    ClassAccessor innerAccessor = ClassAccessor.create(TestObject.class, "InnerThrowsError");
    innerAccessor.constructor(new TestObject()).newInstance();
  }

  @Test(expected = RuntimeException.class)
  public void testConstructorObjectClassOfQArrayWrongParameterValue() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    Object object = accessor.constructor(String.class).newInstance("Alex");
    ClassAccessor innerAccessor = accessor.forInner("Inner");
    innerAccessor.constructor(object, Date.class).newInstance("Alex");
  }

  @Test
  public void testConstructorObjectClassOfQArray() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    Object object = accessor.constructor(String.class).newInstance("Alex");
    ClassAccessor innerAccessor = accessor.forInner("Inner");
    assertNotNull(innerAccessor.constructor(object, Date.class));
  }

  @Test
  public void testConstructorObjectClassOfQArrayStatic() {
    ClassAccessor accessor = ClassAccessor.create("com.j2speed.accessor.separate.PackagePrivateObject");
    ClassAccessor innerAccessor = accessor.forInner("StaticInner");
    assertNotNull(innerAccessor.constructor(Date.class));
  }

  interface AccessPackageProtectedObject {

    boolean isItYou();

    String getName();

    interface AccessInner {

      Date getDate();
    }
  }
}
