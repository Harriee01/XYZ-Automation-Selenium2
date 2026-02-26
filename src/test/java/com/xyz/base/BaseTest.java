package com.xyz.base;

import com.xyz.models.TestData;
import io.qameta.allure.Allure;//attach screenshots to test reports to allure
import org.junit.jupiter.api.AfterEach;// Runs after each test method
import org.junit.jupiter.api.BeforeEach;// Runs before each test method
import org.openqa.selenium.OutputType;// Defines output types for screenshots
import org.openqa.selenium.TakesScreenshot;// Interface for taking screenshots
import org.openqa.selenium.WebDriver;

//BaseTest — parent class for all test classes

public abstract class BaseTest {
//Protected so subclasses (ManagerTests, CustomerTests) can use driver directly.

    protected WebDriver driver;

    //Runs BEFORE each individual @Test method
    @BeforeEach
    public void setUp() {
        // Create ChromeDriver via factory — headless if -Dheadless=true
        driver = DriverFactory.createChromeDriver();


        // Maximizing ensures consistent element visibility across machines.
        driver.manage().window().maximize();

        // Navigate to the app's login page
        driver.get(TestData.BASE_URL);
    }

    //runs AFTER each @Test method — even if the test throws an exception

    @AfterEach
    void tearDown() {
        // attach a final screenshot for post-test inspection in Allure

        try {
            // Check if WebDriver instance supports taking screenshots
            if (driver instanceof TakesScreenshot) {
                // Take screenshot as byte array
                // OutputType.BYTES keeps it in memory
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("Final screenshot", "image/png", "png", png);
            }
        } catch (Exception ignored) {
            // Best-effort only; never block teardown
            // If screenshot fails, test teardown continues
            // Prevents screenshot failures from masking test failures
        }

        // Always quit to release resources
        if (driver != null) {
            driver.quit(); // Closes all browser windows and ends WebDriver session
        }

    }


}
