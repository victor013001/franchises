package com.pragma.challenge.franchises.infrastructure.entrypoints.handler;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchDto;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface BranchHandler {

  /**
   * Add new branch to a franchise
   *
   * @param request Server Request with {@link BranchDto} in body
   * @return Publisher that emits the {@link ServerResponse} with the saved branch
   */
  Mono<ServerResponse> addBranchToFranchise(ServerRequest request);
}
