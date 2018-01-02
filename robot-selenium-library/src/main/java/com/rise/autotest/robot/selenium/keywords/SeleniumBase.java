package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.selenium.WebDriverManager;
import com.rise.autotest.robot.selenium.locator.ElementTag;
import com.rise.autotest.robot.selenium.locator.Locator;
import com.rise.autotest.robot.selenium.locator.LocatorContext;
import com.rise.autotest.robot.selenium.locator.LocatorFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

abstract class SeleniumBase {
    WebDriverManager driverManager = WebDriverManager.instance();

    List<WebElement> findElement(String locatorString, boolean firstOnly, boolean required) {
        return findElement(locatorString, null, firstOnly, required);
    }

    List<WebElement> findElement(String locatorString, String tagName, boolean firstOnly, boolean required ) {

        List<WebElement> webElements = find(locatorString, ElementTag.fromName(tagName), required);

        if(firstOnly && webElements.size() > 1) {
            return Arrays.asList(webElements.get(0));
        }
        return webElements;
    }

    void executeScript(String script, Object... objects) {
        ((JavascriptExecutor) driverManager.getCurrent()).executeScript(script, objects);
    }

    private List<WebElement> find(String locatorString, ElementTag tagName, boolean required) {
        Locator locator = LocatorFactory.instance().parseLocator(locatorString);
        LocatorContext context = buildContext(locatorString, tagName, required);
        return locator.find(driverManager.getCurrent(), context);
    }

    private LocatorContext buildContext(String locatorString, ElementTag tagName, boolean required) {
        LocatorContext context = new LocatorContext();
        context.setRequired(required);

        String criteria = locatorString;
        String[] locatorParts = locatorString.split("=", 2);
        if (locatorParts.length == 2) {
            criteria = locatorParts[1];
        }
        context.setCriteria(criteria);

        if(tagName != null) {
            context.setTag(tagName);
        }

        return context;
    }
}