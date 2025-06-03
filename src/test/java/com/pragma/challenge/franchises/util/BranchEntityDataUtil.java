package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.BranchEntity;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BranchEntityDataUtil {
  private BranchEntityDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static BranchEntity getBranchEntity() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return BranchEntity.builder()
        .uuid(UUID.randomUUID().toString())
        .name("Branch" + randomId)
        .build();
  }

  public static BranchEntity getBranchEntityFixedName() {
    return BranchEntity.builder().uuid(UUID.randomUUID().toString()).name("Branch Zero").build();
  }

  public static BranchEntity getBranchEntityFixedName(Long franchiseId) {
    return BranchEntity.builder()
        .uuid(UUID.randomUUID().toString())
        .name("Branch Zero")
        .franchiseId(franchiseId)
        .build();
  }
}
