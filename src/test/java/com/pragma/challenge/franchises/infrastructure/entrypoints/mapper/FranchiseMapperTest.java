package com.pragma.challenge.franchises.infrastructure.entrypoints.mapper;

import static com.pragma.challenge.franchises.util.FranchiseDtoDataUtil.getFranchiseDto;
import static org.junit.jupiter.api.Assertions.*;

import com.pragma.challenge.franchises.domain.model.Franchise;
import com.pragma.challenge.franchises.infrastructure.entrypoints.dto.FranchiseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FranchiseMapperTest {
  private FranchiseMapper franchiseMapper;

  @BeforeEach
  void setUp() {
    franchiseMapper = new FranchiseMapperImpl();
  }

  @Test
  void testToModel() {
    var franchiseDto = getFranchiseDto();
    var franchise = franchiseMapper.toModel(franchiseDto);
    assertModel(franchiseDto, franchise);
  }

  private void assertModel(FranchiseDto expected, Franchise actual) {
    assertNotNull(actual);
    assertNull(actual.uuid());
    assertEquals(expected.name(), actual.name());
    assertNull(actual.branches());
  }
}
