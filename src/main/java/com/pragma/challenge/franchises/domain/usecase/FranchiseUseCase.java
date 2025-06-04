package com.pragma.challenge.franchises.domain.usecase;

import com.pragma.challenge.franchises.domain.api.FranchiseServicePort;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.FranchiseAlreadyExists;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.FranchiseNotFound;
import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import com.pragma.challenge.franchises.domain.validation.ValidNotBlank;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase implements FranchiseServicePort {

  private final FranchisePersistencePort franchisePersistencePort;

  @Override
  public Mono<Franchise> createFranchise(Franchise franchise) {
    return Mono.fromCallable(
            () -> {
              ValidNotBlank.valid(franchise);
              return franchise;
            })
        .flatMap(
            validFranchise ->
                franchisePersistencePort
                    .franchiseExistsByName(validFranchise.name())
                    .filter(Boolean.FALSE::equals)
                    .switchIfEmpty(Mono.error(FranchiseAlreadyExists::new))
                    .flatMap(exists -> franchisePersistencePort.saveFranchise(validFranchise)));
  }

  @Override
  public Mono<Void> updateFranchise(Franchise franchise) {
    return Mono.fromCallable(
            () -> {
              ValidNotBlank.valid(franchise);
              return franchise;
            })
        .flatMap(
            validFranchise ->
                franchisePersistencePort
                    .franchiseExistsByUuid(validFranchise.uuid())
                    .filter(Boolean.TRUE::equals)
                    .switchIfEmpty(Mono.error(FranchiseNotFound::new))
                    .flatMap(
                        franchiseExists ->
                            franchisePersistencePort.checkNewFranchiseNameUnique(
                                franchise.name(), franchise.uuid()))
                    .filter(result -> result == 0)
                    .switchIfEmpty(Mono.error(FranchiseAlreadyExists::new))
                    .flatMap(
                        validData ->
                            franchisePersistencePort.updateFranchise(
                                franchise.uuid(), franchise.name())));
  }
}
