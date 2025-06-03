package com.pragma.challenge.franchises.domain.spi;

import com.pragma.challenge.franchises.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface BranchPersistencePort {
  /**
   * Save new branch in database
   *
   * @param branch The branch to save
   * @param franchiseId The franchise id to relate the branch
   * @return Publisher that emits the saved Branch with UUID
   */
  Mono<Branch> saveBranch(Branch branch, Long franchiseId);

  /**
   * Check if branch name already exists
   *
   * @param name branch name
   * @return Publisher that emits flag if branch name already exists
   */
  Mono<Boolean> branchExistsByName(String name);

  /**
   * Check if branch exists by uuid
   *
   * @param uuid branch uuid
   * @return Publisher that emits flag if the branch exists
   */
  Mono<Boolean> branchExistsByUuid(String uuid);

  /**
   * Get branch id by uuid
   *
   * @param uuid branch uuid
   * @return Publisher that emits the branch id
   */
  Mono<Long> getBranchIdByUuid(String uuid);
}
