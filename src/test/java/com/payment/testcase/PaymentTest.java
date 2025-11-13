package com.payment.testcase;

import com.payment.base.BaseClass;
import com.payment.pageobjects.CartPage;
import com.payment.pageobjects.HomePage;
import com.payment.pageobjects.PaymentPage;
import com.payment.pageobjects.ProductPage;
import org.testng.annotations.Test;

/**
 * Main test class to automate the Flipkart payment journey.
 * Extends BaseClass to inherit setup and teardown.
 */
public class PaymentTest extends BaseClass {

    @Test(description = "Automate Flipkart payment flow (Login -> Search -> Cart -> Pay -> OTP Page)")
    public void paymentJourney_LoggedInUser() {
        
        System.out.println("--- Test Case: paymentJourney_LoggedInUser START ---");
        
        // Page Objects are initialized as needed
        HomePage homePage = new HomePage();
        
        // 1. Home Page: Perform login
        //homePage.performLogin();
        
        // 2. Home Page: Search for product
        ProductPage productPage = homePage.searchForProduct();
        
        // 3. Product Page: Select product and add to cart
        CartPage cartPage = productPage.selectAndAddToCart();
        
        // 4. Cart Page: Proceed to payment (confirms address)
        PaymentPage paymentPage = cartPage.proceedToPayment();

        // 5. Payment Page: Enter card details and submit
        paymentPage.enterCardDetailsAndReachOTP(
            prop.getProperty("TEST_CARD_NUMBER"),
            prop.getProperty("TEST_EXP_MONTH"),
            prop.getProperty("TEST_EXP_YEAR"),
            prop.getProperty("TEST_CVV")
        );
        
        System.out.println("--- Test Case: paymentJourney_LoggedInUser END ---");
    }
}