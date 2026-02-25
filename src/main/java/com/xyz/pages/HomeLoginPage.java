package com.xyz.pages;

import com.xyz.pages.customer.CustomerSelectPage;
import com.xyz.pages.manager.ManagerHomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;
import com.xyz.utils.PageInitializer;

//Page Object for Home/Login page

public class HomeLoginPage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;

    // Bank Manager Login Button
    // Using className: button has unique class "btn-primary" for bank manager
    @FindBy(xpath = "//button[normalize-space()='Bank Manager Login']")
    private WebElement bankManagerLoginBtn;

    // Customer Login Button
    // Using xpath
    @FindBy(xpath = "//button[normalize-space()='Customer Login']")
    private WebElement customerLoginBtn;

    // Home Button (for navigation)
    @FindBy(xpath = "//button[@class='btn home']")
    private WebElement homeBtn;

    public HomeLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = PageInitializer.createWait(driver);
        this.longWait = PageInitializer.createLongWait(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Click Bank Manager Login button
     * @return ManagerHomePage instance
     */
    @Step("Click Bank Manager Login button")
    public ManagerHomePage clickManagerLogin() {
        wait.until(d -> bankManagerLoginBtn.isDisplayed() && bankManagerLoginBtn.isEnabled());
        bankManagerLoginBtn.click();
        // Return new instance of ManagerHomePage
        return new ManagerHomePage(driver);
    }

    /**
      * Click Customer Login button
     * @return CustomerSelectPage instance
     */
    @Step("Click Customer Login button")
    public CustomerSelectPage clickCustomerLogin() {
        wait.until(d -> customerLoginBtn.isDisplayed() && customerLoginBtn.isEnabled());
        customerLoginBtn.click();
        // Return new instance of CustomerSelectPage
        return new CustomerSelectPage(driver);
    }

    /**
     * Navigate to home page
     */
    @Step("Navigate to home page")
    public void goToHome() {

        wait.until(d -> homeBtn.isDisplayed());
        homeBtn.click();
    }

    /**
     * Check if we're on home page
     * @return boolean true if home button is displayed
     */
    public boolean isOnHomePage() {
        return wait.until(d -> homeBtn.isDisplayed());
    }

    /**
     * Get the current page title
     * @return the title of the current page
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get the current page URL
     * @return the URL of the current page
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

}


