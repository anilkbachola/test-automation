package com.rise.autotest.robot.selenium.locator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

@FunctionalInterface
public interface Locator {

    /**
     * Locate an element using the <b>webDriver</b> instance.
     * Implementations of this interface will provide different strategies to locate an element in a page.
     * <br>
     * Uses implicit waits to wait for element availability. See{@link org.openqa.selenium.support.ui.WebDriverWait}
     *
     * @param webDriver  the web driver session to use.
     * @param context   the context provides the criteria to locate the element. {@link LocatorContext}
     * @return  list of web elements
     * @throws org.openqa.selenium.TimeoutException if element is required and could not be located.
     */
    List<WebElement> find(WebDriver webDriver, LocatorContext context);
}
