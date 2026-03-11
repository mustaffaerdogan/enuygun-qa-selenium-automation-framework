package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.config.ConfigReader;
import com.mustafa.pages.HomePage;
import com.mustafa.pages.SearchResultsPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * PriceSortingTest - Fiyat sıralama testleri
 * Case 2: Price Sorting for Turkish Airlines
 */
@Epic("Flight Search")
@Feature("Price Sorting")
public class PriceSortingTest extends BaseTest {
    
    @Test(priority = 1, description = "Case 2 - Price Sorting for Turkish Airlines")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description: Fiyat sıralaması testi - Turkish Airlines uçuşları")
    @Story("As a user, I should be able to search flights and sort by price")
    public void testPriceSortingForTurkishAirlines() throws InterruptedException {
        logger.info("TEST: Case 2 - Price Sorting for Turkish Airlines Started");
        
        // Arrange - Get test data from config
        String origin = ConfigReader.getProperty("price.sorting.origin");
        String destination = ConfigReader.getProperty("price.sorting.destination");
        String departureDate = ConfigReader.getProperty("price.sorting.departure.date");
        String returnDate = ConfigReader.getProperty("price.sorting.return.date");
        
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
        
        // Ensure hotel checkbox is unchecked
        homePage.ensureHotelCheckboxUnchecked();
        
        logger.info("TEST: Hotel checkbox verified as unchecked");
        
        // Click search button to find flights
        homePage.clickSearchButton();
        
        logger.info("TEST: Search button clicked - Navigating to results page");
        
        // Search Results Page'e geç
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        
        // Verify results page is loaded
        Assert.assertTrue(resultsPage.isResultsPageLoaded(),
            "Search results page should be loaded");
        
        logger.info("TEST: Search results page loaded successfully");
        
        // Apply time filter: 10:00 - 18:00 (same as Case 1)
        logger.info("TEST: Applying departure time filter (10:00 - 18:00)");
        
        resultsPage.expandDepartureTimeFilter()
                   .setDepartureTimeFilter(10, 18)
                   .waitForFiltersToApply();
        
        logger.info("TEST: Departure time filter applied successfully");
        
        // Verify all departure times are in range
        logger.info("TEST: Verifying ALL flights have departure times between 10:00 - 18:00");
        
        boolean allFlightsInRange = resultsPage.verifyDepartureTimesInRange(10, 18);
        
        Assert.assertTrue(allFlightsInRange, 
            "All flights should have departure times between 10:00 - 18:00");
        
        logger.info("TEST: All flights verified - time filter working correctly");
        
        // Select Turkish Airlines filter
        logger.info("TEST: Selecting Turkish Airlines filter");
        
        resultsPage.expandAirlineFilter()
                   .selectTurkishAirlines()
                   .waitForAirlineFiltersToApply();
        
        logger.info("TEST: Turkish Airlines filter applied successfully");
        
        // Verify all flights are Turkish Airlines
        logger.info("TEST: Verifying all flights are Turkish Airlines");
        
        boolean allFlightsAreTK = resultsPage.verifyAllFlightsAreTurkishAirlines();
        
        Assert.assertTrue(allFlightsAreTK,
            "All flights should be Turkish Airlines after applying filter");
        
        logger.info("TEST: All flights verified as Turkish Airlines");
        
        // Sort flights by price (cheapest first)
        logger.info("TEST: Sorting flights by price (cheapest first)");
        
        resultsPage.sortByPriceCheapestFirst();
        
        logger.info("TEST: Price sorting applied successfully");
        
        // Verify prices are sorted correctly (cheapest to most expensive)
        logger.info("TEST: Verifying prices are sorted correctly (cheapest to most expensive)");
        
        boolean pricesSorted = resultsPage.verifyPricesSortedCheapestFirst();
        
        Assert.assertTrue(pricesSorted,
            "Flights should be sorted by price from cheapest to most expensive");
        
        logger.info("TEST: Price sorting verified successfully - flights are in correct order");
        
        logger.info("TEST: Case 2 - Price Sorting for Turkish Airlines Completed");
    }
}
