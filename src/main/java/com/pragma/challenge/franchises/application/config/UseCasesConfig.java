package com.pragma.challenge.franchises.application.config;

import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import com.pragma.challenge.franchises.domain.usecase.FranchiseUseCase;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter.FranchisePersistenceAdapter;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.FranchiseEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

  private final FranchiseEntityMapper franchiseEntityMapper;
  private final FranchiseRepository franchiseRepository;

  @Bean
  public FranchiseUseCase franchiseUseCase(FranchisePersistencePort franchisePersistencePort) {
    return new FranchiseUseCase(franchisePersistencePort);
  }

  @Bean
  public FranchisePersistencePort franchisePersistencePort() {
    return new FranchisePersistenceAdapter(franchiseEntityMapper, franchiseRepository);
  }
}
