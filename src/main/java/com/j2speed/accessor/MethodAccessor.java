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

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * An accessor to a method that returns a value.
 * 
 * @version trunk
 * @since 0.1
 * @author Alessandro Nistico
 * 
 * @param <T>
 *          the return type of the method
 */
public class MethodAccessor<T> extends AbstractMethodAccessor<T> {

  /**
   * Generic builder. Convenience to remove generics pollution while creating an
   * accessor.
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
   * @return an instance of {@link MethodAccessor} with the specified generic
   *         parameters.
   */
  public static <T> MethodAccessor<T> make(@NonNull String methodName, Object target, @NonNull Class<?>... parametersType) {
    return new MethodAccessor<T>(methodName, target, parametersType);
  }

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
  public MethodAccessor(@NonNull String methodName, Object target, @NonNull Class<?>... parametersType) {
    super(methodName, target, parametersType);
  }

  /**
   * Invoke the method using the specified parameters.
   * 
   * @param args
   *          the arguments for the method.
   * @return the result of the specified type.
   */
  public T invoke(@NonNull Object... args) {
    return super.invokeBase(args);
  }
}
