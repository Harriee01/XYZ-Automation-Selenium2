#  XYZ Bank Selenium Automation Framework

[![Java 21](https://img.shields.io/badge/Java-21-orange?logo=java)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.25-green?logo=selenium)](https://www.selenium.dev/)
[![Allure](https://img.shields.io/badge/Allure-2.27-purple)](https://allurereport.org/)
[![CI](https://github.com/Harriee01/XYZ-Automation-Selenium2/actions/workflows/ci.yml/badge.svg)](https://github.com/Harriee01/XYZ-Automation-Selenium2/actions)

> **Professional-grade Selenium test automation** for the [XYZ Bank AngularJS Demo App](https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login).  
> Built with Java 21, Maven, JUnit 5, Allure reporting, and GitHub Actions CI/CD.

---

## 📖 Overview

This project implements **16 automated test cases** across two user roles:

| Role | Test Cases | Scope |
|---|---|---|
| 🏛️ Bank Manager | 8 | Customer management, account creation, validation |
| 👤 Customer | 8 | Deposit, withdrawal, transaction history |

**Architecture:** Page Object Model (POM) with PageFactory, explicit AngularJS waits, Allure reporting, and Docker/CI headless execution.

---

## ⚙️ Prerequisites

| Tool | Version | Installation |
|---|---|---|
| Java JDK | 21 | [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=21) |
| Apache Maven | 3.9+ | [maven.apache.org](https://maven.apache.org/download.cgi) |
| Google Chrome | Latest stable | [google.com/chrome](https://www.google.com/chrome/) |
| Allure CLI | 2.27+ | `brew install allure` / [allurereport.org](https://allurereport.org/docs/install/) |
| Docker | 24+ (optional) | [docker.com](https://www.docker.com/) |

---

## 🚀 Running Tests Locally

### 1. Clone and install dependencies
```bash
git clone https://github.com//xyz-bank-selenium.git
cd xyz-bank-selenium
mvn dependency:resolve   # pre-download all dependencies
```

### 2. Run all tests (headed — browser visible)
```bash
mvn clean test
```

### 3. Run headless (no browser window)
```bash
mvn clean test -Dheadless=true
```

### 4. Run a specific test class
```bash
mvn clean test -Dtest=ManagerTests
mvn clean test -Dtest=CustomerTests
```

### 5. Run a specific test method
```bash
mvn clean test -Dtest=ManagerTests#tc1_addValidCustomer
```

---

## 📊 Viewing Allure Reports

After running tests, results land in `target/allure-results/`.
```bash
# Generate and open HTML report in browser
allure serve target/allure-results

# Or generate static HTML to share
allure generate target/allure-results --clean -o target/allure-report
open target/allure-report/index.html   # macOS
xdg-open target/allure-report/index.html  # Linux