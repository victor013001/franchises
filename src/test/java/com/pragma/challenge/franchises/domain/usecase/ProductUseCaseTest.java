package com.pragma.challenge.franchises.domain.usecase;

import static com.pragma.challenge.franchises.util.BranchDataUtil.getBranches;
import static com.pragma.challenge.franchises.util.ProductDataUtil.getInvalidProduct;
import static com.pragma.challenge.franchises.util.ProductDataUtil.getProduct;
import static com.pragma.challenge.franchises.util.ProductDataUtil.getProductWithUuid;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BranchNotFound;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.FranchiseNotFound;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.ProductAlreadyExists;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.ProductNotFound;
import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import com.pragma.challenge.franchises.domain.spi.ProductPersistencePort;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

  @InjectMocks ProductUseCase productUseCase;

  @Mock ProductPersistencePort productPersistencePort;
  @Mock BranchPersistencePort branchPersistencePort;
  @Mock FranchisePersistencePort franchisePersistencePort;

  @Test
  void createValidProduct() {
    var product = getProduct();

    when(branchPersistencePort.branchExistsByUuid(anyString())).thenReturn(Mono.just(true));
    when(productPersistencePort.productExistsByName(anyString())).thenReturn(Mono.just(false));
    when(branchPersistencePort.getBranchIdByUuid(anyString())).thenReturn(Mono.just(1L));
    when(productPersistencePort.saveProduct(any(Product.class), anyLong()))
        .thenAnswer(
            invocation -> {
              Product productSaved = invocation.getArgument(0);
              return Mono.just(
                  new Product(
                      UUID.randomUUID().toString(),
                      productSaved.name(),
                      productSaved.stock(),
                      null));
            });

    StepVerifier.create(productUseCase.addToBranch(product))
        .consumeNextWith(saved -> assertEquals(product.name(), saved.name()))
        .verifyComplete();

    verify(branchPersistencePort).branchExistsByUuid(anyString());
    verify(productPersistencePort).productExistsByName(anyString());
    verify(branchPersistencePort).getBranchIdByUuid(anyString());
    verify(productPersistencePort).saveProduct(any(Product.class), anyLong());
  }

  @Test
  void createInvalidProduct() {
    var product = getInvalidProduct();

    StepVerifier.create(productUseCase.addToBranch(product)).expectError(BadRequest.class).verify();

    verify(branchPersistencePort, never()).branchExistsByUuid(anyString());
    verify(productPersistencePort, never()).productExistsByName(anyString());
    verify(branchPersistencePort, never()).getBranchIdByUuid(anyString());
    verify(productPersistencePort, never()).saveProduct(any(Product.class), anyLong());
  }

  @Test
  void branchNotFound() {
    var product = getProduct();
    when(branchPersistencePort.branchExistsByUuid(anyString())).thenReturn(Mono.just(false));

    StepVerifier.create(productUseCase.addToBranch(product))
        .expectError(BranchNotFound.class)
        .verify();

    verify(productPersistencePort, never()).productExistsByName(anyString());
    verify(branchPersistencePort, never()).getBranchIdByUuid(anyString());
    verify(productPersistencePort, never()).saveProduct(any(Product.class), anyLong());
  }

  @Test
  void productAlreadyExists() {
    var product = getProduct();

    when(branchPersistencePort.branchExistsByUuid(anyString())).thenReturn(Mono.just(true));
    when(productPersistencePort.productExistsByName(anyString())).thenReturn(Mono.just(true));

    StepVerifier.create(productUseCase.addToBranch(product))
        .expectError(ProductAlreadyExists.class)
        .verify();

    verify(branchPersistencePort, never()).getBranchIdByUuid(anyString());
    verify(productPersistencePort, never()).saveProduct(any(Product.class), anyLong());
  }

  @Test
  void deleteProductSuccess() {
    var uuid = UUID.randomUUID().toString();
    when(productPersistencePort.productExistsByUuid(uuid)).thenReturn(Mono.just(true));
    when(productPersistencePort.deleteByUuid(uuid)).thenReturn(Mono.empty());

    StepVerifier.create(productUseCase.deleteProduct(uuid)).verifyComplete();

    verify(productPersistencePort).productExistsByUuid(uuid);
    verify(productPersistencePort).deleteByUuid(uuid);
  }

  @Test
  void deleteProductNotFound() {
    var uuid = UUID.randomUUID().toString();
    when(productPersistencePort.productExistsByUuid(uuid)).thenReturn(Mono.just(false));

    StepVerifier.create(productUseCase.deleteProduct(uuid))
        .expectError(ProductNotFound.class)
        .verify();

    verify(productPersistencePort).productExistsByUuid(uuid);
    verify(productPersistencePort, never()).deleteByUuid(anyString());
  }

  @Test
  void updateProductSuccess() {
    var product = getProductWithUuid();

    when(productPersistencePort.productExistsByUuid(product.uuid())).thenReturn(Mono.just(true));
    when(productPersistencePort.updateProduct(product.uuid(), product.stock(), product.name()))
        .thenReturn(Mono.empty());
    when(productPersistencePort.checkNewProductNameUnique(anyString(), anyString()))
        .thenReturn(Mono.just(0));

    StepVerifier.create(productUseCase.updateProduct(product)).verifyComplete();

    verify(productPersistencePort).productExistsByUuid(product.uuid());
    verify(productPersistencePort).updateProduct(product.uuid(), product.stock(), product.name());
    verify(productPersistencePort).checkNewProductNameUnique(anyString(), anyString());
  }

  @Test
  void updateProductNotFound() {
    var product = getProductWithUuid();

    when(productPersistencePort.productExistsByUuid(product.uuid())).thenReturn(Mono.just(false));

    StepVerifier.create(productUseCase.updateProduct(product))
        .expectError(ProductNotFound.class)
        .verify();

    verify(productPersistencePort).productExistsByUuid(product.uuid());
    verify(productPersistencePort, never()).updateProduct(any(), anyInt(), anyString());
    verify(productPersistencePort, never()).checkNewProductNameUnique(anyString(), anyString());
  }

  @Test
  void updateProductInvalid() {
    var invalidProduct = getInvalidProduct();

    StepVerifier.create(productUseCase.updateProduct(invalidProduct))
        .expectError(BadRequest.class)
        .verify();

    verify(productPersistencePort, never()).productExistsByUuid(anyString());
    verify(productPersistencePort, never()).updateProduct(any(), anyInt(), anyString());
    verify(productPersistencePort, never()).checkNewProductNameUnique(anyString(), anyString());
  }

  @Test
  void updateProductNameInvalid() {
    var product = getProductWithUuid();

    when(productPersistencePort.productExistsByUuid(product.uuid())).thenReturn(Mono.just(true));
    when(productPersistencePort.checkNewProductNameUnique(anyString(), anyString()))
        .thenReturn(Mono.just(1));

    StepVerifier.create(productUseCase.updateProduct(product))
        .expectError(BadRequest.class)
        .verify();

    verify(productPersistencePort).productExistsByUuid(product.uuid());
    verify(productPersistencePort, never()).updateProduct(any(), anyInt(), anyString());
    verify(productPersistencePort).checkNewProductNameUnique(anyString(), anyString());
  }

  @Test
  void getProductsWithMoreStockByFranchiseUuidSuccess() {
    var uuid = UUID.randomUUID().toString();

    when(franchisePersistencePort.franchiseExistsByUuid(anyString())).thenReturn(Mono.just(true));
    when(branchPersistencePort.getBranchesByFranchiseUuid(anyString()))
        .thenReturn(Flux.fromIterable(getBranches()));
    when(productPersistencePort.getProductWithMoreStockInBranch(anyString()))
        .thenReturn(Mono.just(getProduct()));

    StepVerifier.create(productUseCase.getProductsWithMoreStockByFranchiseUuid(uuid))
        .expectNextCount(2)
        .verifyComplete();

    verify(franchisePersistencePort).franchiseExistsByUuid(anyString());
    verify(branchPersistencePort).getBranchesByFranchiseUuid(anyString());
    verify(productPersistencePort, times(2)).getProductWithMoreStockInBranch(anyString());
  }

  @Test
  void getProductsWithMoreStockByFranchiseUuidBadRequest() {
    var franchiseUuid = UUID.randomUUID().toString();

    when(franchisePersistencePort.franchiseExistsByUuid(anyString())).thenReturn(Mono.just(false));

    StepVerifier.create(productUseCase.getProductsWithMoreStockByFranchiseUuid(franchiseUuid))
        .expectError(FranchiseNotFound.class)
        .verify();

    verify(franchisePersistencePort).franchiseExistsByUuid(anyString());
    verifyNoInteractions(productPersistencePort);
  }
}
