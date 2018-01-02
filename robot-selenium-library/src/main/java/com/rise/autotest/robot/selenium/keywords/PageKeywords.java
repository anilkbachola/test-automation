package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.FailureException;
import com.rise.autotest.robot.selenium.locator.ElementTag;
import com.rise.autotest.robot.selenium.locator.StdTags;
import com.rise.autotest.robot.selenium.util.Util;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.List;

@RobotKeywords
public class PageKeywords extends SeleniumBase {

    /**
     * Returns the current page source.
     * @return page source.
     */
    @RobotKeyword
    public String getSource() {
        return driverManager.getCurrent().getPageSource();
    }

    /**
     * Returns the current page title.
     * @return page title.
     */
    @RobotKeyword
    public String getTitle() {
        return driverManager.getCurrent().getTitle();
    }

    /**
     * Reload current page.
     */
    @RobotKeyword
    public void reloadPage() {
        driverManager.getCurrent().navigate().refresh();
    }

    /**
     * Verify the page title to contain the <b>title</b>. <br>
     *
     * @param title the expected title.
     */
    @RobotKeyword
    @ArgumentNames({ "title" })
    public void titleShouldContain(String title) {
        String actual = getTitle();
        if (!actual.contains(title)) {
            throw new FailureException(
                    String.format("Title should have contained '%s', but was '%s'", title, actual));
        }
    }

    /**
     * Verify the page title to not contain the <b>title</b>. <br>
     * @param title expected title.
     */
    @RobotKeyword
    @ArgumentNames({ "title" })
    public void titleShouldNotContain(String title) {
        String actual = getTitle();
        if (actual.contains(title)) {
            throw new FailureException(
                    String.format("Title should not have contained '%s', but was '%s'", title, actual));
        }
    }

    /**
     * Verify the current page title should be equal to <b>title</b>. <br>
     *
     * @param title expected title.
     */
    @RobotKeyword
    @ArgumentNames({ "title" })
    public void titleShouldBe(String title) {
        String actual = getTitle();
        if (!actual.equals(title)) {
            throw new FailureException(
                    String.format("Title should have been '%s', but was '%s'", title, actual));
        }
    }

    /**
     * Verify the current page title is not equal to <b>title</b>. <br>
     * @param title expected title.
     */
    @RobotKeyword
    @ArgumentNames({ "title" })
    public void titleShouldNotBe(String title) {
        String actual = getTitle();
        if (actual.equals(title)) {
            throw new FailureException(
                    String.format("Title should not have been '%s', but was '%s'", title, actual));
        }
    }

    /**
     * Verify the current page contains the given <b>text</b>. <br>
     *
     * @param text the text to verify.
     */
    @RobotKeyword
    @ArgumentNames({"text"})
    public void pageShouldContainText(String text) {
        if(!isPageContainsText(text)) {
            throw new FailureException(String.format("Page should have contained text '%s' but did not.", text));
        }
    }

    /**
     * Verify the current page does not contain the given <b>text</b>. <br>
     *
     * @param text the text to verify.
     */
    @RobotKeyword
    @ArgumentNames({"text"})
    public void pageShouldNotContainText(String text) {
        if(isPageContainsText(text)) {
            throw new FailureException(String.format("Page should not have contained text '%s' but did.", text));
        }
    }

    @RobotKeywordOverload
    public void pageShouldContainElement(String locator) {
        pageShouldContainElement(locator,null, "");
    }

    /**
     * Verify the current page contains the given element with optional <b>tag</b> and located by the <b>locator</b>.
     * <br>
     *
     * @param locator   the locator string.
     * @param tag   Optional tag name.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({"locator", "tag=NONE", "message=NONE"})
    public void pageShouldContainElement(String locator, String tag, String message) {
        if(!isElementPresent(locator,null)) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should have contained %s '%s' but did not", tag != null ? tag : "element", locator));
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({"locator"})
    public void pageShouldNotContainElement(String locator) {
        pageShouldNotContainElement(locator, null, "");
    }

    /**
     * Verify the current page does not contain the given element with optional <b>tag</b> and located by the <b>locator</b>.
     * <br>
     *
     * @param locator   the locator string.
     * @param tag   Optional tag name.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({"locator", "tag=NONE", "message=NONE"})
    public void pageShouldNotContainElement(String locator, String tag, String message) {
        if(isElementPresent(locator,null)) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should not have contained %s '%s' but did.", tag != null ? tag : "element", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldContainLink(String locator) {
        pageShouldContainLink(locator, "");
    }

    /**
     * Verify the current page contains the given link element located by the <b>locator</b>. <br>
     *
     * The locator must be able to locate link element.
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldContainLink(String locator, String message) {
        if(!isElementPresent(locator, StdTags.LINK.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should have contained link '%s' but did not", locator));
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldNotContainLink(String locator) {
        pageShouldNotContainLink(locator, "");
    }

    /**
     * Verify the current page does not contain the link element located by the <b>locator</b>. <br>
     *
     * The locator must be able to locate an link element.
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldNotContainLink(String locator, String message) {
        if(isElementPresent(locator, StdTags.LINK.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should not have contained link '%s' but did.", locator)
                );
            }
            throw new FailureException(message);
        }
    }
    @RobotKeywordOverload
    public void pageShouldContainImage(String locator) {
        pageShouldContainImage(locator, "");
    }

    /**
     * Verify the current page contains the image element located by the <b>locator</b>. <br>
     *
     * The locator must be able to locate an image element.
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldContainImage(String locator, String message) {
        if(!isElementPresent(locator, StdTags.IMAGE.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should have contained image '%s' but did not.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldNotContainImage(String locator) {
        pageShouldContainImage(locator, "");
    }

    /**
     * Verify the current page does not contain the image element located by the <b>locator</b>. <br>
     *
     * The locator must be able to locate an image element.
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldNotContainImage(String locator, String message) {
        if(isElementPresent(locator, StdTags.IMAGE.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should not have contained image '%s' but did.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldContainCheckbox(String locator) {
        pageShouldContainCheckbox(locator, "");
    }

    /**
     * Verify the current page contains the checkbox element located by the <b>locator</b>. <br>
     *
     * The locator must be able to locate an checkbox element.
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldContainCheckbox(String locator, String message) {
        if(!isElementPresent(locator, StdTags.CHECKBOX.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should have contained checkbox '%s' but did not.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldNotContainCheckbox(String locator) {
        pageShouldNotContainCheckbox(locator, "");
    }

    /**
     * Verify the current page not contains the checkbox element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldNotContainCheckbox(String locator, String message) {
        if(isElementPresent(locator, StdTags.CHECKBOX.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should not have contained checkbox '%s' but did.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldContainRadioButton(String locator) {
        pageShouldContainRadioButton(locator, "");
    }

    /**
     * Verify the current page contains the radio button element located by the <b>locator</b>. <br>
     *
     * The locator must be able to locate an radio button element.
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldContainRadioButton(String locator, String message) {
        if(!isElementPresent(locator, StdTags.RADIO.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should have contained radio button '%s' but did not.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldNotContainRadioButton(String locator) {
        pageShouldNotContainRadioButton(locator, "");
    }

    /**
     * Verify the current page not contains the radio button element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldNotContainRadioButton(String locator, String message) {
        if(isElementPresent(locator, StdTags.RADIO.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should not have contained radio button '%s' but did.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldContainTextfield(String locator) {
        pageShouldContainTextfield(locator, "");
    }

    /**
     * Verify the current page contains the text element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldContainTextfield(String locator, String message) {
        if(!isElementPresent(locator, StdTags.TEXT.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should have contained text field '%s' but did not.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void pageShouldNotContainTextfield(String locator) {
        pageShouldNotContainTextfield(locator, "");
    }

    /**
     * Verify the current page not contains the text element located by the <b>locator</b>. <br>
     *
     * @param locator   the locator string.
     * @param message   Optional message to throw on failures, instead of default one.
     */
    @RobotKeyword
    @ArgumentNames({ "locator", "message=NONE" })
    public void pageShouldNotContainTextfield(String locator, String message) {
        if(isElementPresent(locator, StdTags.TEXT.name())) {
            if (message == null || message.equals("")) {
                throw new FailureException(
                        String.format("Page should not have contained text field '%s' but did.", locator)
                );
            }
            throw new FailureException(message);
        }
    }

    private boolean isPageContainsText(String text) {
        WebDriver webDriver = driverManager.getCurrent();
        webDriver.switchTo().defaultContent();

        if (isTextPresent(text)) {
            return true;
        }

        //if not check the frame(s) content
        List<WebElement> frameElements = findElement("tag=iframe", false, false);
        if(frameElements != null && !frameElements.isEmpty()) {
            for (int i = 0; i < frameElements.size(); i++) {
                webDriver.switchTo().frame(i);
                boolean found = isTextPresent(text);
                webDriver.switchTo().defaultContent();
                if (found) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTextPresent(String text) {
        String locator = String.format("xpath=//*[contains(., %s)]", Util.escapeXpathValue(text));
        return isElementPresent(locator, null);
    }

    private boolean isElementPresent(String locator, String tagName) {
        List<WebElement> elements = findElement(locator, tagName, true, false);
        return elements != null && !elements.isEmpty();
    }

}