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
  private static final String LOG_PREFIX = "[BRANCH_PERSISTENCE_ADAPTER] >>> ";

  private final ProductEntityMapper productEntityMapper;
  private final ProductRepository productRepository;

  @Override
  public Mono<Product> saveProduct(Product product, Long branchId) {
    return productRepository
        .save(productEntityMapper.toEntity(product, branchId))
        .map(productEntityMapper::toModel)
        .doOnSuccess(
            branchSaved ->
                log.info("{} Branch saved with uuid: {}", LOG_PREFIX, branchSaved.uuid()));
  }

  @Override
  public Mono<Boolean> productExistsByName(String name) {
    log.info("{} Checking if Branch exists by name: {}.", LOG_PREFIX, name);
    return productRepository.existsByName(name);
  }
}
