package com.pragma.challenge.franchises.domain.validation;

import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.validation.annotation.NotBlank;
import org.junit.jupiter.api.Test;

class ValidNotBlankTest {

  static class NotBlankObject {
    @NotBlank String text;

    NotBlankObject(String text) {
      this.text = text;
    }
  }

  static class NoAnnotationObject {
    String text;

    NoAnnotationObject(String text) {
      this.text = text;
    }
  }

  @Test
  void testValidNonBlank() {
    var obj = new NotBlankObject("hello");
    assertDoesNotThrow(() -> ValidNotBlank.valid(obj));
  }

  @Test
  void testNullThrows() {
    var obj = new NotBlankObject(null);
    assertThrows(BadRequest.class, () -> ValidNotBlank.valid(obj));
  }

  @Test
  void testBlankThrows() {
    var obj = new NotBlankObject("  ");
    assertThrows(BadRequest.class, () -> ValidNotBlank.valid(obj));
  }

  @Test
  void testNoAnnotationDoesNothing() {
    var obj = new NoAnnotationObject(null);
    assertDoesNotThrow(() -> ValidNotBlank.valid(obj));
  }
}
