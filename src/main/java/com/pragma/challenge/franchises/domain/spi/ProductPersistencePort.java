package com.pragma.challenge.franchises.domain.spi;

import com.pragma.challenge.franchises.domain.model.Product;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {
  /**
   * Save new product in database
   *
   * @param product The product to save
   * @param branchId The branch id to relate the product
   * @return Publisher that emits the saved Product with UUID
   */
  Mono<Product> saveProduct(Product product, Long branchId);

  /**
   * Check if the product name already exists
   *
   * @param name product name
   * @return Publisher that emits flag if the product name already exists
   */
  Mono<Boolean> productExistsByName(String name);
}
