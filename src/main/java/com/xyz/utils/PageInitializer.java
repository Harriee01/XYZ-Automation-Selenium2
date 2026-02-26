package com.xyz.utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * PageInitializer — Helper utility for initializing page objects
 * Provides centralized WebDriver setup, explicit waits, and PageFactory initialization
 * Replaces inheritance-based BasePage for more flexible composition
 */
public class PageInitializer {

    // Default timeout constants (in seconds) for explicit waits
    public static final int DEFAULT_TIMEOUT = 10;
    public static final int EXTENDED_TIMEOUT = 15;

    private PageInitializer() {
        // Utility class - prevent instantiation
    }

    /**
     * Initialize a page object with WebDriver, waits, and PageFactory element locators
     * Call this from  page's constructor: PageInitializer.initializePage(driver, this);
     *
     * @param driver the active WebDriver instance
     * @param pageObject the page object instance to initialize
     * @return the initialized WebDriver for convenience
     */
    public static WebDriver initializePage(WebDriver driver, Object pageObject) {
        // Initialize PageFactory elements - enables @FindBy annotations
        PageFactory.initElements(driver, pageObject);
        return driver;
    }

    /**
     * Create a standard explicit wait for WebDriver operations
     * Default timeout: 10 seconds
     *
     * @param driver the active WebDriver instance
     * @return configured WebDriverWait object
     */
    public static WebDriverWait createWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    /**
     * Create an extended explicit wait for long-running operations
     * Extended timeout: 15 seconds (useful for slow AngularJS renders)
     *
     * @param driver the active WebDriver instance
     * @return configured WebDriverWait object
     */
    public static WebDriverWait createLongWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXTENDED_TIMEOUT));
    }
}
