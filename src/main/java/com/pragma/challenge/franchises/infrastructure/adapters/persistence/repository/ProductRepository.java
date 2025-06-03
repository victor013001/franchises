package com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
  Mono<Boolean> existsByName(String name);
}
