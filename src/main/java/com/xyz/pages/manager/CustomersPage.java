package com.xyz.pages.manager;

import java.util.List;
import java.util.stream.Collectors;

import com.xyz.utils.PageInitializer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

// Page Object for Customers list page.
// * Displays all customers in a table with search functionality.

public class CustomersPage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Search input field
    // Converted from CSS to XPath for better readability in AngularJS dynamic structures
    @FindBy(xpath = "//input[@placeholder='Search Customer']")
    private WebElement searchInput;

    // Customers table
    // Converted from CSS to XPath para AngularJS compatibility
    @FindBy(xpath = "/html/body/div/div/div[2]/div/div[2]/div/div/table")
    private WebElement customersTable;

    // Delete buttons in table
    // Converted from CSS to XPath for robust element identification
    @FindBy(xpath = "//tbody/tr[1]/td[5]/button[1]")
    private List<WebElement> deleteButtons;

    public CustomersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Search for customer by first name
     * @param firstName first name to search
     */
    @Step("Search customer by first name: {firstName}")
    public void searchCustomer(String firstName) {

        wait.until(d -> searchInput.isDisplayed());
        searchInput.clear();
        searchInput.sendKeys(firstName);
        // AngularJS updates table dynamically, wait for results
        wait.until(d -> !getAllCustomers().isEmpty());
    }

    /**
     * Get all customers from table as list of Customer objects
     * @return List<Customer> containing all displayed customers
     */
    public List<Customer> getAllCustomers() {

        wait.until(d -> customersTable.isDisplayed());

        // Get all rows except header
        List<WebElement> rows = customersTable.findElements(By.tagName("tr")).stream()
                .skip(1) // Skip header row
                .collect(Collectors.toList());

        return rows.stream()
                .map(this::extractCustomerFromRow)
                .collect(Collectors.toList());
    }

    /**
     * Extract customer data from table row
     * @param row WebElement representing table row
     * @return Customer object with extracted data
     */
    private Customer extractCustomerFromRow(WebElement row) {
        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() >= 3) {
            return new Customer(
                    cells.get(0).getText(), // First Name
                    cells.get(1).getText(), // Last Name
                    cells.get(2).getText()  // Post Code
            );
        }
        return null;
    }

    /**
     * Check if customer exists in table
     * @param firstName customer first name
     * @param lastName customer last name
     * @return boolean true if customer found
     */
    public boolean isCustomerPresent(String firstName, String lastName) {

        return getAllCustomers().stream()
                .anyMatch(c -> c.firstName().equals(firstName) &&
                        c.lastName().equals(lastName));
    }

    /**
     * Check if customer is visible in the table (searches all customers)
     * @param firstName customer first name to search for
     * @return boolean true if customer found in current view
     */
    @Step("Check if customer is visible: {firstName}")
    public boolean isCustomerVisible(String firstName) {
        return getAllCustomers().stream()
                .anyMatch(c -> c.firstName().contains(firstName));
    }

    /**
     * Delete customer by index
     * @param index row index to delete (0-based)
     */
    public void deleteCustomer(int index) {

        if (index < deleteButtons.size()) {
            deleteButtons.get(index).click();
            // Handle confirmation alert if any
            driver.switchTo().alert().accept();
        }
    }

    /**
     * Record for customer data (Java 21 feature)
     */
    public record Customer(String firstName, String lastName, String postCode) {}

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
