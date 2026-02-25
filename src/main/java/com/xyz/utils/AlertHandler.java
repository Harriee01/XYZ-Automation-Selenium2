package com.xyz.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;


// AlertHandler — encapsulates all browser alert (window.alert / window.confirm) interaction
public class AlertHandler {

    private AlertHandler() {}

    /**
     * Waits for a browser alert to be present, captures its text, then accepts (clicks OK).
     *
     * @param driver the current WebDriver session
     * @return the alert message text (for assertion in tests)
     */
    public static String acceptAndGetText(WebDriver driver) {
        // Poll until alert appears — AngularJS may post-process before showing it
        WaitUtils.waitForAlert(driver);

        Alert alert = driver.switchTo().alert();
        String text = alert.getText();  // capture BEFORE accepting — alert dismissed after accept()
        alert.accept();                 // dismiss alert; returns control to main window
        return text;
    }

    /**
     * Dismisses (cancels) an alert and returns its message.
     * Useful for confirm dialogs where we want to verify cancel behaviour.
     */
    public static String dismissAndGetText(WebDriver driver) {
        WaitUtils.waitForAlert(driver);
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        alert.dismiss();
        return text;
    }

    /**
     * Waits for alert and gets its text without accepting (for inspection).
     * Note: Alert will still be present after this call — must be accepted/dismissed later.
     *
     * @param driver the current WebDriver session
     * @return the alert message text
     */
    public static String getAlertText(WebDriver driver) {
        // Wait for alert to appear
        try {
            WaitUtils.waitForAlert(driver);
            Alert alert = driver.switchTo().alert();
            return alert.getText();
        } catch (Exception e) {
            // No alert present — return empty string for graceful handling
            return "";
        }
    }

    /**
     * Accepts (clicks OK on) the currently displayed alert.
     * Use this after capturing alert text if you called getAlertText() separately.
     *
     * @param driver the current WebDriver session
     */
    public static void acceptAlert(WebDriver driver) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();  // Dismiss alert by clicking OK
        } catch (Exception e) {
            // No alert present — safe to continue (may have been auto-dismissed)
        }
    }
}