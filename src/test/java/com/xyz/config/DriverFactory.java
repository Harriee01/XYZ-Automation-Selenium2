package com.xyz.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


//DriverFactory — creates and configures ChromeDriver instances.
public final class DriverFactory {
//Creates a ChromeDriver instance.
// CI/headless mode is controlled by the system property "headless"
public static WebDriver createChromeDriver() {
    // WebDriverManager resolves and downloads correct chromedriver binary
    WebDriverManager.chromedriver().setup();

    ChromeOptions options = buildChromeOptions();
    return new ChromeDriver(options);
}

    /**
     * Builds ChromeOptions with sensible defaults.
     * Headless flag is read from system property to support both local and CI runs.
     */
    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Headless mode: no visible browser window — required in Docker/GitHub Actions
        // Controlled via -Dheadless=true Maven/JVM property so local devs see the browser
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless=new");   // "new" headless = Chrome 112+ improved mode
            options.addArguments("--no-sandbox");     // Required in Docker root user environments
            options.addArguments("--disable-dev-shm-usage"); // Prevents shared memory issues in containers
        }

        // Consistent window size regardless of headless/headed — avoids responsive layout differences
        options.addArguments("--window-size=1920,1080");

        // Disable Chrome's automation infobar ("Chrome is being controlled by automated test software")
        // Prevents it from overlapping elements in headed mode
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        return options;
    }

}
