package com.xyz.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Page Object for Bank Manager Home page.
// * Contains navigation buttons for manager functions.

public class ManagerHomePage extends BasePage{



    // Add Customer button
    @FindBy(className = "btn-lg")
    private WebElement addCustomerBtn;

    // Open Account button
    @FindBy(className = "btn-lg")
    private WebElement openAccountBtn; // Note: Same class, will need to differentiate by position

    // Customers button
    @FindBy(className = "btn-lg")
    private WebElement customersBtn; // Note: Same class, will need to differentiate by position

    public ManagerHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Click Add Customer button
     * @return AddCustomerPage instance
     */
    public AddCustomerPage clickAddCustomer() {

        // Wait for all buttons to be present, then click first one (Add Customer)
        wait.until(d -> addCustomerBtn.isDisplayed());
        addCustomerBtn.click();

        return new AddCustomerPage(driver);
    }

    /**
     * Click Open Account button
     * @return OpenAccountPage instance
     */
    public OpenAccountPage clickOpenAccount() {

        // Click second button (Open Account) - using findElements approach
        driver.findElements(org.openqa.selenium.By.className("btn-lg")).get(1).click();

        return new OpenAccountPage(driver);
    }

    /**
     * Click Customers button
     * @return CustomersPage instance
     */
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
        WebElement header = driver.findElement(org.openqa.selenium.By.cssSelector(".mainHeading"));
        return wait.until(d -> header.getText());
    }

}
