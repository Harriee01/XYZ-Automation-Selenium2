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