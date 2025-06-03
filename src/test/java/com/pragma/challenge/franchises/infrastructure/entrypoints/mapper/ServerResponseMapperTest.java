package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.exceptions.StandardError;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerResponseMapperTest {
  private ServerResponseMapper responseMapper;

  @BeforeEach
  void setUp() {
    responseMapper = new ServerResponseMapperImpl();
  }

  @Test
  void testToResponse() {
    String data = "Data message";
    String error = null;

    var response = responseMapper.toResponse(data, error);

    assertNotNull(response);
    assertEquals(data, response.data());
    assertEquals(error, response.error());
  }

  @Test
  void testToResponseWithError() {
    Object data = null;
    StandardError error =
        StandardError.builder()
            .code("E000")
            .description("Something went wrong")
            .timestamp(LocalDateTime.now())
            .build();

    var response = responseMapper.toResponse(data, error);

    assertNotNull(response);
    assertEquals(data, response.data());
    assertEquals(error, response.error());
  }
}
