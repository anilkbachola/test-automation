package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.FailureException;
import org.openqa.selenium.WebElement;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.List;

/**
 * Robot Keywords for locating, verifying values of elements in a page
 */
@RobotKeywords
public class ElementKeywords extends SeleniumBase {

    /**
     * Assigns the <b>id</b> to the element located by <b>locator</b>. <br>
     * <br>
     * This is useful in situations where we need to assign a temporary id to an element to refer it later.
     * <br>
     * Assigned id is temporary and will be reset if page refreshed or navigation happens
     * @param locator   the locator to locate the element
     * @param id    the id to assign
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "id" })
    public void assignIdToElement(String locator, String id) {
        List<WebElement> elements = findElement(locator,true,true);
        executeScript(String.format("arguments[0].id = '%s';", id), elements.get(0));
    }

    /**
     * @see ElementKeywords#elementTextShouldBe(String, String, String)
     * @param locator   the locator to find element
     * @param expected  the text to compare
     */
    @RobotKeywordOverload
    public void elementTextShouldBe(String locator, String expected) {
        elementTextShouldBe(locator, expected, "");
    }

    /**
     * Verify the element text identified by <b>locator</b> is equal to <b>text</b>. <br>
     * <br>
     * Returns the custom <b>message</b> if provided, otherwise returns default message.
     * <br>
     * @see ElementKeywords#elementTextShouldContain for contains() type of operation
     *
     * @param locator   the locator to find element
     * @param text  the text to compare
     * @param message   Optional. message to return on failure
     * @throws FailureException if element text not matches with <b>text</b> provided
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "text", "message=NONE" })
    public void elementTextShouldBe(String locator, String text, String message) {
        String actual = getElementText(locator);

        if (!text.equals(actual)) {
            if (message == null || message.isEmpty()) {
                throw new FailureException(
                        String.format("The text of element '%s' should have been '%s', but it was '%s'.", locator,
                                text, actual)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void elementTextShouldNotBe(String locator, String expected) {
        elementTextShouldNotBe(locator, expected, "");
    }

    /**
     * Verify the element text identified by <b>locator</b> is <u>not</u> equal to <b>text</b>. <br>
     * <br>
     * Returns the custom <b>message</b> if provided, otherwise returns default message.
     * <br>
     * @see ElementKeywords#elementTextShouldNotContain for contains() type of operation
     *
     * @param locator   the locator to find element
     * @param text  the text to compare
     * @param message   Optional. message to return on failure
     * @throws FailureException if element text matches with <b>text</b> provided
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "text", "message=NONE" })
    public void elementTextShouldNotBe(String locator, String text, String message) {
        String actual = getElementText(locator);

        if (text.equals(actual)) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("The text of element '%s' should have been '%s', but it was '%s'.", locator,
                                text, actual)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void elementTextShouldContain(String locator, String text) {
        elementTextShouldContain(locator, text, "");
    }

    /**
     * Verify the element text identified by <b>locator</b> contains <b>text</b>. <br>
     * <br>
     * Returns the custom <b>message</b> if provided, otherwise returns default message.
     * <br>
     * @see ElementKeywords#elementTextShouldBe for equals() type of operation
     *
     * @param locator   the locator to find element
     * @param text  the text to compare
     * @param message   Optional. message to return on failure
     * @throws FailureException if element text does not contain the <b>text</b> provided
     */
    @RobotKeyword
    @ArgumentNames({"locator", "text", "message=NONE"})
    public void elementTextShouldContain(String locator, String text, String message) {
        String actual = getElementText(locator);
        if(actual == null || !actual.toLowerCase().contains(text.toLowerCase())) {
            throw new FailureException(
                    String.format("Element should have contained text '%s', but its text was %s.", text, actual));
        }
    }

    @RobotKeywordOverload
    public void elementTextShouldNotContain(String locator, String text) {
        elementTextShouldNotContain(locator, text, "");
    }

    /**
     * Verify the element text identified by <b>locator</b> does <u>not</u> contain <b>text</b>. <br>
     * <br>
     * Returns the custom <b>message</b> if provided, otherwise returns default message.
     * <br>
     * @see ElementKeywords#elementTextShouldNotBe for equals() type of operation
     *
     * @param locator   the locator to find element
     * @param text  the text to compare
     * @param message   Optional. message to return on failure
     * @throws FailureException if element text contains <b>text</b> provided
     */
    @RobotKeyword
    @ArgumentNames({"locator", "text", "message=NONE"})
    public void elementTextShouldNotContain(String locator, String text, String message) {
        String actual = getElementText(locator);
        if(actual != null && actual.toLowerCase().contains(text.toLowerCase())) {
            throw new FailureException(
                    String.format("Element should not have contained text '%s', but its text was %s.", text, actual));
        }
    }

    /**
     * Verify element located by the <b>locator</b> is enabled.<br>
     *
     * @param locator the locator to locate the element.
     *                @throws FailureException if element is disabled.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void elementShouldBeEnabled(String locator) {
        if (!isEnabled(locator)) {
            throw new FailureException(
                    String.format("Element '%s' is expected to be enabled, but found disabled.", locator));
        }
    }

    /**
     * Verify element located by the <b>locator</b> is disabled.<br>
     *
     * @param locator the locator to locate the element
     *                @throws FailureException if element is enabled.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void elementShouldBeDisabled(String locator) {
        if (isEnabled(locator)) {
            throw new FailureException(
                    String.format("Element '%s' is expcted to be disabled, but found enabled.", locator));
        }
    }

    @RobotKeywordOverload
    public void elementShouldBeSelected(String locator) {
        elementShouldBeSelected(locator, "");
    }

    /**
     * Verify element located by <b>locator</b> is selected.<br>
     *
     * @param locator   the locator to locate the element
     * @param message   Optional. message to return on failure.
     *                  @throws FailureException if element is not selected
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldBeSelected(String locator, String message) {
        boolean selected = isSelected(locator);

        if (!selected) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Element '%s' should be selected, but it is not.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void elementShouldNotBeSelected(String locator) {
        elementShouldNotBeSelected(locator, "");
    }

    /**
     * Verify element located by <b>locator</b> is <u>not</u> selected.<br>
     *
     * @param locator   the locator to locate the element
     * @param message   Optional. message to return on failure.
     *                  @throws FailureException if element is selected.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldNotBeSelected(String locator, String message) {
        boolean selected = isSelected(locator);

        if (selected) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Element '%s' should not be selected, but it is.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void elementShouldBeVisible(String locator) {
        elementShouldBeVisible(locator, "");
    }

    /**
     * Verify element located by <b>locator</b> is visible.<br>
     *
     * @param locator   the locator to locate the element
     * @param message   Optional. message to return on failure.
     *                  @throws FailureException if element is not visible.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldBeVisible(String locator, String message) {
        boolean visible = isVisible(locator);

        if (!visible) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Element '%s' should be visible, but it is not.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void elementShouldNotBeVisible(String locator) {
        elementShouldNotBeVisible(locator, "");
    }

    /**
     * Verify element located by <b>locator</b> is hidden.<br>
     *
     * @param locator   the locator to locate the element
     * @param message   Optional. message to return on failure.
     *                  @throws FailureException if element is visible.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldNotBeVisible(String locator, String message) {
        boolean visible = isVisible(locator);

        if (visible) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Element '%s' should not be visible, but it is.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void elementIsHidden(String locator) {
        elementIsHidden(locator, "");
    }

    /**
     * Verify element located by <b>locator</b> is hidden.<br>
     *
     * @param locator   the locator to locate the element
     * @param message   Optional. message to return on failure.
     *                  @throws FailureException if element is visible.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementIsHidden(String locator, String message) {
        elementShouldNotBeVisible(locator, message);
    }

    @RobotKeywordOverload
    public void elementShouldBeClickable(String locator) {
        elementShouldBeClickable(locator, "");
    }


    /**
     * Verify element located by <b>locator</b> is clickable.<br>
     *
     * @param locator   the locator to locate the element
     * @param message   Optional. message to return on failure.
     *                  @throws FailureException if element is not clickable.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldBeClickable(String locator, String message) {
        boolean clickable = isClickable(locator);

        if (!clickable) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Element '%s' should be clickable, but it is not.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void elementShouldNotBeClickable(String locator) {
        elementShouldNotBeClickable(locator, "");
    }

    /**
     * Verify element located by <b>locator</b> is <u>not</u> clickable.<br>
     *
     * @param locator   the locator to locate the element
     * @param message   Optional. message to return on failure.
     *                  @throws FailureException if element is clickable.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldNotBeClickable(String locator, String message) {
        boolean clickable = isClickable(locator);

        if (clickable) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Element '%s' should not be clickable, but it is.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    /**
     * Return the element text located by <b>locator</b>.<br>
     *
     * @param locator   the locator to locate the element
     * @return  element text if the element is found.
     * @throws FailureException if element is not found.
     */
    @RobotKeyword
    @ArgumentNames({"locator"})
    public String getElementText(String locator) {
        List<WebElement> elements = findElement(locator, true, true);
        if(elements.isEmpty()) {
            return null;
        }
        return elements.get(0).getText();
    }

    /**
     * Clears the element text located by <b>locator</b>.<br>
     *
     * @param locator   the locator to locate the element
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void clearElementText(String locator) {
        List<WebElement> elements = findElement(locator, true, true);

        elements.get(0).clear();
    }

    /**
     * Returns the horizontal position of the element located by <b>locator</b>.<br>
     *
     *
     * @param locator the locator to locate the element.
     * @return  horizontal position of the element.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public int getHorizontalPosition(String locator) {
        List<WebElement> elements = findElement(locator, true, false);

        if (elements.isEmpty()) {
            throw new FailureException(
                    String.format("Could not determine position for '%s'.", locator));
        }

        return elements.get(0).getLocation().getX();
    }

    /**
     * Returns the vertical position of the element located by <b>locator</b>.<br>
     *
     *
     * @param locator the locator to locate the element.
     * @return  vertical position of the element.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public int getVerticalPosition(String locator) {
        List<WebElement> elements = findElement(locator, true, false);

        if (elements.isEmpty()) {
            throw new FailureException(
                    String.format("Could not determine position for '%s'.", locator));
        }
        return elements.get(0).getLocation().getY();
    }

    /**
     * Return the element value located by <b>locator</b>. <br>
     * @param locator   the locator to locate the element.
     * @return  value of the element
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public String getValue(String locator) {
        return getValue(locator, null);
    }

    private String getValue(String locator, String tag) {
        List<WebElement> elements = findElement(locator, tag, true, false);

        if (elements.isEmpty()) {
            return null;
        }

        return elements.get(0).getAttribute("value");
    }

    private boolean isVisible(String locator) {
        List<WebElement> elements = findElement(locator, true, false);
        if (elements.isEmpty()) {
            return false;
        }
        WebElement element = elements.get(0);
        return element.isDisplayed();
    }

    private boolean isClickable(String locator) {
        List<WebElement> elements = findElement(locator, true, false);
        if (elements.isEmpty()) {
            return false;
        }
        WebElement element = elements.get(0);
        return element.isDisplayed() && element.isEnabled();
    }

    private boolean isSelected(String locator) {
        List<WebElement> elements = findElement(locator, true, false);
        if (elements.isEmpty()) {
            return false;
        }
        WebElement element = elements.get(0);
        return element.isSelected();
    }

    private boolean isEnabled(String locator) {
        List<WebElement> elements = findElement(locator, true, true);
        WebElement element = elements.get(0);

        //TODO: implement importing form elements.

        if (!element.isEnabled()) {
            return false;
        }
        String readonly = element.getAttribute("readonly");
        return readonly != null && (readonly.equals("readonly") || readonly.equals("true"));

        /*return true;*/
    }



}
