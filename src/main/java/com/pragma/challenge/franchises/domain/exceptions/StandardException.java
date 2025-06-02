package com.pragma.challenge.franchises.domain.exceptions;

import java.time.LocalDateTime;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import lombok.Getter;

@Getter
public class StandardException extends RuntimeException {
  private final int httpStatus;
  private final StandardError standardError;

  public StandardException(ServerResponses serverResponses) {
    super(serverResponses.getMessage());
    this.httpStatus = serverResponses.getHttpStatus();
    this.standardError =
        StandardError.builder()
            .code(serverResponses.getCode())
            .timestamp(LocalDateTime.now())
            .description(serverResponses.getMessage())
            .build();
  }
}
