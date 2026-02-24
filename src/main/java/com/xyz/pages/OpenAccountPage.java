package com.xyz.pages;

import com.xyz.utils.AlertHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;


//Page Object for Open Account form.
// * Handles account creation for existing customers.

public class OpenAccountPage {



    // Customer select dropdown - has id="userSelect"
    @FindBy(id = "userSelect")
    private WebElement customerSelect;

    // Currency select dropdown - has id="currency"
    @FindBy(id = "currency")
    private WebElement currencySelect;

    // Process button
    @FindBy(className = "btn-default")
    private WebElement processBtn;

    public OpenAccountPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Select customer from dropdown by visible text
     * @param customerName full name as "FirstName LastName"
     */
    public void selectCustomer(String customerName) {

        wait.until(d -> customerSelect.isDisplayed());
        Select select = new Select(customerSelect);
        select.selectByVisibleText(customerName);
    }

    /**
     * Select customer by index
     * @param index dropdown index
     */
    public void selectCustomerByIndex(int index) {

        wait.until(d -> customerSelect.isDisplayed());
        Select select = new Select(customerSelect);
        select.selectByIndex(index);
    }

    /**
     * Select currency from dropdown
     * @param currency currency name (e.g., "Dollar")
     */
    public void selectCurrency(String currency) {

        wait.until(d -> currencySelect.isDisplayed());
        Select select = new Select(currencySelect);
        select.selectByVisibleText(currency);
    }

    /**
     * Click Process button and handle alert
     * @return String alert message containing account number on success
     */
    public String clickProcess() {

        wait.until(d -> processBtn.isEnabled());
        processBtn.click();

        // Alert contains account number on success: "Account created successfully with account Number 1016"
        String alertText = AlertHandler.getAlertText(driver);
        AlertHandler.acceptAlert(driver);


        return alertText;
    }

    /**
     * Complete open account flow
     * @param customerName full customer name
     * @param currency currency type
     * @return String alert message
     */
    public String openAccount(String customerName, String currency) {

        selectCustomer(customerName);
        selectCurrency(currency);
        return clickProcess();
    }

    /**
     * Get available customers count
     * @return int number of customers in dropdown
     */
    public int getCustomerCount() {
        wait.until(d -> customerSelect.isDisplayed());
        Select select = new Select(customerSelect);
        return select.getOptions().size();
    }

    /**
     * Check if specific customer exists in dropdown
     * @param customerName full name to check
     * @return boolean true if customer exists
     */
    public boolean isCustomerAvailable(String customerName) {
        wait.until(d -> customerSelect.isDisplayed());
        Select select = new Select(customerSelect);
        return select.getOptions().stream()
                .anyMatch(option -> option.getText().equals(customerName));
    }

}
