package com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter;

import static com.pragma.challenge.franchises.util.BranchDataUtil.getBranch;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.BranchEntity;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.BranchEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.BranchEntityMapperImpl;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.BranchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class BranchPersistenceAdapterTest {

  @InjectMocks BranchPersistenceAdapter branchPersistenceAdapter;
  @Mock BranchRepository branchRepository;
  @Spy BranchEntityMapper branchEntityMapper = new BranchEntityMapperImpl();

  @Test
  void saveBranch() {
    var branch = getBranch();
    var franchiseId = 1L;

    when(branchRepository.save(any(BranchEntity.class)))
        .thenAnswer(
            invocation -> {
              BranchEntity entity = invocation.getArgument(0);
              entity.setId(1L);
              return Mono.just(entity);
            });

    StepVerifier.create(branchPersistenceAdapter.saveBranch(branch, franchiseId))
        .consumeNextWith(branchSaved -> assertNotNull(branchSaved.uuid()))
        .verifyComplete();
  }

  @Test
  void branchExistsByName() {
    var name = "Branch Name";

    when(branchRepository.existsByName(anyString())).thenReturn(Mono.just(true));

    StepVerifier.create(branchPersistenceAdapter.branchExistsByName(name))
        .expectNext(Boolean.TRUE)
        .verifyComplete();

    verify(branchRepository).existsByName(name);
  }
}
