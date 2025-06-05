package com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter;

import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.BranchEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class BranchPersistenceAdapter implements BranchPersistencePort {
  private static final String LOG_PREFIX = "[BRANCH_PERSISTENCE_ADAPTER] >>> ";

  private final BranchEntityMapper branchEntityMapper;
  private final BranchRepository branchRepository;

  @Override
  public Mono<Branch> saveBranch(Branch branch, Long franchiseId) {
    return branchRepository
        .save(branchEntityMapper.toEntity(branch, franchiseId))
        .map(branchEntityMapper::toModel)
        .doOnSuccess(
            branchSaved ->
                log.info("{} Branch saved with uuid: {}", LOG_PREFIX, branchSaved.uuid()));
  }

  @Override
  public Mono<Boolean> branchExistsByName(String name) {
    log.info("{} Checking if Branch exists by name: {}.", LOG_PREFIX, name);
    return branchRepository.existsByName(name);
  }

  @Override
  public Mono<Boolean> branchExistsByUuid(String uuid) {
    log.info("{} Checking if the Branch exists by uuid: {}.", LOG_PREFIX, uuid);
    return branchRepository.existsByUuid(uuid);
  }

  @Override
  public Mono<Long> getBranchIdByUuid(String uuid) {
    log.info("{} Getting Branch id by uuid: {}.", LOG_PREFIX, uuid);
    return branchRepository.getIdByUuid(uuid);
  }

  @Override
  public Mono<Integer> checkNewBranchNameUnique(String name, String uuid) {
    log.info("{} Checking if new Branch Name {} is unique.", LOG_PREFIX, name);
    return branchRepository.newNameUnique(name, uuid);
  }

  @Override
  public Mono<Void> updateBranch(String uuid, String name) {
    log.info("{} Updating name of Branch with uuid: {}.", LOG_PREFIX, uuid);
    return branchRepository.updateBranchByUuid(uuid, name);
  }
}
