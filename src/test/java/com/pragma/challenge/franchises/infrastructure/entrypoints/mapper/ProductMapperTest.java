package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import static com.pragma.challenge.franchises.util.ProductDtoDataUtil.getProductDto;
import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductMapperTest {
  private ProductMapper productMapper;

  @BeforeEach
  void setUp() {
    productMapper = new ProductMapperImpl();
  }

  @Test
  void testToModel() {
    var productDto = getProductDto();
    var product = productMapper.toModel(productDto);
    assertModel(productDto, product);
  }

  private void assertModel(ProductDto expected, Product actual) {
    assertNotNull(actual);
    assertNull(actual.uuid());
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.stock(), actual.stock());
    assertEquals(expected.branchUuid(), actual.branchUuid());
  }
}
