package com.pragma.challenge.franchises.domain.api;

import com.pragma.challenge.franchises.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface BranchServicePort {
  /**
   * Creates new branch and added to a franchise
   *
   * @param branch The branch to save
   * @return Publisher that emits the created Branch
   */
  Mono<Branch> addToFranchise(Branch branch);
}
