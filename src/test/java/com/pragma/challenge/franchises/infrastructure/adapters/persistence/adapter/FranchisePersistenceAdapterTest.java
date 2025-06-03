package com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter;

import static com.pragma.challenge.franchises.util.FranchiseDataUtil.getFranchise;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.FranchiseEntity;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.FranchiseEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.FranchiseEntityMapperImpl;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.FranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranchisePersistenceAdapterTest {

  @InjectMocks FranchisePersistenceAdapter franchisePersistenceAdapter;
  @Mock FranchiseRepository franchiseRepository;
  @Spy FranchiseEntityMapper franchiseEntityMapper = new FranchiseEntityMapperImpl();

  @Test
  void saveFranchiseName() {
    var franchise = getFranchise();
    when(franchiseRepository.save(any(FranchiseEntity.class)))
        .thenAnswer(
            invocationOnMock -> {
              FranchiseEntity franchiseSaved = invocationOnMock.getArgument(0);
              franchiseSaved.setId(1L);
              return Mono.just(franchiseSaved);
            });

    StepVerifier.create(franchisePersistenceAdapter.saveFranchise(franchise))
        .consumeNextWith(franchiseSaved -> assertNotNull(franchiseSaved.uuid()))
        .verifyComplete();
  }

  @Test
  void franchiseExistsByName() {
    var name = "Franchise Name";
    when(franchiseRepository.existsByName(anyString())).thenReturn(Mono.just(true));
    StepVerifier.create(franchisePersistenceAdapter.franchiseExistsByName(name))
        .expectNext(Boolean.TRUE)
        .verifyComplete();
    verify(franchiseRepository).existsByName(name);
  }

  @Test
  void franchiseExistsByUuid() {
    var uuid = "683f017d-2780-8004-b45c-278ac08f8757";

    when(franchiseRepository.existsByUuid(anyString())).thenReturn(Mono.just(true));

    StepVerifier.create(franchisePersistenceAdapter.franchiseExistsByUuid(uuid))
        .expectNext(Boolean.TRUE)
        .verifyComplete();

    verify(franchiseRepository).existsByUuid(uuid);
  }

  @Test
  void getFranchiseIdByUuid() {
    var uuid = "683f017d-2780-8004-b45c-278ac08f8757";
    var franchiseId = 1L;

    when(franchiseRepository.getIdByUuid(anyString())).thenReturn(Mono.just(franchiseId));

    StepVerifier.create(franchisePersistenceAdapter.getFranchiseIdByUuid(uuid))
        .expectNext(franchiseId)
        .verifyComplete();

    verify(franchiseRepository).getIdByUuid(uuid);
  }
}
