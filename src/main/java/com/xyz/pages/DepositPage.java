package com.xyz.pages;

import com.xyz.utils.AlertHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


// Page Object for Deposit form.
// * Handles deposit operations and validation.

public class DepositPage extends BasePage {

    // Amount input field
    // Converted from CSS to XPath for AngularJS ng-model attribute matching
    @FindBy(xpath = "//input[@ng-model='amount']")
    private WebElement amountInput;

    // Deposit button
    @FindBy(className = "btn-default")
    private WebElement depositBtn;

    // Back button
    @FindBy(className = "btn-default")
    private WebElement backBtn; // Note: Same class, will need position

    // Success/Error message
    // Converted from CSS to XPath for better AngularJS error class handling
    @FindBy(xpath = "//span[@class='error ng-binding']")
    private WebElement messageSpan;

    public DepositPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter deposit amount
     * @param amount amount to deposit as string
     */
    @Step("Enter deposit amount: {amount}")
    public void enterAmount(String amount) {
        // Overload accepting String for convenience — handles form input naturally
        wait.until(d -> amountInput.isDisplayed());
        amountInput.clear();
        amountInput.sendKeys(amount);
    }

    /**
     * Enter deposit amount
     * @param amount amount to deposit as double
     */
    public void enterAmount(double amount) {
        // Original method accepting double
        wait.until(d -> amountInput.isDisplayed());
        amountInput.clear();
        amountInput.sendKeys(String.valueOf(amount));
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
}
