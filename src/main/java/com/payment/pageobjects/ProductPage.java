package com.payment.pageobjects;

import com.payment.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for Product Search Results Page.
 * Handles selecting a product and adding it to the cart.
 */
public class ProductPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator for the first product link in the search results
    // This is a generic XPath, might need adjustment for specific layouts
    private final By firstProductLink = By.xpath("//a[@rel='noopener noreferrer' and @target='_blank']");
    
    // Locator for the "ADD TO CART" button on the product details page
    private final By addToCartButton = By.xpath("//button[text()='Add to cart']");

    public ProductPage() {
        this.driver = BaseClass.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
    }

    /**
     * Clicks the first product in the list, switches to the new tab, and adds to cart.
     * @return CartPage object.
     */
    public CartPage selectAndAddToCart() {
        System.out.println("Selecting first product from search results...");
        
        // 1. Wait for product links to be visible and click the first one
        WebElement productLink = wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductLink));
        productLink.click();
        
        // 2. Handle the new tab
        List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowHandles.get(1)); // Switch to the new tab
        
        System.out.println("Switched to product details tab. Adding to cart...");
        
        // 3. Wait for "ADD TO CART" button and click it
        WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        cartButton.click();
        
        // 4. Wait for the cart page/modal element (e.g., "Place Order") to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Place Order']")));
        
        System.out.println("Product added to cart.");
        return new CartPage();
    }
}