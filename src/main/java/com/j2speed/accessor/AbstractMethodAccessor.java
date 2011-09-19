/**
 * Copyright (c) 2007-2011 J2Speed. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.j2speed.accessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Abstract method accessor.
 * 
 * @version trunk
 * @since 0.1
 * @author Alessandro Nistico
 * 
 * @param <T>
 *          the return type of the method
 */
abstract class AbstractMethodAccessor<T> {

  /**
   * The method to invoke.
   */
  @NonNull
  private final Method method;

  /**
   * The target on which to invoke the method.
   */
  @NonNull
  private final Object target;

  /**
   * Constructor.
   * 
   * @param methodName
   *          the method name
   * @param target
   *          the target object
   * @param parametersType
   *          the parameters signature.
   */
  AbstractMethodAccessor(@NonNull String methodName, @NonNull Object target,
    @NonNull Class<?>... parametersType) {
    try {
      method = getMethod(target.getClass(), methodName, parametersType);
      this.target = target;
    } catch (RuntimeException e) {
      throw e;
    }
  }

  /**
   * Common method invocation.
   * 
   * @param args
   *          the arguments for the method.
   * @return the result of the specified type.
   */
  @SuppressWarnings("unchecked")
  final T invokeBase(@NonNull Object... args) {
    try {
      return (T) method.invoke(target, args);
    } catch (RuntimeException e) {
      throw e;
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e.getTargetException());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @NonNull
  static final Method getMethod(@NonNull Class<?> cls, @NonNull String name,
    @NonNull Class<?>... parametersType) {
    while (cls != Object.class) {
      try {
        Method method = cls.getDeclaredMethod(name, parametersType);
        method.setAccessible(true);
        return method;
      } catch (NoSuchMethodException e) {
        cls = cls.getSuperclass();
      }
    }
    throw new RuntimeException("No such method: " + name + "(" + Arrays.toString(parametersType) + ")");
  }
}
