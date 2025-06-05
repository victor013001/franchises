package com.pragma.challenge.franchises.domain.api;

import com.pragma.challenge.franchises.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
  /**
   * Creates new franchise
   *
   * @param franchise The franchise to save
   * @return Publisher that emits the created Franchise
   */
  Mono<Franchise> createFranchise(Franchise franchise);

  /**
   * Update a franchise by uuid
   *
   * @param franchise The franchise to update
   * @return Void Publisher
   */
  Mono<Void> updateFranchise(Franchise franchise);
}
