package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.FailureException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriverException;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

/**
 * Robot keywords to manager browser window.
 */
@RobotKeywords
public class WindowKeywords extends SeleniumBase {

    /**
     * Close the current window.
     */
    @RobotKeyword
    public void closeWindow() {
        if( driverManager.getCurrent() != null ) {
            driverManager.getCurrent().close();
        }
    }

    /**
     * Maximize the current window.
     */
    @RobotKeyword
    public void maximizeWindow() {
        if( driverManager.getCurrent() != null ) {
            driverManager.getCurrent().manage().window().maximize();
        }
    }

    /**
     * Get current window size.
     * @return  width and height of the current window
     */
    @RobotKeyword
    public Object[] getWindowSize() {
        if( driverManager.getCurrent() != null ) {
            Dimension size = driverManager.getCurrent().manage().window().getSize();
            return new Object[] { Integer.toString(size.width), Integer.toString(size.height) };
        }
        return new Object[]{};
    }

    /**
     * Set the <b>width</b> and <b>height</b> to the current widnow.
     * @param width the width to set.
     * @param height    the height to set.
     */
    @RobotKeyword
    @ArgumentNames({ "width", "height" })
    public void setWindowSize(String width, String height) {
        driverManager.getCurrent().manage().window()
                .setSize(new Dimension(Integer.parseInt(width), Integer.parseInt(height)));
    }

    /**
     * Return current window location.
     * @return  the window location.
     */
    @RobotKeyword
    public String getLocation() {
        return driverManager.getCurrent().getCurrentUrl();
    }

    /**
     * Verify the current window location is equals to <b>url</b>. <br>
     *
     * @param url   the expected URL.
     */
    @RobotKeyword
    @ArgumentNames({ "url" })
    public void locationShouldBe(String url) {
        String actual = getLocation();
        if (!actual.equals(url)) {
            throw new FailureException(
                    String.format("Location should have been '%s', but was '%s'", url, actual));
        }
    }

    /**
     * Verify the current window location contains <b>url</b>. <br>
     * @param url   the expected URL part.
     */
    @RobotKeyword
    @ArgumentNames({ "url" })
    public void locationShouldContain(String url) {
        String actual = getLocation();
        if (!actual.contains(url)) {
            throw new FailureException(
                    String.format("Location should have contained '%s', but was '%s'", url, actual));
        }
    }

    @RobotKeywordOverload
    public void alertShouldBePresent() {
        alertShouldBePresent("");
    }

    /**
     * Verify an alert is present and dismiss it.<br>
     * <br>
     * If <b>text</b> is a non-empty string, then it is also verified that the
     * message of the alert equals to text.<br>
     * <br>
     * Will fail if no alert is present. Note that following keywords will fail
     * unless the alert is confirmed by this keyword or another like `Confirm
     * Action`.<br>
     *
     * @param text
     *            Default=NONE. The alert message to verify.
     */
    @RobotKeyword
    @ArgumentNames({ "text=NONE" })
    public void alertShouldBePresent(String text) {
        String alertText = confirmAction("OK");
        if (text != null && !alertText.equals(text)) {
            throw new FailureException(String.format("Alert text should have been '%s' but was '%s'",
                    text, alertText));
        }
    }

    /**
     * Dismisses currently shown confirmation dialog and returns its message.<br>
     * <br>
     * Chooses 'OK' or 'Cancel' option from the dialog based on the <b>action</b>
     *
     * @param action the action to choose ('OK' or 'Cancel')
     * @return  The dialog message.
     */
    @RobotKeyword
    @ArgumentNames({"action"})
    public String confirmAction(String action) {
        try {
            Alert alert = driverManager.getCurrent().switchTo().alert();
            String text = alert.getText().replace("\n", "");
            if ("OK".equalsIgnoreCase(action)) {
                alert.accept();
            } else {
                alert.dismiss();
            }

            return text;
        } catch (WebDriverException wde) {
            throw new FailureException("There were no alerts");
        }
    }
}
