package com.pragma.challenge.franchises.domain.usecase;

import static com.pragma.challenge.franchises.util.BranchDataUtil.getBranch;
import static com.pragma.challenge.franchises.util.BranchDataUtil.getInvalidBranch;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BadRequest;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.BranchAlreadyExists;
import com.pragma.challenge.franchises.domain.exceptions.standard_exception.FranchiseNotFound;
import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
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
class BranchUseCaseTest {
  @InjectMocks BranchUseCase branchUseCase;
  @Mock BranchPersistencePort branchPersistencePort;
  @Mock FranchisePersistencePort franchisePersistencePort;

  @Test
  void addToFranchise() {
    var branch = getBranch();
    when(franchisePersistencePort.franchiseExistsByUuid(anyString())).thenReturn(Mono.just(true));
    when(branchPersistencePort.branchExistsByName(anyString())).thenReturn(Mono.just(false));
    when(franchisePersistencePort.getFranchiseIdByUuid(anyString())).thenReturn(Mono.just(1L));
    when(branchPersistencePort.saveBranch(any(Branch.class), anyLong()))
        .thenAnswer(
            invocation -> {
              Branch branchSaved = invocation.getArgument(0);
              return Mono.just(
                  new Branch(
                      UUID.randomUUID().toString(),
                      branchSaved.name(),
                      branchSaved.products(),
                      null));
            });

    StepVerifier.create(branchUseCase.addToFranchise(branch))
        .consumeNextWith(branchSaved -> assertNotNull(branchSaved.uuid()))
        .verifyComplete();

    verify(franchisePersistencePort).franchiseExistsByUuid(anyString());
    verify(branchPersistencePort).branchExistsByName(anyString());
    verify(franchisePersistencePort).getFranchiseIdByUuid(anyString());
    verify(branchPersistencePort).saveBranch(any(Branch.class), anyLong());
  }

  @Test
  void addInvalidBranch() {
    var branch = getInvalidBranch();

    StepVerifier.create(branchUseCase.addToFranchise(branch))
        .expectError(BadRequest.class)
        .verify();

    verify(franchisePersistencePort, never()).franchiseExistsByUuid(anyString());
    verify(branchPersistencePort, never()).branchExistsByName(anyString());
    verify(franchisePersistencePort, never()).getFranchiseIdByUuid(anyString());
    verify(branchPersistencePort, never()).saveBranch(any(Branch.class), anyLong());
  }

  @Test
  void addBranchFranchiseNotFound() {
    var branch = getBranch();
    when(franchisePersistencePort.franchiseExistsByUuid(anyString())).thenReturn(Mono.just(false));

    StepVerifier.create(branchUseCase.addToFranchise(branch))
        .expectError(FranchiseNotFound.class)
        .verify();

    verify(franchisePersistencePort).franchiseExistsByUuid(anyString());
    verify(branchPersistencePort, never()).branchExistsByName(anyString());
    verify(franchisePersistencePort, never()).getFranchiseIdByUuid(anyString());
    verify(branchPersistencePort, never()).saveBranch(any(Branch.class), anyLong());
  }

  @Test
  void addBranchNameAlreadyExists() {
    var branch = getBranch();
    when(franchisePersistencePort.franchiseExistsByUuid(anyString())).thenReturn(Mono.just(true));
    when(branchPersistencePort.branchExistsByName(anyString())).thenReturn(Mono.just(true));

    StepVerifier.create(branchUseCase.addToFranchise(branch))
        .expectError(BranchAlreadyExists.class)
        .verify();

    verify(franchisePersistencePort).franchiseExistsByUuid(anyString());
    verify(branchPersistencePort).branchExistsByName(anyString());
    verify(franchisePersistencePort, never()).getFranchiseIdByUuid(anyString());
    verify(branchPersistencePort, never()).saveBranch(any(Branch.class), anyLong());
  }
}
