package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.domain.model.Branch;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BranchDataUtil {
  private BranchDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static Branch getBranch() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return new Branch(
        UUID.randomUUID().toString(), "Branch" + randomId, null, UUID.randomUUID().toString());
  }

  public static List<Branch> getBranches() {
    return List.of(getBranch(), getBranch());
  }

  public static Branch getInvalidBranch() {
    return new Branch(null, null, null, null);
  }
}
