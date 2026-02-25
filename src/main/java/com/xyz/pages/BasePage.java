package com.xyz.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

//All page objects should extend this class
//Base page provides common functionality and driver management

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;
    
    // Default timeout constants (in seconds) for explicit waits
    protected static final int DEFAULT_TIMEOUT = 10;
    protected static final int EXTENDED_TIMEOUT = 15;

    //Constructor initializes WebDriver, waits, and PageFactory elements
    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Initialize explicit waits with different timeouts for flexibility
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(EXTENDED_TIMEOUT));

        // Initialize PageFactory elements - enables @FindBy annotations
        PageFactory.initElements(driver, this);

    }

    /**
     * Get current page title
     * @return String page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     * @return String current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}