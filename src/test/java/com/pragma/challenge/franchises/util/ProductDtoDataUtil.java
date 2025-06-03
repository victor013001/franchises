package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductDto;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ProductDtoDataUtil {
  private ProductDtoDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static ProductDto getProductDto() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new ProductDto("Product" + randomId, 100, UUID.randomUUID().toString());
  }

  public static ProductDto getProductDto(String franchiseUuid) {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new ProductDto("Product" + randomId, 100, franchiseUuid);
  }

  public static ProductDto getBadProductDto() {
    return new ProductDto(null, null, null);
  }

  public static ProductDto getProductDtoFixedName() {
    return new ProductDto("Product Zero", 100, UUID.randomUUID().toString());
  }

  public static ProductDto getProductDtoFixedName(String franchiseUuid) {
    return new ProductDto("Product Zero", 100, franchiseUuid);
  }
}
