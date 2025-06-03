package com.pragma.challenge.franchises.application.config;

import com.pragma.challenge.franchises.domain.api.BranchServicePort;
import com.pragma.challenge.franchises.domain.api.FranchiseServicePort;
import com.pragma.challenge.franchises.domain.api.ProductServicePort;
import com.pragma.challenge.franchises.domain.spi.BranchPersistencePort;
import com.pragma.challenge.franchises.domain.spi.FranchisePersistencePort;
import com.pragma.challenge.franchises.domain.spi.ProductPersistencePort;
import com.pragma.challenge.franchises.domain.usecase.BranchUseCase;
import com.pragma.challenge.franchises.domain.usecase.FranchiseUseCase;
import com.pragma.challenge.franchises.domain.usecase.ProductUseCase;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter.BranchPersistenceAdapter;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter.FranchisePersistenceAdapter;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.adapter.ProductPersistenceAdapter;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.BranchEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.FranchiseEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper.ProductEntityMapper;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.BranchRepository;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.FranchiseRepository;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.ProductRepository;
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
  private final ProductEntityMapper productEntityMapper;
  private final ProductRepository productRepository;

  @Bean
  public FranchiseServicePort franchiseServicePort(
      FranchisePersistencePort franchisePersistencePort) {
    return new FranchiseUseCase(franchisePersistencePort);
  }

  @Bean
  public FranchisePersistencePort franchisePersistencePort() {
    return new FranchisePersistenceAdapter(franchiseEntityMapper, franchiseRepository);
  }

  @Bean
  public BranchServicePort branchServicePort(
      BranchPersistencePort branchPersistencePort,
      FranchisePersistencePort franchisePersistencePort) {
    return new BranchUseCase(branchPersistencePort, franchisePersistencePort);
  }

  @Bean
  public BranchPersistencePort branchPersistencePort() {
    return new BranchPersistenceAdapter(branchEntityMapper, branchRepository);
  }

  @Bean
  public ProductServicePort productUseCase(
      ProductPersistencePort productPersistencePort,
      BranchPersistencePort branchPersistencePort,
      FranchisePersistencePort franchisePersistencePort) {
    return new ProductUseCase(
        productPersistencePort, branchPersistencePort, franchisePersistencePort);
  }

  @Bean
  public ProductPersistencePort productPersistencePort() {
    return new ProductPersistenceAdapter(productEntityMapper, productRepository);
  }
}
