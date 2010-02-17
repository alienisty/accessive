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
