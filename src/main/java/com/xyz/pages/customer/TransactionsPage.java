package com.xyz.pages.customer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

//Page Object for Transactions history page.
// * Displays all transactions in a table with filtering options.

public class TransactionsPage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Locator constant for table — used in assertions and test validation
    public static final By TRANSACTION_TABLE_BY = By.xpath("//table[contains(@class, 'table-bordered')]");

    // Transactions table
    // Converted from CSS to XPath for robust table element identification
    @FindBy(xpath = "//table[contains(@class, 'table-bordered')]")
    private WebElement transactionsTable;

    // Reset button
    @FindBy(className = "btn-default")
    private WebElement resetBtn;

    // Back button
    @FindBy(className = "btn-default")
    private WebElement backBtn;

    // Date range inputs
    @FindBy(id = "start")
    private WebElement startDateInput;

    @FindBy(id = "end")
    private WebElement endDateInput;

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Get all transactions as list of Transaction objects
     * @return List<Transaction> containing all transactions
     */
    @Step("Get all transactions")
    public List<Transaction> getAllTransactions() {

        wait.until(d -> transactionsTable.isDisplayed());

        List<Transaction> transactions = new ArrayList<>();
        // Get all rows except header
        List<WebElement> rows = transactionsTable.findElements(By.tagName("tr")).stream()
                .skip(1) // Skip header
                .collect(Collectors.toList());

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 3) {
                transactions.add(new Transaction(
                        cells.get(0).getText(), // Date-Time
                        cells.get(1).getText(), // Amount
                        cells.get(2).getText()  // Transaction Type
                ));
            }
        }

        return transactions;
    }

    /**
     * Get transactions filtered by type
     * @param type "Credit" or "Debit"
     * @return List<Transaction> filtered transactions
     */
    @Step("Get transactions by type: {type}")
    public List<Transaction> getTransactionsByType(String type) {
        return getAllTransactions().stream()
                .filter(t -> t.type().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Click Reset button to clear filters
     */
    @Step("Click Reset button")
    public void clickReset() {

        wait.until(d -> resetBtn.isEnabled());
        resetBtn.click();
        // Wait for table to refresh
        wait.until(d -> !getAllTransactions().isEmpty());
    }

    /**
     * Click Back button
     * @return CustomerDashboardPage
     */
    public CustomerDashboardPage clickBack() {

        // Click third button (Back) - after Reset
        driver.findElements(org.openqa.selenium.By.className("btn-default")).get(2).click();
        return new CustomerDashboardPage(driver);
    }

    /**
     * Filter transactions by date range
     * @param startDate start date in yyyy-MM-dd format
     * @param endDate end date in yyyy-MM-dd format
     */
    @Step("Filter transactions by date range: {startDate} to {endDate}")
    public void filterByDateRange(String startDate, String endDate) {

        wait.until(d -> startDateInput.isDisplayed() && endDateInput.isDisplayed());
        startDateInput.clear();
        startDateInput.sendKeys(startDate);
        endDateInput.clear();
        endDateInput.sendKeys(endDate);
        // AngularJS updates table dynamically
        wait.until(d -> !getAllTransactions().isEmpty());
    }

    /**
     * Get transaction count
     * @return int number of transactions
     */
    public int getTransactionCount() {
        return getAllTransactions().size();
    }

    /**
     * Check if table is read-only (no edit/delete buttons)
     * @return boolean true if no edit/delete buttons present
     */
    public boolean isTableReadOnly() {
        // Verify there are no buttons in the table rows
        List<WebElement> buttons = transactionsTable.findElements(By.tagName("button"));
        return buttons.isEmpty();
    }

    /**
     * Check if table is displayed
     * @return boolean true if table is visible
     */
    public boolean isTableDisplayed() {
        try {
            return wait.until(d -> transactionsTable.isDisplayed());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get table column headers
     * @return List<String> of header texts
     */
    public java.util.List<String> getTableHeaders() {
        wait.until(d -> transactionsTable.isDisplayed());
        // The demo app renders headers as <td> inside <thead> (not <th>), so collect both.
        try {
            WebElement thead = transactionsTable.findElement(By.tagName("thead"));
            List<WebElement> headerCells = thead.findElements(By.cssSelector("tr th, tr td"));
            return headerCells.stream()
                    .map(WebElement::getText)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception ignored) {
            // Fallback: if <thead> is missing, use the first row's cells as "headers"
            List<WebElement> rows = transactionsTable.findElements(By.tagName("tr"));
            if (rows.isEmpty()) {
                return List.of();
            }
            List<WebElement> cells = rows.get(0).findElements(By.cssSelector("th, td"));
            return cells.stream()
                    .map(WebElement::getText)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
    }

    /**
     * Check if a transaction of given type exists
     * @param type "Credit" or "Debit"
     * @return boolean true if at least one transaction of that type exists
     */
    public boolean hasTransactionOfType(String type) {
        return !getTransactionsByType(type).isEmpty();
    }

    /**
     * Record for transaction data
     */
    public record Transaction(String dateTime, String amount, String type) {
        public double getAmountAsDouble() {
            return Double.parseDouble(amount.replaceAll("[^0-9.-]", ""));
        }

        public LocalDateTime getDateTime() {
            // Parse the date-time format from the app
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm:ss a");
            return LocalDateTime.parse(dateTime, formatter);
        }
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
