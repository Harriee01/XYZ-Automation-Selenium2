package com.xyz.tests.manager;

import com.xyz.base.BaseTest;
import com.xyz.models.TestData;
import com.xyz.pages.*;
import com.xyz.utils.TableAssertions;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


//ManagerTests — all 8 Bank Manager user story test cases


@Epic("XYZ Bank")
@Feature("Bank Manager Operations")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ManagerTests extends BaseTest {

    //TC1 — Verify Bank Manager can add a new customer with valid alphabetic First/Last Name.
    @Test
    @Order(1)
    @Story("Add Customer")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC1: Manager adds a new customer with valid alphabetic first and last name; expects success alert.")
    void tc1_addValidCustomer() {
        // Navigate to Add Customer form via fluent page navigation
        AddCustomerPage addCustomerPage = new HomeLoginPage(driver)
                .clickManagerLogin()
                .clickAddCustomer();

        // Submit form with valid data — returns the browser alert text
        String alertText = addCustomerPage.addCustomer(
                TestData.CUSTOMER_FIRST_NAME,
                TestData. CUSTOMER_LAST_NAME,
                TestData. CUSTOMER_POST_CODE
        );

        // Assert the alert mentions success — partial match handles dynamic customer ID
        assertThat(alertText)
                .as("Expected success alert when adding valid customer")
                .contains(TestData.MSG_CUSTOMER_ADDED);
    }

    //TC2 — Verify the form rejects a First Name containing numeric characters.
    @Test
    @Order(2)
    @Story("Add Customer - Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC2: Manager attempts to add customer with numeric first name; expects rejection (no success).")
    void tc2_rejectNumericFirstName() {
        AddCustomerPage addCustomerPage = new HomeLoginPage(driver)
                .clickManagerLogin()
                .clickAddCustomer();

        String alertText = addCustomerPage.addCustomer(
                TestData.CUSTOMER_INVALID_FIRST_NAME_NUMERIC,  // "Harriet123"
                TestData.CUSTOMER_LAST_NAME,
                TestData.CUSTOMER_POST_CODE
        );

        // The alert should NOT say customer was added successfully
        assertThat(alertText)
                .as("Expected rejection for numeric first name but got success message")
                .doesNotContain(TestData.MSG_CUSTOMER_ADDED);
    }

    //TC3 — Verify rejection when First Name contains special characters (@,#,$,%).
    @Test
    @Order(3)
    @Story("Add Customer - Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC3: Manager attempts to add customer with special chars in first name; expects rejection.")
    void tc3_rejectSpecialCharsFirstName() {
        AddCustomerPage addCustomerPage = new HomeLoginPage(driver)
                .clickManagerLogin()
                .clickAddCustomer();

        String alertText = addCustomerPage.addCustomer(
                TestData.CUSTOMER_INVALID_FIRST_NAME_SPECIAL,  // "Harriet@#$"
                TestData.CUSTOMER_LAST_NAME,
                TestData.CUSTOMER_POST_CODE
        );

        assertThat(alertText)
                .as("Expected rejection for special character first name")
                .doesNotContain(TestData.MSG_CUSTOMER_ADDED);
    }

}
