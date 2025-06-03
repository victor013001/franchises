package com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper;

import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.BranchEntity;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

@Mapper(
    builder = @Builder(disableBuilder = true),
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchEntityMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "products", ignore = true)
  BranchEntity mapInternal(Branch branch, Long franchiseId);

  default BranchEntity toEntity(Branch branch, Long franchiseId) {
    if (branch == null || franchiseId == null) {
      return null;
    }
    return mapInternal(branch, franchiseId);
  }

  @Mapping(target = "products", ignore = true)
  @Mapping(target = "franchiseUuid", ignore = true)
  Branch toModel(BranchEntity branchEntity);

  @AfterMapping
  default void setUuid(@MappingTarget BranchEntity branchEntity) {
    if (Objects.isNull(branchEntity.getUuid())) {
      branchEntity.setUuid(UUID.randomUUID().toString());
    }
  }
}
