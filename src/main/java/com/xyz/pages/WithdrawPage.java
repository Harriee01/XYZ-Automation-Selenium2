package com.xyz.pages;

import com.xyz.utils.AlertHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Page Object for Withdraw form.
// * Handles withdrawal operations with validation.
public class WithdrawPage extends BasePage {

    // Amount input field
    @FindBy(css = "input[ng-model='amount']")
    private WebElement amountInput;

    // Withdraw button
    @FindBy(className = "btn-default")
    private WebElement withdrawBtn;

    // Back button
    @FindBy(className = "btn-default")
    private WebElement backBtn;

    // Success/Error message
    @FindBy(css = ".error.ng-binding")
    private WebElement messageSpan;

    public WithdrawPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter withdrawal amount
     * @param amount amount to withdraw
     */
    public void enterAmount(double amount) {

        wait.until(d -> amountInput.isDisplayed());
        amountInput.clear();
        amountInput.sendKeys(String.valueOf(amount));
    }

    /**
     * Click Withdraw button
     * @return String message from span or alert
     */
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
     * Complete withdrawal operation
     * @param amount amount to withdraw
     * @return String result message
     */
    public String withdraw(double amount) {

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

}
