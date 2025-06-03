package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.domain.model.TopProduct;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TopProductDataUtil {
  private TopProductDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static List<TopProduct> getTopProducts() {
    return List.of(getTopProduct(), getTopProduct());
  }

  public static TopProduct getTopProduct() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new TopProduct("Branch" + randomId, "Product" + randomId, 10);
  }
}
