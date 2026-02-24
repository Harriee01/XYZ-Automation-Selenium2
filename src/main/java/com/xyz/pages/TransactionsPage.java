package com.xyz.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Page Object for Transactions history page.
// * Displays all transactions in a table with filtering options.

public class TransactionsPage extends BasePage {

    // Transactions table
    @FindBy(css = ".table.table-bordered")
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
        super(driver);
    }

    /**
     * Get all transactions as list of Transaction objects
     * @return List<Transaction> containing all transactions
     */
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
    public List<Transaction> getTransactionsByType(String type) {
        return getAllTransactions().stream()
                .filter(t -> t.type().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Click Reset button to clear filters
     */
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
}
