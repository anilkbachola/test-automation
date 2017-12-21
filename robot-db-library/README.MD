# Robot Framework Database Library

## Introduction

   Robot Framework `DatabaseLibrary` is `Java` implementation library for writing tests against database.
   
   Provides ready-to-use `keywords` for database testing and uses `jdbc` under the cover for implementation.
   
   Library is fully-documented and can be extended with custom composite keywords.
   
   For more information on `Robot Framework` visit //TODO and `Selenium` visit 
   
   Tested With:
   
   ```text
   Robot Framework: 3.0.2
   ```
   
## Usage

   +  Add this library as a dependency to the project hosting the test suites.
   
      Maven:
      ```xml
       <dependency>
           <groupId>com.rise.autotest</groupId>
           <artifactId>robot-db-library</artifactId>
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
      
      __dbtests_suite.robot__
      ```text
      *** Settings ***
      Documentation       Database Tests
      Library             DatabaseLibrary
      Suite Setup         Log    Suite setup
      Test Setup          Log    Test setup
      Test Teardown       Log    Test Teardown
      Suite Teardown      Close All Connections
      
      *** Test Cases ***
      [TC-001]-Test Setup
          [Documentation]    Sample robot database test file
          [Tags]    Acceptance
          Open Connection     db.properties   true
          Execute Query    create table if not exists users(user_id varchar(50), user_name varchar(50), email varchar(250))
          Execute Query    insert into users(user_id,user_name,email) values('jamesbond007','jamesbond','james.bond@007.com')
          I Open Connection With Properties     db.conf
          I close the connection
          [Teardown]    Close All Connections  
      ```
## Documentation

   [Database Library](https://anilkbachola.github.io/test-automation/) documentation.
      