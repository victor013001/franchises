package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.projection.TopProductProjection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TopProductProjectionDataUtil {
  private TopProductProjectionDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static List<TopProductProjection> getTopProductsProjection() {
    return List.of(getTopProductProjection(), getTopProductProjection());
  }

  public static TopProductProjection getTopProductProjection() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new TopProductProjection("Branch" + randomId, "Product" + randomId, 10);
  }
}
