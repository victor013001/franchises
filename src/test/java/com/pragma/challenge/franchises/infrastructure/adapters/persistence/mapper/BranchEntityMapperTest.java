package com.pragma.challenge.franchises.infrastructure.adapters.persistence.mapper;

import static com.pragma.challenge.franchises.util.BranchDataUtil.getBranch;
import static com.pragma.challenge.franchises.util.BranchEntityDataUtil.getBranchEntity;
import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity.BranchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchEntityMapperTest {
  private BranchEntityMapper branchMapper;

  @BeforeEach
  void setUp() {
    branchMapper = new BranchEntityMapperImpl();
  }

  @Test
  void testToModel() {
    var branchEntity = getBranchEntity();
    var branch = branchMapper.toModel(branchEntity);
    assertModel(branchEntity, branch);
  }

  @Test
  void testToEntity() {
    var branch = getBranch();
    Long franchiseId = 1L;
    var branchEntity = branchMapper.toEntity(branch, franchiseId);
    assertEntity(branch, franchiseId, branchEntity);
  }

  @Test
  void testToModelWithNullInput() {
    assertNull(branchMapper.toModel(null));
  }

  @Test
  void testToEntityWithNullInput() {
    assertNull(branchMapper.toEntity(null, null));
  }

  @Test
  void testToEntityWithNullBranch() {
    Long franchiseId = 1L;
    assertNull(branchMapper.toEntity(null, franchiseId));
  }

  private void assertEntity(Branch expected, Long franchiseId, BranchEntity actual) {
    assertNotNull(actual);
    assertNotNull(actual.getUuid());
    assertEquals(expected.name(), actual.getName());
    assertEquals(franchiseId, actual.getFranchiseId());
  }

  private void assertModel(BranchEntity expected, Branch actual) {
    assertNotNull(actual);
    assertEquals(expected.getUuid(), actual.uuid());
    assertEquals(expected.getName(), actual.name());
  }
}
