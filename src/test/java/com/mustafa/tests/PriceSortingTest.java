package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.pages.HomePage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * PriceSortingTest - Turkish Airlines için fiyat sıralama testleri
 * Case 2: Price Sorting for Turkish Airlines
 */
@Epic("Flight Search")
@Feature("Price Sorting")
public class PriceSortingTest extends BaseTest {
    
    @Test(priority = 1, description = "Case 2 - Price Sorting for Turkish Airlines")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description: Turkish Airlines için fiyat sıralama testi")
    @Story("As a user, I should be able to sort Turkish Airlines flights by price")
    public void testPriceSortingForTurkishAirlines() {
        logger.info("TEST: Case 2 - Price Sorting for Turkish Airlines Started");
        
        // Navigate to Enuygun.com
        HomePage homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageLoaded(), 
            "Enuygun.com home page should be loaded");
        
        // Accept cookies if present
        homePage.acceptCookies();
        
        logger.info("TEST: Successfully navigated to Enuygun.com");
        
        // TODO: Price sorting steps will be added here
        
        logger.info("TEST: Case 2 - Price Sorting for Turkish Airlines Completed");
    }
}
