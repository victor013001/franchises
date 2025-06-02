package com.pragma.challenge.franchises.domain.enums;

import com.pragma.challenge.franchises.domain.constants.ConstantsMsg;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServerResponses {
  BAD_REQUEST("E000", 400, ConstantsMsg.BAD_REQUEST_MSG),
  SERVER_ERROR("E001", 500, ConstantsMsg.SERVER_ERROR_MSG);

  private final String code;
  private final int httpStatus;
  private final String message;
}
