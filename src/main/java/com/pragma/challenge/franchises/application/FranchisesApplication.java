package com.pragma.challenge.franchises.application;

import com.pragma.challenge.franchises.application.config.FranchisesApplicationConfig;
import com.pragma.challenge.franchises.application.config.UseCasesConfig;
import com.pragma.challenge.franchises.application.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({FranchisesApplicationConfig.class, UseCasesConfig.class, WebConfig.class})
public class FranchisesApplication {

  public static void main(String[] args) {
    SpringApplication.run(FranchisesApplication.class, args);
  }
}
