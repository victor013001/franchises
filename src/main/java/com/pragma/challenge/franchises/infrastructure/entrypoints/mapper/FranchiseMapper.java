package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.FranchiseDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.FranchiseUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FranchiseMapper {
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "branches", ignore = true)
  Franchise toModel(FranchiseDto franchiseDto);

  @Mapping(target = "branches", ignore = true)
  @Mapping(target = "uuid", source = "franchiseUuid")
  Franchise toModel(FranchiseUpdateDto franchiseUpdateDto, String franchiseUuid);
}
