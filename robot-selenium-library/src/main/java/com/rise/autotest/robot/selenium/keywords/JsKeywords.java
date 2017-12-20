package com.rise.autotest.robot.selenium.keywords;

import org.openqa.selenium.JavascriptExecutor;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class JsKeywords extends SeleniumBase {


    @RobotKeyword
    @ArgumentNames({ "*scriptLine" })
    public Object executeJavascript(String... scriptLines) {
        String js = String.join(", ", scriptLines);
        return ((JavascriptExecutor) driverManager.getCurrent()).executeScript(js);
    }

    @RobotKeyword
    @ArgumentNames({"*scriptLine"})
    public Object executeAsyncJavascript(String... scriptLines) {
        String js = String.join(", ", scriptLines);
        return ((JavascriptExecutor) driverManager.getCurrent()).executeAsyncScript(js);
    }

    @RobotKeyword
    @ArgumentNames({ "scriptFile" })
    public Object executeJavascriptFromFile(String scriptFile) {
        return null; //TODO: implement this
    }

    @RobotKeyword
    @ArgumentNames({ "scriptFile" })
    public Object executeAsyncJavascriptFromFile(String scriptFile) {
        return null; //TODO: implement this
    }


}
