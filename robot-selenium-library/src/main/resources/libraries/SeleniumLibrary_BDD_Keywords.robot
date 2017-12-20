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