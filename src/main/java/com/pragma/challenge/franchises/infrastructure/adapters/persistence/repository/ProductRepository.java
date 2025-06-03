package com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
  Mono<Boolean> existsByName(String name);

  Mono<Boolean> existsByUuid(String productUuid);

  Mono<Void> deleteByUuid(String productUuid);

  @Modifying
  @Query(
      """
            UPDATE product
            SET stock = :stock,
                name = :name
            WHERE uuid = :uuid
            """)
  Mono<Void> updateProductByUuid(String uuid, Integer stock, String name);

  @Query(
      """
            SELECT CASE WHEN COUNT(*) > 0
                THEN TRUE ELSE FALSE END
            FROM product
            WHERE name = :productName AND uuid <> :productUuid
            """)
  Mono<Integer> newNameUnique(String name, String uuid);
}
