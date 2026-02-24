package com.xyz.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Page Object for Customer Dashboard.
// * Shows account overview and navigation to transactions/deposit/withdraw

public class CustomerDashboardPage extends BasePage {

    // Welcome message with customer name
    @FindBy(css = ".fontBig.ng-binding")
    private WebElement welcomeMessage;

    // Account number dropdown
    @FindBy(id = "accountSelect")
    private WebElement accountSelect;

    // Balance display
    @FindBy(css = ".ng-binding:nth-child(2)")
    private WebElement balanceDisplay;

    // Currency display
    @FindBy(css = ".ng-binding:nth-child(3)")
    private WebElement currencyDisplay;

    // Transactions button
    @FindBy(css = "button[ng-click='transactions()']")
    private WebElement transactionsBtn;

    // Deposit button
    @FindBy(css = "button[ng-click='deposit()']")
    private WebElement depositBtn;

    // Withdraw button
    @FindBy(css = "button[ng-click='withdrawl()']")
    private WebElement withdrawBtn;

    public CustomerDashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get welcome message text
     * @return String welcome message
     */
    public String getWelcomeMessage() {
        wait.until(d -> welcomeMessage.isDisplayed());
        String message = welcomeMessage.getText();

        return message;
    }

    /**
     * Get current account balance
     * @return double current balance
     */
    public double getBalance() {
        wait.until(d -> balanceDisplay.isDisplayed());
        // Balance text might have currency symbol, extract number
        String balanceText = balanceDisplay.getText().replaceAll("[^0-9.-]", "");
        double balance = Double.parseDouble(balanceText);

        return balance;
    }

    /**
     * Click Transactions button
     * @return TransactionsPage instance
     */
    public TransactionsPage clickTransactions() {

        wait.until(d -> transactionsBtn.isEnabled());
        transactionsBtn.click();
        return new TransactionsPage(driver);
    }

    /**
     * Click Deposit button
     * @return DepositPage instance
     */
    public DepositPage clickDeposit() {

        wait.until(d -> depositBtn.isEnabled());
        depositBtn.click();
        return new DepositPage(driver);
    }

    /**
     * Click Withdraw button
     * @return WithdrawPage instance
     */
    public WithdrawPage clickWithdraw() {

        wait.until(d -> withdrawBtn.isEnabled());
        withdrawBtn.click();
        return new WithdrawPage(driver);
    }

    /**
     * Select account by index
     * @param index account index
     */
    public void selectAccount(int index) {

        wait.until(d -> accountSelect.isDisplayed());
        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(accountSelect);
        select.selectByIndex(index);

        // Wait for balance to update after account change
        wait.until(d -> !balanceDisplay.getText().isEmpty());
    }

    /**
     * Get account count
     * @return int number of accounts
     */
    public int getAccountCount() {
        wait.until(d -> accountSelect.isDisplayed());
        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(accountSelect);
        return select.getOptions().size();
    }
}
