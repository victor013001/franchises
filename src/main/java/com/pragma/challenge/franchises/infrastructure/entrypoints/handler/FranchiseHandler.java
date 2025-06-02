package com.pragma.challenge.franchises.infrastructure.entrypoints.handler;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.FranchiseDto;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface FranchiseHandler {

  /**
   * Creates a new franchise handler
   *
   * @param request Server Request with {@link FranchiseDto} in body
   * @return Publisher that emits the {@link ServerResponse} with the saved Franchise
   */
  Mono<ServerResponse> createFranchise(ServerRequest request);
}
