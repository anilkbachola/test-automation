package com.rise.autotest.robot.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.UUID;

class WebDriverManagerTest {

    private WebDriverManager webDriverManager = WebDriverManager.instance();
    private static final String ALIAS = "Test";
    private static final String ANOTHER_ALIAS = "AnotherTest";

    @BeforeEach
    void clean() {
        webDriverManager.closeAll();
    }

    @Test
    void testRegister_WhenDriverNull_ShouldThrowException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> webDriverManager.register(null, ALIAS))
                .withMessage("WebDriver should be instantiated before calling register");
    }

    @Test
    void testRegister_WhenAliasNull_ShouldRegisterDriver() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, null);
        assertThat(sessionId).isNotEmpty();
    }

    @Test
    void testRegister_WhenAliasNotNull_SessionShouldMatchWithAlias() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);
        assertThat(sessionId).isNotEmpty();
        assertThat(UUID.nameUUIDFromBytes(ALIAS.getBytes()).toString()).isEqualTo(sessionId);
    }

    @Test
    void testRegister_WhenSuccess_SessionShouldMatchGetCurrentSession() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);
        assertThat(sessionId).isNotEmpty();
        assertThat(webDriverManager.getCurrentSessionId()).isEqualTo(sessionId);
    }

    @Test
    void testGetCurrent_WhenAtLeastOneDriverRegistered_ShouldReturnCurrentDriver() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        WebDriver anotherWebDriver = mock(FirefoxDriver.class);
        String anotherSessionId = webDriverManager.register(anotherWebDriver, ANOTHER_ALIAS);

        assertThat(webDriverManager.getCurrent()).isNotNull();
        assertThat(webDriverManager.getCurrent()).isEqualTo(anotherWebDriver);
    }

    @Test
    void testGetCurrent_WhenNoDriverRegistered_ShouldReturnNull() {
        assertThat(webDriverManager.getCurrent()).isNull();
    }

    @Test
    void testGetCurrentSession_WhenAtLeastOneDriverRegistered_ShouldReturnCurrentSession() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        assertThat(webDriverManager.getCurrentSessionId()).isNotNull();
    }

    @Test
    void testGetCurrentSession_WhenNoDriverRegistered_ShouldReturnNull() {
        assertThat(webDriverManager.getCurrentSessionId()).isNull();
    }

    @Test
    void testClose_WhenAtLeastOneDriverRegistered_ShouldCloseCurrentDriver() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        WebDriver anotherWebDriver = mock(FirefoxDriver.class);
        String anotherSessionId = webDriverManager.register(anotherWebDriver, ANOTHER_ALIAS);

        webDriverManager.close();

        verifyZeroInteractions(webDriver);
        verify(anotherWebDriver, times(1)).quit();
        assertThat(webDriverManager.size()).isEqualTo(1);
    }

    @Test
    void testCloseAll_WhenAtLeastOneDriverRegistered_ShouldCloseAll() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        WebDriver anotherWebDriver = mock(FirefoxDriver.class);
        String anotherSessionId = webDriverManager.register(anotherWebDriver, ANOTHER_ALIAS);

        webDriverManager.closeAll();

        verify(webDriver, times(1)).quit();
        verify(anotherWebDriver, times(1)).quit();
        assertThat(webDriverManager.size()).isEqualTo(0);
        assertThat(webDriverManager.getCurrentSessionId()).isNull();
    }

    @Test
    void testSwitchBrowser_WhenOnltOneDriverRegistered_ShouldNotSwitch() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        webDriverManager.switchBrowser("Hello");

        assertThat(webDriverManager.getCurrentSessionId()).isEqualTo(sessionId);
    }

    @Test
    void testSwitchBrowser_WhenMoreThanOneDriverRegistered_And_AliasProvided_ShouldSwitch() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        WebDriver anotherWebDriver = mock(FirefoxDriver.class);
        String anotherSessionId = webDriverManager.register(anotherWebDriver, ANOTHER_ALIAS);

        webDriverManager.switchBrowser(ALIAS);

        assertThat(webDriverManager.getCurrentSessionId()).isEqualTo(sessionId);
    }

    @Test
    void testSwitchBrowser_WhenMoreThanOneDriverRegistered_And_SessionIdProvided_ShouldSwitch() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        WebDriver anotherWebDriver = mock(FirefoxDriver.class);
        String anotherSessionId = webDriverManager.register(anotherWebDriver, ANOTHER_ALIAS);

        webDriverManager.switchBrowser(sessionId);

        assertThat(webDriverManager.getCurrentSessionId()).isEqualTo(sessionId);
    }

    @Test
    void testSwitchBrowser_WhenMoreThanOneDriverRegistered_And_NonExistingSessionId_ShouldNotSwitch() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        WebDriver anotherWebDriver = mock(FirefoxDriver.class);
        String anotherSessionId = webDriverManager.register(anotherWebDriver, ANOTHER_ALIAS);

        webDriverManager.switchBrowser(UUID.randomUUID().toString());

        assertThat(webDriverManager.getCurrentSessionId()).isEqualTo(anotherSessionId);
    }

    @Test
    void testSize_WhenNoDriverRegistered_ShouldReturnZero() {
        assertThat(webDriverManager.size()).isEqualTo(0);
    }

    @Test
    void testSize_WhenAtLeastOneDriverRegistered_ShouldReturnCorrectSize() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);
        assertThat(webDriverManager.size()).isEqualTo(1);
    }

    @Test
    void testSize_WhenAtLeastOneDriverRegistered_And_DriverClosed_ShouldReturnCorrectSize() {
        WebDriver webDriver = mock(FirefoxDriver.class);
        String sessionId = webDriverManager.register(webDriver, ALIAS);

        WebDriver anotherWebDriver = mock(FirefoxDriver.class);
        String anotherSessionId = webDriverManager.register(anotherWebDriver, ANOTHER_ALIAS);

        webDriverManager.close();
        assertThat(webDriverManager.size()).isEqualTo(1);
    }

}