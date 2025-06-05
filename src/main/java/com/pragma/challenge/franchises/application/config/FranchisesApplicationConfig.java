package com.pragma.challenge.franchises.application.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@ComponentScan(basePackages = {"com.pragma.challenge.franchises.infrastructure"})
@EnableR2dbcRepositories(
    basePackages = "com.pragma.challenge.franchises.infrastructure.adapters.persistence.repository")
public class FranchisesApplicationConfig {}
