/**
 * Copyright (c) 2007-2011 J2Speed. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.j2speed.accessor;

import java.util.Map;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Factory for all type of accessors. Helps reducing generics cluttering and make creation shorter.
 * 
 * @version trunk
 * @since trunk
 * @author Alessandro Nistico
 */
public abstract class Accessors {
  private Accessors() {/* factory class; */}

  /**
   * Creates an accessor to a non static field.
   * 
   * @param <T>
   *          the type of the target type which hierarchy contains the field.
   * @param <V>
   *          the type of the field.
   * @param fieldName
   *          the field name
   * @param type
   *          the class from which to start searching the field
   * @return an instance of {@link FieldAccessor} with the specified generic parameters.
   */
  @NonNull
  public static <T, V> FieldAccessor<T, V> accessField(@NonNull String fieldName,
    @NonNull Class<? extends T> type) {
    return new FieldAccessor<T, V>(fieldName, type);
  }

  /**
   * Returns the value, if any, in a field with the specified name found in the type hierarchy of
   * the target object.
   * 
   * @param <V>
   *          the type of the field.
   * 
   * @param fieldName
   *          the name of the field to access.
   * @param target
   *          the target object to search.
   * @return the value found in the specified field, or <code>null</code>.
   */
  public static <V> V accessField(@NonNull String fieldName, @NonNull Object target) {
    return new FieldAccessor<Object, V>(fieldName, (Class<?>) target.getClass()).get(target);
  }

  /**
   * Creates an accessor to a static field.
   * 
   * @param <T>
   *          the type of the target type which hierarchy contains the field.
   * @param <V>
   *          the type of the field.
   * @param fieldName
   *          the field name
   * @param type
   *          the class from which to start searching the field
   * @return an instance of {@link FieldAccessor} with the specified generic parameters.
   */
  @NonNull
  public static <T, V> StaticFieldAccessor<T, V> accessStaticField(@NonNull String fieldName,
    @NonNull Class<? extends T> type) {
    return new StaticFieldAccessor<T, V>(fieldName, type);
  }

  /**
   * Creates an accessor to a method that returns a value.
   * 
   * @param <T>
   *          the return type of the method
   * 
   * @param methodName
   *          the method name
   * @param target
   *          the target object
   * @param parametersType
   *          the parameters signature.
   * @return an instance of {@link MethodAccessor} with the specified generic parameters.
   */
  @NonNull
  public static <T> MethodAccessor<T> accessMethod(@NonNull String methodName, Object target,
    @NonNull Class<?>... parametersType) {
    return new MethodAccessor<T>(methodName, target, parametersType);
  }

  /**
   * Creates an accessor to a method that does not return a value.
   * 
   * @param methodName
   *          the method name
   * @param target
   *          the target object
   * @param parametersType
   *          the parameters signature.
   * @return an instance of {@link MethodAccessor} with the specified generic parameters.
   */
  @NonNull
  public static VoidMethodAccessor accessVoidMethod(@NonNull String methodName, Object target,
    @NonNull Class<?>... parametersType) {
    return new VoidMethodAccessor(methodName, target, parametersType);
  }

  /**
   * Creates a {@link ClassAccessor} for the class with the specified name.
   * 
   * @param name
   *          the fully qualified class name.
   * @return an instance of {@link ClassAccessor}.
   */
  @NonNull
  public static final ClassAccessor accessClass(@NonNull String name) {
    return ClassAccessor.create(name);
  }

  /**
   * Creates a {@link ClassAccessor} for the inner class with the specified name within the
   * specified class.
   * <p>
   * The name for the class accepts the dot notation so that a "Nested" class that is an inner class
   * of the inner class "Inner" can be represented as "Inner.Nested".
   * 
   * @param enclosing
   *          the {@link Class} enclosing the class with the specified name.
   * @param name
   *          the inner class name.
   * @return an instance of {@link ClassAccessor}.
   */
  @NonNull
  public static final ClassAccessor accessClass(@NonNull Class<?> enclosing, @NonNull String name) {
    return ClassAccessor.create(enclosing, name);
  }

  /**
   * Creates a {@link ClassAccessor} for the class with the specified name.
   * 
   * @param name
   *          the fully qualified class name.
   * @param loader
   *          the class loader to use to load the class.
   * @return an instance of the {@link ClassAccessor} for the specified class.
   */
  @NonNull
  public static final ClassAccessor accessClass(@NonNull String name, @NonNull ClassLoader loader) {
    return ClassAccessor.create(name, loader);
  }

  /**
   * Creates a {@link ClassAccessor} for the inner class with the specified name within the
   * specified class.
   * <p>
   * The name for the class accepts the dot notation so that a "Nested" class that is an inner class
   * of the inner class "Inner" can be represented as "Inner.Nested".
   * 
   * @param enclosing
   *          the {@link Class} enclosing the class with the specified name.
   * @param name
   *          the inner class name.
   * @param loader
   *          the class loader to use to load the class.
   * @return an instance of {@link ClassAccessor}.
   */
  @NonNull
  public static final ClassAccessor accessClass(@NonNull Class<?> enclosing, @NonNull String name,
    @NonNull ClassLoader loader) {
    return ClassAccessor.create(enclosing, name, loader);
  }

  /**
   * Creates a proxy that give access to the methods in the target that match the ones defined in
   * the provided interface.
   * <p>
   * Note that the target must match all methods in the provided interface, regardless the access
   * level, they can even be private that is.<br>
   * Note also that to match, the return type can be a superclass of the target method return type.
   * </p>
   * <p>
   * If you need to access static methods, the target must be the Class instance in which those
   * methods are defined.
   * </p>
   * 
   * @param <T>
   *          The return type for this method. T must be an interface.
   * @param type
   *          The Class object of the interface.
   * @param target
   *          The object that matches the interface's methods.
   * @return A proxy instance that implements T
   * @throws NoSuchMethodException
   *           if the target doesn't match one of the interface's methods.
   * @throws NoSuchFieldException
   */
  @NonNull
  public static <T> T proxy(@NonNull Class<T> type, @NonNull Object target) {
    return ProxyAccessor.createAccessor(type, target);
  }

  /**
   * Creates a proxy that give access to the methods in the target that match the ones defined in
   * the provided interface.
   * <p>
   * This variant allows to map methods in the interface to methods in the target instance so that
   * they don't have to match natively. For example, if the interface defines a method
   * getSomething() but the target instance defines something() instead, we could provide a Map that
   * contains the key,value pair <"getSomething","something"> and the accessor would map those two
   * methods.
   * </p>
   * <p>
   * If you need to access only static methods, the target must be the Class instance in which those
   * methods are defined.
   * </p>
   * 
   * @param <T>
   *          The return type for this method. T must be an interface.
   * @param type
   *          The Class object of the interface.
   * @param target
   *          The object that matches the interface's methods.
   * @param namesMapping
   *          a mapping of names between a method in the interface to the actual method name in the
   *          target instance or between a standard JavaBean field name and the actual field name in
   *          the target instance.
   * @return A proxy instance that implements T
   * @throws NoSuchMethodException
   *           if the target doesn't match one of the interface's methods.
   * @throws NoSuchFieldException
   */
  @NonNull
  public static <T> T proxy(@NonNull Class<T> type, @NonNull Object target,
    @CheckForNull Map<String, String> namesMapping) {
    return ProxyAccessor.createAccessor(type, target, namesMapping);
  }
}
