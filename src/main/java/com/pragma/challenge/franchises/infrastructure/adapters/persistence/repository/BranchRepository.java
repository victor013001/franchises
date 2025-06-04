package com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.BranchEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {
  Mono<Boolean> existsByName(String name);

  Mono<Boolean> existsByUuid(String uuid);

  Mono<Long> getIdByUuid(String franchiseUuid);

  @Query(
      """
            SELECT EXISTS
                (SELECT 1
                 FROM branch
                 WHERE uuid = :uuid AND name = :name)
            """)
  Mono<Integer> newNameUnique(String name, String uuid);

  @Modifying
  @Query(
      """
            UPDATE branch
            SET name = :name
            WHERE uuid = :uuid
            """)
  Mono<Void> updateBranchByUuid(String uuid, String name);
}
