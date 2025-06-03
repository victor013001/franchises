package com.pragma.challenge.franchises.util;

import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.ProductEntity;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ProductEntityDataUtil {
  private ProductEntityDataUtil() throws InstantiationException {
    throw new InstantiationException("Data class cannot be instantiated");
  }

  public static ProductEntity getProductEntity() {
    var randomId = ThreadLocalRandom.current().nextLong();
    return ProductEntity.builder()
        .uuid(UUID.randomUUID().toString())
        .name("Product" + randomId)
        .stock(100)
        .build();
  }

  public static ProductEntity getProductEntityFixedName() {
    return ProductEntity.builder()
        .uuid(UUID.randomUUID().toString())
        .name("Product Zero")
        .stock(100)
        .build();
  }

  public static ProductEntity getProductEntityFixedName(Long branchId) {
    return ProductEntity.builder()
        .uuid(UUID.randomUUID().toString())
        .name("Product Zero")
        .stock(100)
        .branchId(branchId)
        .build();
  }
}
