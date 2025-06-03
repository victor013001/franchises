package com.pragma.challenge.franchises;

import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.PRODUCT_BASE_PATH;
import static com.pragma.challenge.franchises.util.BranchEntityDataUtil.getBranchEntityFixedName;
import static com.pragma.challenge.franchises.util.FranchiseEntityDataUtil.getFranchiseEntity;
import static com.pragma.challenge.franchises.util.ProductDtoDataUtil.getBadProductDto;
import static com.pragma.challenge.franchises.util.ProductDtoDataUtil.getProductDto;
import static com.pragma.challenge.franchises.util.ProductDtoDataUtil.getProductDtoFixedName;
import static com.pragma.challenge.franchises.util.ProductEntityDataUtil.getProductEntityFixedName;
import static com.pragma.challenge.franchises.util.ProductUpdateDtoDataUtil.getBadProductUpdateDto;
import static com.pragma.challenge.franchises.util.ProductUpdateDtoDataUtil.getProductUpdateDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.pragma.challenge.franchises.domain.constants.ConstantsMsg;
import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardError;
import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.BranchRepository;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.FranchiseRepository;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository.ProductRepository;
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
class ProductRouterRestITTest {

  @Autowired WebTestClient webTestClient;
  @Autowired ProductRepository productRepository;
  @Autowired BranchRepository branchRepository;
  @Autowired FranchiseRepository franchiseRepository;

  private String branchUuid;
  private String productUuid;

  @BeforeEach
  void setUp() {
    var franchiseSaved = franchiseRepository.save(getFranchiseEntity()).block();
    assert franchiseSaved != null;
    var branchSaved =
        branchRepository.save(getBranchEntityFixedName(franchiseSaved.getId())).block();
    assert branchSaved != null;
    branchUuid = branchSaved.getUuid();
    var productSaved =
        productRepository.save(getProductEntityFixedName(branchSaved.getId())).block();
    assert productSaved != null;
    productUuid = productSaved.getUuid();
  }

  @AfterEach
  void cleanDatabase() {
    productRepository.deleteAll().then(branchRepository.deleteAll()).block();
  }

  @Test
  void addProductToBranch() {
    webTestClient
        .post()
        .uri(PRODUCT_BASE_PATH)
        .bodyValue(getProductDto(branchUuid))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(
            new ParameterizedTypeReference<DefaultServerResponse<Product, StandardError>>() {})
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
  void addProductBadRequest() {
    webTestClient
        .post()
        .uri(PRODUCT_BASE_PATH)
        .bodyValue(getBadProductDto())
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
        .uri(PRODUCT_BASE_PATH)
        .bodyValue(getProductDtoFixedName(branchUuid))
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
                  ServerResponses.PRODUCT_ALREADY_EXISTS.getMessage(), error.getDescription());
            });
  }

  @Test
  void deleteProduct() {
    webTestClient
        .delete()
        .uri(String.format("%s/%s", PRODUCT_BASE_PATH, productUuid))
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
              assertEquals(ConstantsMsg.PRODUCT_DELETED_SUCCESSFULLY_MSG, data);
            });
  }

  @Test
  void deleteNotFoundProduct() {
    webTestClient
        .delete()
        .uri(String.format("%s/%s", PRODUCT_BASE_PATH, "1"))
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
              assertEquals(ServerResponses.PRODUCT_NOT_FOUND.getMessage(), error.getDescription());
            });
  }

  @Test
  void updateProduct() {
    webTestClient
        .patch()
        .uri(String.format("%s/%s", PRODUCT_BASE_PATH, productUuid))
        .bodyValue(getProductUpdateDto())
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
              assertEquals(ConstantsMsg.PRODUCT_UPDATED_SUCCESSFULLY_MSG, data);
            });
  }

  @Test
  void updateProductBadRequest() {
    webTestClient
        .patch()
        .uri(String.format("%s/%s", PRODUCT_BASE_PATH, " "))
        .bodyValue(getBadProductUpdateDto())
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
  void updateNotFoundProduct() {
    webTestClient
        .patch()
        .uri(String.format("%s/%s", PRODUCT_BASE_PATH, "1"))
        .bodyValue(getProductUpdateDto())
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
              assertEquals(ServerResponses.PRODUCT_NOT_FOUND.getMessage(), error.getDescription());
            });
  }
}
