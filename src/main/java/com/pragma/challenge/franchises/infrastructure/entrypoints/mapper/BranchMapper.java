package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "products", ignore = true)
  Branch toModel(BranchDto branchDto);

  @Mapping(target = "products", ignore = true)
  @Mapping(target = "uuid", source = "branchUuid")
  @Mapping(target = "franchiseUuid", constant = "IGNORE")
  Branch toModel(BranchUpdateDto branchUpdateDto, String branchUuid);
}
