package com.pragma.challenge.franchises.application;

import com.pragma.challenge.franchises.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestFranchisesApplication {
  public static void main(String[] args) {
    SpringApplication.from(FranchisesApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
