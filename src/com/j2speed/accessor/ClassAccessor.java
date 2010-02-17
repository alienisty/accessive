/**
 * Copyright © 2009 J2Speed. All rights reserved.
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * An accessor for classes that are package private or for private inner
 * classes.
 * <p>
 * With this object it is possible to create instances of otherwise non accessible classes.
 * <p>
 * Created instances can be proxied using a known interface to make their use more convenient.
 * 
 * @version trunk
 * @since 1.0
 * @author Alessandro Nistico
 */
public class ClassAccessor {

  /**
   * The accessed class
   */
  @NonNull
  private final Class<?> accessedClass;

  /**
   * The enclosing class of the accessed one if that is an inner class.
   */
  @CheckForNull
  private final Class<?> enclosingClass;

  /**
   * Creates a {@link ClassAccessor} for the class with the specified name.
   * 
   * @param name
   *          the fully qualified class name.
   * @return an instance of {@link ClassAccessor}.
   */
  @NonNull
  public static final ClassAccessor create(@NonNull String name) {
    return create(name, ClassAccessor.class.getClassLoader());
  }

  /**
   * Creates a {@link ClassAccessor} for the inner class with the specified name
   * within the specified class.
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
  public static final ClassAccessor create(@NonNull Class<?> enclosing, @NonNull String name) {
    return create(enclosing, name, ClassAccessor.class.getClassLoader());
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
  public static final ClassAccessor create(@NonNull String name, @NonNull ClassLoader loader) {
    try {
      return new ClassAccessor(loader.loadClass(name));
    }
    catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a {@link ClassAccessor} for the inner class with the specified name
   * within the specified class.
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
  public static final ClassAccessor create(@NonNull Class<?> enclosing, @NonNull String name, @NonNull ClassLoader loader) {
    return create(enclosing.getName() + "$" + name.replace('.', '$'), loader);
  }

  /**
   * Constructor.
   * 
   * @param accessedClass
   *          the class to access
   */
  private ClassAccessor(@NonNull Class<?> accessedClass) {
    if (accessedClass.isInterface()) { throw new IllegalArgumentException("The specified class is an interface"); }
    this.enclosingClass = accessedClass.getEnclosingClass();
    this.accessedClass = accessedClass;
  }

  /**
   * Returns the accessed {@link Class} instance.
   * 
   * @return the the accessed {@link Class} instance.
   */
  @NonNull
  public Class<?> getAccessedClass() {
    return accessedClass;
  }

  /**
   * Creates a {@link ClassAccessor} for the inner class with the specified name
   * within the accessed class.
   * <p>
   * The name for the class accepts the dot notation so that a "Nested" class that is an inner class
   * of the inner class "Inner" can be represented as "Inner.Nested".
   * 
   * @param name
   *          the inner class name.
   * @return an instance of {@link ClassAccessor}.
   */
  @NonNull
  public ClassAccessor forInner(@NonNull String name) {
    return create(accessedClass, name, accessedClass.getClassLoader());
  }

  /**
   * Creates a {@link ClassConstructor} that can be used to create instances of
   * the accessed {@link Class}.
   * 
   * @param parameterTypes
   *          the parameters types for the constructor to build.
   * @return an instance of the specified {@link ClassConstructor}.
   */
  @NonNull
  public ClassConstructor constructor(@NonNull Class<?>... parameterTypes) {
    if (enclosingClass != null && !Modifier.isStatic(accessedClass.getModifiers())) { throw new IllegalStateException("The class " + accessedClass + " is a non-static inner class"); }
    try {
      Constructor<?> constructor = accessedClass.getDeclaredConstructor(parameterTypes);
      return new ClassConstructor(constructor);
    }
    catch (RuntimeException e) {
      throw e;
    }
    catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a {@link ClassConstructor} that can be used to create instances of
   * the accessed {@link Class}.
   * 
   * @param enclosing
   *          the instance of the enclosing the class the the {@link ClassConstructor} will use to
   *          build an instance of the
   *          inner class.
   * @param parameterTypes
   *          the parameters types for the constructor to build.
   * @return an instance of the specified {@link ClassConstructor}.
   */
  @NonNull
  public ClassConstructor constructor(@NonNull Object enclosing, @NonNull Class<?>... parameterTypes) {
    if (enclosingClass == null) { throw new IllegalStateException("The class " + accessedClass + " is not an inner class"); }
    if (!enclosingClass.isInstance(enclosing)) { throw new IllegalArgumentException("Enclosing instance of type " + enclosing.getClass() + " is not an instance of " + enclosingClass); }
    try {
      Class<?>[] newParameterTypes = new Class<?>[parameterTypes.length + 1];
      System.arraycopy(parameterTypes, 0, newParameterTypes, 1, parameterTypes.length);
      newParameterTypes[0] = enclosingClass;
      Constructor<?> constructor = accessedClass.getDeclaredConstructor(newParameterTypes);
      return new InnerClassConstructor(enclosing, constructor);
    }
    catch (RuntimeException e) {
      throw e;
    }
    catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * A constructor for the accessed {@link Class} that allows to create
   * instances.
   * 
   * @author Alessandro Nistico
   */
  public static class ClassConstructor {
    @NonNull
    private final Constructor<?> constructor;

    private ClassConstructor(@NonNull Constructor<?> constructor) {
      constructor.setAccessible(true);
      this.constructor = constructor;
    }

    /**
     * Creates a new instance of the accessed class.
     * 
     * @param initargs
     *          the parameters to use to build the instance.
     * @return the instance.
     */
    @NonNull
    public Object newInstance(@NonNull Object... initargs) {
      try {
        return constructor.newInstance(initargs);
      }
      catch (RuntimeException e) {
        throw e;
      }
      catch (InvocationTargetException e) {
        Throwable targetException = e.getTargetException();
        if (targetException instanceof RuntimeException) { throw (RuntimeException) targetException; }
        if (targetException instanceof Error) { throw (Error) targetException; }
        throw new RuntimeException(targetException);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    /**
     * Creates a {@link ProxyAccessor} for a newly created instance of the
     * accessed class.
     * 
     * @param <I>
     *          the type used to proxy the instance.
     * @param type
     *          the interface to use to proxy the instance.
     * @param initargs
     *          the parameters to use to build the instance.
     * @return the proxied instance.
     */
    @NonNull
    public final <I> I newProxy(@NonNull Class<I> type, @NonNull Object... initargs) {
      return ProxyAccessor.createAccessor(type, newInstance(initargs));
    }

    /**
     * Creates a {@link ProxyAccessor} for a newly created instance of the
     * accessed class.
     * 
     * @param <I>
     *          the type used to proxy the instance.
     * @param type
     *          the interface to use to proxy the instance.
     * @param namesMapping
     *          a mapping of names between a method in the interface to the
     *          actual method name in the target instance or between a standard
     *          JavaBean field name and the actual field name in the target
     *          instance.
     * @param initargs
     *          the parameters to use to build the instance.
     * @return the proxied instance.
     */
    @NonNull
    public final <I> I newProxy(@NonNull Class<I> type, @NonNull Map<String, String> namesMapping, @NonNull Object... initargs) {
      return ProxyAccessor.createAccessor(type, newInstance(initargs), namesMapping);
    }
  }

  /**
   * A {@link ClassConstructor} for an inner class.
   * 
   * @author Alessandro Nistico
   * 
   * @param <T>
   */
  private static class InnerClassConstructor extends ClassConstructor {
    @NonNull
    private final Object enclosing;

    private InnerClassConstructor(@NonNull Object enclosing, @NonNull Constructor<?> constructor) {
      super(constructor);
      this.enclosing = enclosing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object newInstance(Object... initargs) {
      Object[] newInitargs = new Object[initargs.length + 1];
      System.arraycopy(initargs, 0, newInitargs, 1, initargs.length);
      newInitargs[0] = enclosing;

      return super.newInstance(newInitargs);
    }
  }
}
