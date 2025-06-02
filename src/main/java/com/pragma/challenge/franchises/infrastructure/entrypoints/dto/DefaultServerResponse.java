package com.pragma.challenge.franchises.infrastructure.entrypoints.dto;

public record DefaultServerResponse<T, E>(T data, E error) {}
