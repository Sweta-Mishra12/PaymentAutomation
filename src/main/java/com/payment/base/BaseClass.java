package com.payment.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Base Class for the Test Framework.
 * Handles property loading, WebDriver initialization, and tear down.
 * All test classes MUST extend this class.
 */
public class BaseClass {
    public static Properties prop;
    // ThreadLocal ensures each thread (test) has its own WebDriver instance
    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Static block to load the configuration file once
    static {
        try {
            prop = new Properties();
            // Correct path based on your architecture
            FileInputStream ip = new FileInputStream(
                System.getProperty("user.dir") + "/src/test/resources/TestData/testdata.properties"
            );
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FATAL: Could not load testdata.properties file.");
        }
    }

    /**
     * Gets the WebDriver instance for the current thread.
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Initialization method run before every test method.
     * Sets up the browser and navigates to the URL.
     */
    @BeforeMethod
    public void setup() {
        String browserName = prop.getProperty("BROWSER", "Chrome");

        if (browserName.equalsIgnoreCase("Chrome")) {
            WebDriverManager.chromedriver().setup();
            driver.set(new ChromeDriver());
        } else if (browserName.equalsIgnoreCase("Firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver.set(new FirefoxDriver());
        } else {
             System.out.println("WARNING: Browser property not set. Using default Chrome browser.");
             WebDriverManager.chromedriver().setup();
             driver.set(new ChromeDriver());
        }

        // Apply global configuration settings
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        // Navigate to the starting URL
        String url = prop.getProperty("FLIPKART_URL");
        getDriver().get(url);
    }

    /**
     * Teardown method run after every test method.
     * Closes the browser and removes the driver from the thread.
     */
    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove(); // Remove the WebDriver instance from the thread
        }
    }
}