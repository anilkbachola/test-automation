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
    
    <dependency>
       <groupId>com.rise.autotest</groupId>
       <artifactId>robot-util</artifactId>
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

## Documentation

   [Selenium Library](https://anilkbachola.github.io/test-automation/) documentation.

