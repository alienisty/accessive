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

import java.lang.reflect.Field;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Base implementation to allow access to a private field of a class.
 * 
 * @version trunk
 * @since 0.8
 * @author Alessandro Nistico
 * 
 * @param <T>
 *          the type of the target type which hierarchy contains the field.
 * @param <V>
 *          the type of the field.
 */
abstract class BaseFieldAccessor<T, V> {

  /**
   * The field to access.
   */
  @NonNull
  protected final Field field;

  /**
   * Constructor.
   * 
   * @param fieldName
   *          the field name
   * @param type
   *          the class from which to start searching the field
   */
  public BaseFieldAccessor(@NonNull String fieldName, @NonNull Class<? extends T> type) {
    if (fieldName == null || type == null)
      throw new NullPointerException();
    try {
      field = getField(type, fieldName);
    } catch (RuntimeException e) {
      throw e;
    }
  }

  /**
   * Utility method to search for a field starting from the specified class and going up the
   * hierarchy until the field is found or the {@link Object} class is reached.
   * 
   * @param cls
   *          the class from which to start
   * @param name
   *          the field name
   * @return the {@link Field} found
   * @throws NoSuchFieldException
   *           if the field cannot be found within the specified class hierarchy.
   */
  @NonNull
  static Field getField(@NonNull Class<?> cls, @NonNull String name) {
    while (cls != Object.class) {
      try {
        Field field = cls.getDeclaredField(name);
        field.setAccessible(true);
        return field;
      } catch (NoSuchFieldException e) {
        cls = cls.getSuperclass();
      }
    }
    throw new RuntimeException("No such field: " + name);
  }
}
