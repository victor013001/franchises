package com.pragma.challenge.franchises;

import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.BRANCH_BASE_PATH;
import static com.pragma.challenge.franchises.util.BranchDtoDataUtil.getBadBranchDto;
import static com.pragma.challenge.franchises.util.BranchDtoDataUtil.getBranchDto;
import static com.pragma.challenge.franchises.util.BranchDtoDataUtil.getBranchDtoFixedName;
import static com.pragma.challenge.franchises.util.BranchEntityDataUtil.getBranchEntityFixedName;
import static com.pragma.challenge.franchises.util.FranchiseEntityDataUtil.getFranchiseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardError;
import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.BranchRepository;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.FranchiseRepository;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.DefaultServerResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("it")
@AutoConfigureWebTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BranchRouterRestITTest {

  @Autowired WebTestClient webTestClient;
  @Autowired BranchRepository branchRepository;
  @Autowired FranchiseRepository franchiseRepository;

  private String franchiseUuid;

  @BeforeEach
  void setUp() {
    var saved = franchiseRepository.save(getFranchiseEntity()).block();
    assert saved != null;
    franchiseUuid = saved.getUuid();
    branchRepository.save(getBranchEntityFixedName(saved.getId())).block();
  }

  @AfterEach
  void cleanDatabase() {
    branchRepository.deleteAll().then(franchiseRepository.deleteAll()).block();
  }

  @Test
  void addBranchToFranchise() {
    webTestClient
        .post()
        .uri(BRANCH_BASE_PATH)
        .bodyValue(getBranchDto(franchiseUuid))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Branch, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              var data = response.data();
              assertNotNull(data);
              assertNotNull(data.uuid());
            });
  }

  @Test
  void addBranchBadRequest() {
    webTestClient
        .post()
        .uri(BRANCH_BASE_PATH)
        .bodyValue(getBadBranchDto())
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Object, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              var error = response.error();
              assertNotNull(error);
              assertNotNull(error.getDescription());
              assertEquals(ServerResponses.BAD_REQUEST.getMessage(), error.getDescription());
            });
  }

  @Test
  void addBranchNameAlreadyExists() {
    webTestClient
        .post()
        .uri(BRANCH_BASE_PATH)
        .bodyValue(getBranchDtoFixedName(franchiseUuid))
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Object, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              var error = response.error();
              assertNotNull(error);
              assertNotNull(error.getDescription());
              assertEquals(
                  ServerResponses.BRANCH_ALREADY_EXISTS.getMessage(), error.getDescription());
            });
  }
}
