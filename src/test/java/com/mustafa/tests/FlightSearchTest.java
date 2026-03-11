package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.config.ConfigReader;
import com.mustafa.pages.HomePage;
import com.mustafa.pages.SearchResultsPage;
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
    public void testBasicFlightSearchAndTimeFilter() throws InterruptedException {
        logger.info("TEST: Case 1 - Basic Flight Search and Time Filter Started");
        
        // Arrange - Get test data from config
        String origin = ConfigReader.getProperty("flight.origin");
        String destination = ConfigReader.getProperty("flight.destination");
        String departureDate = ConfigReader.getProperty("flight.departure.date");
        String returnDate = ConfigReader.getProperty("flight.return.date");
        
        logger.info("Test Data - Origin: " + origin + ", Destination: " + destination);
        logger.info("Test Data - Departure: " + departureDate + ", Return: " + returnDate);
        
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
        
        // Select departure and return dates
        homePage.selectDepartureDate(departureDate)
                .selectReturnDate(returnDate);
        
        logger.info("TEST: Departure and return dates selected successfully");
        
        // Ensure hotel checkbox is unchecked (otel listesi istemiyoruz)
        homePage.ensureHotelCheckboxUnchecked();
        
        logger.info("TEST: Hotel checkbox verified as unchecked");
        
        // Click search button to find flights
        homePage.clickSearchButton();
        
        logger.info("TEST: Search button clicked - Navigating to results page");
        
        // Search Results Page'e geç
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        
        // Verify results page is loaded (URL ve element kontrolü içerir)
        Assert.assertTrue(resultsPage.isResultsPageLoaded(),
            "Search results page should be loaded with correct URL");
        
        logger.info("TEST: Search results page loaded successfully");
        
        // Apply time filter: 10:00 - 18:00
        logger.info("TEST: Applying departure time filter (10:00 - 18:00)");
        
        resultsPage.expandDepartureTimeFilter()
                   .setDepartureTimeFilter(10, 18)
                   .waitForFiltersToApply();
        
        logger.info("TEST: Departure time filter applied successfully");
        
        // Verify filtered results - TÜM uçuşların kalkış saatlerini kontrol et
        logger.info("TEST: Verifying ALL flights have departure times between 10:00 - 18:00");
        
        boolean allFlightsInRange = resultsPage.verifyDepartureTimesInRange(10, 18);
        
        Assert.assertTrue(allFlightsInRange, 
            "All flights should have departure times between 10:00 - 18:00");
        
        logger.info("TEST: All flights verified - time filter working correctly");
        
        // Verify flight destinations match config (origin and destination cities)
        logger.info("TEST: Verifying flight destinations match config");
        
        boolean destinationsMatch = resultsPage.verifyFlightDestinations(origin, destination);
        
        Assert.assertTrue(destinationsMatch,
            "All flights should match origin: " + origin + " and destination: " + destination);
        
        logger.info("TEST: All flight destinations verified successfully");
        
        logger.info("TEST: Case 1 - Basic Flight Search and Time Filter Completed");
    }
}
