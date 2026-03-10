package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.pages.HomePage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * FlightSearchTest - Uçuş arama ve zaman filtresi testleri
 * Case 1: Basic Flight Search and Time Filter
 */
@Epic("Flight Search")
@Feature("Basic Search and Time Filter")
public class FlightSearchTest extends BaseTest {
    
    @Test(priority = 1, description = "Case 1 - Basic Flight Search and Time Filter")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Temel uçuş arama ve zaman filtresi testi")
    @Story("As a user, I should be able to search for flights and apply time filters")
    public void testBasicFlightSearchAndTimeFilter() {
        logger.info("TEST: Case 1 - Basic Flight Search and Time Filter Started");
        
        // Navigate to Enuygun.com
        HomePage homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageLoaded(), 
            "Enuygun.com home page should be loaded");
        
        // Accept cookies if present
        homePage.acceptCookies();
        
        logger.info("TEST: Successfully navigated to Enuygun.com");
        
        // TODO: Flight search and time filter steps will be added here
        
        logger.info("TEST: Case 1 - Basic Flight Search and Time Filter Completed");
    }
}
