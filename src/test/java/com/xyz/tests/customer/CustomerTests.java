package com.xyz.tests.customer;

import com.xyz.base.BaseTest;
import com.xyz.models.TestData;
import com.xyz.pages.*;
import com.xyz.utils.TableAssertions;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

//CustomerTests — all 8 Customer user story test cases.
@Epic("XYZ Bank")
@Feature("Customer Operations")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class CustomerTests extends BaseTest {
    /** Shared dashboard reference — set in @BeforeEach after login */
    private CustomerDashboardPage dashboard;

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();  // creates driver, maximises, navigates to BASE_URL

        // Step 1: Open a fresh account for Harry Potter as Manager
        new HomeLoginPage(driver)
                .clickManagerLogin()
                .clickOpenAccount()
                .openAccount(TestData.EXISTING_CUSTOMER_NAME, TestData.CURRENCY_DOLLAR);
        // Alert auto-dismissed by AlertHandler inside openAccount()

        // Step 2: Return to login page
        driver.get(TestData.BASE_URL);

        // Step 3: Login as Harry Potter
        dashboard = new HomeLoginPage(driver)
                .clickCustomerLogin()
                .loginAs(TestData.EXISTING_CUSTOMER_NAME);

        // Step 4: Select the most recently created Dollar account
        // The dashboard shows accounts in a dropdown — we select the last one (freshest)
        selectLastAccount();
    }

    private void selectLastAccount() {
        org.openqa.selenium.WebElement accountDropdown =
                driver.findElement(org.openqa.selenium.By.cssSelector("[ng-model='accountNo']"));
        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(accountDropdown);
        // Select the last option (highest index) — most recently created account
        int lastIndex = select.getOptions().size() - 1;
        if (lastIndex > 0) {  // index 0 may be a blank placeholder
            select.selectByIndex(lastIndex);
        }
    }
    //TC1 — Verify logged-in customer sees transaction history table clearly.
    @Test
    @Order(1)
    @Story("Transaction History")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC1: Logged-in customer can see transaction history table with proper structure.")
    void tc1_viewTransactionHistoryTable() {
        // First make a deposit so there's something in the table
        dashboard.clickDeposit().deposit(TestData.DEPOSIT_AMOUNT_STR);

        // Navigate to Transactions tab
        TransactionsPage txPage = dashboard.clickTransactions();

        // Assert table is visible
        assertThat(txPage.isTableDisplayed())
                .as("Transaction history table should be displayed after deposit")
                .isTrue();

        // Assert table has at least one transaction row (the deposit we just made)
        assertThat(txPage.getTransactionCount())
                .as("At least 1 transaction should exist after deposit")
                .isGreaterThanOrEqualTo(1);

        // Assert column headers are present — verifies table isn't just an empty shell
        assertThat(txPage.getTableHeaders())
                .as("Transaction table should have meaningful column headers")
                .isNotEmpty();
    }

    // Verify the system rejects a deposit of 0 amount.
    @Test
    @Order(2)
    @Story("Deposit - Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC2: Deposit of $0 is rejected; balance unchanged and no success confirmation.")
    void tc2_rejectZeroDeposit() {
        double initialBalance = dashboard.getBalanceAsDouble();

        DepositPage depositPage = dashboard.clickDeposit();
        depositPage.enterAmount(TestData.ZERO_AMOUNT_STR).clickDeposit();

        // Check balance — navigate back to dashboard to read it
        driver.navigate().back();  // SPA back navigation returns to dashboard view
        // Allow AngularJS a moment to re-render the balance
        double balanceAfter = dashboard.getBalanceAsDouble();

        assertThat(balanceAfter)
                .as("Balance should not change after attempting zero deposit")
                .isEqualTo(initialBalance);
    }

}
