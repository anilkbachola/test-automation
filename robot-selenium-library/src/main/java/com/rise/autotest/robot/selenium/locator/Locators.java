package com.rise.autotest.robot.selenium.locator;

import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Locators implements Locator {

    private static final long DEFAULT_WAIT_SLEEP_MILLIS = 100L;
    private static final int DEFAULT_WAIT_SECS = 20;

    /**
     * Filter elements based on tag name and additional element attributes.
     * Retrieves the list of attributes to filter from the LocatorContext
     * @param context {@code LocatorContext}
     * @param elements  web elements to filter
     */
    void filter(LocatorContext context, List<WebElement> elements) {
        if( elements == null || elements.isEmpty() || context == null || context.getTag() == null) {
            return;
        }
        //List<WebElement> result = new ArrayList<>();
        Map<String, String> attributes = context.getTag().getAttributes();

        Iterator<WebElement> itr = elements.iterator();
        while(itr.hasNext()) {
            WebElement element = itr.next();
            if (!element.getTagName().equalsIgnoreCase(context.getTag().getTagName())) {
                itr.remove();
                continue;
            }
            if(!attributes.isEmpty()) {
                for (Map.Entry<String, String> entry : attributes.entrySet()) {
                    if (!element.getAttribute(entry.getKey()).equals(attributes.get(entry.getKey()))) {
                        itr.remove();
                        break;
                    }
                }
            }
        }

        /*result = elements.stream().filter(element -> {
            if (!element.getTagName().toLowerCase().equals(context.getTag().getTagName())) {
                return true;
            }
            if(!attributes.isEmpty()) {
                for (String name : attributes.keySet()) {
                    if (!element.getAttribute(name).equals(attributes.get(name))) {
                       return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());*/

        /*for(WebElement element : elements) {
            if (element.getTagName().toLowerCase().equals(context.getTag().getTagName())) {
                if(context.getTag().getAttributes().isEmpty()) {
                    result.add(element);
                } else {
                    for (String name : attributes.keySet()) {
                        if (element.getAttribute(name).equals(attributes.get(name))) {
                            result.add(element);
                        }
                    }
                }
            }
        }
        return result;*/
        //return result;
    }

    /**
     * Explicit WebDriver wait, to wait for an element availability or clickability etc.. in the current instance.
     * By default waits for 10 secs with 100millis delay between each attempts.
     * @param webDriver  current webdriver instance
     * @return {@link WebDriverWait} with default wait and delay value
     */
    WebDriverWait wait(WebDriver webDriver) {
        return wait(webDriver, DEFAULT_WAIT_SECS, DEFAULT_WAIT_SLEEP_MILLIS);
    }

    /**
     * Explicit WebDriver wait, to wait for an element availability or clickability etc.. in the current instance.
     * By default waits for 10 secs with 100millis delay between each attempts.
     * @param webDriver  current webdriver instance
     * @param timeout timeout in secs
     * @return {@link WebDriverWait} with default wait and delay value
     */
    WebDriverWait wait(WebDriver webDriver, int timeout) {
        return wait(webDriver, timeout, DEFAULT_WAIT_SLEEP_MILLIS);
    }

    /**
     * Explicit WebDriver wait, to wait for an element availability or clickability etc.. in the current instance.
     * @param webDriver  current webdriver instance
     * @param timeout   timeout in secs
     * @param sleep sleep delay in millis
     * @return {@link WebDriverWait} with default wait and delay value
     */
    WebDriverWait wait(WebDriver webDriver, int timeout, long sleep) {
        return new WebDriverWait(webDriver,timeout, sleep);
    }

    /**
     * Locator to find elements by 'xpath'. Uses Selenium {@code By.xpath}
     * Gets the 'xpath' expression from {@code LocatorContext}
     */
    public static class XPathLocator extends Locators {

        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {
            List<WebElement> elements = null;
            try {
                elements = wait(webDriver).until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(context.getCriteria())));
                filter(context, elements);
            } catch (TimeoutException ex) {
                if(context.isRequired()) {
                    throw  ex;
                }
            }
            return elements == null? Collections.emptyList() : elements;
        }
    }

    /**
     * Locator to find elements by its 'name' attribute. Uses Selenium {@code By.name}
     * Gets the 'name' value from {@code LocatorContext}
     */
    public static class NameLocator extends Locators {
        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {

            List<WebElement> elements = null;
            try {
                elements = wait(webDriver).until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(context.getCriteria()))
                );
                filter(context, elements);
            } catch (TimeoutException ex) {
                if(context.isRequired()) {
                    throw  ex;
                }
            }
            return elements == null? Collections.emptyList() : elements;
        }
    }

    /**
     * Locator to find elements by its 'id' attribute. Uses Selenium {@code By.id}
     * Gets the 'id' value from {@code LocatorContext}
     */
    public static class IdLocator extends Locators {

        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {
            List<WebElement> elements = null;
            try {
                elements = wait(webDriver)
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(context.getCriteria())));
                filter(context, elements);
            } catch (TimeoutException ex) {
                if(context.isRequired()) {
                    throw  ex;
                }
            }
            return elements == null? Collections.emptyList() : elements;
        }
    }

    /**
     * Locator to find elements by its 'id' or 'name' attribute. Uses Selenium {@code By.id} and {@code By.name}
     * Gets the 'id' or 'name' value from {@code LocatorContext}
     */
    public static class IdOrNameLocator extends Locators {

        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {
            List<WebElement> elementsById = null;
            List<WebElement> elementsByName = null;
            List<WebElement> elements = null;
            try {
                elementsById = wait(webDriver)
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(context.getCriteria())));

                elementsByName = wait(webDriver)
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(context.getCriteria())));

                elements = (List<WebElement>)CollectionUtils.union(elementsById, elementsByName);
                filter(context, elements);
            } catch (TimeoutException ex) {
                if(context.isRequired()) {
                    throw  ex;
                }
            }
            return elements == null? Collections.emptyList() : elements;
        }
    }


    public static class LinkTextLocator extends Locators {

        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {
            List<WebElement> elements = null;
            try {
                //check for link text
                elements = wait(webDriver)
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(context.getCriteria())));

                //check for partial link text
                if(elements == null || elements.isEmpty()) {
                    elements = wait(webDriver).until(
                            ExpectedConditions.presenceOfAllElementsLocatedBy(By.partialLinkText(context.getCriteria())));
                }
                filter(context, elements);
            } catch (TimeoutException ex) {
                if(context.isRequired()) {
                    throw  ex;
                }
            }
            return elements == null? Collections.emptyList() : elements;
        }
    }

    public static class CssLocator extends Locators {

        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {
            List<WebElement> elements = null;
            try {
                elements = wait(webDriver)
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(context.getCriteria())));
                filter(context, elements);
            } catch (TimeoutException ex) {
                if(context.isRequired()) {
                    throw  ex;
                }
            }
            return elements == null? Collections.emptyList() : elements;
        }
    }

    public static class ClassNameLocator extends Locators {

        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {
            List<WebElement> elements = null;
            try {
                elements = wait(webDriver)
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(context.getCriteria())));
                filter(context, elements);
            } catch (TimeoutException ex) {
                if(context.isRequired()) {
                    throw  ex;
                }
            }
            return elements == null? Collections.emptyList() : elements;
        }
    }

    public static class TagNameLocator extends Locators {

        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {

            List<WebElement> elements = null;
            try {
                elements = wait(webDriver)
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName(context.getCriteria())));
                filter(context, elements);
            } catch (TimeoutException ex) {
                if(context.isRequired()) {
                    throw  ex;
                }
            }
            return elements == null? Collections.emptyList() : elements;
        }
    }

    public static class SizzleLocator extends Locators {

        @Override public List<WebElement> find(WebDriver webDriver, LocatorContext context) {

            String js = String.format("return jQuery('%s').get();", context.getCriteria().replaceAll("'", "\\'"));
            Object data = ((JavascriptExecutor) webDriver).executeScript(js);
            List<WebElement> elements = new ArrayList<>();
            if (data != null) {
                if (data instanceof List<?>) {
                    elements.addAll((List<WebElement>) data);
                } else if (data instanceof WebElement) {
                    elements.add((WebElement) data);
                }
            }
            filter(context, elements);
            return elements;
        }
    }

}
