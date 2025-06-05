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
  public static final String BRANCH_ADDED_TO_FRANCHISE_SUCCESSFULLY_MSG =
      "The branch was created successfully and added to the franchise.";
  public static final String BRANCH_ALREADY_EXISTS_MSG =
      "The branch could not be created due to a data conflict.";
  public static final String FRANCHISE_NOT_FOUND_MSG = "The franchise provided was not found.";
  public static final String PRODUCT_CREATED_SUCCESSFULLY_MSG =
      "The product was created successfully and added to the branch.";
  public static final String PRODUCT_ALREADY_EXISTS_MSG =
      "The product could not be created due to a data conflict.";
  public static final String BRANCH_NOT_FOUND_MSG = "The branch provided was not found.";
  public static final String PRODUCT_DELETED_SUCCESSFULLY_MSG =
      "The product was deleted successfully.";
  public static final String PRODUCT_NOT_FOUND_MSG = "The product provided was not found.";
  public static final String PRODUCT_UPDATED_SUCCESSFULLY_MSG =
      "The product was updated successfully.";
  public static final String TOP_PRODUCT_FOUND_SUCCESSFULLY_MSG =
      "The branches top product by franchise were found successfully.";
  public static final String BRANCH_UPDATED_SUCCESSFULLY_MSG =
      "The branch was updated successfully.";
  public static final String FRANCHISE_UPDATED_SUCCESSFULLY_MSG =
      "The franchise was updated successfully.";
}
