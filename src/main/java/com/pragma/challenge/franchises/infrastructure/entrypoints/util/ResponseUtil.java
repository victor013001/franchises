package com.pragma.challenge.franchises.infrastructure.entrypoints.util;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardError;
import com.pragma.challenge.franchises.infrastructure.entrypoints.mapper.ServerResponseMapper;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@UtilityClass
public class ResponseUtil {
  public static Mono<ServerResponse> buildResponse(
      HttpStatus httpStatus,
      Object data,
      StandardError error,
      ServerResponseMapper responseMapper) {
    return Mono.defer(
        () ->
            ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseMapper.toResponse(data, error)));
  }

  public static StandardError buildStandardError(ServerResponses serverResponses) {
    return StandardError.builder()
        .code(serverResponses.getCode())
        .description(serverResponses.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }
}
