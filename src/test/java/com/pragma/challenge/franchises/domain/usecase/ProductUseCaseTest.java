package com.pragma.challenge.franchises.domain.usecase;

import static com.pragma.challenge.franchises.util.ProductDataUtil.getInvalidProduct;
import static com.pragma.challenge.franchises.util.ProductDataUtil.getProduct;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BranchNotFound;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.ProductAlreadyExists;
import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
import com.pragma.challenge.franchises.domain.spi.ProductPersistencePort;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

  @InjectMocks ProductUseCase productUseCase;

  @Mock ProductPersistencePort productPersistencePort;
  @Mock BranchPersistencePort branchPersistencePort;

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
}
