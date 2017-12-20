package com.rise.autotest.robot.selenium.util;

import static org.assertj.core.api.Assertions.assertThat;
import com.rise.autotest.robot.selenium.BrowserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

class WebDriverBuilderTest {

    private WebDriver webDriver;
    private String firefoxCapabilities = "{\n" + "\t\"capabilities\": {\n" + "\t\t\"platformName\": \"MAC\",\n"
            + "\t\t\"moz:firefoxOptions\": {\n" + "\t\t\t\"prefs\": {\n" + "\t\t\t\t\"dom.ipc.processCount\": 8\n"
            + "\t\t\t},\n" + "\t\t\t\"log\": {\n" + "\t\t\t\t\"level\": \"trace\"\n" + "\t\t\t}\n" + "\t\t},\n"
            + "\t\t\"goog:chromeOptions\": {\n" + "\t\t\t\"args\": [\"--headless\"]\n" + "\t\t},\n"
            + "\t\t\"loggingPrefs\": {\n" + "\t\t\t\"driver\": \"INFO\", \n" + "\t\t\t\"browser\": \"DEBUG\"\n"
            + "\t\t},\n" + "\t\t\"timeouts\": {\n" + "\t\t\t\"script\": 10000,\n" + "\t\t\t\"pageLoad\": 11000,\n"
            + "\t\t\t\"implicit\": 12000\n" + "\t\t},\n" + "\t\t\"version\": \"56\"\n" + "\t}\n" + "}";

    @AfterEach
    void clean() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test void buildDriver_WhenBrowserTypeIsCHROME_ShouldReturnChromeDriver() {
        BrowserType browser = BrowserType.CHROME;
        webDriver = WebDriverBuilder.buildDriver(browser, firefoxCapabilities);

        assertThat(webDriver).isInstanceOf(ChromeDriver.class);
    }

    @Test void buildDriver_WhenBrowserTypeIsFIREFOX_ShouldReturnFirefoxDriver() {
        BrowserType browser = BrowserType.FIREFOX;
        webDriver = WebDriverBuilder.buildDriver(browser, firefoxCapabilities);

        assertThat(webDriver).isInstanceOf(FirefoxDriver.class);
    }

    @Test void buildDriver_WhenBrowserTypeIsOPERA_ShouldReturnOperaDriver() {

    }

    @Test void buildDriver_WhenBrowserTypeIsSAFARI_ShouldReturnSafariDriver() {

    }

    @Test void buildDriver_WhenBrowserTypeIsIEXPLORER_ShouldReturnInternetExplorerDriver() {

    }


}