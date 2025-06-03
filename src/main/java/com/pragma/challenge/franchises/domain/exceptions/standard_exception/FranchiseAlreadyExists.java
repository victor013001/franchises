package com.pragma.challenge.franchises.domain.exceptions.standard_exception;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;

public class FranchiseAlreadyExists extends StandardException {
  public FranchiseAlreadyExists() {
    super(ServerResponses.FRANCHISE_ALREADY_EXISTS);
  }
}
