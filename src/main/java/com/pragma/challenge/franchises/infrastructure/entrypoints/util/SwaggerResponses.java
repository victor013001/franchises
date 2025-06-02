package com.pragma.challenge.franchises.infrastructure.entrypoints.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class SwaggerResponses {

  private SwaggerResponses() throws InstantiationException {
    throw new InstantiationException("Utility class");
  }

  @Data
  @Schema(name = "DefaultMessageResponse")
  @AllArgsConstructor
  public static class DefaultMessageResponse {
    private String data;
  }
}
