package com.pragma.challenge.franchises.domain.usecase;

import com.pragma.challenge.franchises.domain.api.BranchServicePort;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BranchAlreadyExists;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BranchNotFound;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.FranchiseNotFound;
import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase implements BranchServicePort {

  private final BranchPersistencePort branchPersistencePort;
  private final FranchisePersistencePort franchisePersistencePort;

  @Override
  public Mono<Branch> addToFranchise(Branch branch) {
    return Mono.just(branch)
        .filter(this::isValidBranch)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            validBranch ->
                franchisePersistencePort
                    .franchiseExistsByUuid(branch.franchiseUuid())
                    .filter(Boolean.TRUE::equals)
                    .switchIfEmpty(Mono.error(FranchiseNotFound::new))
                    .flatMap(
                        franchiseExists ->
                            branchPersistencePort.branchExistsByName(validBranch.name()))
                    .filter(Boolean.FALSE::equals)
                    .switchIfEmpty(Mono.error(BranchAlreadyExists::new))
                    .flatMap(
                        newName ->
                            franchisePersistencePort.getFranchiseIdByUuid(branch.franchiseUuid()))
                    .flatMap(
                        franchiseId -> branchPersistencePort.saveBranch(validBranch, franchiseId)));
  }

  @Override
  public Mono<Void> updateBranch(Branch branch) {
    return Mono.just(branch)
        .filter(this::isValidBranch)
        .switchIfEmpty(Mono.error(BadRequest::new))
        .flatMap(
            validBranch ->
                branchPersistencePort
                    .branchExistsByUuid(validBranch.uuid())
                    .filter(Boolean.TRUE::equals)
                    .switchIfEmpty(Mono.error(BranchNotFound::new))
                    .flatMap(
                        branchExists ->
                            branchPersistencePort.checkNewBranchNameUnique(
                                branch.name(), branch.uuid()))
                    .filter(result -> result == 0)
                    .switchIfEmpty(Mono.error(BranchAlreadyExists::new))
                    .flatMap(
                        validData ->
                            branchPersistencePort.updateBranch(branch.uuid(), branch.name())));
  }

  private boolean isValidBranch(Branch branch) {
    return Strings.isNotBlank(branch.name()) && Strings.isNotBlank(branch.franchiseUuid());
  }
}
