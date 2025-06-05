package com.pragma.challenge.franchises.infrastructure.entrypoints.router;

import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.FRANCHISE_BASE_PATH;
import static com.pragma.challenge.franchises.util.BranchUpdateDtoDataUtil.getBadBranchUpdateDto;
import static com.pragma.challenge.franchises.util.BranchUpdateDtoDataUtil.getBranchUpdateDto;
import static com.pragma.challenge.franchises.util.FranchiseDtoDataUtil.*;
import static com.pragma.challenge.franchises.util.FranchiseEntityDataUtil.getFranchiseEntity;
import static com.pragma.challenge.franchises.util.FranchiseEntityDataUtil.getFranchiseEntityFixedName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.pragma.challenge.franchises.config.TestcontainersConfiguration;
import com.pragma.challenge.franchises.domain.constants.ConstantsMsg;
import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardError;
import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.FranchiseRepository;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.DefaultServerResponse;
import java.util.List;
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
class FranchisesRouterRestITTest {

  @Autowired WebTestClient webTestClient;
  @Autowired FranchiseRepository franchiseRepository;

  private String franchiseUuid;

  @BeforeEach
  void setUp() {
    var saved =
        franchiseRepository
            .saveAll(List.of(getFranchiseEntity(), getFranchiseEntityFixedName()))
            .blockLast();
    assert saved != null;
    franchiseUuid = saved.getUuid();
  }

  @AfterEach
  void cleanDatabase() {
    franchiseRepository.deleteAll().block();
  }

  @Test
  void createFranchise() {
    webTestClient
        .post()
        .uri(FRANCHISE_BASE_PATH)
        .bodyValue(getFranchiseDto())
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Franchise, StandardError>>() {})
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
  void createFranchiseBadRequest() {
    webTestClient
        .post()
        .uri(FRANCHISE_BASE_PATH)
        .bodyValue(getBadFranchiseDto())
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
  void createFranchiseNameAlreadyExists() {
    webTestClient
        .post()
        .uri(FRANCHISE_BASE_PATH)
        .bodyValue(getFranchiseDtoFixedName())
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
                  ServerResponses.FRANCHISE_ALREADY_EXISTS.getMessage(), error.getDescription());
            });
  }

  @Test
  void updateFranchise() {
    webTestClient
        .patch()
        .uri(String.format("%s/%s", FRANCHISE_BASE_PATH, franchiseUuid))
        .bodyValue(getBranchUpdateDto())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<String, StandardError>>() {})
        .consumeWith(
            exchangeResult -> {
              var response = exchangeResult.getResponseBody();
              assertNotNull(response);
              var data = response.data();
              assertNotNull(data);
              assertEquals(ConstantsMsg.FRANCHISE_UPDATED_SUCCESSFULLY_MSG, data);
            });
  }

  @Test
  void updateFranchiseBadRequest() {
    webTestClient
        .patch()
        .uri(String.format("%s/%s", FRANCHISE_BASE_PATH, " "))
        .bodyValue(getBadBranchUpdateDto())
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
  void updateNotFoundFranchise() {
    webTestClient
        .patch()
        .uri(String.format("%s/%s", FRANCHISE_BASE_PATH, "1"))
        .bodyValue(getBranchUpdateDto())
        .exchange()
        .expectStatus()
        .isNotFound()
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
                  ServerResponses.FRANCHISE_NOT_FOUND.getMessage(), error.getDescription());
            });
  }
}
