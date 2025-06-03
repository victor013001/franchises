package com.pragma.challenge.franchises.domain.validation;

import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.validation.annotation.Max;
import com.pragma.challenge.franchises.domain.validation.annotation.Min;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidIntRange {
  private static final String LOG_PREFIX = "[VALID_INT_RANGE] >>>";

  private ValidIntRange() throws InstantiationException {
    throw new InstantiationException("Validation class cannot be instantiated");
  }

  public static void valid(Object object) {
    Class<?> clazz = object.getClass();
    for (Field field : clazz.getDeclaredFields()) {
      field.trySetAccessible();
      if (shouldValidateRange(field)) {
        validateRange(field, object);
      }
    }
  }

  private static boolean shouldValidateRange(Field field) {
    return (field.isAnnotationPresent(Min.class) || field.isAnnotationPresent(Max.class))
        && (field.getType() == Integer.class || field.getType() == int.class);
  }

  private static void validateRange(Field field, Object object) {
    try {
      Integer value = (Integer) field.get(object);
      if (value == null) return;

      if (field.isAnnotationPresent(Min.class)) {
        int min = field.getAnnotation(Min.class).value();
        if (value < min) {
          log.info(
              "{} Field '{}' is below minimum: {} < {}", LOG_PREFIX, field.getName(), value, min);
          throw new BadRequest();
        }
      }

      if (field.isAnnotationPresent(Max.class)) {
        int max = field.getAnnotation(Max.class).value();
        if (value > max) {
          log.info(
              "{} Field '{}' exceeds maximum: {} > {}", LOG_PREFIX, field.getName(), value, max);
          throw new BadRequest();
        }
      }
    } catch (IllegalAccessException e) {
      log.warn("{} Could not access field: {}", LOG_PREFIX, field.getName());
    }
  }
}
