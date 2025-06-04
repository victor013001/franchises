package com.pragma.challenge.franchises.domain.spi;

import com.pragma.challenge.franchises.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
  /**
   * Save new franchise in database
   *
   * @param franchise The franchise to save
   * @return Publisher that emits the saved Franchise with UUID
   */
  Mono<Franchise> saveFranchise(Franchise franchise);

  /**
   * Check if franchise name already exists
   *
   * @param name franchise name
   * @return Publisher that emits flag if franchise name already exists
   */
  Mono<Boolean> franchiseExistsByName(String name);

  /**
   * Check if franchise exists by uuid
   *
   * @param uuid franchise uuid
   * @return Publisher that emits flag if franchise exists
   */
  Mono<Boolean> franchiseExistsByUuid(String uuid);

  /**
   * Get franchise id by uuid
   *
   * @param uuid franchise uuid
   * @return Publisher that emits the franchise id
   */
  Mono<Long> getFranchiseIdByUuid(String uuid);

  /**
   * Check if the new franchise name is unique
   *
   * @param name new franchise name
   * @param uuid franchise uuid
   * @return Publisher that emits flag if the franchise exists
   */
  Mono<Integer> checkNewFranchiseNameUnique(String name, String uuid);

  /**
   * Update the name of the franchise uuid
   *
   * @param uuid franchise uuid
   * @param name franchise name
   * @return Void Publisher
   */
  Mono<Void> updateFranchise(String uuid, String name);
}
