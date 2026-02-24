package com.xyz.models;

//Centralized test data repository for XYZ Bank test suite.

public final class TestData {

    // Customer Data
    public static final String CUSTOMER_FIRST_NAME = "John";
    public static final String CUSTOMER_LAST_NAME = "Doe";
    public static final String CUSTOMER_POST_CODE = "12345";
    public static final String CUSTOMER_INVALID_FIRST_NAME_NUMERIC = "John123";
    public static final String CUSTOMER_INVALID_FIRST_NAME_SPECIAL = "John@#$";
    public static final String CUSTOMER_INVALID_POST_CODE = "ABCDE";

    // Account Data
    public static final String CURRENCY_DOLLAR = "Dollar";
    public static final Double DEPOSIT_AMOUNT = 100.0;
    public static final Double WITHDRAWAL_AMOUNT = 50.0;
    public static final Double INVALID_ZERO_AMOUNT = 0.0;
    public static final Double INVALID_NEGATIVE_AMOUNT = -50.0;

    // URLs
    public static final String BASE_URL = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";

    // Timeouts
    public static final int DEFAULT_TIMEOUT_SECONDS = 10;
    public static final int EXTENDED_TIMEOUT_SECONDS = 15;
}
