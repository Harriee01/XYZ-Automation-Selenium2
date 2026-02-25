package com.xyz.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

//Page Object for Customer selection page.
// * Allows customers to select their name from dropdown.

public class CustomerSelectPage extends BasePage {

    // Customer dropdown
    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    // Login button
    @FindBy(className = "btn-default")
    private WebElement loginBtn;

    public CustomerSelectPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Select customer by visible text
     * @param customerName full name as "FirstName LastName"
     */
    @Step("Select customer: {customerName}")
    public void selectCustomer(String customerName) {

        wait.until(d -> customerDropdown.isDisplayed());
        Select select = new Select(customerDropdown);
        select.selectByVisibleText(customerName);
    }

    /**
     * Select customer by index
     * @param index dropdown index (0 for first customer)
     */
    @Step("Select customer by index: {index}")
    public void selectCustomerByIndex(int index) {

        wait.until(d -> customerDropdown.isDisplayed());
        Select select = new Select(customerDropdown);
        select.selectByIndex(index);
    }

    /**
     * Click Login button
     * @return CustomerDashboardPage instance
     */
    @Step("Click Login button")
    public CustomerDashboardPage clickLogin() {

        wait.until(d -> loginBtn.isEnabled());
        loginBtn.click();
        return new CustomerDashboardPage(driver);
    }

    /**
     * Complete customer login flow
     * @param customerName customer name to select
     * @return CustomerDashboardPage
     */
    @Step("Login as customer: {customerName}")
    public CustomerDashboardPage loginAs(String customerName) {
        selectCustomer(customerName);
        return clickLogin();
    }

    /**
     * Get list of all available customers
     * @return List<String> of customer names
     */
    public List<String> getAllCustomers() {
        wait.until(d -> customerDropdown.isDisplayed());
        Select select = new Select(customerDropdown);
        return select.getOptions().stream()
                .skip(1) // Skip first option (---Your Name---)
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Check if customer exists in dropdown
     * @param customerName customer name to check
     * @return boolean true if exists
     */
    public boolean isCustomerAvailable(String customerName) {
        return getAllCustomers().contains(customerName);
    }

    /**
     * Get customer count
     * @return int number of customers
     */
    public int getCustomerCount() {
        return getAllCustomers().size();
    }
}
