package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.FranchiseEntity;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class FranchiseEntityDataUtil {
  private FranchiseEntityDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static FranchiseEntity getFranchiseEntity() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return FranchiseEntity.builder()
        .uuid(UUID.randomUUID().toString())
        .name("Franchise " + randomId)
        .build();
  }

  public static FranchiseEntity getFranchiseEntityFixedName() {
    return FranchiseEntity.builder()
        .uuid(UUID.randomUUID().toString())
        .name("Franchise Zero")
        .build();
  }
}
