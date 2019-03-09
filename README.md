# Project Title
This project has consists of parts:

* Ui Test Automation: e-commerce website
* Rest Test Api: 
* Load Test: bbc.com

## Getting Started

Readme contains limited information to protect the task details

### Prerequisites for Test Execution

* Download and activate Lombok plugin (for IntelliJ)
* Download chromedriver compatible with project Selenium version
* Update from properties files

## Running the Tests

* Ui and Api tests:
  * Either tru IDE as JUnit tests
  * Or, with command: `./gradlew clean build -Dtest=automation-task-tests` 
* Load test is not included in the Gradle task, it can be run with Jmeter or IDE-Jmeter plugin
    