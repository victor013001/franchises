package com.pragma.challenge.franchises.domain.api;

import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.domain.model.TopProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServicePort {
  /**
   * Creates new product and added to a branch
   *
   * @param product The product to save
   * @return Publisher that emits the created Product
   */
  Mono<Product> addToBranch(Product product);

  /**
   * Delete a product by uuid
   *
   * @param productUuid The product uuid
   * @return Void Publisher
   */
  Mono<Void> deleteProduct(String productUuid);

  /**
   * Update a product by uuid
   *
   * @param product The product to update
   * @return Void Publisher
   */
  Mono<Void> updateProduct(Product product);

  /**
   * Get a Flux of Branches and Product with more Stock for the Franchise Uuid
   *
   * @param franchiseUuid The Franchise Uuid
   * @return Publisher that emits the Flux of TopProducts by branches
   */
  Flux<TopProduct> getProductsWithMoreStockByFranchiseUuid(String franchiseUuid);
}
