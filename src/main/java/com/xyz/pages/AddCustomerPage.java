package com.xyz.pages;

import com.xyz.utils.AlertHandler;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


//Page Object for Add Customer form.
// * Handles customer creation with validation.

public class AddCustomerPage extends BasePage {


    // First Name input - has id="firstName"
    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    // Last Name input - has id="lastName"
    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    // Post Code input - has id="postCode"
    @FindBy(id = "postCode")
    private WebElement postCodeInput;

    // Add Customer button
    @FindBy(className = "btn-default")
    private WebElement addCustomerBtn;

    public AddCustomerPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter first name in the form
     * @param firstName customer's first name
     */
    @Step("Enter first name: {firstName}")
    public void enterFirstName(String firstName) {

        wait.until(d -> firstNameInput.isDisplayed());
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
    }

    /**
     * Enter last name in the form
     * @param lastName customer's last name
     */
    @Step("Enter last name: {lastName}")
    public void enterLastName(String lastName) {

        wait.until(d -> lastNameInput.isDisplayed());
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
    }

    /**
     * Enter post code in the form
     * @param postCode customer's post code
     */
    @Step("Enter post code: {postCode}")
    public void enterPostCode(String postCode) {

        wait.until(d -> postCodeInput.isDisplayed());
        postCodeInput.clear();
        postCodeInput.sendKeys(postCode);
    }

    /**
     * Click Add Customer button and handle alert
     * @return String alert message
     */
    @Step("Click Add Customer button")
    public String clickAddCustomer() {

        wait.until(d -> addCustomerBtn.isEnabled());
        addCustomerBtn.click();

        // AngularJS shows success/error via alert
        String alertText = AlertHandler.getAlertText(driver);
        AlertHandler.acceptAlert(driver);


        return alertText;
    }

    /**
     * Complete add customer flow in one step
     * @param firstName customer's first name
     * @param lastName customer's last name
     * @param postCode customer's post code
     * @return String alert message
     */
    @Step("Add customer: {firstName} {lastName} (PostCode: {postCode})")
    public String addCustomer(String firstName, String lastName, String postCode) {

        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostCode(postCode);
        return clickAddCustomer();
    }

    /**
     * Check if form is displayed
     * @return boolean true if form visible
     */
    public boolean isFormDisplayed() {
        return wait.until(d -> firstNameInput.isDisplayed() &&
                lastNameInput.isDisplayed() &&
                postCodeInput.isDisplayed());
    }
}
