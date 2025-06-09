package com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter;

import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.domain.spi.ProductPersistencePort;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.ProductEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistencePort {
  private static final String LOG_PREFIX = "[PRODUCT_PERSISTENCE_ADAPTER] >>> ";

  private final ProductEntityMapper productEntityMapper;
  private final ProductRepository productRepository;

  @Override
  public Mono<Product> saveProduct(Product product, Long branchId) {
    return productRepository
        .save(productEntityMapper.toEntity(product, branchId))
        .map(productEntityMapper::toModel)
        .doOnSuccess(
            productSaved ->
                log.info("{} Product saved with uuid: {}", LOG_PREFIX, productSaved.uuid()));
  }

  @Override
  public Mono<Boolean> productExistsByName(String name) {
    return productRepository
        .existsByName(name)
        .doFirst(() -> log.info("{} Checking if Product exists by name: {}.", LOG_PREFIX, name));
  }

  @Override
  public Mono<Boolean> productExistsByUuid(String productUuid) {
    return productRepository
        .existsByUuid(productUuid)
        .doFirst(
            () -> log.info("{} Checking if Product exists by uuid: {}.", LOG_PREFIX, productUuid));
  }

  @Override
  public Mono<Void> deleteByUuid(String productUuid) {
    return productRepository
        .deleteByUuid(productUuid)
        .doFirst(() -> log.info("{} Deleting Product with uuid: {}.", LOG_PREFIX, productUuid));
  }

  @Override
  public Mono<Void> updateProduct(String uuid, Integer stock, String name) {
    return productRepository
        .updateProductByUuid(uuid, stock, name)
        .doFirst(
            () ->
                log.info("{} Updating stock and name of Product with uuid: {}.", LOG_PREFIX, uuid));
  }

  @Override
  public Mono<Integer> checkNewProductNameUnique(String name, String uuid) {
    return productRepository
        .newNameUnique(name, uuid)
        .doFirst(() -> log.info("{} Checking if new Product Name {} is unique.", LOG_PREFIX, name));
  }

  @Override
  public Mono<Product> getProductWithMoreStockInBranch(String branchUuid) {
    return productRepository
        .findTopProductByBranchUuid(branchUuid)
        .map(productEntityMapper::toModel)
        .doFirst(
            () ->
                log.info(
                    "{} Getting top product of branch with uuid: {}.", LOG_PREFIX, branchUuid));
  }
}
