package com.rise.autotest.robot.selenium;

import com.rise.autotest.robot.CustomAnnotationLibrary;

import java.io.File;

/**
 * Library to expose selenium keywords
 */
public class SeleniumLibrary extends CustomAnnotationLibrary {

    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
    private static final String KEYWORDS_PATTERN = SeleniumLibrary.class.getPackage().getName().replaceAll("\\.",
            File.separator)+ "/keywords/**/*.class";
    private static final String KEYWORDS_DOC_FILE = "SeleniumLibrary.robodoc";

    /**
     * Initializes the Robot Selenium library.
     * Parses and adds keywords in "/keywords/*" and its sub packages for classes annotated with {@link org.robotframework.javalib.annotation.RobotKeywords}
     */
    public SeleniumLibrary() {
        super(KEYWORDS_PATTERN, KEYWORDS_DOC_FILE);
    }

}
