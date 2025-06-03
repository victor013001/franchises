package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductUpdateDto;
import java.util.concurrent.ThreadLocalRandom;

public class ProductUpdateDtoDataUtil {
  private ProductUpdateDtoDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static ProductUpdateDto getProductUpdateDto() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new ProductUpdateDto("Product" + randomId, 10);
  }

  public static ProductUpdateDto getBadProductUpdateDto() {
    return new ProductUpdateDto(null, -1);
  }
}
