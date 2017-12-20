package com.rise.autotest.robot.db;

import com.rise.autotest.robot.CustomAnnotationLibrary;

import java.io.File;

public class DatabaseLibrary extends CustomAnnotationLibrary {

    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

    private static final String KEYWORDS_PATTERN = DatabaseLibrary.class.getPackage().getName().replaceAll("\\.",
            File.separator)+"/keywords/**/*.class";
    private static final String KEYWORDS_DOC_FILE = "DatabaseLibrary.robodoc";

    /**
     * Initializes the Robot Database library.
     * Parses and adds keywords in "/keywords/*" and its sub packages for classes annotated with {@link org.robotframework.javalib.annotation.RobotKeywords}
     */
    public DatabaseLibrary() {
        super(KEYWORDS_PATTERN, KEYWORDS_DOC_FILE);
    }
}
