package com.pragma.challenge.franchises.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("java:S1075")
public class ConstantsRoute {
  public static final String FRANCHISE_BASE_PATH = "/api/v1/franchise";
  public static final String BRANCH_BASE_PATH = "/api/v1/branch";
  public static final String PRODUCT_BASE_PATH = "/api/v1/product";
  public static final String PRODUCT_UUID_PARAM = "productUuid";
  public static final String TOP_PRODUCT_BASE_PATH = "/top";
  public static final String FRANCHISE_UUID_PARAM = "franchiseUuid";
  public static final String BRANCH_UUID_PARAM = "branchUuid";
}
