package com.pragma.challenge.franchises.domain.usecase;

import com.pragma.challenge.franchises.domain.api.ProductServicePort;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BranchNotFound;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.FranchiseNotFound;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.ProductAlreadyExists;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.ProductNotFound;
import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.domain.model.TopProduct;
import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import com.pragma.challenge.franchises.domain.spi.ProductPersistencePort;
import com.pragma.challenge.franchises.domain.validation.ValidIntRange;
import com.pragma.challenge.franchises.domain.validation.ValidNotBlank;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase implements ProductServicePort {

  private final ProductPersistencePort productPersistencePort;
  private final BranchPersistencePort branchPersistencePort;
  private final FranchisePersistencePort franchisePersistencePort;

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

  @Override
  public Mono<Void> deleteProduct(String productUuid) {
    return productPersistencePort
        .productExistsByUuid(productUuid)
        .filter(Boolean.TRUE::equals)
        .switchIfEmpty(Mono.error(ProductNotFound::new))
        .flatMap(productExists -> productPersistencePort.deleteByUuid(productUuid));
  }

  @Override
  public Mono<Void> updateProduct(Product product) {
    return Mono.fromCallable(
            () -> {
              ValidNotBlank.valid(product);
              ValidIntRange.valid(product);
              return product;
            })
        .flatMap(
            validProduct ->
                productPersistencePort
                    .productExistsByUuid(product.uuid())
                    .filter(Boolean.TRUE::equals)
                    .switchIfEmpty(Mono.error(ProductNotFound::new))
                    .flatMap(
                        productExists ->
                            productPersistencePort.checkNewProductNameUnique(
                                product.name(), product.uuid()))
                    .filter(result -> result == 0)
                    .switchIfEmpty(Mono.error(BadRequest::new))
                    .flatMap(
                        validData ->
                            productPersistencePort.updateProduct(
                                product.uuid(), product.stock(), product.name())));
  }

  @Override
  public Flux<TopProduct> getProductsWithMoreStockByFranchiseUuid(String franchiseUuid) {
    return franchisePersistencePort
        .franchiseExistsByUuid(franchiseUuid)
        .filter(Boolean.TRUE::equals)
        .switchIfEmpty(Mono.error(FranchiseNotFound::new))
        .flatMapMany(
            exists ->
                productPersistencePort.getProductsWithMoreStockByFranchiseUuid(franchiseUuid));
  }
}
