*** Settings ***
Documentation       Behavior-driven keywords, extended from core keywords
Library             DatabaseLibrary

*** Keywords ***
I open connection with properties
    [Documentation]    Opens a new connection. Refer to `Open Connection` core keyword.
    [Arguments]    ${configFile}
    Open Connection    ${configFile}

I close the connection
    [Documentation] close the connection
    Close Connection