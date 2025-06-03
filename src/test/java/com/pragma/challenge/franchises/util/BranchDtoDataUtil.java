package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchDto;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BranchDtoDataUtil {
  private BranchDtoDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static BranchDto getBranchDto() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new BranchDto("Branch" + randomId, UUID.randomUUID().toString());
  }

  public static BranchDto getBranchDto(String franchiseUuid) {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new BranchDto("Branch" + randomId, franchiseUuid);
  }

  public static BranchDto getBadBranchDto() {
    return new BranchDto(null, null);
  }

  public static BranchDto getBranchDtoFixedName() {
    return new BranchDto("Branch Zero", UUID.randomUUID().toString());
  }

  public static BranchDto getBranchDtoFixedName(String franchiseUuid) {
    return new BranchDto("Branch Zero", franchiseUuid);
  }
}
