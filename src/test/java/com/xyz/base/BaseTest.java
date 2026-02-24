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
}
