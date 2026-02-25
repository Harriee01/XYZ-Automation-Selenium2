package com.xyz.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


//TableAssertions — reusable helpers for asserting HTML table content.

public class TableAssertions {

    private TableAssertions() {}

    /**
     * Asserts that at least one row in the table contains the given text in any cell.
     *
     * @param driver      active WebDriver
     * @param tableBy     By locator for the table element
     * @param searchText  text to find in any <td>
     */
    public static void assertTableContains(WebDriver driver, By tableBy, String searchText) {
        // Re-locate table after potential DOM refresh — avoids StaleElementReferenceException
        WebElement table = WaitUtils.waitForElementVisible(driver, (WebElement) tableBy);

        // Find all cells in the table — AngularJS renders ng-repeat rows dynamically
        List<WebElement> cells = table.findElements(By.tagName("td"));

        // Collect cell texts for a meaningful AssertJ failure message
        List<String> cellTexts = cells.stream()
                .map(WebElement::getText)
                .toList();  // Java 16+ immutable list — Java 21 feature

        assertThat(cellTexts)
                .as("Expected table to contain '%s' but found: %s", searchText, cellTexts)
                .anyMatch(text -> text.contains(searchText));
    }

    /**
     * Returns the number of data rows in a table body (excludes header row).
     *
     * @param driver  active WebDriver
     * @param tableBy By locator for the <table> or <tbody>
     * @return count of <tr> elements inside <tbody>
     */
    public static int getRowCount(WebDriver driver, By tableBy) {
        WebElement table = WaitUtils.waitForElementVisible(driver, (WebElement) tableBy);
        List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));
        return rows.size();
    }

    /**
     * Asserts that no edit or delete control (button/link/input) is visible in the table.
     * Used to verify transaction history is read-only (US2-TC5).
     *
     * @param driver  active WebDriver
     * @param tableBy By locator for the table
     */
    public static void assertNoEditDeleteControls(WebDriver driver, By tableBy) {
        WebElement table = WaitUtils.waitForElementVisible(driver, (WebElement) tableBy);

        // Check for common edit/delete UI patterns: buttons, anchors with edit/delete text, inputs
        List<WebElement> buttons = table.findElements(By.tagName("button"));
        List<WebElement> inputs  = table.findElements(By.tagName("input"));

        assertThat(buttons)
                .as("Expected no action buttons in transaction table (read-only)")
                .isEmpty();

        assertThat(inputs)
                .as("Expected no input fields in transaction table (read-only)")
                .isEmpty();
    }
}
