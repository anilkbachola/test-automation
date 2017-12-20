package com.rise.autotest.robot;

public class FatalException extends RuntimeException {

    //Fatal exception, stop execution of remaining steps and quits
    public static final boolean ROBOT_EXIT_ON_FAILURE = true;

    public FatalException() {
    }

    public FatalException(String message) {
        super(message);
    }

    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }
}
