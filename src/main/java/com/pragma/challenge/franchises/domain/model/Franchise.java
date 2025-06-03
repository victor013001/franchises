package com.pragma.challenge.franchises.domain.model;

import com.pragma.challenge.franchises.domain.validation.annotation.NotBlank;
import java.util.List;

public record Franchise(String uuid, @NotBlank String name, List<Branch> branches) {}
