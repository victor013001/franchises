package com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper;

import static com.pragma.challenge.franchises.util.ProductDataUtil.getProduct;
import static com.pragma.challenge.franchises.util.ProductEntityDataUtil.getProductEntity;
import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductEntityMapperTest {
  private ProductEntityMapper productMapper;

  @BeforeEach
  void setUp() {
    productMapper = new ProductEntityMapperImpl();
  }

  @Test
  void testToModel() {
    var productEntity = getProductEntity();
    var product = productMapper.toModel(productEntity);
    assertModel(productEntity, product);
  }

  @Test
  void testToEntity() {
    var product = getProduct();
    Long branchId = 2L;
    var productEntity = productMapper.toEntity(product, branchId);
    assertEntity(product, branchId, productEntity);
  }

  @Test
  void testToModelWithNullInput() {
    assertNull(productMapper.toModel(null));
  }

  @Test
  void testToEntityWithNullInput() {
    assertNull(productMapper.toEntity(null, null));
  }

  private void assertEntity(Product expected, Long branchId, ProductEntity actual) {
    assertNotNull(actual);
    assertNotNull(actual.getUuid());
    assertEquals(expected.name(), actual.getName());
    assertEquals(expected.stock(), actual.getStock());
    assertEquals(branchId, actual.getBranchId());
  }

  private void assertModel(ProductEntity expected, Product actual) {
    assertNotNull(actual);
    assertEquals(expected.getUuid(), actual.uuid());
    assertEquals(expected.getName(), actual.name());
    assertEquals(expected.getStock(), actual.stock());
  }
}
