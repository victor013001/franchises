package com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter;

import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.FranchiseEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class FranchisePersistenceAdapter implements FranchisePersistencePort {
  private static final String LOG_PREFIX = "[FRANCHISE_PERSISTENCE_ADAPTER] >>> ";

  private final FranchiseEntityMapper franchiseEntityMapper;
  private final FranchiseRepository franchiseRepository;

  @Override
  public Mono<Franchise> saveFranchise(Franchise franchise) {
    return franchiseRepository
        .save(franchiseEntityMapper.toEntity(franchise))
        .map(franchiseEntityMapper::toModel)
        .doOnSuccess(
            franchiseSaved ->
                log.info("{} Franchise saved with uuid: {}", LOG_PREFIX, franchiseSaved.uuid()));
  }

  @Override
  public Mono<Boolean> franchiseExistsByName(String name) {
    return franchiseRepository
        .existsByName(name)
        .doFirst(() -> log.info("{} Checking if Franchise exists by name: {}.", LOG_PREFIX, name));
  }

  @Override
  public Mono<Boolean> franchiseExistsByUuid(String uuid) {
    return franchiseRepository
        .existsByUuid(uuid)
        .doFirst(() -> log.info("{} Checking if Franchise exists by uuid: {}.", LOG_PREFIX, uuid));
  }

  @Override
  public Mono<Long> getFranchiseIdByUuid(String uuid) {
    return franchiseRepository
        .getIdByUuid(uuid)
        .doFirst(() -> log.info("{} Getting Franchise id by uuid: {}.", LOG_PREFIX, uuid));
  }

  @Override
  public Mono<Integer> checkNewFranchiseNameUnique(String name, String uuid) {
    return franchiseRepository
        .newNameUnique(name, uuid)
        .doFirst(
            () -> log.info("{} Checking if new Franchise Name {} is unique.", LOG_PREFIX, name));
  }

  @Override
  public Mono<Void> updateFranchise(String uuid, String name) {
    return franchiseRepository
        .updateFranchiseByUuid(uuid, name)
        .doFirst(() -> log.info("{} Updating name of Franchise with uuid: {}.", LOG_PREFIX, uuid));
  }
}
