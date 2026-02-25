package com.xyz.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xyz.utils.AlertHandler;

import io.qameta.allure.Step;

//Page Object for Withdraw form.
// * Handles withdrawal operations with validation.
public class WithdrawPage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Amount input field
    // Converted from CSS to XPath for AngularJS ng-model attribute matching
    @FindBy(xpath = "//input[@ng-model='amount']")
    private WebElement amountInput;

    // Withdraw button
    @FindBy(className = "btn-default")
    private WebElement withdrawBtn;

    // Success/Error message
    // Converted from CSS to XPath for better AngularJS error class handling
    @FindBy(xpath = "//span[@class='error ng-binding']")
    private WebElement messageSpan;

    public WithdrawPage(WebDriver driver) {
        this.driver = driver;
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageInitializer.initElements(driver, this);
    }

    /**
     * Enter withdrawal amount as string
     * @param amount amount to withdraw as string
     * @return this WithdrawPage for method chaining
     */
    @Step("Enter withdrawal amount: {amount}")
    public WithdrawPage enterAmount(String amount) {
        // Overload accepting String for convenience — handles form input naturally
        wait.until(d -> amountInput.isDisplayed());
        amountInput.clear();
        amountInput.sendKeys(amount);
        return this;  // Enable fluent chaining
    }

    /**
     * Enter withdrawal amount as double
     * @param amount amount to withdraw as double
     * @return this WithdrawPage for method chaining
     */
    public WithdrawPage enterAmount(double amount) {
        // Original method accepting double
        wait.until(d -> amountInput.isDisplayed());
        amountInput.clear();
        amountInput.sendKeys(String.valueOf(amount));
        return this;  // Enable fluent chaining
    }

    /**
     * Click Withdraw button
     * @return String message from span or alert
     */
    @Step("Click Withdraw button")
    public String clickWithdraw() {

        wait.until(d -> withdrawBtn.isEnabled());
        withdrawBtn.click();

        String message = getWithdrawMessage();

        return message;
    }

    /**
     * Get withdrawal result message
     * @return String message
     */
    private String getWithdrawMessage() {
        // Check for alert first (insufficient funds typically shows alert)
        String alertText = AlertHandler.getAlertText(driver);
        if (!alertText.isEmpty()) {
            AlertHandler.acceptAlert(driver);
            return alertText;
        }

        // Otherwise check message span (success message)
        try {
            wait.until(d -> messageSpan.isDisplayed());
            return messageSpan.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Complete withdrawal operation with amount as string
     * @param amount amount to withdraw as string
     * @return String result message
     */
    @Step("Withdraw amount: {amount}")
    public String withdraw(String amount) {
        // Overload accepting String for test convenience
        enterAmount(amount);
        return clickWithdraw();
    }

    /**
     * Complete withdrawal operation with amount as double
     * @param amount amount to withdraw as double
     * @return String result message
     */
    public String withdraw(double amount) {
        // Original method accepting double
        enterAmount(amount);
        return clickWithdraw();
    }

    /**
     * Click Back button
     * @return CustomerDashboardPage
     */
    public CustomerDashboardPage clickBack() {

        // Click second button (Back)
        driver.findElements(org.openqa.selenium.By.className("btn-default")).get(1).click();
        return new CustomerDashboardPage(driver);
    }

    /**
     * Check if amount field is displayed
     * @return boolean true if displayed
     */
    public boolean isAmountFieldDisplayed() {
        return wait.until(d -> amountInput.isDisplayed());
    }

    /**
     * Validate if withdrawal is possible with current balance
     * @param amount amount to withdraw
     * @return boolean true if amount is positive and not zero
     */
    public boolean isValidWithdrawalAmount(double amount) {
        return amount > 0;
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
