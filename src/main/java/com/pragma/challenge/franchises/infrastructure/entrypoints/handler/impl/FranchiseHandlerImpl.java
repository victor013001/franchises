package com.pragma.challenge.franchises.infrastructure.entrypoints.handler.impl;

import static com.pragma.challenge.franchises.infrastructure.entrypoints.util.ResponseUtil.*;

import com.pragma.challenge.franchises.domain.api.FranchiseServicePort;
import com.pragma.challenge.franchises.domain.constants.ConstantsRoute;
import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.FranchiseDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.FranchiseUpdateDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.handler.FranchiseHandler;
import com.pragma.challenge.franchises.infrastructure.entrypoints.mapper.FranchiseMapper;
import com.pragma.challenge.franchises.infrastructure.entrypoints.mapper.ServerResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseHandlerImpl implements FranchiseHandler {
  private static final String LOG_PREFIX = "[FRANCHISE_HANDLER] >>>";

  private final ServerResponseMapper responseMapper;
  private final FranchiseServicePort franchiseServicePort;
  private final FranchiseMapper franchiseMapper;

  @Override
  public Mono<ServerResponse> createFranchise(ServerRequest request) {
    return request
        .bodyToMono(FranchiseDto.class)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            franchiseDto -> {
              log.info("{} Creating Franchise with Name {}", LOG_PREFIX, franchiseDto.name());
              return franchiseServicePort
                  .createFranchise(franchiseMapper.toModel(franchiseDto))
                  .doOnSuccess(
                      franchise ->
                          log.info(
                              "{} Franchise: {} saved with uuid: {}",
                              LOG_PREFIX,
                              franchise.name(),
                              franchise.uuid()));
            })
        .flatMap(
            franchise ->
                buildResponse(
                    ServerResponses.FRANCHISE_CREATED_SUCCESSFULLY.getHttpStatus(),
                    franchise,
                    null,
                    responseMapper))
        .doOnError(logErrorHandler(log, LOG_PREFIX))
        .onErrorResume(StandardException.class, standardErrorHandler(responseMapper))
        .onErrorResume(genericErrorHandler(responseMapper));
  }

  @Override
  public Mono<ServerResponse> updateName(ServerRequest request) {
    Mono<String> uuidMono =
        Mono.just(request.pathVariable(ConstantsRoute.FRANCHISE_UUID_PARAM))
            .filter(Strings::isNotBlank);

    Mono<FranchiseUpdateDto> dtoMono = request.bodyToMono(FranchiseUpdateDto.class);

    return Mono.zip(uuidMono, dtoMono)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            tuple -> {
              String franchiseUuid = tuple.getT1();
              FranchiseUpdateDto franchiseUpdateDto = tuple.getT2();
              log.info("{} Updating Franchise with uuid: {}", LOG_PREFIX, franchiseUuid);
              return franchiseServicePort
                  .updateFranchise(franchiseMapper.toModel(franchiseUpdateDto, franchiseUuid))
                  .doOnSuccess(
                      product ->
                          log.info(
                              "{} Franchise with uuid: {} updated.", LOG_PREFIX, franchiseUuid));
            })
        .then(
            buildResponse(
                ServerResponses.FRANCHISE_UPDATED_SUCCESSFULLY.getHttpStatus(),
                ServerResponses.FRANCHISE_UPDATED_SUCCESSFULLY.getMessage(),
                null,
                responseMapper))
        .doOnError(logErrorHandler(log, LOG_PREFIX))
        .onErrorResume(StandardException.class, standardErrorHandler(responseMapper))
        .onErrorResume(genericErrorHandler(responseMapper));
  }
}
