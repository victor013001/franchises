package com.pragma.challenge.franchises.infrastructure.entrypoints.handler.impl;

import static com.pragma.challenge.franchises.infrastructure.entrypoints.util.ResponseUtil.*;

import com.pragma.challenge.franchises.domain.api.ProductServicePort;
import com.pragma.challenge.franchises.domain.constants.ConstantsRoute;
import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.handler.ProductHandler;
import com.pragma.challenge.franchises.infrastructure.entrypoints.mapper.ProductMapper;
import com.pragma.challenge.franchises.infrastructure.entrypoints.mapper.ServerResponseMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductHandlerImpl implements ProductHandler {
  private static final String LOG_PREFIX = "[PRODUCT_HANDLER] >>>";

  private final ServerResponseMapper responseMapper;
  private final ProductServicePort productServicePort;
  private final ProductMapper productMapper;

  @Override
  public Mono<ServerResponse> createProduct(ServerRequest request) {
    return request
        .bodyToMono(ProductDto.class)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            productRequest -> {
              log.info("{} Creating Product with Name: {}", LOG_PREFIX, productRequest.name());
              return productServicePort
                  .addToBranch(productMapper.toModel(productRequest))
                  .doOnSuccess(
                      product ->
                          log.info(
                              "{} Product: {} saved with uuid: {}.",
                              LOG_PREFIX,
                              product.name(),
                              product.uuid()));
            })
        .flatMap(
            product ->
                buildResponse(
                    ServerResponses.PRODUCT_CREATED_SUCCESSFULLY.getHttpStatus(),
                    product,
                    null,
                    responseMapper))
        .doOnError(logErrorHandler(log, LOG_PREFIX))
        .onErrorResume(StandardException.class, standardErrorHandler(responseMapper))
        .onErrorResume(genericErrorHandler(responseMapper));
  }

  @Override
  public Mono<ServerResponse> deleteProduct(ServerRequest request) {
    return Mono.just(request.queryParam(ConstantsRoute.PRODUCT_UUID_PARAM))
        .map(Optional::get)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            productUuid -> {
              log.info("{} Deleting Product with uuid: {}", LOG_PREFIX, productUuid);
              return productServicePort
                  .deleteProduct(productUuid)
                  .doOnSuccess(
                      product ->
                          log.info("{} Product with uuid: {} deleted.", LOG_PREFIX, productUuid));
            })
        .flatMap(
            ignore ->
                buildResponse(
                    ServerResponses.PRODUCT_DELETED_SUCCESSFULLY.getHttpStatus(),
                    ServerResponses.PRODUCT_DELETED_SUCCESSFULLY.getMessage(),
                    null,
                    responseMapper))
        .doOnError(logErrorHandler(log, LOG_PREFIX))
        .onErrorResume(StandardException.class, standardErrorHandler(responseMapper))
        .onErrorResume(genericErrorHandler(responseMapper));
  }
}
