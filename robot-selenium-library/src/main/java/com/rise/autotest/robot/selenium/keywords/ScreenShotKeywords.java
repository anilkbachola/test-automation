package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.util.RobotLogger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.io.File;
import java.io.IOException;

/**
 * Robot keywords to manage screenshots.
 */
@RobotKeywords
public class ScreenShotKeywords extends SeleniumBase {

    @RobotKeywordOverload
    public void captureScreenshot() {
        captureScreenshot(null);
    }

    /**
     * Take a screenshot of the current page and embed into the log. <br>
     *
     * @param fileName  name to be used for the captured screenshot.
     *                  Not mandatory, a new filename will be generated, if not provided.
     */
    @RobotKeyword
    @ArgumentNames({"fileName=NONE"})
    public void captureScreenshot(String fileName) {
        File logDir = RobotLogger.getLogDir();
        File destFile = new File(logDir, normalizeFilename(fileName));

        WebDriver webDriver = driverManager.getCurrent();
        if (webDriver.getClass().toString().contains("HtmlUnit")) {
            RobotLogger.warn("HTMLunit is not supporting screenshots.");
            return;
        }

        try {
            ExpectedCondition<Boolean> pageLoadCondition = driver -> ((JavascriptExecutor)driver)
                    .executeScript("return document.readyState").equals("complete");
           /* new WebDriverWait(webDriver,20)
                    .until(ExpectedConditions.jsReturnsValue("return document.readyState")).equals("complete");*/
           new WebDriverWait(webDriver, 30).until(pageLoadCondition);

            File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, destFile);
            RobotLogger.html(String.format(
                    "</td></tr><tr><td colspan=\"3\"><a href=\"%s\"><img src=\"%s\" width=\"800px\"></a>", destFile, destFile));
        } catch (IOException e) {
            RobotLogger.warn(String.format("Can't write screenshot '%s'", destFile.getAbsolutePath()));
        } catch (Exception e) {
            RobotLogger.warn("Issue capturing screenshot");
        }
    }

    private String normalizeFilename(String filename) {
        if (filename == null) {
            filename = String.format("sc-%d.png", System.currentTimeMillis());
        }
        return filename.replace('/', File.separatorChar);
    }
}
