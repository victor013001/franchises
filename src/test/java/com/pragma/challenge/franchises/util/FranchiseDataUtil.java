package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.domain.model.Franchise;
import java.util.concurrent.ThreadLocalRandom;

public class FranchiseDataUtil {
  private FranchiseDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static Franchise getFranchise() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new Franchise(null, "Franchise" + randomId, null);
  }

  public static Franchise getInvalidFranchise() {
    return new Franchise(null, null, null);
  }
}
