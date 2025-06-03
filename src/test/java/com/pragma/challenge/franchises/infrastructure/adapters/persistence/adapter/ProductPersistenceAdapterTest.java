package com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter;

import static com.pragma.challenge.franchises.util.ProductDataUtil.getProduct;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.ProductEntity;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.ProductEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.ProductEntityMapperImpl;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
