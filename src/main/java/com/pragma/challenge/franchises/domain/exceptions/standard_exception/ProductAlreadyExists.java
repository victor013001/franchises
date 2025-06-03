package com.pragma.challenge.franchises.domain.exceptions.standard_exception;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;

public class ProductAlreadyExists extends StandardException {
  public ProductAlreadyExists() {
    super(ServerResponses.PRODUCT_ALREADY_EXISTS);
  }
}
