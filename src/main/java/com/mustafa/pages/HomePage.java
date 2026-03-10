package com.mustafa.pages;

import com.mustafa.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * HomePage - Enuygun.com ana sayfa için Page Object
 */
public class HomePage extends BasePage {
    
    // Locators - İleride eklenecek
    private final By cookieAcceptButton = By.id("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll");
    
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
     */
    @Step("Accept cookies if present")
    public HomePage acceptCookies() {
        try {
            if (isElementVisible(cookieAcceptButton)) {
                click(cookieAcceptButton);
                logger.info("Cookies accepted");
            }
        } catch (Exception e) {
            logger.info("Cookie dialog not found or already accepted");
        }
        return this;
    }
}
