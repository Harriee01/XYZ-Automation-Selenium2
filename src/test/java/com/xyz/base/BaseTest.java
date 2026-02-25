package com.xyz.base;

import com.xyz.config.DriverFactory;
import com.xyz.models.TestData;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

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

    //uns AFTER each @Test method — even if the test throws an exception

    @AfterEach
    void tearDown() {
        // attach a final screenshot for post-test inspection in Allure

        try {
            if (driver instanceof TakesScreenshot) {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.getLifecycle().addAttachment("Final screenshot", "image/png", "png", png);
            }
        } catch (Exception ignored) {
            // Best-effort only; never block teardown
        }

        // Always quit to release resources
        if (driver != null) {
            driver.quit();
        }

    }


}
