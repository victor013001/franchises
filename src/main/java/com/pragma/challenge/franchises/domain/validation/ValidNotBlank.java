package com.pragma.challenge.franchises.domain.validation;

import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.validation.annotation.NotBlank;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

@Slf4j
public class ValidNotBlank {
  private static final String LOG_PREFIX = "[VALID_NOT_BLANK] >>>";

  private ValidNotBlank() throws InstantiationException {
    throw new InstantiationException("Validation class cannot be instantiated");
  }

  public static void valid(Object object) {
    Class<?> clazz = object.getClass();
    for (Field field : clazz.getDeclaredFields()) {
      field.trySetAccessible();
      if (field.isAnnotationPresent(NotBlank.class) && field.getType() == String.class) {
        try {
          String value = (String) field.get(object);
          if (Objects.isNull(value) || Strings.isBlank(value)) {
            log.info("{} The Field: {} cannot be blank", LOG_PREFIX, field);
            throw new BadRequest();
          }
        } catch (IllegalAccessException e) {
          log.warn("{} Could not access field: {}", LOG_PREFIX, field.getName());
        }
      }
    }
  }
}
