package com.payment.pageobjects;

import com.payment.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object for Flipkart Home Page.
 * Handles login and product search.
 * NOTE: The login process has been updated to handle OTP-based authentication.
 */
public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // Locators for Search
    private final By searchInput = By.xpath("//input[contains(@placeholder,'Search for Products')]");
    private final By searchButton = By.cssSelector("button[type='submit']");
    
    // Locators for Login Modal (OTP Flow)
    private final By usernameInput = By.xpath("//input[@type='text' and not(@name)]"); 
    private final By requestOTPButton = By.xpath("//button/span[text()='Request OTP']");
    private final By loginModalCloseButton = By.xpath("//button[text()='✕']"); 

    public HomePage() {
        this.driver = BaseClass.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
    }

  

    /**
     * Searches for a product.
     * @return ProductPage object.
     */
    public ProductPage searchForProduct() {
        String product = BaseClass.prop.getProperty("SEARCH_PRODUCT");
        System.out.println("Searching for product: " + product);
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        searchBox.sendKeys(product);
        driver.findElement(searchButton).click();
        return new ProductPage();
    }
}