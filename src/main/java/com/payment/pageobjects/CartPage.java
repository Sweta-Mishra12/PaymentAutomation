package com.payment.pageobjects;

import com.payment.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object for the Cart Page.
 * Handles proceeding to the payment step.
 */
public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator for "Place Order" button on the cart page
    private final By placeOrderButton = By.xpath("//span[text()='Place Order']");
    
    // Locator for "Deliver Here" button on the address selection page
    // Logged-in users will see this step.
    private final By deliverHereButton = By.xpath("//button[text()='Deliver Here']");

    public CartPage() {
        this.driver = BaseClass.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
    }

    /**
     * Clicks "Place Order" and confirms the delivery address.
     * @return PaymentPage object.
     */
    public PaymentPage proceedToPayment() {
        System.out.println("Proceeding from cart to payment...");
        
        // 1. Click "Place Order" from the cart
        WebElement orderButton = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
        orderButton.click();
        
        // 2. On the Address page, confirm by clicking "Deliver Here"
        // This assumes the user has a default address saved
        try {
            WebElement deliverButton = wait.until(ExpectedConditions.elementToBeClickable(deliverHereButton));
            deliverButton.click();
            System.out.println("Confirmed delivery address.");
        } catch (Exception e) {
            System.out.println("Could not find 'Deliver Here' button, assuming address step was different or skipped.");
        }
        
        return new PaymentPage();
    }
}