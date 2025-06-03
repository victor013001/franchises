package com.pragma.challenge.franchises.domain.validation;

import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.validation.annotation.Max;
import com.pragma.challenge.franchises.domain.validation.annotation.Min;
import org.junit.jupiter.api.Test;

class ValidIntRangeTest {

  static class ValidIntObject {
    @Min(1)
    @Max(10)
    Integer value;

    ValidIntObject(Integer value) {
      this.value = value;
    }
  }

  static class NoAnnotationIntObject {
    Integer value;

    NoAnnotationIntObject(Integer value) {
      this.value = value;
    }
  }

  @Test
  void testValidValueInRange() {
    var obj = new ValidIntObject(5);
    assertDoesNotThrow(() -> ValidIntRange.valid(obj));
  }

  @Test
  void testValueBelowMinThrows() {
    var obj = new ValidIntObject(0);
    assertThrows(BadRequest.class, () -> ValidIntRange.valid(obj));
  }

  @Test
  void testValueAboveMaxThrows() {
    var obj = new ValidIntObject(15);
    assertThrows(BadRequest.class, () -> ValidIntRange.valid(obj));
  }

  @Test
  void testValueNullIsIgnored() {
    var obj = new ValidIntObject(null);
    assertDoesNotThrow(() -> ValidIntRange.valid(obj));
  }

  @Test
  void testNoAnnotatedFieldsDoesNothing() {
    var obj = new NoAnnotationIntObject(100);
    assertDoesNotThrow(() -> ValidIntRange.valid(obj));
  }
}
