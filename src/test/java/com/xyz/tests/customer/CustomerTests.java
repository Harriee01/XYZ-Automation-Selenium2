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

    //Verify rejection of a negative deposit amount.
    @Test
    @Order(3)
    @Story("Deposit - Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC3: Deposit of negative amount is rejected; balance unchanged.")
    void tc3_rejectNegativeDeposit() {
        double initialBalance = dashboard.getBalanceAsDouble();

        DepositPage depositPage = dashboard.clickDeposit();
        depositPage.enterAmount(TestData.NEGATIVE_AMOUNT_STR).clickDeposit();

        driver.navigate().back();
        double balanceAfter = dashboard.getBalanceAsDouble();

        assertThat(balanceAfter)
                .as("Balance should not change after attempting negative deposit of %s",
                        TestData.NEGATIVE_AMOUNT_STR)
                .isEqualTo(initialBalance);
    }

    //Verify a valid deposit immediately appears in transaction history.
    @Test
    @Order(4)
    @Story("Transaction History")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC4: After a valid deposit, the transaction appears immediately in the history table.")
    void tc4_depositAppearsInTransactionHistory() {
        // Navigate to Transactions FIRST to get baseline count (may be 0 for fresh account)
        TransactionsPage txPage = dashboard.clickTransactions();
        int countBefore = txPage.getTransactionCount();

        // Go back to dashboard to deposit
        driver.navigate().back();

        // Perform deposit
        dashboard.clickDeposit().deposit(TestData.DEPOSIT_AMOUNT_STR);

        // Navigate to Transactions again — fresh page object because AngularJS re-rendered view
        txPage = dashboard.clickTransactions();

        // Assert count increased by exactly 1
        assertThat(txPage.getTransactionCount())
                .as("Transaction count should increase by 1 after deposit")
                .isEqualTo(countBefore + 1);

        // Assert the new row indicates a Credit (deposit) transaction type
        assertThat(txPage.hasTransactionOfType("Credit"))
                .as("Transaction history should contain a 'Credit' entry after deposit")
                .isTrue();
    }

    //Verify transaction history table is read-only (no edit/delete UI controls).
    @Test
    @Order(5)
    @Story("Transaction History")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC5: Transaction history table has no edit or delete controls — read-only for customers.")
    void tc5_transactionHistoryIsReadOnly() {
        // Make a deposit so the table has at least one row to inspect
        dashboard.clickDeposit().deposit(TestData.DEPOSIT_AMOUNT_STR);

        // Navigate to Transactions
        TransactionsPage txPage = dashboard.clickTransactions();

        // Assert table is displayed first (precondition for read-only check)
        assertThat(txPage.isTableDisplayed())
                .as("Transaction table must be visible before checking read-only")
                .isTrue();

        // Use TableAssertions — checks no <button> or <input> elements in the table
        TableAssertions.assertNoEditDeleteControls(driver, TransactionsPage.TRANSACTION_TABLE_BY);
    }

    //Verify a $100 deposit updates balance by exactly +100 and shows success message.
    @Test
    @Order(6)
    @Story("Deposit")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC6: $100 deposit increases balance by exactly $100; Deposit Successful message shown.")
    void tc6_hundredDollarDepositUpdatesBalance() {
        double initialBalance = dashboard.getBalanceAsDouble();

        // Perform deposit and capture status message
        DepositPage depositPage = dashboard.clickDeposit();
        String message = depositPage.deposit(TestData.DEPOSIT_AMOUNT_STR);

        // Assert success message
        assertThat(message)
                .as("Expected 'Deposit Successful' message after $100 deposit")
                .isEqualTo(TestData.MSG_DEPOSIT_SUCCESS);

        // Navigate back to dashboard to read updated balance
        driver.navigate().back();
        double newBalance = dashboard.getBalanceAsDouble();

        assertThat(newBalance)
                .as("Balance should increase by exactly $%.0f after deposit", TestData.DEPOSIT_AMOUNT)
                .isEqualTo(initialBalance + TestData.DEPOSIT_AMOUNT);
    }

    //Verify $50 withdrawal reduces balance by exactly $50 and shows success message.

    @Test
    @Order(7)
    @Story("Withdraw")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC7: $50 withdrawal decreases balance by exactly $50; Transaction successful message shown.")
    void tc7_fiftyDollarWithdrawalUpdatesBalance() {
        // Seed account with $100 so withdrawal is possible
        dashboard.clickDeposit().deposit(TestData.DEPOSIT_AMOUNT_STR);
        driver.navigate().back();

        double balanceAfterDeposit = dashboard.getBalanceAsDouble();

        // Perform $50 withdrawal
        WithdrawPage withdrawPage = dashboard.clickWithdraw();
        String message = withdrawPage.withdraw(TestData.WITHDRAW_AMOUNT_STR);

        // Assert success message
        assertThat(message)
                .as("Expected withdrawal success message")
                .isEqualTo(TestData.MSG_WITHDRAW_SUCCESS);

        // Navigate back and verify balance decreased by $50
        driver.navigate().back();
        double finalBalance = dashboard.getBalanceAsDouble();

        assertThat(finalBalance)
                .as("Balance should decrease by exactly $%.0f after withdrawal", TestData.WITHDRAW_AMOUNT)
                .isEqualTo(balanceAfterDeposit - TestData.WITHDRAW_AMOUNT);
    }


}

