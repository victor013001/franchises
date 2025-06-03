package com.pragma.challenge.franchises.infrastructure.entrypoints.router;

import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.FRANCHISE_UUID_PARAM;
import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.PRODUCT_BASE_PATH;
import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.PRODUCT_UUID_PARAM;
import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.TOP_PRODUCT_BASE_PATH;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.pragma.challenge.franchises.domain.constants.ConstantsMsg;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductUpdateDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.handler.ProductHandler;
import com.pragma.challenge.franchises.infrastructure.entrypoints.util.SwaggerResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProductRouterRest {
  @Bean
  @RouterOperations({
    @RouterOperation(
        path = PRODUCT_BASE_PATH,
        method = RequestMethod.POST,
        beanClass = ProductHandler.class,
        beanMethod = "createProduct",
        operation =
            @Operation(
                operationId = "createProduct",
                summary = "Create new Product",
                requestBody =
                    @RequestBody(
                        required = true,
                        content =
                            @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ProductDto.class))),
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = ConstantsMsg.PRODUCT_CREATED_SUCCESSFULLY_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultProductResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = ConstantsMsg.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "404",
                      description = ConstantsMsg.BRANCH_NOT_FOUND_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "409",
                      description = ConstantsMsg.PRODUCT_ALREADY_EXISTS_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = ConstantsMsg.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class)))
                })),
    @RouterOperation(
        path = PRODUCT_BASE_PATH,
        method = RequestMethod.DELETE,
        beanClass = ProductHandler.class,
        beanMethod = "deleteProduct",
        operation =
            @Operation(
                operationId = "deleteProduct",
                summary = "Delete Product by uuid",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = PRODUCT_UUID_PARAM,
                      description = "Product uuid to delete")
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = ConstantsMsg.PRODUCT_DELETED_SUCCESSFULLY_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultMessageResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = ConstantsMsg.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "404",
                      description = ConstantsMsg.PRODUCT_NOT_FOUND_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = ConstantsMsg.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class)))
                })),
    @RouterOperation(
        path = PRODUCT_BASE_PATH,
        method = RequestMethod.PATCH,
        beanClass = ProductHandler.class,
        beanMethod = "updateProduct",
        operation =
            @Operation(
                operationId = "updateProduct",
                summary = "Update Product by uuid",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = PRODUCT_UUID_PARAM,
                      description = "Product uuid to delete")
                },
                requestBody =
                    @RequestBody(
                        required = true,
                        content =
                            @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ProductUpdateDto.class))),
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = ConstantsMsg.PRODUCT_UPDATED_SUCCESSFULLY_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultProductResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = ConstantsMsg.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "404",
                      description = ConstantsMsg.PRODUCT_NOT_FOUND_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = ConstantsMsg.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class)))
                })),
    @RouterOperation(
        path = PRODUCT_BASE_PATH + TOP_PRODUCT_BASE_PATH,
        method = RequestMethod.GET,
        beanClass = ProductHandler.class,
        beanMethod = "getFranchiseTopBranchesProduct",
        operation =
            @Operation(
                operationId = "getFranchiseTopBranchesProduct",
                summary = "Get Branches top product for a franchise",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = FRANCHISE_UUID_PARAM,
                      description = "Franchise uuid to find branches top product")
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = ConstantsMsg.PRODUCT_UPDATED_SUCCESSFULLY_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultTopProductResponse.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = ConstantsMsg.BAD_REQUEST_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "404",
                      description = ConstantsMsg.FRANCHISE_NOT_FOUND_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class))),
                  @ApiResponse(
                      responseCode = "500",
                      description = ConstantsMsg.SERVER_ERROR_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultErrorResponse.class)))
                }))
  })
  public RouterFunction<ServerResponse> productRouterFunction(ProductHandler productHandler) {
    return nest(
        path(PRODUCT_BASE_PATH),
        route(POST(""), productHandler::createProduct)
            .andRoute(
                DELETE(String.format("/{%s}", PRODUCT_UUID_PARAM)), productHandler::deleteProduct)
            .andRoute(
                PATCH(String.format("/{%s}", PRODUCT_UUID_PARAM)), productHandler::updateProduct)
            .andRoute(
                GET(String.format("%s/{%s}", TOP_PRODUCT_BASE_PATH, FRANCHISE_UUID_PARAM)),
                productHandler::getFranchiseTopBranchesProduct));
  }
}
