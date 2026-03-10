package com.mustafa.pages;

import com.mustafa.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * HomePage - Enuygun.com ana sayfa için Page Object
 */
public class HomePage extends BasePage {
    
    // Locators
    private final By cookieAcceptButton = By.id("onetrust-accept-btn-handler");
    
    // Flight Search Form Locators
    private final By roundTripRadioButton = By.cssSelector("span[name='flightTrip']");
    private final By originInput = By.cssSelector("[data-testid='endesign-flight-origin-autosuggestion-input']");
    private final By originFirstOption = By.cssSelector("[data-testid='endesign-flight-origin-autosuggestion-option-item-0']");
    private final By destinationInput = By.cssSelector("[data-testid='endesign-flight-destination-autosuggestion-input']");
    private final By destinationFirstOption = By.cssSelector("[data-testid='endesign-flight-destination-autosuggestion-option-item-0']");
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Ana sayfanın yüklendiğini doğrula
     */
    @Step("Verify home page is loaded")
    public boolean isHomePageLoaded() {
        logger.info("Verifying home page is loaded");
        return driver.getCurrentUrl().contains("enuygun.com");
    }
    
    /**
     * Cookie kabul et (varsa)
     * OneTrust cookie consent dialog'ındaki "KABUL ET" butonuna tıklar
     */
    @Step("Accept cookies if present")
    public HomePage acceptCookies() {
        try {
            if (isElementVisible(cookieAcceptButton)) {
                click(cookieAcceptButton);
                logger.info("Cookies accepted via OneTrust dialog");
                Thread.sleep(1000); // Cookie dialog'un kapanması için kısa bir bekleme
            }
        } catch (Exception e) {
            logger.info("Cookie dialog not found or already accepted");
        }
        return this;
    }
    
    /**
     * Gidiş-Dönüş (Round Trip) seçeneğini seç
     */
    @Step("Select round trip option")
    public HomePage selectRoundTrip() {
        try {
            click(roundTripRadioButton);
            logger.info("Round trip option selected");
            Thread.sleep(500);
        } catch (Exception e) {
            logger.warn("Could not click round trip button, might be already selected: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Kalkış noktası gir ve ilk seçeneği seç
     */
    @Step("Enter origin city: {origin}")
    public HomePage enterOrigin(String origin) {
        try {
            click(originInput);
            Thread.sleep(300);
            type(originInput, origin);
            logger.info("Origin entered: " + origin);
            Thread.sleep(1000);
            
            waitForElement(originFirstOption);
            click(originFirstOption);
            logger.info("First origin option selected");
            Thread.sleep(500);
        } catch (Exception e) {
            logger.error("Error entering origin: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Varış noktası gir ve ilk seçeneği seç
     */
    @Step("Enter destination city: {destination}")
    public HomePage enterDestination(String destination) {
        try {
            click(destinationInput);
            Thread.sleep(300);
            type(destinationInput, destination);
            logger.info("Destination entered: " + destination);
            Thread.sleep(1000);
            
            waitForElement(destinationFirstOption);
            click(destinationFirstOption);
            logger.info("First destination option selected");
            Thread.sleep(500);
        } catch (Exception e) {
            logger.error("Error entering destination: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Uçuş arama formu görünür mü kontrol et
     */
    public boolean isFlightSearchFormVisible() {
        return isElementVisible(originInput) && isElementVisible(destinationInput);
    }
}
