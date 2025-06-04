package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchUpdateDto;
import java.util.concurrent.ThreadLocalRandom;

public class BranchUpdateDtoDataUtil {
  private BranchUpdateDtoDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static BranchUpdateDto getBranchUpdateDto() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new BranchUpdateDto("Branch" + randomId);
  }

  public static BranchUpdateDto getBadBranchUpdateDto() {
    return new BranchUpdateDto(null);
  }
}
