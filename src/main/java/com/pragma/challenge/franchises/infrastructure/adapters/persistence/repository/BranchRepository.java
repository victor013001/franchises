package com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {
  Mono<Boolean> existsByName(String name);
}
