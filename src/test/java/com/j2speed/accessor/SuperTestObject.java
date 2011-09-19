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
public class SuperTestObject {
  @SuppressWarnings("unused")
  @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "URF_UNREAD_FIELD")
  private Boolean kind = Boolean.TRUE;

  @SuppressWarnings("unused")
  @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "URF_UNREAD_FIELD")
  private Boolean race = Boolean.FALSE;

  private int aPrivate = 26071973;
  @SuppressWarnings("unused")
  private static int aSuperStaticPrivate = 27022008;

  @SuppressWarnings("unused")
  private int getPrivate() {
    return aPrivate;
  }

  @SuppressWarnings("unused")
  private int setPrivate(int newValue) {
    int oldValue = aPrivate;
    aPrivate = newValue;
    return oldValue;
  }
}
