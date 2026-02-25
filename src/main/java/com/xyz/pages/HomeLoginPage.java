package com.xyz.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Page Object for Home/Login page

public class HomeLoginPage extends BasePage {

    // Bank Manager Login Button
    // Using className: button has unique class "btn-primary" for bank manager
    @FindBy(className = "btn-primary")
    private WebElement bankManagerLoginBtn;

    // Customer Login Button
    // Using className: button has class "btn-default" for customer
    @FindBy(className = "btn-default")
    private WebElement customerLoginBtn;

    // Home Button (for navigation)
    @FindBy(className = "home")
    private WebElement homeBtn;

    public HomeLoginPage(WebDriver driver) {
        super(driver);
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

}


