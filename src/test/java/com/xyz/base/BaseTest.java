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

}
