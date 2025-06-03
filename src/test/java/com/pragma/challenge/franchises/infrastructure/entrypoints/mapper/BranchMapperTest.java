package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import static com.pragma.challenge.franchises.util.BranchDtoDataUtil.getBranchDto;
import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.model.Branch;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.BranchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchMapperTest {
  private BranchMapper branchMapper;

  @BeforeEach
  void setUp() {
    branchMapper = new BranchMapperImpl();
  }

  @Test
  void testToModel() {
    var branchDto = getBranchDto();
    var branch = branchMapper.toModel(branchDto);
    assertModel(branchDto, branch);
  }

  private void assertModel(BranchDto expected, Branch actual) {
    assertNotNull(actual);
    assertNull(actual.uuid());
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.franchiseUuid(), actual.franchiseUuid());
    assertNull(actual.products());
  }
}
