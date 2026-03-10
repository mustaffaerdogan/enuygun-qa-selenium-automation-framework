package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.config.ConfigReader;
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
        
        // Arrange - Get test data from config
        String origin = ConfigReader.getProperty("flight.origin");
        String destination = ConfigReader.getProperty("flight.destination");
        
        logger.info("Test Data - Origin: " + origin + ", Destination: " + destination);
        
        // Navigate to Enuygun.com
        HomePage homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageLoaded(), 
            "Enuygun.com home page should be loaded");
        
        // Accept cookies if present
        homePage.acceptCookies();
        
        logger.info("TEST: Successfully navigated to Enuygun.com");
        
        // Act - Fill flight search form
        homePage.selectRoundTrip();
        
        // Verify search form is visible
        Assert.assertTrue(homePage.isFlightSearchFormVisible(),
            "Flight search form should be visible");
        
        // Enter origin and destination
        homePage.enterOrigin(origin)
                .enterDestination(destination);
        
        logger.info("TEST: Origin and destination entered successfully");
        
        // TODO: Date selection and search button click will be added in next step
        // TODO: Time filter steps will be added after search results page
        
        logger.info("TEST: Case 1 - Basic Flight Search and Time Filter Completed");
    }
}
