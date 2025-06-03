package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "products", ignore = true)
  Branch toModel(BranchDto branchDto);
}
