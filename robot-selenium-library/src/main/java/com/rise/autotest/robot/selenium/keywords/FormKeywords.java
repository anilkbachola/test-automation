package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.FailureException;
import com.rise.autotest.robot.selenium.locator.ElementTag;
import com.rise.autotest.robot.selenium.locator.StdTags;
import org.openqa.selenium.WebElement;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.List;

@RobotKeywords
public class FormKeywords extends SeleniumBase {

    /**
     * Submits the form identified by the <b>locator</b>. <br>
     * If the locator is empty, the first form in the page will be submitted. <br>
     *
     * @param locator   The locator to locate the form.
     */
    @RobotKeyword
    @ArgumentNames({"locator=NONE"})
    public void submitForm(String locator) {
        if (locator == null) {
            locator = "xpath=//form";
        }
        List<WebElement> webElements = findElement(locator, StdTags.FORM.name(), true, true);
        webElements.get(0).submit();
    }

    /**
     * Selects the checkbox identified by the <b>locator</b>. <br>
     * If the checkbox is already checked, does nothing.
     *
     * @param locator   the locator to locate the checkbox
     */
    @RobotKeyword
    @ArgumentNames({"locator"})
    public void selectCheckbox(String locator) {
        WebElement element = getCheckbox(locator);
        if(!element.isSelected()) {
            element.click();
        }
    }

    /**
     * UnSelects the checkbox identified by the <b>locator</b>. <br>
     * If the checkbox is not checked, selects the checkbox.
     *
     * @param locator   the locator to locate the checkbox
     */
    @RobotKeyword
    @ArgumentNames({"locator"})
    public void unSelectCheckbox(String locator) {
        WebElement element = getCheckbox(locator);
        if(element.isSelected()) {
            element.click();
        }
    }

    /**
     * Types the given <b>filePath</b> into the input field identified by <b>locator</b>.<br>
     * This keyword is most often used to input files into upload forms.
     * The file specified with filePath must be available on the same host where the Selenium Server is running.<br>
     * <br>
     * Example:
     * <table border="1" cellspacing="0" summary="">
     * <tr>
     * <td>Choose File</td>
     * <td>my_upload_field</td>
     * <td>/home/user/files/trades.csv</td>
     * </tr>
     * </table>
     *
     * @param locator   the locator to locate the file upload element
     * @param filePath  the filepath to input
     */
    @RobotKeyword
    @ArgumentNames({"locator", "filePath"})
    public void chooseFile(String locator, String filePath) {
        WebElement element = findElement(locator, StdTags.FILEUPLOAD.name(),true, true).get(0);
        if(element != null) {
            element.sendKeys(filePath);
        }
    }

    /**
     * Types the given password into password field identified by the <b>locator</b>.<br>
     * <br>
     * Clears the old value in the password field before typing
     *
     * @param locator   the locator to locate the password field
     * @param password  the password to type
     */
    @RobotKeyword
    @ArgumentNames({"locator", "password"})
    public void inputPassword(String locator, String password) {
        WebElement element = findElement(locator, StdTags.FILEUPLOAD.name(),true, true).get(0);
        if(element != null) {
            element.clear();
            element.sendKeys(password);
        }
    }

    /**
     * Types the given text into the text field identified by the <b>locator</b>. <br>
     * <br>
     * Clears the old value in the text field before typing.
     *
     * @param locator   the locator to locate the text field
     * @param text  the tex to type
     */
    @RobotKeyword
    @ArgumentNames({"locator", "text"})
    public void inputText(String locator, String text) {
        WebElement element = findElement(locator, StdTags.TEXT.name(),true, true).get(0);
        if(element != null) {
            element.clear();
            element.sendKeys(text);
        }
    }


    @RobotKeywordOverload
    @ArgumentNames({"locator", "value"})
    public void textFieldValueShouldBe(String locator, String value) {
        textFieldValueShouldBe(locator, value, "");
    }

    /**
     * Verify the value of the text field identified by the <b>locator</b> is equal to <b>value</b>.<br>
     *
     * @param locator   the locator to locate the text field
     * @param value     the value to compare
     * @param message   Optional custom error message (Default=NONE)
     */
    @RobotKeyword
    @ArgumentNames({"locator", "value", "message=NONE"})
    public void textFieldValueShouldBe(String locator, String value, String message) {
        String actual = getTextFieldValue(locator);
        if (!actual.equals(value)) {
            if (message == null) {
                throw new FailureException(
                        String.format("Value of text field '%s' should have been '%s' but was '%s'", locator, value, actual)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({"locator", "value"})
    public void textFieldValueShouldContain(String locator, String value) {
        textFieldValueShouldContain(locator, value, "");
    }

    /**
     * Verify the value of the text field identified by the <b>locator</b> contains <b>value</b>.<br>
     *
     * @param locator   the locator to locate the text field
     * @param value     the value to compare
     * @param message   Optional custom error message (Default=NONE)
     */
    @RobotKeyword
    @ArgumentNames({"locator", "value", "message=NONE"})
    public void textFieldValueShouldContain(String locator, String value, String message) {
        String actual = getTextFieldValue(locator);
        if (!actual.contains(value)) {
            if (message == null) {
                throw new FailureException(
                        String.format("Value of text field '%s' should have contain '%s' but was '%s'", locator, value,
                                actual)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({"locator", "value"})
    public void textFieldValueShouldNotBe(String locator, String value) {
        textFieldValueShouldNotBe(locator, value, "");
    }

    /**
     * Verify the value of the text field identified by the <b>locator</b> is equal to <b>value</b>.<br>
     *
     * @param locator   the locator to locate the text field
     * @param value     the value to compare
     * @param message   Optional custom error message (Default=NONE)
     */
    @RobotKeyword
    @ArgumentNames({"locator", "value", "message=NONE"})
    public void textFieldValueShouldNotBe(String locator, String value, String message) {
        String actual = getTextFieldValue(locator);
        if (actual.equals(value)) {
            if (message == null) {
                throw new FailureException(
                        String.format("Value of text field '%s' should not have been '%s' but did '%s'", locator, value,
                                actual)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({"locator", "value"})
    public void textFieldValueShouldNotContain(String locator, String value) {
        textFieldValueShouldNotContain(locator, value, "");
    }

    /**
     * Verify the value of the text field identified by the <b>locator</b> does not contains <b>value</b>.<br>
     *
     * @param locator   the locator to locate the text field
     * @param value     the value to compare
     * @param message   Optional custom error message (Default=NONE)
     */
    @RobotKeyword
    @ArgumentNames({"locator", "value", "message=NONE"})
    public void textFieldValueShouldNotContain(String locator, String value, String message) {
        String actual = getTextFieldValue(locator);
        if (actual.contains(value)) {
            if (message == null) {
                message = String.format("Value of text field '%s' should not have contain '%s' but did '%s'", locator, value,
                        actual);
            }
            throw new FailureException(message);
        }
    }



    @RobotKeywordOverload
    public void textAreaValueShouldBe(String locator, String value) {
        textAreaValueShouldBe(locator, value, "");
    }

    /**
     * Verify the value of the text area identified by the <b>locator</b> is equal to <b>value</b>.<br>
     *
     * @param locator   the locator to locate the text field
     * @param value     the value to compare
     * @param message   Optional custom error message (Default=NONE)
     */
    @RobotKeyword
    @ArgumentNames({"locator", "value", "message=NONE"})
    public void textAreaValueShouldBe(String locator, String value, String message) {
        String actual = getTextAreaValue(locator);
        if(!actual.equals(value)) {
            if (message == null) {
                throw new FailureException(
                        String.format("Value of text area '%s' should have been '%s' but was '%s'", locator, value, actual)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void textAreaValueShouldNotBe(String locator, String value) {
        textAreaValueShouldNotBe(locator, value, "");
    }


    /**
     * Verify the value of the text area identified by the <b>locator</b> is equal to <b>value</b>.<br>
     *
     * @param locator   the locator to locate the text field
     * @param value     the value to compare
     * @param message   Optional custom error message (Default=NONE)
     */
    @RobotKeyword
    @ArgumentNames({"locator", "value", "message=NONE"})
    public void textAreaValueShouldNotBe(String locator, String value, String message) {
        String actual = getTextAreaValue(locator);
        if(actual.equals(value)) {
            if (message == null) {
                throw new FailureException(
                        String.format("Value of text area '%s' should not have been '%s' but did '%s'", locator, value, actual)
                );
            }
            throw new FailureException(message);
        }
    }

    @RobotKeywordOverload
    public void textAreaValueShouldContain(String locator, String value) {
        textAreaValueShouldContain(locator, value, "");
    }

    /**
     * Verify the value of the text area identified by the <b>locator</b> contains <b>value</b>.<br>
     *
     * @param locator   the locator to locate the text field
     * @param value     the value to compare
     * @param message   Optional custom error message (Default=NONE)
     */
    @RobotKeyword
    @ArgumentNames({"locator", "value", "message=NONE"})
    public void textAreaValueShouldContain(String locator, String value, String message) {
        String actual = getTextAreaValue(locator);
        if (!actual.contains(value)) {
            if (message == null) {
                throw new FailureException(
                        String.format("Value of text area '%s' should have contain '%s' but was '%s'", locator, value,
                                actual)
                );
            }
            throw new FailureException(message);
        }
    }



    @RobotKeywordOverload
    @ArgumentNames({"locator", "value"})
    public void textAreaValueShouldNotContain(String locator, String value) {
        textAreaValueShouldNotContain(locator, value, "");
    }

    /**
     * Verify the value of the text area identified by the <b>locator</b> does not contain <b>value</b>.<br>
     *
     * @param locator   the locator to locate the text field
     * @param value     the value to compare
     * @param message   Optional custom error message (Default=NONE)
     */
    @RobotKeyword
    @ArgumentNames({"locator", "value", "message=NONE"})
    public void textAreaValueShouldNotContain(String locator, String value, String message) {
        String actual = getTextAreaValue(locator);
        if (actual.contains(value)) {
            if (message == null) {
                message = String.format("Value of text area '%s' should not have contain '%s' but did '%s'", locator, value,
                        actual);
            }
            throw new FailureException(message);
        }
    }

    private String getTextAreaValue(String locator) {
        WebElement element = findElement(locator, StdTags.TEXTAREA.name(), true,true).get(0);
        return  element == null? "" : element.getText();
    }

    private String getTextFieldValue(String locator) {
        WebElement element = findElement(locator, StdTags.TEXT.name(), true,true).get(0);
        return  element == null? "" : element.getText();
    }

    //TODO: add radio button functionality


    /**
     * Click the button identified by the <b>locator</b>.<br>
     *
     * @param locator the locator to locate the button element
     */
    public void clickButton(String locator) {
        WebElement element = findElement(locator, StdTags.INPUTBUTTON.name(),true, true).get(0);
        element.click();
    }



    private WebElement getCheckbox(String locator) {
        return findElement(locator, StdTags.CHECKBOX.name(), true, true).get(0);
    }



}
