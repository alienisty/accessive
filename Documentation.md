# Introduction #

Sometimes we sacrifice encapsulation just to let us access a method or field of an object from its unit test. This framework uses reflection and dynamic proxies to access private portion of your objects when unit testing them, allowing you to keep your objects strongly encapsulated and still test their internals using the accessors provided by the framework itself.

For example, given the following class:
```
public class AnObject {
  private int anInt;
  private String aString;
  ...
  private void aMethod(int value) {
    ...
  }

  private String anotherMethod() {
    ...
  }
}
```

If you needed to access those fields from the unit tests, you would normally sacrifice encapsulation for testing and you might change the class to:
```
public class AnObject {
  int anInt;
  String aString;
  ...
  void aMethod(int value) {
    ...
  }

  String anotherMethod() {
    ...
  }
}
```

and you think that that's right, because is one of the practices described in the JUnit documentation: "write your tests in the same package so you can access package private elements of the object".

I've never been happy with that because, sometimes, you change visibility not for a design choice, but for testing necessity. Unfortunately, unless well documented, this information is lost, and future maintainers may use this accessibility for development, thinking that that is OK by design, undermining the stability and correctness of the application.

What if we could make those fields and methods accessible only when testing but leaving them private normally? Well that's why I thought of this small framework.

# How to use the framework #

The classes of the framework are very simple and they are there just remove the boiler plate code to use reflection and make a method or a field accessible.

NOTE: if your classes install a security manager, you need to add to the security policy file the following privileges during the execution of unit tests:
```
permission java.lang.reflect.ReflectPermission "suppressAccessChecks";
```

## Field accessor ##

With the class `FieldAccessor` you can access a field of an object, no matter what is its visibility access. Given the above class, if we wanted to access "anInt" and "aString" we simply write:
```
...
AType target = ...;
FieldAccessor<AType,Integer> anInt = new FieldAccessor<AType,Integer>("anInt", AType.class);
FieldAccessor<AType,String> aString = new FieldAccessor<AType,String>("aString", AType.class);
...
assertEquals(26071973,anInt.get(target).intValue());
assertEquals("expected", aString.get(target));
...
```

where "target" is an instance of the object containing the fields.

Accessing static fields is mainly the same, but you use the StaticFieldAccessor class:
```
...
AType target = ...;
StaticFieldAccessor<AType,Integer> aStaticInt = new StaticFieldAccessor <AType,Integer>("aStaticInt", AType.class);
...
assertEquals(26071973,aStaticInt.get().intValue());
...
```
You can avoid generics clutter by using the provided factory methods, eg. the above could be expressed as:
```
...
AType target = ...;
StaticFieldAccessor<AType,Integer> aStaticInt = StaticFieldAccessor.make("aStaticInt", AType.class);
...
assertEquals(26071973,anStaticInt.get().intValue());
...
```

Note that, while the `FieldAccessor` class walks the class hierarchy up to search for the field, the StaticFieldAccessor only accepts static fields declared in the specified class.

## Method accessor ##

With the classes `MethodAccessor<V>` and `VoidMethodAccessor` you can access a method of an object, no matter what is its visibility access. Given the above class, if we wanted to access "aMethod" and "anotherMethod" we simply write:
```
...
VoidMethodAccessor aMethod = new VoidMethodAccessor("aMethod", target, int.class);
MethodAccessor<String> anotherMethod = new MethodAccessor<String>("anotherMethod", target);
...
assertEquals("expected", anotherMethod.invoke());
...
aMethod.invoke(26072007);
...
```
You can avoid generics clutter by using the provided factory methods, eg. the above could be expressed as:
```
...
VoidMethodAccessor aMethod = new VoidMethodAccessor("aMethod", target, int.class);
MethodAccessor<String> anotherMethod = MethodAccessor.make("anotherMethod", target);
...
```
Note that the framework uses varargs making the syntax very neat.

## Class accessor ##

The `ClassAccessor` allows you to access and create instances of private inner classes.
Once you obtain a `ClassAccessor` instance through one of the provided factory methods, you can then obtain `ClassConstructor`s with which you can create new instances or `ProxyAccessors`  for an instance created by the `ClassConstructor`.
For example, given the class:
```
public class AnOuterClass {

  private class AnInnerClass {
    ...
  }
}
```

you can create a `ClassAccessor` as:
```
...
ClassAccessor inner = ClassAccessor.create(AnOuterClass.class,"AnInnerClass");
...
ClassConstructor ctor = inner.constructor(new AnOuterClass());
...
AnAccessorInterface access = ctor.newProxy(AnAccessorInterface.class);
...
```

See the class documentation for more details.

## Proxy accessor ##

The `ProxyAccessor` is a strongly typed accessor, and more convenient when you want to make accessible multiple methods and fields on an object.
It uses the power of dynamic proxies. Let us show an example:
Imagine to have a class:
```
class ATarget {
  private AType field;
  private void aMethod(int aValue) {
    ...
  }
  private String anotherMethod() {
    ...
  }
}
```
We need to define an interface for the dynamic proxy
```
interface AnObjectAccessor {
  public void aMethod(int aValue);
  public String anotherMethod();
  public AType getField();
  public void setField(AType value);
}
```
Then we can simply use the accessor as:
```
...
AnObjectAccessor accessor = new ProxyAccessor.createAccessor(AnObjectAccessor.class, target);
assertEquals("expected", accessor.anotherMethod());
...
accessor.aMethod(26072007);
...
accessor.getField();
accessor.setField(value);
...
```
Note how `JavaBean` style methods are automatically mapped on matching fields without the need of defining them in the accessed class.

Sometimes, though, existing objects do not follow the `JavaBean` convention; to solve that problem, a `ProxyAccessor` can be created by providing a mapping between expected and actual names. For example, if we had:
```
class AnotherTarget {
  private AType NonJavaBeanField;
  private void aMethod(int aValue) {
    ...
  }
  private String anotherMethod() {
    ...
  }
}
```

you can still create a `ProxyAccessor` for `AnotherTarget` using `AnObjectAccessor` as:
```
...
Map<String,String> mapping = new HashMap<String,String>();
// map the interface method to a JavaBean method
mapping.put("getField", "getNonJavaBeanField");
// map a JavaBean field to a non JavaBean field
mapping.put("nonJavaBeanField", "NonJavaBeanField");
// Note how the mapping is transitive so that in the end "getField" is mapped to "NonJavaBeanField"
AnObjectAccessor accessor = new ProxyAccessor.createAccessor(AnObjectAccessor.class, target, mapping);
assertEquals("expected", accessor.anotherMethod());
...
accessor.aMethod(26072007);
...
accessor.getField();
accessor.setField(value);
...
```

As you can see the proxy accessor makes things even more natural.

## Accessors factory ##

A convenience static factory is provide to allow its static import and use its methods directly to create accessors, so for example you can do something like this:
```
import static com.j2speed.accessor.Accessor.*;
...
FieldAccessor<AType,Integer> anInt = accessField("anInt", AType.class);
...
StaticFieldAccessor<AType,Integer> aStaticInt = accessStaticField("aStaticInt", AType.class);
...
VoidMethodAccessor aMethod = accessVoidMethod("aMethod", target, int.class);
...
MethodAccessor<String> anotherMethod = accessMethod("anotherMethod", target);
...
AnObjectAccessor accessor = proxy(AnObjectAccessor.class, target);
```
As you can see the factory allows to further reduce the clutter and provides a pretty neat syntax.

See the class javadoc for all provided factory methods.