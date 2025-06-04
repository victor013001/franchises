package com.pragma.challenge.franchises.domain.spi;

import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.domain.model.TopProduct;
import reactor.core.publisher.Flux;
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

  /**
   * Check if the product exists by uuid
   *
   * @param productUuid product uuid
   * @return Publisher that emits flag if the product exists
   */
  Mono<Boolean> productExistsByUuid(String productUuid);

  /**
   * Delete a product by uuid
   *
   * @param productUuid product uuid
   * @return Void Publisher
   */
  Mono<Void> deleteByUuid(String productUuid);

  /**
   * Update the name and stock of the product uuid
   *
   * @param uuid product uuid
   * @param stock product stock
   * @param name product name
   * @return Void Publisher
   */
  Mono<Void> updateProduct(String uuid, Integer stock, String name);

  /**
   * Check if the new product name is unique
   *
   * @param name new product name
   * @param uuid product uuid
   * @return Publisher that emits flag if the product exists
   */
  Mono<Integer> checkNewProductNameUnique(String name, String uuid);

  /**
   * Get a Flux of Branches and Product with more Stock for the Franchise Uuid
   *
   * @param franchiseUuid The Franchise Uuid
   * @return Publisher that emits the Flux of TopProducts by branches
   */
  Flux<TopProduct> getProductsWithMoreStockByFranchiseUuid(String franchiseUuid);
}
