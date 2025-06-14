package com.pragma.challenge.franchises.infrastructure.entrypoints.handler.impl;

import static com.pragma.challenge.franchises.infrastructure.entrypoints.util.ResponseUtil.*;

import com.pragma.challenge.franchises.domain.api.BranchServicePort;
import com.pragma.challenge.franchises.domain.constants.ConstantsRoute;
import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchUpdateDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.handler.BranchHandler;
import com.pragma.challenge.franchises.infrastructure.entrypoints.mapper.BranchMapper;
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
public class BranchHandlerImpl implements BranchHandler {
  private static final String LOG_PREFIX = "[BRANCH_HANDLER] >>>";

  private final ServerResponseMapper responseMapper;
  private final BranchServicePort branchServicePort;
  private final BranchMapper branchMapper;

  @Override
  public Mono<ServerResponse> addBranchToFranchise(ServerRequest request) {
    return request
        .bodyToMono(BranchDto.class)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            branchDto -> {
              log.info(
                  "{} Adding Branch with Name: {} to Franchise: {}",
                  LOG_PREFIX,
                  branchDto.name(),
                  branchDto.franchiseUuid());
              return branchServicePort
                  .addToFranchise(branchMapper.toModel(branchDto))
                  .doOnSuccess(
                      branch ->
                          log.info(
                              "{} Branch: {} saved with uuid: {} and added to franchise: {}.",
                              LOG_PREFIX,
                              branch.name(),
                              branch.uuid(),
                              branchDto.franchiseUuid()));
            })
        .flatMap(
            branch ->
                buildResponse(
                    ServerResponses.BRANCH_ADDED_TO_FRANCHISE_SUCCESSFULLY.getHttpStatus(),
                    branch,
                    null,
                    responseMapper))
        .doOnError(logErrorHandler(log, LOG_PREFIX))
        .onErrorResume(StandardException.class, standardErrorHandler(responseMapper))
        .onErrorResume(genericErrorHandler(responseMapper));
  }

  @Override
  public Mono<ServerResponse> updateName(ServerRequest request) {
    Mono<String> uuidMono =
        Mono.just(request.pathVariable(ConstantsRoute.BRANCH_UUID_PARAM))
            .filter(Strings::isNotBlank);

    Mono<BranchUpdateDto> dtoMono = request.bodyToMono(BranchUpdateDto.class);

    return Mono.zip(uuidMono, dtoMono)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            tuple -> {
              String branchUuid = tuple.getT1();
              BranchUpdateDto branchUpdateDto = tuple.getT2();
              log.info("{} Updating Branch with uuid: {}", LOG_PREFIX, branchUuid);
              return branchServicePort
                  .updateBranch(branchMapper.toModel(branchUpdateDto, branchUuid))
                  .doOnSuccess(
                      product ->
                          log.info("{} Branch with uuid: {} updated.", LOG_PREFIX, branchUuid));
            })
        .then(
            buildResponse(
                ServerResponses.BRANCH_UPDATED_SUCCESSFULLY.getHttpStatus(),
                ServerResponses.BRANCH_UPDATED_SUCCESSFULLY.getMessage(),
                null,
                responseMapper))
        .doOnError(logErrorHandler(log, LOG_PREFIX))
        .onErrorResume(StandardException.class, standardErrorHandler(responseMapper))
        .onErrorResume(genericErrorHandler(responseMapper));
  }
}
