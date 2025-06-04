package com.pragma.challenge.franchises.infrastructure.entrypoints.handler;

import com.pragma.challenge.franchises.domain.model.TopProduct;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductUpdateDto;
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
   * @param request Server Request with Product Uuid in path variable
   * @return Publisher that emits the {@link ServerResponse} with the success message
   */
  Mono<ServerResponse> deleteProduct(ServerRequest request);

  /**
   * Update a product by uuid
   *
   * @param request Server Request with {@link ProductUpdateDto} in body and Product UUID in path
   *     variable
   * @return Publisher that emits the {@link ServerResponse} with the updated product
   */
  Mono<ServerResponse> updateProduct(ServerRequest request);

  /**
   * Get list of Branches with Product with more Stock for the Franchise Uuid
   *
   * @param request Server Request with the Product UUID in path variable
   * @return Publisher that emits the {@link ServerResponse} with the {@link TopProduct}
   */
  Mono<ServerResponse> getFranchiseTopBranchesProduct(ServerRequest request);
}
