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

import static com.j2speed.accessor.Accessors.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("all")
public class ProxyAccessorTest {

  private TestObject test;

  @Before
  public void setUp() throws Exception {
    test = new TestObject();
  }

  @After
  public void tearDown() throws Exception {
    test = null;
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorNoMatchingTarget1() {
    ProxyAccessor.createAccessor(TestObjectAccess.class, new Object());
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorNoMatchingTarget2() {
    ProxyAccessor.createAccessor(TestWrong.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnIs1() {
    ProxyAccessor.createAccessor(TestWrongOnIs1.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnIs2() {
    ProxyAccessor.createAccessor(TestWrongOnIs2.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnIs3() {
    ProxyAccessor.createAccessor(TestWrongOnIs3.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnGet1() {
    ProxyAccessor.createAccessor(TestWrongOnGet1.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnGet2() {
    ProxyAccessor.createAccessor(TestWrongOnGet2.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnGet3() {
    ProxyAccessor.createAccessor(TestWrongOnGet3.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnSet1() {
    ProxyAccessor.createAccessor(TestWrongOnSet1.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnSet2() {
    ProxyAccessor.createAccessor(TestWrongOnSet2.class, test);
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAccessorWrongOnSet3() {
    ProxyAccessor.createAccessor(TestWrongOnSet3.class, test);
  }

  @Test
  public void testCreateAccessorClassOfTObject() {
    TestObjectAccess access = ProxyAccessor.createAccessor(TestObjectAccess.class, test);
    assertEquals(26071973, access.getPrivate());
    access.setPrivate(26072007);
    assertEquals(26072007, access.getPrivate());
    assertTrue(access.isKind().booleanValue());
    assertFalse(access.isRace());
    assertTrue(access.isMagic());
    access.setMagic(false);
    assertFalse(access.isMagic());
    assertEquals(26072007, access.getAPrivate());
  }

  @Test
  public void testCreateAccessorClassOfTObjectMapOfStringString() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("getBirthDate", "getPrivate");
    map.put("shutUp", "throwingMethod");
    map.put("getNonStandard", "getANonStandardJavaBeanStyleField");
    map.put("aNonStandardJavaBeanStyleField", "ANonStandardJavaBeanStyleField");

    TestRenameObjectAccess access = ProxyAccessor.createAccessor(TestRenameObjectAccess.class, test, map);
    assertEquals(26071973, access.getBirthDate());
    try {
      access.shutUp();
      fail("Expected TestException");
    }
    catch (TestException e) {
      // OK
    }
    assertEquals(-1, access.getNonStandard());
  }

  @Test(expected = RuntimeException.class)
  public void testWrongReturnType() {
    ProxyAccessor.createAccessor(TestWrongReturnType.class, new WrongReturnTypeObject());
  }

  @Test
  public void testWrongReturnTypeButRightField() {
    String date = "26/07/73";
    TestWrongReturnType access = ProxyAccessor.createAccessor(TestWrongReturnType.class, new WrongReturnTypeButRightFieldObject(date));
    assertSame(date, access.getDate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckInterface1() {
    CheckInterface check = ProxyAccessor.createAccessor(CheckInterface.class, ProxyAccessor.class);
    check.checkInterface(TestObject.class);
  }

  @Test
  public void testCheckInterface2() {
    CheckInterface check = ProxyAccessor.createAccessor(CheckInterface.class, ProxyAccessor.class);
    check.checkInterface(TestObjectAccess.class);
  }

  /**
   * Test method for {@link com.j2speed.accessor.Accessors#proxy(java.lang.Class, java.lang.Object)}
   * .
   */
  @Test
  public void testProxyClassOfTObject() {
    CheckInterface check = proxy(CheckInterface.class, ProxyAccessor.class);
    assertNotNull(check);
  }

  /**
   * Test method for
   * {@link com.j2speed.accessor.Accessors#proxy(java.lang.Class, java.lang.Object, java.util.Map)}.
   */
  @Test
  public void testProxyClassOfTObjectMapOfStringString() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("getBirthDate", "getPrivate");
    map.put("shutUp", "throwingMethod");
    map.put("getNonStandard", "getANonStandardJavaBeanStyleField");
    map.put("aNonStandardJavaBeanStyleField", "ANonStandardJavaBeanStyleField");

    TestRenameObjectAccess access = proxy(TestRenameObjectAccess.class, test, map);
    assertNotNull(access);
  }

  private interface TestObjectAccess extends Super {

    public boolean isMagic();

    public boolean getMagic();

    public boolean isRace();

    public void setMagic(boolean magic);

    public int getPrivate();

    public int getAPrivate();
  }

  private interface TestWrong {

    boolean izWrong();
  }

  private interface TestWrongOnIs1 {

    boolean isWrong();
  }

  private interface TestWrongOnIs2 {

    boolean isWrong(int i);
  }

  private interface TestWrongOnIs3 {

    int isWrong();
  }

  private interface TestWrongOnGet1 {

    void getWrong();
  }

  private interface TestWrongOnGet2 {

    double getWrong();
  }

  private interface TestWrongOnGet3 {

    int getWrong(int i);
  }

  private interface TestWrongOnSet1 {

    void setWrong();
  }

  private interface TestWrongOnSet2 {

    double setWrong(int i);
  }

  private interface TestWrongOnSet3 {

    void setWrong(double i);
  }

  private interface TestWrongReturnType {

    String getDate();
  }

  private interface TestRenameObjectAccess {

    public int getBirthDate();

    public void shutUp() throws TestException;

    public int getNonStandard();
  }

  private interface CheckInterface {

    public void checkInterface(Class<?> type);
  }

  private interface Super {

    int setPrivate(int newValue);

    Boolean isKind();
  }

  private class WrongReturnTypeObject {

    private Date getDate() {
      return new Date();
    }
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "")
  private class WrongReturnTypeButRightFieldObject extends WrongReturnTypeObject {

    private String date;

    private WrongReturnTypeButRightFieldObject(String date) {
      this.date = date;
    }

    private Date getDate() {
      return null;
    }
  }
}
