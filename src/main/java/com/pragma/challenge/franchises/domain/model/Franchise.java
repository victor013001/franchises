package com.pragma.challenge.franchises.domain.model;

import java.util.List;

public record Franchise(String uuid, String name, List<Branch> branches) {}
