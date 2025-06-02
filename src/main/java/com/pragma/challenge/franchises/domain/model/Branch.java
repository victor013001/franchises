package com.pragma.challenge.franchises.domain.model;

import java.util.List;

public record Branch(String uuid, String name, List<Product> products) {}
