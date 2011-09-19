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

/**
 * TODO: Document me.
 * 
 * @version trunk
 * @since 0.1
 * @author Alessandro Nistico
 * 
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings()
public class TestObject extends SuperTestObject {

  @SuppressWarnings("unused")
  @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "URF_UNREAD_FIELD")
  private boolean magic = true;
  @SuppressWarnings("unused")
  @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UUF_UNUSED_FIELD")
  private int wrong;
  @SuppressWarnings("unused")
  private static int aStaticPrivate = 27022008;

  @SuppressWarnings("unused")
  private int ANonStandardJavaBeanStyleField = -1;

  @SuppressWarnings("unused")
  private void throwingMethod() throws TestException {
    throw new TestException("from throwingMethod");
  }

  @SuppressWarnings("unused")
  private void nonThrowingMethod() {
    // do nothing
  }

  @SuppressWarnings("unused")
  @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "")
  private class InnerThrowsThrowable {

    private InnerThrowsThrowable() throws Throwable {
      throw new Throwable();
    }
  }

  @SuppressWarnings("unused")
  @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "")
  private class InnerThrowsRuntimeException {

    private InnerThrowsRuntimeException() {
      throw new RuntimeException();
    }
  }

  @SuppressWarnings("unused")
  @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "")
  private class InnerThrowsError {

    private InnerThrowsError() {
      throw new Error();
    }
  }

}
