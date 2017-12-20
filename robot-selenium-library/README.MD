# Robot Framework Selenium Library

## Introduction

Robot Framework `SeleniumLibrary` is `Java` implementation library for web testing that leverages Selenium 3 libraries.

Provides read to use `keywords` for web testing using selenium and also provides __extension__ keywords for `behavior-driven` style of writing tests.

Library is fully-documented and can be extended with custom composite keywords.

For more information on `Robot Framework` visit //TODO and `Selenium` visit 

Tested With:

```text
Selenium: 3.7.1
Robot Framework: 3.0.2
```

## Usage

   +  Add this library as a dependency to the project hosting the test suites.

   Maven:
   ```xml
    <dependency>
        <groupId>com.rise.autotest</groupId>
        <artifactId>robot-selenium-library</artifactId>
        <version>1.0</version>
    </dependency>
   ```

   + Import the library into your test suites file and start using the keywords. 

      For full list of keywords refer to `Keyword Documentation` section.
   
      __Google_TestSuite.robot__
      ```text
       *** Settings ***
       Documentation       Google.com Automation Test Suite
       Library             SeleniumLibrary
       Suite Setup         Log    Starting execution
       Suite Teardown      Close All Browsers
     
       *** Variables ***
       ${Google URL}      http://www.google.com
       ${Browser}      chrome
     
       *** Test Cases ***
       [TC-001]-Open Google.com
           [Documentation]    Test to verify site opens
           [Tags]    Regression    Acceptance
           [Setup]  Log    Test-specific setup
           Launch Google.com
           Location Should Be    https://www.google.com/
           Title Should Contain    Google
           [Teardown]    Close Browser
         
       *** Keywords ***
       Launch Google.com
           Open Browser    ${Google URL}     ${Browser}   goog1
     ```

## Extending Keywords

   One of the best feature of `Robot Framework` is its ability to `extend` and `compose` keywords. 
   Robot Framework supports writing custom keywords into its own Keyword `Resource` files, which can be later imported into Test suites.
   
   The keywords in this library can be extended are composed in to a more generic keywords to be used in test suites.
   This Library show-cases a sample of extending the core keywords to behavior-driven style keywords and also showcases how to generate documentation for these extended keywords.

   Below is a sample resource file with extended keywords.
   
   __SeleniumLibrary_BDD_Keywords.robot__
   ```text
    *** Settings ***
    Documentation       Behavior-driven keywords, extended from core keywords
    Library             SeleniumLibrary
    
    *** Keywords ***
    I open chrome browser and navigate to
        [Documentation]    Opens chrome browser session and navigate to the given URL
        [Arguments]    ${url}
        Open Browser    ${url}    CHROME
    
    I open Firefox browser and navigate to
        [Documentation]    Opens firefox browser session and navigate to the given URL
        [Arguments]    ${url}
        Open Browser    ${url}    FIREFOX
   ```

## Keyword Documentation

   Click for [Core Keywords](../docs/SeleniumLibrary-core.html) documentation.
   
   Click for [BDD Keywords](../docs/SeleniumLibrary-bdd.html) documentation.

## About Web Driver

WebDriver is a remote control interface that enables introspection and control of user agents. 
It provides a platform- and language-neutral wire protocol as a way for out-of-process programs to remotely instruct the behavior of web browsers.

W3C Web Driver Candidate Spec: https://w3c.github.io/webdriver/webdriver-spec.html


### WebDriver Capabilities

#### Introduction

WebDriver capabilities are used to communicate the  features supported by a given implementation.
The local end may use capabilities to define which features it requires the remote end to satisfy when creating a new session. 
Likewise, the remote end uses capabilities to describe the full feature set for a session.

Below is a list of standard capabilities, that each implementation MUST support. 
WebDriver Spec#capabilities: https://w3c.github.io/webdriver/webdriver-spec.html#capabilities

**Standard WebDriver Capabilities**

|  Capability Key           | Value Type| Description               |
| ------------------------- | --------- | ------------------------- |
| browserName               | String    | Identifies the user agent |
| browserVersion            | String    | Identifies the version of the user agent |
| platformName              | String    | Identifies the operating system of the endpoint node |
| acceptInsecureCerts       | Boolean   | Indicates whether untrusted and self-signed TLS certificates are implicitly trusted for the duration of the session |
| pageLoadStrategy          | String    | Defines current session's page load strategy |
| proxy                     | JSON      | Defines current session's proxy configuration |
| setWindowRect             | Boolean   | Indicates whether the remote end supports all of the commands in Resizing and Positioning Windows |
| timeouts                  | JSON      | Describes the timeouts imposed on certain session operations |
| unhandledPromptBehavior   | String    | Describes the current session’s user prompt handler |



**"timeouts" Object**

|  Capability Key           | Value Type| Description               |
| ------------------------- | --------- | ------------------------- |
| script                    | Number    | Determines when to interrupt a script that is being evaluated |
| pageLoad                  | Number    | Provides the timeout limit used to interrupt navigation of the browsing context |
| implicit                  | Number    | Gives the timeout of when to abort locating an element |


**"proxy" Object**

| Key                   | Value Type| Description               |   Valid Values    |
| --------------------- | --------- | ------------------------- | ----------------- |
| proxyType             | string    | Indicates the type of proxy configuration | "pac", "direct", "autodetect", "system", or "manual". |
| proxyAutoconfigUrl    | string    | URL for a proxy auto-config file if proxyType is equal to "pac" | Any URL |
| ftpProxy              | string    | proxy host for FTP traffic when the proxyType is "manual" | A host and optional port for scheme "ftp" |
| httpProxy             | string    | proxy host for HTTP traffic when the proxyType is "manual" | A host and optional port for scheme "http" |
| noProxy               | string    | Lists the address for which the proxy should be bypassed when the proxyType is "manual" | A List containing any number of Strings |
| sslProxy              | string    | proxy host for encrypted TLS traffic when the proxyType is "manual" | A host and optional port for scheme "https" |
| socksProxy            | string    | proxy host for a SOCKS proxy when the proxyType is "manual" | A host and optional port with an undefined scheme |
| socksVersion          | number    | the SOCKS proxy version when the proxyType is "manual" | Any integer between 0 and 255 inclusive |
    

**additional capabilities**

|  Capability Key           | Value Type| Description               |
| ------------------------- | --------- | ------------------------- |
| takesScreenshot           | boolean   | screenshot capability |
| javascriptEnabled         | boolean   | 
| handlesAlerts             | boolean   |
| databaseEnabled           | boolean   |
| locationContextEnabled    | boolean   |
| applicationCacheEnabled   | boolean   | 
| networkConnectionEnabled  | boolean   |
| cssSelectorsEnabled       | boolean   | 
| webStorageEnabled         | boolean   | 
| rotatable                 | boolean   | 
| applicationName           | string    |
| acceptSslCerts            | boolean   |
| nativeEvents              | boolean   | has native events support |
| unexpectedAlertBehaviour  | string    | Same as unexpectedPromptBehaviour, Valid values: Default (Indicates the behavior is not set), Ignore, Accept, Dismiss. |
| elementScrollBehavior     | string    | Top or Bottom |
| hasTouchScreen            | boolean   |  
| overlappingCheckDisabled  | boolean   |
| loggingPrefs              | loggingprefs object |
| webdriver.logging.profiler.enabled | boolean | 
| 

#### Firefox(geckodriver) Capabilities

geckodriver also supports capabilities with the `moz:` prefix, which can be used to define Firefox-specific capabilities.

**moz:firefoxOptions**

|  Capability Key           | Value Type| Description               |
| ------------------------- | --------- | ------------------------- |
| binary                    | String    | Absolute path of the Firefox binary |
| args                      | Array     | Command line arguments to pass to the Firefox binary. These must include the leading dash (-) where required, e.g. ["-devtools"] |
| profile                   | String    | Base64-encoded ZIP of a profile directory to use for the Firefox instance. |
| log                       | Log       | To increase the logging verbosity of geckodriver and Firefox, you may pass a log object that may look like {"log": {"level": "trace"}} to include all trace-level logs and above. |
| prefs                     | Prefs     | Map of preference name to preference value, which can be a string, a boolean or an integer |


**moz:webdriverClick**

A boolean value to indicate which kind of interactability checks to run when performing a click on elements.

**"log" Object**

|  Key                      | Value Type| Description               |
| ------------------------- | --------- | ------------------------- |
|   level                   | String    | Set the level of verbosity of geckodriver and Firefox. Available levels are trace, debug, config, info, warn, error, and fatal. If left undefined the default is info |


**"prefs" Object**

|  Key                      | Value Type| Description               |
| ------------------------- | --------- | ------------------------- |
| preference name           | String, number, boolean    | One entry per preference to override |


The following example selects a specific Firefox binary to run with a prepared profile from the filesystem in headless mode (available on certain systems and recent Firefoxen)

```json
{
"capabilities": {
    "moz:firefoxOptions": {
        "binary": "/usr/local/firefox/bin/firefox",
        "args": ["-headless", "-profile", "/path/to/my/profile"],
        "prefs": {
            "dom.ipc.processCount": 8
        },
        "log": {
            "level": "trace"
        }
    }  
  }
}
```

System.setProperty("webdriver.gecko.driver", "/Users/bachola/_tools/geckodriver");

#### Chrome(chromeDriver) Capabilities

chrome driver also supports capabilities with `goog:` prefix, which can be used to define Chrome-specific capabilities.

**goog:chromeOptions**

|  Capability Key   | Value Type| Description               |
| ----------------- | --------- | ------------------------- |
| args              | string    | List of command-line arguments to use when starting Chrome. Arguments with an associated value should be separated by a '=' sign (e.g., ['start-maximized', 'user-data-dir=/tmp/temp_profile']). See here for a list of Chrome arguments |
| binary            | string    | Path to the Chrome executable to use (on Mac OS X, this should be the actual binary, not just the app. e.g., '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome') |
| extensions        | list of strings    | A list of Chrome extensions to install on startup. Each item in the list should be a base-64 encoded packed Chrome extension (.crx) |
| localState        | dictionary | A dictionary with each entry consisting of the name of the preference and its value. These preferences are applied to the Local State file in the user data folder |
| prefs             | dictionary | A dictionary with each entry consisting of the name of the preference and its value. These preferences are only applied to the user profile in use. See the 'Preferences' file in Chrome's user data directory for examples. |
| detach            | boolean false | If false, Chrome will be quit when ChromeDriver is killed, regardless of whether the session is quit. If true, Chrome will only be quit if the session is quit (or closed). |
| debuggerAddress   | string    | An address of a Chrome debugger server to connect to, in the form of <hostname/ip:port>, e.g. '127.0.0.1:38947' |
| excludeSwitches   | list of strings | List of Chrome command line switches to exclude that ChromeDriver by default passes when starting Chrome.  Do not prefix switches with --. |
| minidumpPath      | string    | Directory to store Chrome minidumps . (Supported only on Linux.) |
| mobileEmulation   | dictionary    | A dictionary with either a value for “deviceName,” or values for “deviceMetrics” and “userAgent.” Refer to Mobile Emulation for more information. |
| perfLoggingPrefs  | dictionary | An optional dictionary that specifies performance logging preferences. |
| windowTypes       | list of strings | A list of window types that will appear in the list of window handles. For access to <webview> elements, include "webview" in this list. |


**perfLoggingPrefs Object**

|  Name                     | Value Type| Description               |
| ------------------------- | --------- | ------------------------- |
| enableNetwork           | boolean, true | Whether or not to collect events from Network domain. |
| enablePage      | boolean, true | Whether or not to collect events from Page domain. |
| enableTimeline | boolean, true | Whether or not to collect events from Timeline domain. Note: when tracing is enabled, Timeline domain is implicitly disabled, unless enableTimeline is explicitly set to true. |
| tracingCategories | string | A comma-separated string of Chrome tracing categories for which trace events should be collected. An unspecified or empty string disables tracing. |
| bufferUsageReportingLevel | positive integer, 1000 | The requested number of milliseconds between DevTools trace buffer usage events. For example, if 1000, then once per second, DevTools will report how full the trace buffer is. If a report indicates the buffer usage is 100%, a warning will be issued. |

**loggingPrefs Object**

|  Key      | Value Type | Description               |
| --------- | ---------  | ------------------------- |
| driver    | String     | Driver logging level, valid values "INFO", "DEBUG" |
| browser   | String     | Browser logging level, valid values "INFO", "DEBUG" |


```json
{
"capabilities": {
    "goog:chromeOptions": {
        "args": ["--headless"]
    },
    "loggingPrefs": {
        "driver": "INFO", 
        "browser": "INFO"
    }
}
}
```

System.setProperty("webdriver.chrome.driver", "/Users/bachola/_tools/chromedriver");

#### Internet Explorer

|  Capability Key   | Value Type| Description               |
| ----------------- | --------- | ------------------------- |
| ignoreProtectedModeSettings |  boolean   | value indicating whether to ignore the settings of the Internet Explorer Protected Mode. |
| ignoreZoomSetting | boolean, false | value indicating whether to ignore the zoom level of Internet Explorer |
| initialBrowserUrl | string | the initial URL displayed when IE is launched. |
| enablePersistentHover | boolean, true | value indicating whether to enable persistently sending WM_MOUSEMOVE messages to the IE window during a mouse hover.      |
| elementScrollBehavior | string |  value for describing how elements are scrolled into view in the IE driver. Defaults to scrolling the element to the top of the viewport.  valid values: top, bottom |
| requireWindowFocus    | boolean, false  | value indicating whether to require the browser window to have focus before interacting with elements.       |
| browserAttachTimeout | timespan | amount of time the driver will attempt to look for a newly launched instance of Internet Explorer. |
| ie.browserCommandLineSwitches | string | command line arguments used in launching Internet Explorer when the Windows CreateProcess API is used. |
| ie.forceCreateProcessApi | boolean, false | value indicating whether to force the use of the Windows CreateProcess API when launching Internet Explorer. |
| ie.usePerProcessProxy | boolean, false| value indicating whether to use the supplied proxy settings on a per-process basis, not updating the system installed proxy setting. This property is only valid when setting a proxy, where the `proxyType` property is either Direct, System or Manual |
| ie.ensureCleanSession | boolean, true | value indicating whether to clear the Internet Explorer cache before launching the browser. |
| ie.forceShellWindowsApi | boolean, false | value indicating whether to force the use of the Windows ShellWindows API when attaching to Internet Explorer. |
| ie.fileUploadDialogTimeout | timespan | amount of time the driver will attempt to look for the file selection dialog when attempting to upload a file. |
| ie.enableFullPageScreenshot | boolean, true | Deprecated. value indicating whether to enable full-page screenshots for the IE driver. |
| unexpectedAlertBehaviour  | string    | Same as unexpectedPromptBehaviour, Valid values: Default (Indicates the behavior is not set), Ignore, Accept, Dismiss. |
| ie.validateCookieDocumentType | string |

#### Safari

safari.options

|  Capability Key   | Value Type| Description               |
| ----------------- | --------- | ------------------------- |
| port              |  string   | default to 0 |
| technologyPreview | boolean   | default to false. |
| cleanSession      | boolean   | default to false. |


#### Opera

operaOptions

|  Capability Key   | Value Type| Description               |
| ----------------- | --------- | ------------------------- |
| args              | string    | List of command-line arguments to use when starting Chrome. Arguments with an associated value should be separated by a '=' sign (e.g., ['start-maximized', 'user-data-dir=/tmp/temp_profile']). See here for a list of Chrome arguments |
| binary            | string    | Path to the Chrome executable to use (on Mac OS X, this should be the actual binary, not just the app. e.g., '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome') |
| extensions        | list of strings    | A list of Chrome extensions to install on startup. Each item in the list should be a base-64 encoded packed Chrome extension (.crx) |


#### Edge

edgeOptions

#### Handling Capabilities in Selenium Library

This Selenium Library takes the capabilities json as one of the parameter to `Open Browser` keyword along with the browser name.

The capabilities string is optional to open browser session.
if no capabilities, default capabilities (`browserName`, `platformName`) will be applied based on the browserName provided.

Selenium handles the standard web driver capabilities and as well browser specific capabilities (options). 
Browser specific capabilities will need to be under specific key in the capabilities json. Below are the details:

```text
Firefox: "moz:firefoxOptions"
Google Chrome: "goog:chromeOptions"
Opera: "operaOptions"
Internet Explorer: "se:ieOptions"
Safari: "safari.options"
Edge: "edgeOptions"

```

This Selenium Library handles multiple browser options in single capabilities json, only the options relevant to the
parameter `browserName` will be picked and applied. 

For example the following capabilities json is having both firefox and chrome options along with standard capabilities
But only relevant options will be applied based on the `browserName` argument to the `Openn Browser` keyword.

```json
{
"capabilities": {
	"platformName": "MAC",
	"moz:firefoxOptions": {
		"prefs": {
			"dom.ipc.processCount": 8
		},
		"log": {
			"level": "trace"
		}
	},
	"goog:chromeOptions": {
		"args": ["--headless"]
	},
	"loggingPrefs": {
		"driver": "INFO", 
		"browser": "DEBUG"
	},
	"timeouts": {
		"script": 10000,
		"pageLoad": 11000,
		"implicit": 12000
	},
	"version": "56"
  }
}
```

### Locating UI Elements

Locating elements in Selenium can be done by using certain type of locators. 
The keywords in this Selenium Library takes 'locator' string as argument. 
The locator string will be in format `locatorType=locatorText (for xpath it can be of format //locatorText)`. `locatorType` is case-insensitive. 

Following types of locator strings are supported in this library.

#### Id Locator

This is the most efficient and preferred way to locate an element.

Example:
```html
<div id="coolestWidgetEvah">...</div>
``` 
Locator String:
```text
id=coolestWidgetEvah
```

#### Class Name Locator

“Class” in this case refers to the attribute on the DOM element. 
Often in practical use there are many DOM elements with the same class name, thus finding multiple elements becomes the more practical option over finding the first element.

Example:
```html
<div class="cheese"><span>Cheddar</span></div><div class="cheese"><span>Gouda</span></div>
```
Locator String:
```text
classname=cheese
```

#### TagName Locator
The DOM Tag Name of the element.

Example:
```html
<iframe src="..."></iframe>
```
Locator String:

```text
tag=iframe
```

#### Name Locator

Find the element with matching name attribute.

Example:
```html
<input name="cheese" type="text"/>
```
Locator String:
```text
name=cheese
```

#### LinkText Locator

Find the link element with matching visible text.

Example:
```html
<a href="http://www.google.com/search?q=cheese">cheese</a>>
```
Locator String:
```text
linktext=cheese

linktext=http://www.google.com/search?q=cheese

```

#### CSS Locator

Like the name implies it is a locator strategy by css. Native browser support is used by default, so please refer to w3c css selectors for a list of generally available css selectors. 
If a browser does not have native support for css queries, then Sizzle is used.

Beware that not all browsers were created equal, some css that might work in one version may not work in another.

Example:
```html
<div id="food"><span class="dairy">milk</span><span class="dairy aged">cheese</span></div>
```
Locator String:
```text
css=#food span.dairy.aged
```

#### XPath Locator
At a high level, WebDriver uses a browser’s native XPath capabilities wherever possible.
On those browsers that don’t have native XPath support, Selenium provided our own implementation.

This can lead to some unexpected behaviour unless you are aware of the differences in the various XPath engines.

Example:
```html
<input type="text" name="example" />
<INPUT type="text" name="other" />
```
Locator String:
```text
xpath=//input

//input - if a locator starts with '//' XPATH type is assumed.
```

#### IdOrName Locator

Find the element with matching name or id attribute.

Example:
```html
<input type="text" name="example" />
```

Locator String:
```text
IdOrName=example
```

#### Sizzle Locator

Refer to CSS Locator.



