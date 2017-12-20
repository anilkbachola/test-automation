package com.rise.autotest.robot.selenium.keywords;

import org.openqa.selenium.JavascriptExecutor;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class JsKeywords extends SeleniumBase {

    /**
     * Execute given javascript.
     * @param scriptLines script code lines
     * @return  result of the javascript execution.
     */
    @RobotKeyword
    @ArgumentNames({ "*scriptLine" })
    public Object executeJavascript(String... scriptLines) {
        String js = String.join(", ", scriptLines);
        return ((JavascriptExecutor) driverManager.getCurrent()).executeScript(js);
    }

    /**
     * Execute given javascript asynchronously
     * @param scriptLines script code lines
     * @return  result of the javascript execution.
     */
    @RobotKeyword
    @ArgumentNames({"*scriptLine"})
    public Object executeAsyncJavascript(String... scriptLines) {
        String js = String.join(", ", scriptLines);
        return ((JavascriptExecutor) driverManager.getCurrent()).executeAsyncScript(js);
    }

    /**
     * Execute the javascript from a file
     * @param scriptFile script file path
     * @return result of the javascript execution.
     */
    @RobotKeyword
    @ArgumentNames({ "scriptFile" })
    public Object executeJavascriptFromFile(String scriptFile) {
        return null; //TODO: implement this
    }


    /**
     * Execute the javascript from a file asynchronously
     * @param scriptFile script file path
     * @return result of the javascript execution.
     */
    @RobotKeyword
    @ArgumentNames({ "scriptFile" })
    public Object executeAsyncJavascriptFromFile(String scriptFile) {
        return null; //TODO: implement this
    }


}
