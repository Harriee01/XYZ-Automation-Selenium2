package com.xyz.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Page Object for Bank Manager Home page.
// * Contains navigation buttons for manager functions.

public class ManagerHomePage extends BasePage {

    // Add Customer button — click to navigate to AddCustomerPage
    @FindBy(className = "btn-lg")
    private WebElement addCustomerBtn;

    public ManagerHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Click Add Customer button
     * @return AddCustomerPage instance
     */
    @Step("Click Add Customer button")
    public AddCustomerPage clickAddCustomer() {
        // Wait for button to be displayed and enabled before clicking
        wait.until(d -> addCustomerBtn.isDisplayed());
        addCustomerBtn.click();

        return new AddCustomerPage(driver);
    }

    /**
     * Click Open Account button
     * @return OpenAccountPage instance
     */
    @Step("Click Open Account button")
    public OpenAccountPage clickOpenAccount() {

        // Click second button (Open Account) - using findElements approach
        driver.findElements(org.openqa.selenium.By.className("btn-lg")).get(1).click();

        return new OpenAccountPage(driver);
    }

    /**
     * Click Customers button
     * @return CustomersPage instance
     */
    @Step("Click Customers button")
    public CustomersPage clickCustomers() {

        // Click third button (Customers)
        driver.findElements(org.openqa.selenium.By.className("btn-lg")).get(2).click();

        return new CustomersPage(driver);
    }

    /**
     * Get page header text
     * @return String header text
     */
    public String getHeaderText() {
        // Converted from CSS to XPath for better class-based element matching
        WebElement header = driver.findElement(org.openqa.selenium.By.xpath("//h1[contains(@class, 'mainHeading')]"));
        return wait.until(d -> header.getText());
    }

}
