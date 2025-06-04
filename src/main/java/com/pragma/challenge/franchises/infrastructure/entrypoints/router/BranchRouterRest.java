package com.pragma.challenge.franchises.infrastructure.entrypoints.router;

import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.BRANCH_BASE_PATH;
import static com.pragma.challenge.franchises.domain.constants.ConstantsRoute.BRANCH_UUID_PARAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.pragma.challenge.franchises.domain.constants.ConstantsMsg;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchUpdateDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.handler.BranchHandler;
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
public class BranchRouterRest {
  @Bean
  @RouterOperations({
    @RouterOperation(
        path = BRANCH_BASE_PATH,
        method = RequestMethod.POST,
        beanClass = BranchHandler.class,
        beanMethod = "addBranchToFranchise",
        operation =
            @Operation(
                operationId = "addBranchToFranchise",
                summary = "Add branch to Franchise",
                requestBody =
                    @RequestBody(
                        required = true,
                        content =
                            @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = BranchDto.class))),
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = ConstantsMsg.BRANCH_ADDED_TO_FRANCHISE_SUCCESSFULLY_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultBranchResponse.class))),
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
                      responseCode = "409",
                      description = ConstantsMsg.BRANCH_ALREADY_EXISTS_MSG,
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
        path = BRANCH_BASE_PATH,
        method = RequestMethod.PATCH,
        beanClass = BranchHandler.class,
        beanMethod = "updateName",
        operation =
            @Operation(
                operationId = "updateName",
                summary = "Update branch name",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = BRANCH_UUID_PARAM,
                      description = "Branch uuid to update name")
                },
                requestBody =
                    @RequestBody(
                        required = true,
                        content =
                            @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = BranchUpdateDto.class))),
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = ConstantsMsg.BRANCH_UPDATED_SUCCESSFULLY_MSG,
                      content =
                          @Content(
                              mediaType = MediaType.APPLICATION_JSON_VALUE,
                              schema =
                                  @Schema(
                                      implementation =
                                          SwaggerResponses.DefaultBranchResponse.class))),
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
                      description = ConstantsMsg.BRANCH_ALREADY_EXISTS_MSG,
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
  public RouterFunction<ServerResponse> branchRouterFunction(BranchHandler branchHandler) {
    return nest(
        path(BRANCH_BASE_PATH),
        route(POST(""), branchHandler::addBranchToFranchise)
            .andRoute(PATCH(String.format("/{%s}", BRANCH_UUID_PARAM)), branchHandler::updateName));
  }
}
