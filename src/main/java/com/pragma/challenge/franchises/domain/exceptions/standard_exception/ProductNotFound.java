package com.pragma.challenge.franchises.domain.exceptions.standard_exception;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;

public class ProductNotFound extends StandardException {
  public ProductNotFound() {
    super(ServerResponses.PRODUCT_NOT_FOUND);
  }
}
