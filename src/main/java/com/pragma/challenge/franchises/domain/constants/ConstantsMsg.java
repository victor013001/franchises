package com.pragma.challenge.franchises.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantsMsg {
  public static final String BAD_REQUEST_MSG =
      "The request could not be processed due to invalid or incomplete data.";
  public static final String SERVER_ERROR_MSG =
      "An unexpected server error occurred. Please try again later.";
  public static final String FRANCHISE_CREATED_SUCCESSFULLY_MSG = "Franchise created successfully.";
  public static final String FRANCHISE_ALREADY_EXISTS_MSG =
      "The franchise could not be created due to a data conflict.";
}
