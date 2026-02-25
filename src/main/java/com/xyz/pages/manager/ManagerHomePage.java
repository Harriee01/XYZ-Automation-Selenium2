package com.xyz.pages.manager;

import com.xyz.pages.PageInitializer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

//Page Object for Bank Manager Home page.
// * Contains navigation buttons for manager functions.

public class ManagerHomePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Add Customer button — click to navigate to AddCustomerPage
    @FindBy(xpath = "//button[normalize-space()='Add Customer']")
    private WebElement addCustomerBtn;

    public ManagerHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageInitializer.initElements(driver, this);
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
        driver.findElements(org.openqa.selenium.By.xpath("//button[normalize-space()='Open Account']")).get(1).click();

        return new OpenAccountPage(driver);
    }

    /**
     * Click Customers button
     * @return CustomersPage instance
     */
    @Step("Click Customers button")
    public CustomersPage clickCustomers() {

        // Click third button (Customers)
        driver.findElements(org.openqa.selenium.By.xpath("//button[normalize-space()='Customers']")).get(2).click();

        return new CustomersPage(driver);
    }

    /**
     * Get page header text
     * @return String header text
     */
    public String getHeaderText() {
        // Converted from CSS to XPath for better class-based element matching
        WebElement header = driver.findElement(org.openqa.selenium.By.xpath("//div[@class='box mainhdr']"));
        return wait.until(d -> header.getText());
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
