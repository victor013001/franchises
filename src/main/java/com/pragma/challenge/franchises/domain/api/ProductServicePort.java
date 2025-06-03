package com.pragma.challenge.franchises.domain.api;

import com.pragma.challenge.franchises.domain.model.Product;
import reactor.core.publisher.Mono;

public interface ProductServicePort {
  /**
   * Creates new product and added to a branch
   *
   * @param product The product to save
   * @return Publisher that emits the created Product
   */
  Mono<Product> addToBranch(Product product);
}
