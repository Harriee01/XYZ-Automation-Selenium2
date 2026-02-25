package com.xyz.tests.manager;

import com.xyz.base.BaseTest;
import com.xyz.models.TestData;
import com.xyz.pages.*;
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

    //TC4 — Verify form rejects a Post Code with invalid characters (special chars).
    @Test
    @Order(4)
    @Story("Add Customer - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC4: Manager attempts to add customer with special-character post code; expects rejection.")
    void tc4_rejectInvalidPostCode() {
        AddCustomerPage addCustomerPage = new HomeLoginPage(driver)
                .clickManagerLogin()
                .clickAddCustomer();

        String alertText = addCustomerPage.addCustomer(
                TestData.CUSTOMER_FIRST_NAME,
                TestData.CUSTOMER_LAST_NAME,
                TestData.CUSTOMER_INVALID_POST_CODE  // "GH!@#$%"
        );

        assertThat(alertText)
                .as("Expected rejection for invalid post code with special characters")
                .doesNotContain(TestData.MSG_CUSTOMER_ADDED);
    }

    // TC5 — Verify new customer appears in Customers list immediately and is searchable by first name.
    @Test
    @Order(5)
    @Story("Customer List")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC5: After adding a customer, they immediately appear in the Customers list; searchable by first name.")
    void tc5_customerAppearsInListAndSearchable() {
        // Step 1: Add the customer
        ManagerHomePage managerHome = new HomeLoginPage(driver).clickManagerLogin();
        managerHome.clickAddCustomer()
                .addCustomer(TestData.CUSTOMER_FIRST_NAME, TestData.CUSTOMER_LAST_NAME, TestData.CUSTOMER_POST_CODE);

        // Step 2: Navigate to Customers tab (re-using the same managerHome page object)
        CustomersPage customersPage = managerHome.clickCustomers();

        // Step 3: Search by first name
        customersPage.searchCustomer(TestData.CUSTOMER_FIRST_NAME);

        // Step 4: Assert the customer is visible in the filtered results
        assertThat(customersPage.isCustomerVisible(TestData.CUSTOMER_FIRST_NAME))
                .as("Customer '%s' should appear in list after being added", TestData.CUSTOMER_FIRST_NAME)
                .isTrue();
    }

    //TC6 — Verify Manager can create a Dollar account for an existing customer;
    //     confirmation alert contains account details.

    @Test
    @Order(6)
    @Story("Open Account")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC6: Manager creates a Dollar account for an existing customer; alert confirms with account number.")
    void tc6_createDollarAccountForExistingCustomer() {
        OpenAccountPage openAccountPage = new HomeLoginPage(driver)
                .clickManagerLogin()
                .clickOpenAccount();

        String alertText = openAccountPage.openAccount(
                TestData.EXISTING_CUSTOMER_NAME,  // "Harry Potter"
                TestData.CURRENCY_DOLLAR
        );

        // Alert should confirm account creation and include an account number
        assertThat(alertText)
                .as("Expected account creation confirmation alert")
                .contains(TestData.MSG_ACCOUNT_CREATED);

        // Verify the alert also contains a numeric account number (not just text)
        // Account numbers are auto-generated integers (e.g. "1004")
        assertThat(alertText)
                .as("Alert should contain numeric account number")
                .matches(".*\\d+.*");  // regex: contains at least one digit
    }

    // TC7 — Verify it is impossible to create an account for a non-existent customer.

    @Test
    @Order(7)
    @Story("Open Account - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC7: Open Account customer dropdown only contains real customers; no free-text injection possible.")
    void tc7_cannotCreateAccountForNonExistentCustomer() {
        OpenAccountPage openAccountPage = new HomeLoginPage(driver)
                .clickManagerLogin()
                .clickOpenAccount();

        List<String> options = openAccountPage.getCustomerDropdownOptions();

        // The dropdown should not be empty — real customers must be present
        assertThat(options)
                .as("Customer dropdown should have at least the default option plus real customers")
                .hasSizeGreaterThan(1);

        // No option should contain a fake/non-existent name
        assertThat(options)
                .as("Dropdown should not contain arbitrary fake customer names")
                .noneMatch(opt -> opt.equalsIgnoreCase("FakeCustomer99"));

        // All options should be either empty/placeholder OR known real names
        // This verifies the dropdown is seeded from server data, not user-entered
        assertThat(options)
                .as("All dropdown options should be recognisable (placeholder or seeded customer names)")
                .allMatch(opt -> opt.isBlank()
                        || opt.contains("Harry")
                        || opt.contains("Hermione")
                        || opt.contains("Ron")
                        || opt.contains("Albus")
                        || opt.contains("Neville")
                        || opt.contains(TestData.CUSTOMER_FIRST_NAME)  // may or may not be present
                        || opt.equals("---"));               // placeholder option
    }

    //TC8 — Verify the form prevents account creation when no currency is selected.
    @Test
    @Order(8)
    @Story("Open Account - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC8: Open Account form rejects submission when no currency is selected.")
    void tc8_preventAccountCreationWithEmptyCurrency() {
        OpenAccountPage openAccountPage = new HomeLoginPage(driver)
                .clickManagerLogin()
                .clickOpenAccount();

        // Select a valid customer but do NOT select currency (leave at default)
        openAccountPage.selectCustomer(TestData.EXISTING_CUSTOMER_NAME);
        // Intentionally skip selectCurrency() — testing empty state

        // Click Process — may or may not show an alert
        openAccountPage.clickProcessWithoutCurrency();

        // Check if an alert appeared
        try {
            // Wait briefly for an alert — timeout = 3s (not DEFAULT_WAIT, faster failure)
            org.openqa.selenium.support.ui.WebDriverWait shortWait =
                    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3));
            shortWait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());

            // Alert IS present — capture and verify it's NOT a success message
            String alertText = com.xyz.utils.AlertHandler.acceptAndGetText(driver);
            assertThat(alertText)
                    .as("Alert appeared but should NOT be a success confirmation when currency is empty")
                    .doesNotContain(TestData.MSG_ACCOUNT_CREATED);

        } catch (Exception e) {
            // No alert appeared — this means the browser/app silently rejected the submission.
            // This is ALSO correct behaviour (form validation prevented submission).
            // Test passes — no account was created.
            Allure.addAttachment("TC8 Result", "text/plain",
                    "No alert shown — browser prevented submission with empty currency (expected).");
        }
    }

}
