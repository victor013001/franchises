package com.pragma.challenge.franchises.domain.exceptions.standard_exception;

import com.pragma.challenge.franchises.domain.exceptions.StandardException;
import com.pragma.challenge.franchises.domain.enums.ServerResponses;

public class BadRequest extends StandardException {
  public BadRequest() {
    super(ServerResponses.BAD_REQUEST);
  }
}
