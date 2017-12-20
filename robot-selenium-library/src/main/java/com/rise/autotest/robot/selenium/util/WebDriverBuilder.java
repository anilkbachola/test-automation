package com.rise.autotest.robot.selenium.util;

import com.rise.autotest.robot.FatalException;
import com.rise.autotest.robot.selenium.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WebDriverBuilder {

    private WebDriverBuilder() { }

    /**
     * Build and returns WebDriver with given in capabilities.<br>
     * <br>
     * By default builds Firefox driver
     *
     * @param browser   the browser type. {@link BrowserType}
     * @param capabilitiesJson  browser capabilities JSON.
     * @return  {@code WebDriver} instance
     */
    public static WebDriver buildDriver(BrowserType browser, String capabilitiesJson) {

        WebDriver webDriver;
        DesiredCapabilities capabilities = parseCapabilities(browser, capabilitiesJson);

        switch (browser) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.merge(capabilities);
                webDriver = new ChromeDriver(chromeOptions);
                break;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions(capabilities);
                webDriver = new FirefoxDriver(firefoxOptions);
                break;
            case SAFARI:
                SafariOptions safariOptions = new SafariOptions(capabilities);
                webDriver = new SafariDriver(safariOptions);
                break;
            case IEXPLORER:
                InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions(capabilities);
                webDriver = new InternetExplorerDriver(internetExplorerOptions);
                break;
            case OPERA:
                OperaOptions operaOptions = new OperaOptions();
                operaOptions.merge(capabilities);
                webDriver = new OperaDriver(operaOptions);
                break;
            default:
                throw new FatalException("Unsupported browser type");

        }
        setTimeouts(webDriver, capabilities);
        webDriver.manage().window().maximize();
        return webDriver;
    }

    //TODO: understand the need for this. So far only Firefox supports, chrome does not support.
    /*Not all drivers support the Web Driver spec, "timeouts" settings. This is an effort to
        read the timeout properties and set them manually with driver.
     */
    private static void setTimeouts(WebDriver webDriver, DesiredCapabilities capabilities) {
        Object timeoutsObject = capabilities.getCapability("timeouts");
        if(timeoutsObject != null) {
            Map<String, Object> timeoutsSettings = (Map<String, Object>) timeoutsObject;

            Object implicitTimeout = timeoutsSettings.get("implicit");
            Object scriptTimeout = timeoutsSettings.get("script");
            Object pageLoadTimeout = timeoutsSettings.get("pageLoad");

            if (implicitTimeout != null && Long.valueOf(String.valueOf(implicitTimeout)) > 0) {
                webDriver.manage().timeouts()
                        .implicitlyWait(Long.valueOf(String.valueOf(implicitTimeout)), TimeUnit.MILLISECONDS);
            }

            if (scriptTimeout != null && Long.valueOf(String.valueOf(scriptTimeout)) > 0) {
                webDriver.manage().timeouts()
                        .setScriptTimeout(Long.valueOf(String.valueOf(scriptTimeout)), TimeUnit.MILLISECONDS);
            }

            if (pageLoadTimeout != null && Long.valueOf(String.valueOf(pageLoadTimeout)) > 0) {
                webDriver.manage().timeouts()
                        .pageLoadTimeout(Long.valueOf(String.valueOf(pageLoadTimeout)), TimeUnit.MILLISECONDS);
            }
        }
    }

    private static DesiredCapabilities parseCapabilities(BrowserType browser, String capabilitiesJson) {
        if(capabilitiesJson == null) {
            return browser.capabilities();
        }

        Map<String, ?> capabilitiesMap = capabilitiesJsonAsMap(capabilitiesJson);
        MutableCapabilities mutableCapabilities = new MutableCapabilities(capabilitiesMap);
        return new DesiredCapabilities(browser.capabilities(), mutableCapabilities);
    }

    private static Map<String,Object> capabilitiesJsonAsMap(String capabilitiesJson) {
        JSONParser jsonParser = new JSONParser();
        Object json = null;
        try {
            json = jsonParser.parse(capabilitiesJson);

            JSONObject jsonObject = (JSONObject)json;

            JSONObject capabilitiesObject = (JSONObject) jsonObject.get("capabilities");

            return (Map<String, Object>)capabilitiesObject;
        } catch (ParseException e) {
            throw new FatalException("Unable to parse the capabilities json");
        }

    }

}
