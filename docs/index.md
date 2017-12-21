# Test Automation Framework


## Introduction

   This project is implemented to provide a robust framework for implementing automated test suites.
   
   The goal is to write to automated test suites without having to write any code and any non-technical user should be able write tests in plain english.
   
## About Robot Framework

   **Robot Framework** is a generic test automation framework for acceptance testing and acceptance test-driven development (ATDD).
   It has easy-to-use tabular test data syntax and it utilizes the keyword-driven testing approach.
   
   Its testing capabilities can be extended by test libraries implemented either with Python or Java, and users can create new higher-level keywords from existing ones using the same syntax that is used for creating test cases.
   
   Robot Framework is operating system and application independent. The core framework is implemented using Python and runs also on Jython (JVM) and IronPython (.NET)


## Implemented Libraries

1. **Database Library**

   {% include_relative DatabaseLibrary.md %}
   
   ### Keyword Documentation
   
   + [Core Keywords Documentation](DatabaseLibrary/keywords/DatabaseLibrary-core.html)
   + [BDD Style Keywords Documentation](DatabaseLibrary/keywords/DatabaseLibrary-bdd.html)
   + [JavaDoc](DatabaseLibrary/javadoc/index.html)
   

2. **Selenium Library**

   {% include_relative SeleniumLibrary.md %}
   
   ## Keyword Documentation
    
   + [Core Keywords Documentation](SeleniumLibrary/keywords/SeleniumLibrary-core.html)
   + [BDD Style Keywords Documentation](SeleniumLibrary/keywords/SeleniumLibrary-bdd.html)
   + [JavaDoc](SeleniumLibrary/javadoc/index.html)   
   
