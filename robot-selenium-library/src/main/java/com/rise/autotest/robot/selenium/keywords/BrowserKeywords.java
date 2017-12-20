package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.selenium.BrowserType;
import com.rise.autotest.robot.selenium.WebDriverManager;
import com.rise.autotest.robot.selenium.util.WebDriverBuilder;
import org.openqa.selenium.WebDriver;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

/**
 * Robot Keywords to manage WebBrowser/Driver sessions
 */
@RobotKeywords
public class BrowserKeywords extends SeleniumBase {

    private static final String DEFAULT_BROWSER_NAME = BrowserType.FIREFOX.browserName();

    /**
     * Opens the 'firefox' browser by default and navigates to the given <b>url</b>.<br>
     * And registers the browser with {@link WebDriverManager}
     *
     * @param url   URL to navigate
     * @return  session id of the browser/driver instance.
     */
    @RobotKeywordOverload
    public String openBrowser(String url) {
        return openBrowser(url, DEFAULT_BROWSER_NAME);
    }

    /**
     * Opens the browser specified by <b>browserName</b> and navigates to the given <b>url</b>.<br>
     * And registers the browser with {@link WebDriverManager}
     *
     * @param url   URL to navigate
     * @param browserName   name of the browser. See {@link BrowserType}
     * @return  session id of the browser/driver instance.
     */
    @RobotKeywordOverload
    public String openBrowser(String url, String browserName) {
        return openBrowser(url, browserName, null, null);
    }


    /**
     * Opens the browser specified by <b>browserName</b> and assigns <b>alias</b> if provided.<br>
     * Navigates to the given <b>url</b>.
     * And registers the browser with {@link WebDriverManager}
     * <br>
     * Each browser window opened will have a session id and optionally an alias.
     * Which later can be used to refer to that particular browser instance
     *
     * @param url   URL to navigate
     * @param browserName   name of the browser. {@link BrowserType}
     * @param alias An alias for the browser session
     * @param capabilitiesJson Web Driver capabilities as json string
     * @return  session id of the browser/driver instance.
     */
    @RobotKeyword
    @ArgumentNames({"url", "browserName=firefox", "alias=NONE", "capabilities=NONE" })
    public String openBrowser(String url, String browserName, String alias, String capabilitiesJson) {
        BrowserType browser = BrowserType.fromName(browserName);
        WebDriver webDriver = WebDriverBuilder.buildDriver(browser, capabilitiesJson);

        webDriver.get(url);
        return driverManager.register(webDriver, alias);
    }

    /**
     * Close recently opened web driver/browser session.
     * @see WebDriverManager#close()
     */
    @RobotKeywordOverload
    public void closeBrowser() {
        closeBrowser(null);
    }


    /**
     * Close a web driver/browser session associated with the given <b>sessionIdorAlias</b>.<br>
     *
     * If the provided session id or alias could not found, returns silently.
     *
     * @param sessionIdOrAlias the session id or alias associated with a web driver session.
     *                         Optional, if not provided closes the recently opened connection.
     * @see WebDriverManager#close(String)
     */
    @RobotKeyword
    @ArgumentNames({"sessionIdOrAlias=NONE"})
    public void closeBrowser(String sessionIdOrAlias) {
        if(sessionIdOrAlias != null) {
            driverManager.close(sessionIdOrAlias);
        } else {
            driverManager.close();
        }
    }

    /**
     * Closes all web driver/browser sessions currently open.<br>
     *
     * @see WebDriverManager#closeAll()
     */
    @RobotKeyword
    public void closeAllBrowsers() {
        driverManager.closeAll();
    }

    /**
     * Switch to a browser/driver instance associated with given <b>sessionIdOralias</b>. <br>
     *
     * @param sessionIdOrAlias  the session id or alias associated with a web driver session.
     */
    @RobotKeyword
    @ArgumentNames({"sessionIdOrAlias"})
    public void switchBrowser(String sessionIdOrAlias) {
        driverManager.switchBrowser(sessionIdOrAlias);
    }
}
