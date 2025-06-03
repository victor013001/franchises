package com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper;

import static com.pragma.challenge.franchises.util.FranchiseDataUtil.getFranchise;
import static com.pragma.challenge.franchises.util.FranchiseEntityDataUtil.getFranchiseEntity;
import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.FranchiseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FranchiseEntityMapperTest {
  private FranchiseEntityMapper franchiseMapper;

  @BeforeEach
  void setUp() {
    franchiseMapper = new FranchiseEntityMapperImpl();
  }

  @Test
  void testToModel() {
    var franchiseEntity = getFranchiseEntity();
    var franchise = franchiseMapper.toModel(franchiseEntity);
    assertModel(franchiseEntity, franchise);
  }

  @Test
  void testToEntity() {
    var franchise = getFranchise();
    var franchiseEntity = franchiseMapper.toEntity(franchise);
    assertEntity(franchise, franchiseEntity);
  }

  @Test
  void testToModelWithNullInput() {
    assertNull(franchiseMapper.toModel(null));
  }

  @Test
  void testToEntityWithNullInput() {
    assertNull(franchiseMapper.toEntity(null));
  }

  private void assertEntity(Franchise expected, FranchiseEntity actual) {
    assertNotNull(actual);
    assertNotNull(actual.getUuid());
    assertEquals(expected.name(), actual.getName());
  }

  private void assertModel(FranchiseEntity expected, Franchise actual) {
    assertNotNull(actual);
    assertEquals(expected.getUuid(), actual.uuid());
    assertEquals(expected.getName(), actual.name());
  }
}
