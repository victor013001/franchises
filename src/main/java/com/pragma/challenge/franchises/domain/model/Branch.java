package com.pragma.challenge.franchises.domain.model;

import com.pragma.challenge.franchises.domain.validation.annotation.NotBlank;
import java.util.List;

public record Branch(
    String uuid, @NotBlank String name, List<Product> products, @NotBlank String franchiseUuid) {}
