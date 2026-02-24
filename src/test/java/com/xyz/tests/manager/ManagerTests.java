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
}
