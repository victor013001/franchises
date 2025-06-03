package com.pragma.challenge.franchises.infrastructure;

import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.FRANCHISE_BASE_PATH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.pragma.challenge.franchises.domain.constants.ConstantsMsg;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.FranchiseDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.handler.FranchiseHandler;
import com.pragma.challenge.franchises.infrastructure.entrypoints.util.SwaggerResponses;
import io.swagger.v3.oas.annotations.Operation;
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
public class FranchiseRouterRest {
  @Bean
  @RouterOperations({
    @RouterOperation(
        path = FRANCHISE_BASE_PATH,
        method = RequestMethod.POST,
        beanClass = FranchiseHandler.class,
        beanMethod = "createFranchise",
        operation =
            @Operation(
                operationId = "createFranchise",
                summary = "Create new Franchise",
                requestBody =
                    @RequestBody(
                        required = true,
                        content =
                            @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FranchiseDto.class))),
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = ConstantsMsg.FRANCHISE_CREATED_SUCCESSFULLY_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultFranchiseResponse.class))),
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
                      responseCode = "409",
                      description = ConstantsMsg.FRANCHISE_ALREADY_EXISTS_MSG,
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
  })
  public RouterFunction<ServerResponse> routerFunction(FranchiseHandler franchiseHandler) {
    return nest(path(FRANCHISE_BASE_PATH), route(POST(""), franchiseHandler::createFranchise));
  }
}
