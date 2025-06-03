package com.pragma.challenge.franchises.domain.usecase;

import static com.pragma.challenge.franchises.util.FranchiseDataUtil.getFranchise;
import static com.pragma.challenge.franchises.util.FranchiseDataUtil.getInvalidFranchise;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.FranchiseAlreadyExists;
import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {
  @InjectMocks FranchiseUseCase franchiseUseCase;
  @Mock FranchisePersistencePort franchisePersistencePort;

  @Test
  void createFranchise() {
    var franchise = getFranchise();
    when(franchisePersistencePort.franchiseExistsByName(anyString())).thenReturn(Mono.just(false));
    when(franchisePersistencePort.saveFranchise(any(Franchise.class)))
        .thenAnswer(
            invocationOnMock -> {
              Franchise franchiseSaved = invocationOnMock.getArgument(0);
              return Mono.just(
                  new Franchise(
                      UUID.randomUUID().toString(),
                      franchiseSaved.name(),
                      franchiseSaved.branches()));
            });

    StepVerifier.create(franchiseUseCase.createFranchise(franchise))
        .consumeNextWith(franchiseSaved -> assertNotNull(franchiseSaved.uuid()))
        .verifyComplete();

    verify(franchisePersistencePort).franchiseExistsByName(anyString());
    verify(franchisePersistencePort).saveFranchise(any(Franchise.class));
  }

  @Test
  void createInvalidFranchise() {
    var franchise = getInvalidFranchise();

    StepVerifier.create(franchiseUseCase.createFranchise(franchise))
        .expectError(BadRequest.class)
        .verify();

    verify(franchisePersistencePort, never()).franchiseExistsByName(anyString());
    verify(franchisePersistencePort, never()).saveFranchise(any(Franchise.class));
  }

  @Test
  void createFranchiseNameAlreadyExists() {
    var franchise = getFranchise();
    when(franchisePersistencePort.franchiseExistsByName(anyString())).thenReturn(Mono.just(true));

    StepVerifier.create(franchiseUseCase.createFranchise(franchise))
        .expectError(FranchiseAlreadyExists.class)
        .verify();

    verify(franchisePersistencePort).franchiseExistsByName(anyString());
    verify(franchisePersistencePort, never()).saveFranchise(any(Franchise.class));
  }
}
