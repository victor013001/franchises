package com.pragma.challenge.franchises.domain.model;

import com.pragma.challenge.franchises.domain.validation.annotation.Min;
import com.pragma.challenge.franchises.domain.validation.annotation.NotBlank;

public record ProductNoBranch(String uuid, @NotBlank String name, @Min(1) Integer stock) {}
