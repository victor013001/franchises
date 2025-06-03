package com.pragma.challenge.franchises.infrastructure.entrypoints.util;

import com.pragma.challenge.franchises.domain.exceptions.StandardError;
import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.domain.model.Product;
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

  @Data
  @Schema(name = "DefaultErrorResponse")
  @AllArgsConstructor
  public static class DefaultErrorResponse {
    private StandardError error;
  }

  @Data
  @Schema(name = "DefaultFranchiseResponse")
  @AllArgsConstructor
  public static class DefaultFranchiseResponse {
    private Franchise data;
  }

  @Data
  @Schema(name = "DefaultBranchResponse")
  @AllArgsConstructor
  public static class DefaultBranchResponse {
    private Branch data;
  }

  @Data
  @Schema(name = "DefaultProductResponse")
  @AllArgsConstructor
  public static class DefaultProductResponse {
    private Product data;
  }
}
