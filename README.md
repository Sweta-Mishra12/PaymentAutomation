PaymentAutomation: Flipkart Logged-in Checkout Flow

This project is a robust Selenium TestNG automation suite, built using the Page Object Model (POM) architecture, designed to test the full end-to-end checkout flow on Flipkart.

Crucially, this script handles the modern Flipkart login process which requires a hover action and a manual OTP step for security.

⚙️ Project Architecture

The project adheres to the Page Object Model (POM) pattern, utilizing a standard Maven directory structure:

Package

Responsibility

com.payment.base

Framework Base: Contains BaseClass.java for setting up and tearing down the WebDriver, loading configurations, and providing helper utilities (like the Selenium Actions class for hovering).

com.payment.pageobjects

Page Objects: Stores all classes representing pages (HomePage.java, LoginPage.java for OTP), containing only element locators and page-specific actions.

com.payment.testcase

Test Scripts: Contains the main TestNG class, PaymentTest.java, which defines the execution flow by orchestrating calls to the Page Objects.

src/test/resources/TestData

Configuration: Holds testdata.properties for storing environment-specific variables and user credentials.

🚀 Step-by-Step Execution Guide

Prerequisites

You must have the following installed on your system:

Java Development Kit (JDK 11+)

Apache Maven

An IDE (Eclipse or IntelliJ IDEA is highly recommended)

The TestNG Plugin for your IDE (required for running via right-click).

Step 1: Clone the Repository

Clone this project to your local machine:

git clone 
cd PaymentAutomation


Step 2: Update Maven Dependencies

Open your IDE (Eclipse or IntelliJ) and load the project. You may need to refresh or re-import the project to ensure all dependencies from the pom.xml are downloaded.

Step 3: Configure Login Credentials (MANDATORY)

The script will fail if it cannot start the login process. You must provide a valid Flipkart mobile number or email.

Navigate to: Payment page class under pageobject package 

Update the mobile number property with your registered mobile number or email in this **enterMobileNumber** method:

# Use your actual Flipkart registered mobile number or email
LOGIN_USERNAME=your-real-mobile-number-or-email


Step 4: Run the Test Suite

You can execute the entire test suite using either your IDE or the command line.

Option A: Run via IDE (TestNG Suite)

In your IDE, right-click on the testng.xml file located in the project root.

Select Run As → 1 TestNG Suite.

Option B: Run via Maven Command Line

Open your terminal and navigate to the root PaymentAutomation directory.

Execute the Maven test command:

mvn clean install test


🚨 CRITICAL: Manual OTP Intervention

Due to security measures, the test execution will pause to allow you to log in manually.

The script pauses after calling the loginPage.enterMobileNumber(username) method.

Script Pauses: The browser will be open on the Flipkart login page, with your mobile number/email entered, and the "Request OTP" button clicked.

Manual Login: Check your device for the OTP. Manually enter the OTP into the browser and click the "Verify" or "Login" button to complete the authentication.

Resume Script: Once you are successfully logged in, immediately return to your console/terminal window (where the script is running) and press the ENTER key.

The automation will then resume, completing the product search, cart addition, and payment page navigation.
