package com.rise.autotest.robot.util;

import com.rise.autotest.robot.FailureException;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RobotLogger {

    private static String logDir = null;
    private static final String LOG_LONG_MESSAGE_COMMAND  = new StringBuilder()
            .append("from __future__ import with_statement\n")
            .append("\n")
            .append("with open('%s', 'r') as msg_file:\n")
            .append("    msg = msg_file.read()\n")
            .append("    logger.%s(msg%s)")
            .toString();

    private static final String LOG_MESSAGE_COMMAND = "logger.%s('%s'%s)";

    private RobotLogger() {

    }

    public static File getLogDir() {
        if (logDir == null
                && !loggingPythonInterpreter.get().eval("EXECUTION_CONTEXTS.current").toString().equals("None")) {
            PyString logDirName = (PyString) loggingPythonInterpreter.get()
                    .eval("BuiltIn().get_variables()['${LOG FILE}']");
            if (logDirName != null && !(logDirName.asString().equalsIgnoreCase("NONE"))) {
                return new File(logDirName.asString()).getParentFile();
            }
            logDirName = (PyString) loggingPythonInterpreter.get().eval("BuiltIn().get_variables()['${OUTPUTDIR}']");
            return new File(logDirName.asString()).getParentFile();
        } else {
            return new File(logDir);
        }
    }

    public static void trace(String msg) {
        log(msg, LogType.TRACE);
    }

    public static void debug(String msg) {
        log(msg, LogType.DEBUG);
    }

    public static void info(String msg) {
        log(msg, LogType.INFO);
    }

    public static void html(String msg) {
        log(msg, LogType.HTML);
    }

    public static void warn(String msg) {
        log(msg, LogType.WARN);
    }

    public static void error(String msg) {
        log(msg, LogType.ERROR);
    }

    private static void log(String msg, LogType logType) {
        String[] methodParams = logType.getParams();
        msg = String.valueOf(msg);
        if (msg.length() > 1024) {
            // Message is too large.There is a hard limit of 100k in the Jython source code parser
            File tempFile;
            try {
                tempFile = File.createTempFile("SeleniumLibrary", ".log");
                tempFile.deleteOnExit();
                logLongMessage(msg, methodParams, tempFile);
            } catch (IOException e) {
                throw new FailureException("Error trying to log message", e);
            }
        } else {
            logShortMessage(msg, methodParams);
        }
    }

    private static void logLongMessage(String message, String[] methodParams, File tempFile) {
        try (FileWriter writer = new FileWriter(tempFile)){
            // Write message to temp file
            writer.write(message);
            // Read the message in Python back and log it.
            loggingPythonInterpreter.get().exec(String.format(
                    LOG_LONG_MESSAGE_COMMAND, tempFile.getAbsolutePath().replace("\\", "\\\\"),
                    methodParams[0], methodParams[1]));

        } catch (IOException e) {
            throw new FailureException("Error trying to log message", e);
        }
    }

    private static void logShortMessage(String message, String[] methodParams) {
        // Message is small enough to get parsed by Jython
        loggingPythonInterpreter.get().exec(String.format(LOG_MESSAGE_COMMAND, methodParams[0],
                message.replace("\\", "\\\\").replace("'", "\\'")
                        .replace("\n", "\\n"), methodParams[1]));
    }


    private static ThreadLocal<PythonInterpreter> loggingPythonInterpreter = ThreadLocal.withInitial(() -> {
        PythonInterpreter pythonInterpreter = new PythonInterpreter();
        pythonInterpreter.exec(
                "from robot.libraries.BuiltIn import BuiltIn; from robot.running.context import EXECUTION_CONTEXTS; from robot.api import logger;");
        return pythonInterpreter;
    });

    private enum LogType {
        DEBUG(new String[] { "debug", "" }),
        HTML(new String[] { "info", ", True, False" }),
        INFO(new String[] { "info", "" }),
        TRACE(new String[] { "info", "" }),
        WARN(new String[] { "info", "" }),
        ERROR(new String[] { "info", "" });

        final String[] params;
        LogType(String[] params) {
            this.params = params;
        }

        public String[] getParams() {
            return params;
        }
    }
}
