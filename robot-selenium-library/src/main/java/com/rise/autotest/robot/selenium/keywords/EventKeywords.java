package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.FailureException;
import com.rise.autotest.robot.selenium.locator.ElementTag;
import com.rise.autotest.robot.selenium.locator.StdTags;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.List;

/**
 * Robot Keywords to manage Mouse and keyboard events
 */
@RobotKeywords
public class EventKeywords extends SeleniumBase {

    /**
     * Click an element located by the <b>locator</b>. <br>
     *
     * @param locator the locator to locate the element.
     * @throws FailureException if element is not found.
     */
    @RobotKeyword
    @ArgumentNames({"locator"})
    public void clickElement(String locator) {
        List<WebElement> elements = findElement(locator, true, true);

        elements.get(0).click();
    }

    /**
     * Click an element located by <b>locator</b> at <b>xOffset</b> and <b>yOffset</b> location. <br>
     *
     * @param locator   the locator to locate the element.
     * @param xOffset   horizontal offset.
     * @param yOffset   vertical offset.
     * @throws FailureException if element is not found.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "xOffset", "yOffset" })
    public void clickElementAtCoordinates(String locator, String xOffset, String yOffset) {
        List<WebElement> elements = findElement(locator, true, true);
        WebElement element = elements.get(0);
        Actions actions = new Actions(driverManager.getCurrent());
        actions.moveToElement(element)
                .moveByOffset(Integer.parseInt(xOffset), Integer.parseInt(yOffset))
                .click()
                .perform();
    }

    /**
     * Double-Click an element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if element is not found.
     */
    @RobotKeyword
    @ArgumentNames({"locator"})
    public void doubleClickElement(String locator) {
        List<WebElement> elements = findElement(locator, true, true);
        Actions actions = new Actions(driverManager.getCurrent());
        actions.doubleClick(elements.get(0)).perform();
    }

    /**
     * Focus an element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if element is not found.
     */
    @RobotKeyword
    @ArgumentNames({"locator"})
    public void focus(String locator) {
        List<WebElement> elements = findElement(locator, true, true);
        executeScript("arguments[0].focus();", elements.get(0));
    }

    /**
     * Perform Drag-drop from source element located by <b>source</b> to target element located by <b>target</b>.
     * <br>
     *
     * @param source    the locator to locate the source element.
     * @param target    the locator to locate the target element.
     * @throws FailureException if either source or target element is not found.
     */
    @RobotKeyword
    @ArgumentNames({"source", "target"})
    public void dragAndDrop(String source, String target) {
        List<WebElement> sourceElements = findElement(source, true, true);
        List<WebElement> targetElements = findElement(target, true, true);
        Actions actions = new Actions(driverManager.getCurrent());
        actions.dragAndDrop(sourceElements.get(0), targetElements.get(0)).perform();
    }

    /**
     * Perform Drag-drop from source element located by <b>source</b> to target element located by <b>xOffset</b>
     * and <b>yOffset</b>.<br>
     *
     * @param source    the locator to locate the source element.
     * @param xOffset   target xOffset location.
     * @param yOffset   target yOffset location.
     * @throws FailureException if either source or target element is not found.
     */
    @RobotKeyword
    @ArgumentNames({ "source", "xOffset", "yOffset" })
    public void dragAndDropByOffset(String source, int xOffset, int yOffset) {
        List<WebElement> elements = findElement(source, true, true);

        Actions actions = new Actions(driverManager.getCurrent());
        actions.dragAndDropBy(elements.get(0), xOffset, yOffset).perform();
    }

    /**
     * Mouse-down on an element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if the element is not found.
     */
    @RobotKeyword
    @ArgumentNames({"locator"})
    public void mouseDown(String locator) {
        List<WebElement> elements = findElement(locator, true, true);
        Actions actions = new Actions(driverManager.getCurrent());
        actions.clickAndHold(elements.get(0)).perform();
    }

    /**
     * Mouse-up from an element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if the element is not found.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void mouseUp(String locator) {
        List<WebElement> elements = findElement(locator, true, true);

        WebElement element = elements.get(0);
        Actions actions = new Actions(driverManager.getCurrent());
        actions.release(element).perform();
    }

    /**
     * Mouse-over on an element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if the element is not found.
     */
    @RobotKeyword
    @ArgumentNames({"locator"})
    public void mouseOver(String locator) {
        List<WebElement> elements = findElement(locator, true, true);
        Actions actions = new Actions(driverManager.getCurrent());
        actions.moveToElement(elements.get(0)).perform();
    }

    /**
     * Mouse-out from an element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if the element is not found.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void mouseOut(String locator) {
        List<WebElement> elements = findElement(locator, true, true);

        WebElement element = elements.get(0);
        Dimension size = element.getSize();
        int offsetX = size.getWidth() / 2 + 1;
        int offsetY = size.getHeight() / 2 + 1;

        Actions actions = new Actions(driverManager.getCurrent());
        actions.moveToElement(element).moveByOffset(offsetX, offsetY).perform();
    }

    /**
     * Open Context menu on the element located by the  <b>locator</b>. <br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if the element is not found.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void openContextMenu(String locator) {
        List<WebElement> elements = findElement(locator, true, true);

        Actions actions = new Actions(driverManager.getCurrent());
        actions.contextClick(elements.get(0)).perform();
    }

    /**
     * Click a link located by the <b>locator</b>.<br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if the element is not found.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void clickLink(String locator) {
        List<WebElement> elements = findElement(locator, StdTags.LINK.name(), true, true );

        elements.get(0).click();
    }

    /**
     * Mouse-down on a link located by the <b>locator</b>.<br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if the element is not found.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void mouseDownOnLink(String locator) {
        List<WebElement> elements = findElement(locator, StdTags.LINK.name(), true, true);

        Actions actions = new Actions(driverManager.getCurrent());
        actions.clickAndHold(elements.get(0)).perform();
    }

    /**
     * Click an image located by the <b>locator</b>.<br>
     *
     * @param locator   the locator to locate the element.
     * @throws FailureException if the element is not found.
     */
    @RobotKeyword
    @ArgumentNames({ "locator" })
    public void clickImage(String locator) {

        List<WebElement> elements = findElement(locator, StdTags.IMAGE.name(), true, true);

        if (elements.isEmpty()) {
            elements = findElement(locator, StdTags.BUTTON.name(), true, true);
        }
        WebElement element = elements.get(0);
        element.click();
    }

    /**
     * Send input or keys <b>key</b> to the element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator to locate the element.
     * @param key   the input keys to send.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "key" })
    public void sendKeys(String locator, String key) {
        if (key.startsWith("\\") && key.length() > 1) {
            key = mapAsciiKeyCodeToKey(Integer.parseInt(key.substring(1))).toString();
        }
        List<WebElement> element = findElement(locator, true, false);
        element.get(0).sendKeys(key);
    }

    /**
     * Simulate a javascript/dom <b>event</b> like 'click', 'mouseOver' etc. on an element located by the <b>locator</b>.
     * <br>
     *
     * @param locator   the locator to locate the element.
     * @param event the javascript/dom event name.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "event" })
    public void simulateEvent(String locator, String event) {
        List<WebElement> elements = findElement(locator, true, true);

        String script = "element = arguments[0];" + "eventName = arguments[1];" + "if (document.createEventObject) {"
                + "return element.fireEvent('on' + eventName, document.createEventObject());" + "}"
                + "var evt = document.createEvent(\"HTMLEvents\");" + "evt.initEvent(eventName, true, true);"
                + "return !element.dispatchEvent(evt);";

        executeScript(script, elements.get(0), event);
    }


    private CharSequence mapAsciiKeyCodeToKey(int keyCode) {
        switch (keyCode) {
            case 0:
                return Keys.NULL;
            case 8:
                return Keys.BACK_SPACE;
            case 9:
                return Keys.TAB;
            case 10:
                return Keys.RETURN;
            case 13:
                return Keys.ENTER;
            case 24:
                return Keys.CANCEL;
            case 27:
                return Keys.ESCAPE;
            case 32:
                return Keys.SPACE;
            case 42:
                return Keys.MULTIPLY;
            case 43:
                return Keys.ADD;
            case 44:
                return Keys.SEPARATOR;
            case 45:
                return Keys.SUBTRACT;
            case 56:
                return Keys.DECIMAL;
            case 57:
                return Keys.DIVIDE;
            case 59:
                return Keys.SEMICOLON;
            case 61:
                return Keys.EQUALS;
            case 127:
                return Keys.DELETE;
            default:
                return String.valueOf((char) keyCode);
        }
    }
}
