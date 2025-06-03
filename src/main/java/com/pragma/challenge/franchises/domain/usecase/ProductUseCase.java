package com.pragma.challenge.franchises.domain.usecase;

import com.pragma.challenge.franchises.domain.api.ProductServicePort;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BranchNotFound;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.ProductAlreadyExists;
import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
import com.pragma.challenge.franchises.domain.spi.ProductPersistencePort;
import com.pragma.challenge.franchises.domain.validation.ValidIntRange;
import com.pragma.challenge.franchises.domain.validation.ValidNotBlank;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase implements ProductServicePort {

  private final ProductPersistencePort productPersistencePort;
  private final BranchPersistencePort branchPersistencePort;

  @Override
  public Mono<Product> addToBranch(Product product) {
    return Mono.fromCallable(
            () -> {
              ValidNotBlank.valid(product);
              ValidIntRange.valid(product);
              return product;
            })
        .flatMap(
            validProduct ->
                branchPersistencePort
                    .branchExistsByUuid(product.branchUuid())
                    .filter(Boolean.TRUE::equals)
                    .switchIfEmpty(Mono.error(BranchNotFound::new))
                    .flatMap(
                        branchExists ->
                            productPersistencePort.productExistsByName(validProduct.name()))
                    .filter(Boolean.FALSE::equals)
                    .switchIfEmpty(Mono.error(ProductAlreadyExists::new))
                    .flatMap(
                        newName -> branchPersistencePort.getBranchIdByUuid(product.branchUuid()))
                    .flatMap(
                        franchiseId ->
                            productPersistencePort.saveProduct(validProduct, franchiseId)));
  }
}
