package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.domain.model.Product;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ProductDataUtil {
  private ProductDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static Product getProduct() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new Product(null, "Product" + randomId, 100, UUID.randomUUID().toString());
  }

  public static Product getInvalidProduct() {
    return new Product(null, null, null, null);
  }
}
