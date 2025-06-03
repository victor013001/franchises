package com.pragma.challenge.franchises.domain.exceptions.standard_exception;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;

public class BranchNotFound extends StandardException {
  public BranchNotFound() {
    super(ServerResponses.BRANCH_NOT_FOUND);
  }
}
