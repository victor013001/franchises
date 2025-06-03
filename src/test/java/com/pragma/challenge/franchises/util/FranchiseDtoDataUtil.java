package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.FranchiseDto;
import java.util.concurrent.ThreadLocalRandom;

public class FranchiseDtoDataUtil {
  private FranchiseDtoDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static FranchiseDto getFranchiseDto() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new FranchiseDto("Franchise" + randomId);
  }

  public static FranchiseDto getBadFranchiseDto() {
    return new FranchiseDto(null);
  }

  public static FranchiseDto getFranchiseDtoFixedName() {
    return new FranchiseDto("Franchise Zero");
  }
}
