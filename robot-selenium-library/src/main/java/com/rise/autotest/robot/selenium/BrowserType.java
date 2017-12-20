package com.rise.autotest.robot.selenium;

import org.openqa.selenium.remote.DesiredCapabilities;

public enum BrowserType {
    ANDROID("ANDROID", DesiredCapabilities.android()),
    CHROME("CHROME", DesiredCapabilities.chrome()),
    EDGE("EDGE", DesiredCapabilities.edge()),
    FIREFOX("FIREFOX", DesiredCapabilities.firefox()),
    HTMLUNIT("HTMLUNIT", DesiredCapabilities.htmlUnit()),
    IEXPLORER("IEXPLORER", DesiredCapabilities.internetExplorer()),
    IPHONE("IPHONE", DesiredCapabilities.iphone()),
    IPAD("IPAD", DesiredCapabilities.ipad()),
    OPERA("OPERA", DesiredCapabilities.operaBlink()),
    SAFARI("SAFARI", DesiredCapabilities.safari()),;

    private final String browserName;
    private final DesiredCapabilities capabilities;

    /**
     * Constructor.
     * @param browserName   name of the browser
     * @param capabilities  default browser capabilities. See {@link DesiredCapabilities}
     */
    BrowserType(String browserName, DesiredCapabilities capabilities) {
        this.browserName = browserName;
        this.capabilities = capabilities;
    }

    public String browserName() {
        return browserName;
    }

    public DesiredCapabilities capabilities() {
        return capabilities;
    }

    public static BrowserType fromName(String browserName) {
        return BrowserType.valueOf(browserName.toUpperCase());
    }
}
