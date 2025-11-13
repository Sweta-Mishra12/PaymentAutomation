package com.payment.pageobjects;

import com.payment.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object for the Payment Page.
 * Handles selecting a payment method and entering details.
 */
public class PaymentPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // --- Locators for Pre-Payment Steps (Login/Address) ---
    // Improved: Using attribute-based or text-based locators for stability
    private final By mobileNumberInput = By.xpath("//input[@type='text' and @autocomplete='off']"); // Target based on attributes
    private final By ContinueButton = By.xpath("//button/span[text()='CONTINUE']");
    private final By login = By.xpath("//button/span[text()='Login']");
    
    // The previous locator using class name was good, keeping it stable
    private final By deliverHereButton = By.xpath("//button[@class='QqFHMw FA45gW _7Pd1Fp']"); 
    
    // More stable: targeting the 'TO PAYMENT' button by its text or role
    private final By lastContinueButton = By.xpath("//button/span[text()='CONTINUE']"); // This may overlap with other Continue buttons
    // If the above fails, use a more specific text-based locator for the payment final button:
    // private final By lastContinueButton = By.xpath("//button[text()='CONTINUE']"); 

    private final By acceptContinueButton = By.xpath("//button[text()='ACCEPT AND CONTINUE']");

    // --- Locators for Payment Details ---
    // FIXED SYNTAX ERROR: Removed extra backslashes. Using a text-based locator for stability is highly recommended.
    // If text fails, use this corrected structural XPath:
    private final By creditCardOption = By.xpath("//*[@id=\"container\"]/div[2]/div/section[1]/div/div/div/section/div/div[3]/div[1]/div/div/div/span");
    // RECOMMENDED STABLE ALTERNATIVE (Test this first):
    // private final By creditCardOption = By.xpath("//div[text()='Credit / Debit Card']"); 
    
    // Iframe Locator
    private final By cardIframe = By.xpath("//*[@id=\"right-section\"]");
    
    // Improved: Locators for inputs inside the Payment Card view (adjust based on actual element attributes)
    private final By cardNumberInput = By.xpath("//*[@id=\"cc-input\"]"); // Assuming the input field uses 'card_number' name inside the iframe
    private final By expiryMonthInput = By.xpath("//*[@id=\"cards\"]/div/div[2]/div[1]/input"); // Assuming name attribute is used
    //private final By expiryYearInput = By.name("expiry_year"); // Assuming name attribute is used
    private final By cvvInput = By.xpath("//input[@id='cvv-input']"); // Using static ID 
    
    // Improved: Target the Pay button by its text
    private final By payButton = By.xpath("//button[text()='PAY']");

    public PaymentPage() {
        this.driver = BaseClass.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
    }
    
    /**
     * Enters the mobile number, clicks Continue, and pauses for 30 seconds 
     * for manual OTP/Password entry and Login click.
     * @param mobNumber The mobile number to enter.
     */
    public void enterMobileNumber(String mobNumber) {
        System.out.println("Entering mobile number: " + mobNumber);
        try {
            // Wait for and enter the mobile number/email
            WebElement mobileField = wait.until(ExpectedConditions.visibilityOfElementLocated(mobileNumberInput));
            // Using the passed parameter 'mobNumber' (or email if that's what is expected)
            mobileField.sendKeys(Your number); 

            // Click the main Continue button
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(ContinueButton));
            continueBtn.click();
            System.out.println("Mobile number entered and Continue clicked.");

            // Wait for the login button to appear (this is the OTP/Password page)
            wait.until(ExpectedConditions.elementToBeClickable(login));
            
            // --- CRITICAL MANUAL WAITING POINT (30 SECONDS) ---
            System.out.println("TEST PAUSED: Waiting 30 seconds for Manual OTP/Password entry and Login click.");
            Thread.sleep(Duration.ofSeconds(30).toMillis());
            // --- END MANUAL WAITING POINT ---

            // Check if the login button is still visible/present after the 30s wait, 
            // and if so, click it (assuming the user entered OTP but didn't click login).
            if (!driver.findElements(login).isEmpty()) {
                 wait.until(ExpectedConditions.elementToBeClickable(login)).click();
                 System.out.println("Login button clicked after wait.");
            } else {
                 System.out.println("Login button not found after wait. Assuming manual login succeeded and page advanced.");
            }
                     
        } catch (Exception e) {
            System.out.println("Error during mobile number entry: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Handles the sequence of clicks to navigate from the address/contact page to the payment page.
     * Includes checking if the address step was skipped before attempting clicks.
     */
    public void navigateToPaymentSection() {
        System.out.println("Navigating through address confirmation to the Payment page...");
        
        // CHECK 1: If payment options are already visible, skip address steps
        if (!driver.findElements(creditCardOption).isEmpty()) {
            System.out.println("Payment options (Credit Card) are already visible. Skipping address steps.");
            return;
        }

        try {
            // Wait for the deliver here button to appear
            WebElement deliverBtn = wait.until(ExpectedConditions.elementToBeClickable(deliverHereButton));
            
            // 1. Click "DELIVER HERE"
            deliverBtn.click();
            System.out.println("Clicked 'DELIVER HERE'.");
            
            // 2. Click the final "CONTINUE" button to proceed to payment options
            WebElement finalContinueBtn = wait.until(ExpectedConditions.elementToBeClickable(lastContinueButton));
            finalContinueBtn.click();
            System.out.println("Clicked final 'CONTINUE' to reach payment options.");
            
            // 3. Optional: Handle the 'Accept and Continue' button if it appears
            if (!driver.findElements(acceptContinueButton).isEmpty()) {
                WebElement acceptBtn = wait.until(ExpectedConditions.elementToBeClickable(acceptContinueButton));
                acceptBtn.click();
                System.out.println("Clicked 'Accept and Continue' (if present).");
            }
            
        } catch (Exception e) {
            System.out.println("Error during navigation to payment section (Address step): " + e.getMessage());
            
            // FINAL CHECK: Verify if we landed on the payment page anyway
             if (!driver.findElements(creditCardOption).isEmpty()) {
                System.out.println("Address step failed, but payment options (Credit Card) are now visible. Proceeding with payment.");
            } else {
                 throw e; // Re-throw the exception if we still aren't on the payment page
            }
        }
    }

    /**
     * Selects Credit Card, enters test card details, and clicks Pay.
     * @param cardNumber Test card number
     * @param expMonth Expiry month (MM)
     * @param expYear Expiry year (YY)
     * @param cvv Test CVV
     */
    public void enterCardDetailsAndReachOTP(String cardNumber, String expMonth, String expYear, String cvv) {
        
        // --- PRE-PAYMENT STEPS ---
        String dummyMobileNumber = "9999999999"; 
        
        enterMobileNumber(dummyMobileNumber);
        navigateToPaymentSection();
        // -------------------------

        System.out.println("Entering payment details...");
        
        try {
            // 1. Select "Credit/Debit/ATM Card"
            WebElement cardOption = wait.until(ExpectedConditions.elementToBeClickable(creditCardOption));
            cardOption.click();
            
            // 2. Wait for and switch to the card number iframe
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(cardIframe));
            
            // 3. Enter Card Number
            WebElement cardInput = wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberInput));
            cardInput.sendKeys(cardNumber);
            
            // 4. Switch back to default content
            driver.switchTo().defaultContent();
            
            // The remaining fields should now be visible in the default content, 
            // BUT ensure the locators (name/xpath) are correct for fields OUTSIDE the iframe.

            // 5. Enter Expiry Month
            WebElement monthInput = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryMonthInput));
            monthInput.sendKeys(expMonth);
            
            // 6. Enter Expiry Year
//            WebElement yearInput = wait.until(ExpectedConditions.visibilityOfElementLocated(expiryYearInput));
//            yearInput.sendKeys(expYear);
            
            // 7. Enter CVV
            WebElement cvvField = wait.until(ExpectedConditions.visibilityOfElementLocated(cvvInput));
            cvvField.sendKeys(cvv);
            
            // 8. Click "Pay"
            WebElement payBtn = wait.until(ExpectedConditions.elementToBeClickable(payButton));
            payBtn.click();
            
            // 9. Wait for the 3D Secure/Bank OTP page to load
            wait.until(ExpectedConditions.not(ExpectedConditions.titleContains("Flipkart")));
            
            System.out.println("Successfully reached the 3D Secure / OTP Page.");
            System.out.println("Current Page Title: " + driver.getTitle());

        } catch (Exception e) {
            System.out.println("Error during payment details entry: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
