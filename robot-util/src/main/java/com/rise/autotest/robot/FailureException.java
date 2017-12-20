package com.rise.autotest.robot;

public class FailureException extends RuntimeException {
    //Just marks the test as failed, continues with next steps.
    public static final boolean ROBOT_EXIT_ON_FAILURE = false;

    public FailureException(String message) {
        super(message);
    }

    public FailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailureException(Throwable cause) {
        super(cause);
    }
}
