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

import java.lang.reflect.Modifier;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Allow to access a private field of a class.
 * 
 * @version trunk
 * @since 0.1
 * @author Alessandro Nistico
 * 
 * @param <T>
 *          the type of the target type which hierarchy contains the field.
 * @param <V>
 *          the type of the field.
 */
public class FieldAccessor<T, V> extends BaseFieldAccessor<T, V> {

  /**
   * Generic builder. Convenience to remove generics pollution while creating an accessor.
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
  public static <T, V> FieldAccessor<T, V> make(@NonNull String fieldName,
    @NonNull Class<? extends T> type) {
    return new FieldAccessor<T, V>(fieldName, type);
  }

  /**
   * Constructor.
   * 
   * @param fieldName
   *          the field name
   * @param type
   *          the class from which to start searching the field
   */
  public FieldAccessor(@NonNull String fieldName, @NonNull Class<? extends T> type) {
    super(fieldName, type);
    if (Modifier.isStatic(field.getModifiers())) {
      throw new IllegalArgumentException("Field " + fieldName + " is static");
    }
  }

  /**
   * Gets the value of the field.
   * 
   * @param target
   *          the instance from which to get the value.
   * @return the current field value.
   */
  @SuppressWarnings("unchecked")
  public final V get(@NonNull T target) {
    try {
      return (V) field.get(target);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets the value for the field.
   * 
   * @param target
   *          the target instance on which to set the new value.
   * @param value
   *          the value to set.
   */
  public final void set(@NonNull T target, V value) {
    try {
      field.set(target, value);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
