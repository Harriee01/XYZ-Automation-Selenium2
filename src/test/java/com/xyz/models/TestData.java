package com.xyz.models;

//Centralized test data repository for XYZ Bank test suite.

public final class TestData {

    // Customer Data
    public static final String CUSTOMER_FIRST_NAME = "John";
    public static final String CUSTOMER_LAST_NAME = "Doe";
    public static final String CUSTOMER_POST_CODE = "12345";
    public static final String CUSTOMER_INVALID_FIRST_NAME_NUMERIC = "John123";
    public static final String CUSTOMER_INVALID_FIRST_NAME_SPECIAL = "John@#$";
    public static final String CUSTOMER_INVALID_POST_CODE = "GH!@#$%";  // includes special chars
    public static final String EXISTING_CUSTOMER_NAME = "Harry Potter";  // pre-seeded customer in demo app

    // Account Data
    public static final String CURRENCY_DOLLAR = "Dollar";
    public static final String CURRENCY_POUND = "Pound";
    public static final String CURRENCY_RUPEE = "Rupee";
    public static final Double DEPOSIT_AMOUNT = 100.0;
    public static final String DEPOSIT_AMOUNT_STR = "100";  // String version for form input
    public static final Double WITHDRAW_AMOUNT = 50.0;
    public static final String WITHDRAW_AMOUNT_STR = "50";  // String version for form input
    public static final Double ZERO_AMOUNT = 0.0;
    public static final String ZERO_AMOUNT_STR = "0";  // String version for form input
    public static final Double NEGATIVE_AMOUNT = -50.0;
    public static final String NEGATIVE_AMOUNT_STR = "-50";  // String version for form input

    // Success Messages
    public static final String MSG_CUSTOMER_ADDED = "Customer added successfully";  // Partial match for alert
    public static final String MSG_ACCOUNT_CREATED = "Account created successfully";  // Partial match for alert
    public static final String MSG_DEPOSIT_SUCCESS = "Deposit Successful";  // Message shown after deposit
    public static final String MSG_WITHDRAW_SUCCESS = "Transaction successful";  // Message shown after withdrawal

    // URLs
    public static final String BASE_URL = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";

    // Timeouts (in seconds)
    public static final int DEFAULT_TIMEOUT_SECONDS = 10;
    public static final int EXTENDED_TIMEOUT_SECONDS = 15;
}
