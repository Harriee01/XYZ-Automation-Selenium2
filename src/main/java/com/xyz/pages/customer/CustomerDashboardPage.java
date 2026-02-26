package com.xyz.pages.customer;

// Import PageInitializer utility for creating WebDriverWait instances
import com.xyz.utils.PageInitializer;// for creating WebDriverWait instances with consistent timeouts across pages
import org.openqa.selenium.By;// Used for locating elements by different strategies
import org.openqa.selenium.WebDriver;// Main interface for browser interaction
import org.openqa.selenium.WebElement;// Represents a single HTML element on the page
import org.openqa.selenium.support.FindBy;// Annotation for Page Factory pattern (element initialization)
import org.openqa.selenium.support.PageFactory;// Initializes Page Object elements
import org.openqa.selenium.support.ui.WebDriverWait;// Provides explicit wait functionality

// Import Allure annotation for reporting and test documentation
import io.qameta.allure.Step;

//Java utilities for working with collections and text processing
import java.util.List; // For handling lists of web elements
import java.util.regex.Matcher;// For pattern matching (regex)
import java.util.regex.Pattern; // For defining regex patterns

//Page Object for Customer Dashboard.
// * Shows account overview and navigation to transactions/deposit/withdraw

public class CustomerDashboardPage {
//accessible in this package and subclasses
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Welcome message with customer name

    @FindBy(xpath = "//span[@class='fontBig ng-binding']")
    private WebElement welcomeMessage;

    // Account number dropdown
    @FindBy(id = "accountSelect")
    private WebElement accountSelect;

    // Text on the page that contains the "Balance :" label and value
    private static final By BALANCE_TEXT_LOCATOR =
            By.xpath("//*[contains(normalize-space(.), 'Balance :')]");
    private static final Pattern BALANCE_PATTERN =
            Pattern.compile("Balance\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");//group1 captures the numeric balance value after "Balance :"

    // Transactions button

    @FindBy(xpath = "//button[@ng-click='transactions()']")
    private WebElement transactionsBtn;

    // Deposit button

    @FindBy(xpath = "//button[@ng-click='deposit()']")
    private WebElement depositBtn;

    // Withdraw button -  the typo 'withdrawl' matches the actual app HTML

    @FindBy(xpath = "//button[@ng-click='withdrawl()']")
    private WebElement withdrawBtn;

    //This constructor initializes the page object when the class is instantiated
    public CustomerDashboardPage(WebDriver driver) {
        this.driver = driver;// assign the parameter driver to the instance variable for use in methods
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageFactory.initElements(driver, this);//this finds and assigns elements annotated with @FindBy to the corresponding fields in this class
    }

    /**
     * Get welcome message text
     * @return String welcome message
     */
    //The @Step annotation makes this method a step in Allure reports
    @Step("Get welcome message")
    public String getWelcomeMessage() {
        //wait until welcome message is displayed to ensure the page has loaded and the element is present before trying to get its text
        wait.until(d -> welcomeMessage.isDisplayed());
        String message = welcomeMessage.getText();

        return message;
    }

    /**
     * Get current account balance as double
     * @return double current balance
     */
    @Step("Get current balance")
    public double getBalanceAsDouble() {
        // Wait until some element containing "Balance :" is present
        WebElement balanceElement = wait.until(d -> {
            List<WebElement> els = d.findElements(BALANCE_TEXT_LOCATOR);// find and return the first element that contains "Balance :" in its text matching the BALANCE_TEXT_LOCATOR.
            // If no such element is found, it returns null else it gives the first term
            return els.isEmpty() ? null : els.get(0);
        });

        // Get text content from the balance element
        String text = balanceElement.getText();

        // Apply regex pattern to extract numeric balance value
        Matcher matcher = BALANCE_PATTERN.matcher(text);
        if (!matcher.find()) {
            throw new org.openqa.selenium.NoSuchElementException(
                    "Could not parse Balance from text: " + text);
        }
        // Extract captured number from regex group(1) and convert to double
        // group(1) refers to the number part in parentheses in regex
        return Double.parseDouble(matcher.group(1));
    }

    /**
     * Click Transactions button
     * @return TransactionsPage instance
     */
    @Step("Click Transactions button")
    public TransactionsPage clickTransactions() {
        // Wait until transactions button is enabled
        wait.until(d -> transactionsBtn.isEnabled());
        transactionsBtn.click();
        // Return new TransactionsPage object (navigating to new page)
        // This enables method chaining (calling multiple methods one after another on the same line)
        return new TransactionsPage(driver);
    }

    /**
     * Click Deposit button
     * @return DepositPage instance
     */
    @Step("Click Deposit button")
    public DepositPage clickDeposit() {
        // Wait until deposit button is enabled
        wait.until(d -> depositBtn.isEnabled());
        depositBtn.click();
        // Return new DepositPage object (navigating to new page)
        // This enables method chaining in tests
        return new DepositPage(driver);
    }

    /**
     * Click Withdraw button
     * @return WithdrawPage instance
     */
    @Step("Click Withdraw button")
    public WithdrawPage clickWithdraw() {
        // Wait until withdraw button is enabled
        wait.until(d -> withdrawBtn.isEnabled());
        withdrawBtn.click();
        // Return new WithdrawPage object (navigating to new page)
        // This enables method chaining in tests
        return new WithdrawPage(driver);
    }

    /**
     * Opens the account dropdown and picks one of the options by its position number.
     * @param index account index
     */
    @Step("Select account at index: {index}")
    public void selectAccount(int index) {
        // Wait until account dropdown is displayed
        wait.until(d -> accountSelect.isDisplayed());

        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(accountSelect); // Create Select object, a special Selenium wrapper, to work with HTML

        select.selectByIndex(index); // Select option by its index in the dropdown

        // After selecting an account, just ensure the dropdown is still interactable.
        wait.until(d -> accountSelect.isDisplayed());
    }

    /**
     * Counts how many account options are inside the dropdown and returns that number.
     * @return int number of accounts
     */
    public int getAccountCount() {
        // Wait until account dropdown is displayed
        wait.until(d -> accountSelect.isDisplayed());

        // Create Select object to work with HTML dropdown
        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(accountSelect);
        // Get all options from dropdown and return count
        return select.getOptions().size();
    }

    /**
     * Get the current page title,
     * @return the title of the current page
     */
    public String getPageTitle() {
        // Return browser's current page title using WebDriver
        return driver.getTitle();
    }

    /**
     * Get the current page URL
     * @return the URL of the current page
     */
    public String getCurrentUrl() {
        // Return browser's current URL using WebDriver
        return driver.getCurrentUrl();
    }
}
