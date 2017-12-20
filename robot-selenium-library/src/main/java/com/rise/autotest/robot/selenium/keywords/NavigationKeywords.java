package com.rise.autotest.robot.selenium.keywords;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

/**
 * Robot keywords to manage browser navigation.
 */
@RobotKeywords
public class NavigationKeywords extends SeleniumBase {

    /**
     * Navigate the browser to the specified <b>url</b>. <br>
     *
     * @param url   the URL to navigate to.
     */
    @RobotKeyword
    @ArgumentNames({ "url" })
    public void goTo(String url) {
        driverManager.getCurrent().get(url);
    }

    /**
     * Go back.
     */
    @RobotKeyword
    public void goBack() {
        driverManager.getCurrent().navigate().back();
    }

    /**
     * Go forward.
     */
    @RobotKeyword
    public void goForward() {
        driverManager.getCurrent().navigate().forward();
    }

    /**
     * Refresh current page.
     */
    @RobotKeyword
    public void refresh() {
        driverManager.getCurrent().navigate().refresh();
    }

}
