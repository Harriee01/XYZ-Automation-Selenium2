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
    void setUp() {
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
        if (driver != null) {
            // Attach screenshot to Allure report on failure for easy debugging
            takeScreenshotForAllure();
            driver.quit();  // always quit — prevents orphaned chromedriver.exe processes
        }

        //Captures a screenshot and attaches it to the current Allure test result.
        protected void takeScreenshotForAllure() {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                // Allure.addAttachment streams the bytes into the report
                Allure.addAttachment("Screenshot", "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
            } catch (Exception e) {
                // Don't fail the test because of a screenshot failure
                // Log to stderr for CI visibility but swallow exception
                System.err.println("[BaseTest] Screenshot capture failed: " + e.getMessage());
            }
        }

    }


}
