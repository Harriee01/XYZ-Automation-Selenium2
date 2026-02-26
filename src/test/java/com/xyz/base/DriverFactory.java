package com.xyz.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


//DriverFactory — creates and configures ChromeDriver instances.
//final because it cannot be extended
public final class DriverFactory {
//Creates a ChromeDriver instance.
// CI/headless mode is controlled by the system property "headless"
public static WebDriver createChromeDriver() {
    // WebDriverManager resolves and downloads correct chromedriver binary
    WebDriverManager.chromedriver().setup();//automatically detects the browser version and downloads the matching driver

    ChromeOptions options = buildChromeOptions();
    return new ChromeDriver(options);// Creates a new ChromeDriver instance with the specified options
}

    /**
     * Builds ChromeOptions with sensible defaults.
     * Headless flag is read from system property to support both local and CI runs.
     */
    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();//create new ChromeOptions to configure the browser behaviour

        // Headless mode: no visible browser window — required in GitHub Actions

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless=new");   // "new" headless = Chrome 112+ improved mode to use newer headless mode
            options.addArguments("--no-sandbox");     // Required in some CI environments to run Chrome without sandboxing
            options.addArguments("--disable-dev-shm-usage"); // Prevents shared memory issues
        }

        // Consistent window size regardless of headless/headed — avoids responsive layout differences
        options.addArguments("--window-size=1920,1080");

        // Disable Chrome's automation infobar ("Chrome is being controlled by automated test software")
        // Prevents it from overlapping elements in headed mode that may interfere with tests
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        return options;
    }

}
