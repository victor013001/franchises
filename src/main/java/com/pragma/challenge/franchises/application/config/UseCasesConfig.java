package com.pragma.challenge.franchises.application.config;

import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import com.pragma.challenge.franchises.domain.usecase.BranchUseCase;
import com.pragma.challenge.franchises.domain.usecase.FranchiseUseCase;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter.BranchPersistenceAdapter;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter.FranchisePersistenceAdapter;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.BranchEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.FranchiseEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.BranchRepository;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

  private final FranchiseEntityMapper franchiseEntityMapper;
  private final FranchiseRepository franchiseRepository;
  private final BranchEntityMapper branchEntityMapper;
  private final BranchRepository branchRepository;

  @Bean
  public FranchiseUseCase franchiseUseCase(FranchisePersistencePort franchisePersistencePort) {
    return new FranchiseUseCase(franchisePersistencePort);
  }

  @Bean
  public FranchisePersistencePort franchisePersistencePort() {
    return new FranchisePersistenceAdapter(franchiseEntityMapper, franchiseRepository);
  }

  @Bean
  public BranchUseCase branchUseCase(
      BranchPersistencePort branchPersistencePort,
      FranchisePersistencePort franchisePersistencePort) {
    return new BranchUseCase(branchPersistencePort, franchisePersistencePort);
  }

  @Bean
  public BranchPersistencePort branchPersistencePort() {
    return new BranchPersistenceAdapter(branchEntityMapper, branchRepository);
  }
}
