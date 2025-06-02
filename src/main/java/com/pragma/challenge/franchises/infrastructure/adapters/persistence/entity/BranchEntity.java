package com.pragma.challenge.franchises.infrastructure.adapters.persistence.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branch")
public class BranchEntity {
  @Id private Long id;
  private String uuid;
  private String name;
  @Transient private List<ProductEntity> products;
  private Long franchiseId;
}
