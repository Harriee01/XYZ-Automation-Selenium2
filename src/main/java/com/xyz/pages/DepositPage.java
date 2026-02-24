package com.xyz.pages;

import com.xyz.utils.AlertHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


// Page Object for Deposit form.
// * Handles deposit operations and validation.


public class DepositPage {

    // Amount input field
    @FindBy(css = "input[ng-model='amount']")
    private WebElement amountInput;

    // Deposit button
    @FindBy(className = "btn-default")
    private WebElement depositBtn;

    // Back button
    @FindBy(className = "btn-default")
    private WebElement backBtn; // Note: Same class, will need position

    // Success/Error message
    @FindBy(css = ".error.ng-binding")
    private WebElement messageSpan;

    public DepositPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter deposit amount
     * @param amount amount to deposit
     */
    public void enterAmount(double amount) {

        wait.until(d -> amountInput.isDisplayed());
        amountInput.clear();
        amountInput.sendKeys(String.valueOf(amount));
    }

    /**
     * Click Deposit button
     * @return String message from span or alert
     */
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
     * Complete deposit operation
     * @param amount amount to deposit
     * @return String result message
     */
    public String deposit(double amount) {

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
