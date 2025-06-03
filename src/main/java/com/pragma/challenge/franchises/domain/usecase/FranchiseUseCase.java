package com.pragma.challenge.franchises.domain.usecase;

import com.pragma.challenge.franchises.domain.api.FranchiseServicePort;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.FranchiseAlreadyExists;
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
    return Mono.just(franchise)
        .flatMap(
            franchise1 -> {
              ValidNotBlank.valid(franchise1);
              return Mono.just(franchise1);
            })
        .flatMap(
            validFranchise ->
                franchisePersistencePort
                    .franchiseExistsByName(validFranchise.name())
                    .filter(Boolean.FALSE::equals)
                    .switchIfEmpty(Mono.error(FranchiseAlreadyExists::new))
                    .flatMap(exists -> franchisePersistencePort.saveFranchise(validFranchise)));
  }
}
