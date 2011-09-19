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
package com.j2speed.accessor.separate;

import java.util.Date;

class PackagePrivateObject {

  final String name;

  public PackagePrivateObject(String name) {
    this.name = name;
  }

  public boolean isItYou() {
    return true;
  }

  public Object createInner() {
    return new Inner(new Date());
  }

  private class Inner {

    private final Date date;

    private Inner(Date date) {
      this.date = date;
    }

    @SuppressWarnings("unused")
    private Date getDate() {
      return date;
    }

    @Override
    public String toString() {
      return name + date;
    }

    @SuppressWarnings("unused")
    private class Nested {}
  }

  @SuppressWarnings("unused")
  private static class StaticInner {

    private final Date date;

    private StaticInner(Date date) {
      this.date = date;
    }

    private Date getDate() {
      return date;
    }

    @Override
    public String toString() {
      return "Static" + date;
    }
  }
}
