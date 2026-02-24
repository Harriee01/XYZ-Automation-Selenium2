package com.xyz.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.xyz.TestData;

import java.time.Duration;

// Utility class for handling explicit waits,providing reusable wait conditions for common scenarios
public class WaitUtils {

    private WaitUtils() {} // Private constructor for utility class

    /**
     * Wait for element to be visible
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @return WebElement when visible
     */
    public static WebElement waitForElementVisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestData.DEFAULT_TIMEOUT_SECONDS));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }


    /**
     * Wait for element to be clickable
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @return WebElement when clickable
     */
    public static WebElement waitForElementClickable(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestData.DEFAULT_TIMEOUT_SECONDS));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait  by checking for presence of text
     * @param driver WebDriver instance
     * @param element WebElement containing text
     * @param text Expected text
     * @return boolean true if text present
     */
    public static boolean waitForTextToBePresent(WebDriver driver, WebElement element, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestData.DEFAULT_TIMEOUT_SECONDS));
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Wait until an alert is present.
     * The app uses window.alert() for "Customer added successfully" feedback.
     *
     * @return
     */
    public static Alert waitForAlert(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestData.DEFAULT_TIMEOUT_SECONDS));
        return wait.until(ExpectedConditions.alertIsPresent());
    }

}
