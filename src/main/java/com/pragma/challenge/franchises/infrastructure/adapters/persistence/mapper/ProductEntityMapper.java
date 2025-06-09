package com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper;

import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.ProductEntity;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

@Mapper(
    builder = @Builder(disableBuilder = true),
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductEntityMapper {
  @Mapping(target = "id", ignore = true)
  ProductEntity mapInternal(Product product, Long branchId);

  @Mapping(target = "branchUuid", ignore = true)
  Product toModel(ProductEntity productEntity);

  default ProductEntity toEntity(Product product, Long branchId) {
    if (product == null || branchId == null) {
      return null;
    }
    return mapInternal(product, branchId);
  }

  @AfterMapping
  default void setUuid(@MappingTarget ProductEntity productEntity) {
    if (Objects.isNull(productEntity.getUuid())) {
      productEntity.setUuid(UUID.randomUUID().toString());
    }
  }
}
