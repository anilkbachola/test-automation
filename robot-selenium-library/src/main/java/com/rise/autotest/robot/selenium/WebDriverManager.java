package com.rise.autotest.robot.selenium;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Manages web driver instances. Keeps a cache of all open/running instances
 */
public class WebDriverManager {

    private static WebDriverManager webDriverManager;
    private Map<UUID, WebDriverTuple> sessionIdDriverMap = new TreeMap<>();
    private Deque<UUID> sessionIdStack = new ArrayDeque<>();

    /**
     * private constructor to hide default constructor
     */
    private WebDriverManager() { }

    /**
     * initialize the instance of this class to be singleton
     * @return this instance {@code WebDriverManager}
     */
    public static synchronized WebDriverManager instance() {
        if(webDriverManager == null) {
            synchronized (WebDriverManager.class) {
                if(webDriverManager == null) {
                    webDriverManager = new WebDriverManager();
                }
            }
        }
        return webDriverManager;
    }

    /**
     * Register a new driver and assign the driver session with a session id
     * @param webDriver driver instance to register
     * @param alias alias for register
     * @return session id for this driver
     */
    public synchronized String register(WebDriver webDriver, String alias) {
        if(webDriver == null ) {
            throw new IllegalArgumentException("WebDriver should be instantiated before calling register");
        }
        UUID sessionId = newSessionId(alias);
        WebDriverTuple webDriverTuple = new WebDriverTuple(sessionId, webDriver, alias);
        sessionIdDriverMap.put(sessionId, webDriverTuple);
        sessionIdStack.push(sessionId);
        return sessionId.toString();
    }

    /**
     * Return the current web driver
     * @return current {@code WebDriver} instance
     */
    public WebDriver getCurrent() {
        if(sessionIdStack.isEmpty()) {
            return null;
        }
        WebDriverTuple webDriverTuple = sessionIdDriverMap.get(sessionIdStack.peek());
        return webDriverTuple == null ? null : webDriverTuple.getWebDriver();
    }

    /**
     * return session id associated with current web driver.
     * @return current session id
     */
    public String getCurrentSessionId() {
        return sessionIdStack.isEmpty() ? null : sessionIdStack.peek().toString();
    }

    /**
     * Close the current webdriver instance and window
     */
    public void close() {
        if(!sessionIdStack.isEmpty()) {
            UUID sessionId = sessionIdStack.pop();
            WebDriver webDriver = sessionIdDriverMap.get(sessionId).getWebDriver();
            webDriver.quit();
            sessionIdDriverMap.remove(sessionId);
        }
    }

    /**
     * Closed the browser associated with <b>sessionIdOrAlias</b>
     * @param sessionIdOrAlias session id or alias
     */
    public void close(String sessionIdOrAlias) {
        UUID sessionId = fromSessionIdOrAlias(sessionIdOrAlias);
        if(sessionId != null) {
            WebDriver webDriver = sessionIdDriverMap.get(sessionId).getWebDriver();
            webDriver.quit();
            sessionIdDriverMap.remove(sessionId);
            sessionIdStack.remove(sessionId);
        }
    }

    /**
     * Close all web driver instances
     */
    public void closeAll() {
        Iterator<Map.Entry<UUID, WebDriverTuple>> iterator = sessionIdDriverMap.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<UUID, WebDriverTuple> entry = iterator.next();
            entry.getValue().getWebDriver().quit();
            iterator.remove();
        }
        sessionIdStack.clear();
    }

    /**
     * Make another driver as current. Accepts alias or sessionId as argument
     * @param sessionIdOrAlias switch another instance based on session id or alias
     */
    public void switchBrowser(String sessionIdOrAlias) {
        UUID sessionId = fromSessionIdOrAlias(sessionIdOrAlias);
        if(sessionId != null && sessionIdDriverMap.containsKey(sessionId)) {
            sessionIdStack.remove(sessionId);
            sessionIdStack.push(sessionId);
        }
    }

    /**
     * returns the number of web driver sessions active
     * @return number of active driver sessions
     */
    public int size() {
        return sessionIdDriverMap.size();
    }

    /**
     * Cleanup the state of this manager.
     */
    public void cleanup() {
        closeAll();
        sessionIdStack.clear();
        sessionIdDriverMap.clear();
    }

    private UUID newSessionId(String alias) {
        return alias!= null && !alias.isEmpty() ? UUID.nameUUIDFromBytes(alias.getBytes()): UUID.randomUUID();
    }

    private UUID fromSessionIdOrAlias(String sessionIdOrAlias) {
        UUID sessionId = null;
        if(sessionIdOrAlias != null && !sessionIdOrAlias.isEmpty()) {
            try {
                sessionId = UUID.fromString(sessionIdOrAlias);
            } catch (IllegalArgumentException ex) {
                //ignore, passed value is an alias
                sessionId = UUID.nameUUIDFromBytes(sessionIdOrAlias.getBytes());
            }
        }
       return sessionId;
    }

    @Getter
    @AllArgsConstructor
    private static class WebDriverTuple {

        @NonNull
        private UUID sessionId;

        @NonNull
        private WebDriver webDriver;

        private String alias;
    }
}