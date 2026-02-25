package com.xyz.pages.customer;

import com.xyz.pages.PageInitializer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xyz.utils.AlertHandler;

import io.qameta.allure.Step;


// Page Object for Deposit form.
// * Handles deposit operations and validation.

public class DepositPage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Amount input field
    // Converted from CSS to XPath for AngularJS ng-model attribute matching
    @FindBy(xpath = "//input[@ng-model='amount']")
    private WebElement amountInput;

    // Deposit button
    @FindBy(className = "btn-default")
    private WebElement depositBtn;

    // Success/Error message
    // Converted from CSS to XPath for better AngularJS error class handling
    @FindBy(xpath = "//span[@class='error ng-binding']")
    private WebElement messageSpan;

    public DepositPage(WebDriver driver) {
        this.driver = driver;
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageInitializer.initElements(driver, this);
    }

    /**
     * Enter deposit amount
     * @param amount amount to deposit as string
     * @return this DepositPage for method chaining
     */
    @Step("Enter deposit amount: {amount}")
    public DepositPage enterAmount(String amount) {
        // Overload accepting String for convenience — handles form input naturally
        wait.until(d -> amountInput.isDisplayed());
        amountInput.clear();
        amountInput.sendKeys(amount);
        return this;  // Enable fluent chaining
    }

    /**
     * Enter deposit amount
     * @param amount amount to deposit as double
     * @return this DepositPage for method chaining
     */
    public DepositPage enterAmount(double amount) {
        // Original method accepting double
        wait.until(d -> amountInput.isDisplayed());
        amountInput.clear();
        amountInput.sendKeys(String.valueOf(amount));
        return this;  // Enable fluent chaining
    }

    /**
     * Click Deposit button
     * @return String message from span or alert
     */
    @Step("Click Deposit button")
    public String clickDeposit() {

        wait.until(d -> depositBtn.isEnabled());
        depositBtn.click();

        // AngularJS shows success message in span, errors sometimes in alert
        String message = getDepositMessage();

        return message;
    }

    /**
     * Get deposit result message
     * @return String message
     */
    private String getDepositMessage() {
        // Check for alert first
        String alertText = AlertHandler.getAlertText(driver);
        if (!alertText.isEmpty()) {
            AlertHandler.acceptAlert(driver);
            return alertText;
        }

        // Otherwise check message span
        try {
            wait.until(d -> messageSpan.isDisplayed());
            return messageSpan.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Complete deposit operation with amount as string
     * @param amount amount to deposit as string
     * @return String result message
     */
    @Step("Deposit amount: {amount}")
    public String deposit(String amount) {
        // Overload accepting String for test convenience
        enterAmount(amount);
        return clickDeposit();
    }

    /**
     * Complete deposit operation with amount as double
     * @param amount amount to deposit as double
     * @return String result message
     */
    public String deposit(double amount) {
        // Original method accepting double
        enterAmount(amount);
        return clickDeposit();
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
     * Get current amount field value
     * @return String current value
     */
    public String getAmountValue() {
        return amountInput.getAttribute("value");
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
