package com.xyz.pages.customer;

import com.xyz.pages.PageInitializer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

//Page Object for Customer Dashboard.
// * Shows account overview and navigation to transactions/deposit/withdraw

public class CustomerDashboardPage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Welcome message with customer name
    // Converted from CSS to XPath for AngularJS ng-binding compatibility
    @FindBy(xpath = "//span[@class='fontBig ng-binding']")
    private WebElement welcomeMessage;

    // Account number dropdown
    @FindBy(id = "accountSelect")
    private WebElement accountSelect;

    // Balance display
    // Converted from CSS :nth-child(2) to XPath for explicit targeting
    @FindBy(xpath = "//span[contains(@class, 'ng-binding')][position()=2]")
    private WebElement balanceDisplay;

    // Transactions button
    // Converted from CSS to XPath for better AngularJS element matching
    @FindBy(xpath = "//button[@ng-click='transactions()']")
    private WebElement transactionsBtn;

    // Deposit button
    // Converted from CSS to XPath for robust element identification
    @FindBy(xpath = "//button[@ng-click='deposit()']")
    private WebElement depositBtn;

    // Withdraw button - note the typo 'withdrawl' matches the actual app HTML
    // Converted from CSS to XPath for AngularJS compatibility
    @FindBy(xpath = "//button[@ng-click='withdrawl()']")
    private WebElement withdrawBtn;

    public CustomerDashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageInitializer.initElements(driver, this);
    }

    /**
     * Get welcome message text
     * @return String welcome message
     */
    @Step("Get welcome message")
    public String getWelcomeMessage() {
        wait.until(d -> welcomeMessage.isDisplayed());
        String message = welcomeMessage.getText();

        return message;
    }

    /**
     * Get current account balance as double
     * @return double current balance
     */
    @Step("Get current balance")
    public double getBalanceAsDouble() {
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
    @Step("Click Transactions button")
    public TransactionsPage clickTransactions() {

        wait.until(d -> transactionsBtn.isEnabled());
        transactionsBtn.click();
        return new TransactionsPage(driver);
    }

    /**
     * Click Deposit button
     * @return DepositPage instance
     */
    @Step("Click Deposit button")
    public DepositPage clickDeposit() {

        wait.until(d -> depositBtn.isEnabled());
        depositBtn.click();
        return new DepositPage(driver);
    }

    /**
     * Click Withdraw button
     * @return WithdrawPage instance
     */
    @Step("Click Withdraw button")
    public WithdrawPage clickWithdraw() {

        wait.until(d -> withdrawBtn.isEnabled());
        withdrawBtn.click();
        return new WithdrawPage(driver);
    }

    /**
     * Select account by index
     * @param index account index
     */
    @Step("Select account at index: {index}")
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

    /**
     * Get the current page title
     * @return the title of the current page
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get the current page URL
     * @return the URL of the current page
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
