package com.pragma.challenge.franchises.domain.exceptions.standard_exception;

import com.pragma.challenge.franchises.domain.exceptions.StandardException;
import com.pragma.challenge.franchises.domain.enums.ServerResponses;

public class ServerError extends StandardException {
  public ServerError() {
    super(ServerResponses.SERVER_ERROR);
  }
}
