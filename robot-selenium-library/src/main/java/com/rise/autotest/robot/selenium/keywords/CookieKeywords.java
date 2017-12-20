package com.rise.autotest.robot.selenium.keywords;

import com.rise.autotest.robot.FailureException;
import org.openqa.selenium.Cookie;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Robot Keywords for Browser Cookie Management
 */
@RobotKeywords
public class CookieKeywords extends SeleniumBase {

    /**
     * Deletes all cookies.<br>
     */
    @RobotKeyword
    public void deleteAllCookies() {
        driverManager.getCurrent().manage().deleteAllCookies();
    }

    /**
     * Deletes cookie matching <b>name</b>.<br>
     * <br>
     * If the cookie is not found, nothing happens<br>
     *
     * @param name The name of the cookie to delete.
     */
    @RobotKeyword
    @ArgumentNames({ "name" })
    public void deleteCookie(String name) {
        driverManager.getCurrent().manage().deleteCookieNamed(name);
    }

    /**
     * Returns all cookies of the current page.<br>
     *
     * @return All cookies of the current page.
     */
    @RobotKeyword
    public String getCookies() {
        StringBuilder sb = new StringBuilder();

        ArrayList<Cookie> cookies = new ArrayList<>(driverManager.getCurrent().manage().getCookies());
        for (int i = 0; i < cookies.size(); i++) {
            sb.append(cookies.get(i).getName() )
                    .append("=")
                    .append(cookies.get(i).getValue());
            if (i != cookies.size() - 1) {
                sb.append("; ");
            }
        }

        return sb.toString();
    }

    /**
     * Returns value of cookie found with <b>name</b>.<br>
     * <br>
     * If no cookie is found with name, this keyword fails.<br>
     *
     * @param name The name of the cookie
     * @return The value of the found cookie
     */
    @RobotKeyword
    @ArgumentNames({ "name" })
    public String getCookieValue(String name) {
        Cookie cookie = driverManager.getCurrent().manage().getCookieNamed(name);

        if (cookie != null) {
            return cookie.getValue();
        } else {
            throw new FailureException(String.format("Cookie with name %s not found.", name));
        }
    }

    /**
     * Add a new cookie with given <b>name</b> and <b>value</b>.<br>
     *
     * @param name cookie name
     * @param value cookie value
     */
    @RobotKeywordOverload
    public void addCookie(String name, String value) {
        addCookie(name, value, null);
    }

    /**
     * Add a new cookie with given <b>name</b>, <b>value</b> and <b>path</b>.<br>
     * @param name  cookie name
     * @param value cookie value
     * @param path  cookie path
     */
    @RobotKeywordOverload
    public void addCookie(String name, String value, String path) {
        addCookie(name, value, path, null);
    }

    /**
     * Add a new cookie with given <b>name</b>, <b>value</b> and <b>path</b> in a given <b>domain</b>.<br>
     * @param name  cookie name
     * @param value cookie value
     * @param path  cookie path
     * @param domain domain to add to
     */
    @RobotKeywordOverload
    public void addCookie(String name, String value, String path, String domain) {
        addCookie(name, value, path, domain, "");
    }

    /**
     * Add a new cookie with given <b>name</b>, <b>value</b> and <b>path</b> in a given <b>domain</b>.<br>
     * @param name  cookie name
     * @param value cookie value
     * @param path  cookie path
     * @param domain domain to add to
     * @param secure    denotes a secure cookier or not. Supported values: 'true' or 'false'
     */
    @RobotKeywordOverload
    public void addCookie(String name, String value, String path, String domain, String secure) {
        addCookie(name, value, path, domain, secure, null);
    }

    /**
     * Add a new cookie with given <b>name</b>, <b>value</b> and <b>path</b> in a given <b>domain</b>.<br>
     * @param name  cookie name
     * @param value cookie value
     * @param path  cookie path
     * @param domain domain to add to
     * @param secure    denotes a secure cookie or not. Supported values: 'true' or 'false'
     * @param expiry    cookie expiration time
     */
    @RobotKeyword
    @ArgumentNames({ "name", "value", "path=NONE", "domain=NONE", "secure=NONE", "expiry=NONE" })
    public void addCookie(String name, String value, String path, String domain, String secure, String expiry) {
        Date expireTime = null;
        if(expiry != null) {
            try {
                SimpleDateFormat formatter =  new SimpleDateFormat("yyyyMMdd HHmmss");
                expireTime = formatter.parse(expiry);
            } catch (ParseException e) {
                throw new FailureException(
                    String.format("Failed to parse the expire date string '%s'. Expected format is '%s'",
                            expiry, "yyyyMMdd HHmmss")
                );
            }
        }

        Cookie cookie = new Cookie(name, value, domain, path, expireTime, "true".equalsIgnoreCase(secure));
        driverManager.getCurrent().manage().addCookie(cookie);
    }

}
