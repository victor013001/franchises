package com.pragma.challenge.franchises.domain.exceptions.standard_exception;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;

public class FranchiseNotFound extends StandardException {
  public FranchiseNotFound() {
    super(ServerResponses.FRANCHISE_NOT_FOUND);
  }
}
