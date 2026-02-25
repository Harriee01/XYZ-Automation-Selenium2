package com.xyz.pages.manager;

import com.xyz.utils.PageInitializer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.xyz.models.TestData.*;
import com.xyz.utils.AlertHandler;

import io.qameta.allure.Step;

import static com.xyz.models.TestData.CURRENCY_DOLLAR;
import static com.xyz.models.TestData.EXISTING_CUSTOMER_NAME;


//Page Object for Open Account form.
// * Handles account creation for existing customers.

public class OpenAccountPage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Customer select dropdown - has id="userSelect"
    @FindBy(id = "userSelect")
    private WebElement customerSelect;

    // Currency select dropdown - has id="currency"
    @FindBy(id = "currency")
    private WebElement currencySelect;

    // Process button
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement processBtn;

    public OpenAccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Select customer from dropdown by visible text
     */
    @Step("Select customer: {customerName}")
    public void selectCustomer() {

        wait.until(d -> customerSelect.isDisplayed());
        Select select = new Select(customerSelect);
        select.selectByVisibleText(EXISTING_CUSTOMER_NAME );
    }

    /**
     * Select customer by index
     * @param index dropdown index
     */
    @Step("Select customer by index: {index}")
    public void selectCustomerByIndex(int index) {

        wait.until(d -> customerSelect.isDisplayed());
        Select select = new Select(customerSelect);
        select.selectByIndex(index);
    }

    /**
     * Select currency from dropdown
     * @param currency currency name (see TestData.CURRENCY_DOLLAR)
     */
    @Step("Select currency: {currency}")
    public void selectCurrency(String currency) {

        wait.until(d -> currencySelect.isDisplayed());
        Select select = new Select(currencySelect);
        select.selectByVisibleText(CURRENCY_DOLLAR);
    }

    /**
     * Click Process button and handle alert
     * @return String alert message containing account number on success
     */
    @Step("Click Process button")
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
    @Step("Open account for {customerName} in {currency}")
    public String openAccount(String customerName, String currency) {

        selectCustomer();
        selectCurrency(CURRENCY_DOLLAR);
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

    /**
     * Get all customer dropdown options as a list of strings
     * @return List<String> of all dropdown options
     */
    public java.util.List<String> getCustomerDropdownOptions() {
        // Wait for dropdown to load
        wait.until(d -> customerSelect.isDisplayed());
        Select select = new Select(customerSelect);
        // Map WebElements to their text values
        return select.getOptions().stream()
                .map(WebElement::getText)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Click Process button without validating currency selection.
     * Used to test form validation when currency is not selected.
     */
    public void clickProcessWithoutCurrency() {
        wait.until(d -> processBtn.isEnabled());
        processBtn.click();
        // Note: May or may not trigger an alert; calling code handles both cases
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