package com.pragma.challenge.franchises.infrastructure.entrypoints.util;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardError;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;
import com.pragma.challenge.franchises.infrastructure.entrypoints.mapper.ServerResponseMapper;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@UtilityClass
public class ResponseUtil {
  public static Mono<ServerResponse> buildResponse(
      int httpStatus, Object data, StandardError error, ServerResponseMapper responseMapper) {
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

  public static Consumer<Throwable> logErrorHandler(Logger log, String logPrefix) {
    return ex ->
        log.error(
            "{} Exception {} caught. Caused by: {}",
            logPrefix,
            ex.getClass().getSimpleName(),
            ex.getMessage());
  }

  public static Function<StandardException, Mono<ServerResponse>> standardErrorHandler(
      ServerResponseMapper responseMapper) {
    return ex -> buildResponse(ex.getHttpStatus(), null, ex.getStandardError(), responseMapper);
  }

  public static Function<Throwable, Mono<ServerResponse>> genericErrorHandler(
      ServerResponseMapper responseMapper) {
    return ex ->
        buildResponse(
            ServerResponses.SERVER_ERROR.getHttpStatus(),
            null,
            buildStandardError(ServerResponses.SERVER_ERROR),
            responseMapper);
  }
}
