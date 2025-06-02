package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.DefaultServerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServerResponseMapper {
  DefaultServerResponse<Object, Object> toResponse(Object data, Object error);
}
