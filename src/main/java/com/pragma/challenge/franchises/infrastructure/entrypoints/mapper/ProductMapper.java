package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import com.pragma.challenge.franchises.domain.model.Product;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductDto;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.ProductUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
  @Mapping(target = "uuid", ignore = true)
  Product toModel(ProductDto productDto);

  @Mapping(target = "uuid", source = "productUuid")
  @Mapping(target = "branchUuid", constant = "IGNORE")
  Product toModel(ProductUpdateDto productUpdateDto, String productUuid);
}
