package com.pragma.challenge.franchises.infrastructure.entrypoints.handler;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductDto;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ProductHandler {

  /**
   * Add new product to a branch
   *
   * @param request Server Request with {@link ProductDto} in body
   * @return Publisher that emits the {@link ServerResponse} with the saved product
   */
  Mono<ServerResponse> createProduct(ServerRequest request);

  /**
   * Delete product from a branch
   *
   * @param request Server Request with Product Uuid in query params
   * @return Publisher that emits the {@link ServerResponse} with the success message
   */
  Mono<ServerResponse> deleteProduct(ServerRequest request);
}
