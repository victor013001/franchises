package com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter;

import static com.pragma.challenge.franchises.util.ProductDataUtil.getProduct;
import static com.pragma.challenge.franchises.util.TopProductProjectionDataUtil.getTopProductsProjection;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.ProductEntity;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.ProductEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.ProductEntityMapperImpl;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.ProductRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductPersistenceAdapterTest {

  @InjectMocks ProductPersistenceAdapter productPersistenceAdapter;

  @Mock ProductRepository productRepository;

  @Spy ProductEntityMapper productEntityMapper = new ProductEntityMapperImpl();

  @Test
  void saveProduct() {
    var product = getProduct();
    var branchId = 1L;

    when(productRepository.save(any(ProductEntity.class)))
        .thenAnswer(
            invocation -> {
              ProductEntity entity = invocation.getArgument(0);
              entity.setId(1L);
              return Mono.just(entity);
            });

    StepVerifier.create(productPersistenceAdapter.saveProduct(product, branchId))
        .expectNextMatches(p -> p.name().equals(product.name()))
        .verifyComplete();

    verify(productRepository).save(any(ProductEntity.class));
  }

  @Test
  void productExistsByName() {
    var name = "Coke";
    when(productRepository.existsByName(name)).thenReturn(Mono.just(true));

    StepVerifier.create(productPersistenceAdapter.productExistsByName(name))
        .expectNext(true)
        .verifyComplete();

    verify(productRepository).existsByName(anyString());
  }

  @Test
  void productExistsByUuid() {
    var uuid = "683f017d-2780-8004-b45c-278ac08f8757";

    when(productRepository.existsByUuid(anyString())).thenReturn(Mono.just(true));

    StepVerifier.create(productPersistenceAdapter.productExistsByUuid(uuid))
        .expectNext(Boolean.TRUE)
        .verifyComplete();

    verify(productRepository).existsByUuid(uuid);
  }

  @Test
  void deleteByUuid() {
    var uuid = "683f017d-2780-8004-b45c-278ac08f8757";

    when(productRepository.deleteByUuid(anyString())).thenReturn(Mono.empty());

    StepVerifier.create(productPersistenceAdapter.deleteByUuid(uuid)).verifyComplete();

    verify(productRepository).deleteByUuid(uuid);
  }

  @Test
  void updateProduct() {
    var uuid = "683f017d-2780-8004-b45c-278ac08f8757";
    var stock = 10;
    var name = "Updated Name";

    when(productRepository.updateProductByUuid(uuid, stock, name)).thenReturn(Mono.empty());

    StepVerifier.create(productPersistenceAdapter.updateProduct(uuid, stock, name))
        .verifyComplete();

    verify(productRepository).updateProductByUuid(uuid, stock, name);
  }

  @Test
  void getProductsWithMoreStockByFranchiseUuid() {
    var franchiseUuid = UUID.randomUUID().toString();
    var topProductProjections = getTopProductsProjection();
    when(productRepository.findTopStockProductsByFranchise(anyString()))
        .thenReturn(Flux.fromIterable(topProductProjections));

    StepVerifier.create(
            productPersistenceAdapter.getProductsWithMoreStockByFranchiseUuid(franchiseUuid))
        .expectNextCount(2)
        .verifyComplete();

    verify(productRepository).findTopStockProductsByFranchise(franchiseUuid);
  }
}
