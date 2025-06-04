package com.pragma.challenge.franchises.infrastructure.adapters.persistence.projection;

public record TopProductProjection(String branchName, String productName, Integer stock) {}
