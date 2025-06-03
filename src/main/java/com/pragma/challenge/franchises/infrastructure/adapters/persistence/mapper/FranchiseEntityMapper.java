package com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper;

import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.FranchiseEntity;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

@Mapper(
    builder = @Builder(disableBuilder = true),
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface FranchiseEntityMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "branches", ignore = true)
  FranchiseEntity toEntity(Franchise franchise);

  @Mapping(target = "branches", ignore = true)
  Franchise toModel(FranchiseEntity franchiseEntity);

  @AfterMapping
  default void setUuid(@MappingTarget FranchiseEntity franchiseEntity) {
    if (Objects.isNull(franchiseEntity.getUuid())) {
      franchiseEntity.setUuid(UUID.randomUUID().toString());
    }
  }
}
