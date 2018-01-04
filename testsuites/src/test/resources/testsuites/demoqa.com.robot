*** Settings ***
Documentation   SeleniumLibrary showcase using demoqa.com as test site
Library     SeleniumLibrary
Suite Setup     Log     Execution Started
Suite Teardown  Log     Execution Completed

*** Variables ***
${url}      http://demoqa.com/
${browser}      chrome


*** Test Cases ***
[TC001] Launch Test Demo
    [Documentation]     Test launch of the website
    [Tags]      Regression      Acceptance
    [Setup]  Log    setup task
    Open Browser   ${url}    ${browser}
    Location Should Be    http://demoqa.com/
    [Teardown]    Close Browser

[TC002] Launch with specific capabilities
    [Documentation]    Test launch browser with specific capabilities
    [Tags]    Acceptance
    [Setup]    Log   setup
    Open Browser    ${url}    ${browser}    browser1    {"capabilities": {"platformName": "MAC", "goog:chromeOptions": {"args": ["--headless"] }, "loggingPrefs": {"driver": "INFO", "browser": "DEBUG"}, "timeouts": {"script": 10000, "pageLoad": 11000, "implicit": 12000 }, "version": "56"} }
    [Teardown]    Close Browser


[TC003] Browser aliases, session id and switching sessions
    [Documentation]    Test launch browser with specific capabilities
    [Tags]    Demo
    Open Browser   ${url}    ${browser}    demoqa
    Title Should Contain    Demoqa
    Open Browser   http://www.google.com    ${browser}    google
    Title Should Contain    Google
    Switch Browser    demoqa
    Location Should Be    http://demoqa.com/
    ${sessionId} =     Open Browser    http://www.amazon.com    ${browser}
    Title Should Contain    Amazon
    Switch Browser    google
    Close Browser    ${sessionId}
    Title Should Contain    Google
    Close Browser    demoqa
    [Teardown]    Close All Browsers


[TC004] Navigation Test Demo
    [Documentation]     Test navigation of website
    [Tags]      Regression      Acceptance
    Open Browser    ${url}    ${browser}
    Click Link    css=#menu-item-158 > a
    Location Should Be    http://demoqa.com/about-us/
    Page Should Contain Text    About us
    Go Back
    Click Link    css=#menu-item-155 > a
    Location Should Be    http://demoqa.com/services/
    Page Should Contain Text    Services
    Go Back
    Click Link    css=#menu-item-66 > a
    Click Link    css=#menu-item-73 > a
    Location Should Be    http://demoqa.com/draggable/
    Capture Screenshot
    [Teardown]    Close Browser
