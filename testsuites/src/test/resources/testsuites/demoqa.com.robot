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

[TC002] Navigation Test Demo
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
