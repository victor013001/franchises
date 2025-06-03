package com.pragma.challenge.franchises.domain.exceptions.standard_exception;

import com.pragma.challenge.franchises.domain.enums.ServerResponses;
import com.pragma.challenge.franchises.domain.exceptions.StandardException;

public class BranchAlreadyExists extends StandardException {
  public BranchAlreadyExists() {
    super(ServerResponses.BRANCH_ALREADY_EXISTS);
  }
}
