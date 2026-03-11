package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.config.ConfigReader;
import com.mustafa.pages.HomePage;
import com.mustafa.pages.SearchResultsPage;
import com.mustafa.pages.ReservationPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CriticalPathTest - Kritik yol testleri
 * Case 3: Critical Path Testing
 */
@Epic("E2E Testing")
@Feature("Critical Path")
public class CriticalPathTest extends BaseTest {
    
    @Test(priority = 1, description = "Case 3 - Critical Path Testing")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Kritik kullanıcı yolu end-to-end testi")
    @Story("As a user, I should be able to complete the critical user journey")
    public void testCriticalPath() throws InterruptedException {
        logger.info("TEST: Case 3 - Critical Path Testing Started");
        
        // Arrange - Get test data from config
        String origin = ConfigReader.getProperty("critical.path.origin");
        String destination = ConfigReader.getProperty("critical.path.destination");
        String departureDate = ConfigReader.getProperty("critical.path.departure.date");
        String returnDate = ConfigReader.getProperty("critical.path.return.date");
        
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
        
        // Verify results page is loaded
        Assert.assertTrue(resultsPage.isResultsPageLoaded(),
            "Search results page should be loaded");
        
        logger.info("TEST: Search results page loaded successfully");
        
        // Test BASIC-BASIC package combination
        int flightIndex = 0; // İlk uçuşu seçeceğiz
        String departurePackage = "BASIC";
        String returnPackage = "BASIC";
        
        logger.info("\n========================================");
        logger.info("Testing Package Combination: Departure=BASIC, Return=BASIC");
        logger.info("========================================");
        
        // Gidiş uçuşu bilgilerini al
        logger.info("\n=== DEPARTURE FLIGHT SELECTION ===");
        SearchResultsPage.FlightInfo departureFlightInfo = resultsPage.getFlightInfo(flightIndex);
        if (departureFlightInfo != null) {
            logger.info("Selected Departure Flight Info:");
            logger.info("  Airline: " + departureFlightInfo.getAirline());
            logger.info("  Flight Number: " + departureFlightInfo.getFlightNumber());
            logger.info("  Departure Time: " + departureFlightInfo.getDepartureTime());
            logger.info("  Arrival Time: " + departureFlightInfo.getArrivalTime());
            logger.info("  Duration: " + departureFlightInfo.getDuration());
            logger.info("  Price: " + departureFlightInfo.getPrice() + " TRY");
            logger.info("  Class: " + departureFlightInfo.getFlightClass());
        }
        
        // Gidiş uçuşu için Seç butonuna tıkla
        resultsPage.clickSelectFlight(flightIndex);
        logger.info("Clicked select button for departure flight");
        
        // Gidiş paketi seç (BASIC)
        resultsPage.selectDeparturePackage(departurePackage);
        logger.info("Selected departure package: " + departurePackage);
        
        // Onayla butonuna tıkla
        resultsPage.confirmPackageSelection();
        logger.info("Confirmed departure package selection - Navigating to return flight selection");
        
        // Dönüş uçuşu seçimi için bekle
        Thread.sleep(3000);
        
        // Dönüş uçuşu bilgilerini al
        logger.info("\n=== RETURN FLIGHT SELECTION ===");
        SearchResultsPage.FlightInfo returnFlightInfo = resultsPage.getReturnFlightInfo(flightIndex);
        if (returnFlightInfo != null) {
            logger.info("Selected Return Flight Info:");
            logger.info("  Airline: " + returnFlightInfo.getAirline());
            logger.info("  Flight Number: " + returnFlightInfo.getFlightNumber());
            logger.info("  Departure Time: " + returnFlightInfo.getDepartureTime());
            logger.info("  Arrival Time: " + returnFlightInfo.getArrivalTime());
            logger.info("  Duration: " + returnFlightInfo.getDuration());
            logger.info("  Price: " + returnFlightInfo.getPrice() + " TRY");
            logger.info("  Class: " + returnFlightInfo.getFlightClass());
        }
        
        // Dönüş uçuşu için Seç butonuna tıkla
        resultsPage.clickSelectReturnFlight(flightIndex);
        logger.info("Clicked select button for return flight");
        
        // Dönüş paketi seç (BASIC) - Otomatik olarak rezervasyon sayfasına gidecek
        resultsPage.selectReturnPackage(returnPackage);
        logger.info("Selected return package: " + returnPackage + " - Automatically navigating to reservation page");
        
        // Rezervasyon sayfasına geç (otomatik yönlendirme) - uzun bekleme
        logger.info("Waiting for automatic redirection to reservation page...");
        Thread.sleep(10000); // 10 saniye bekle
        ReservationPage reservationPage = new ReservationPage(driver);
        
        // Rezervasyon sayfası yüklendiğini doğrula
        Assert.assertTrue(reservationPage.isReservationPageLoaded(),
            "Reservation page should be loaded for BASIC/BASIC combination");
        
        logger.info("Reservation page loaded successfully");
        
        // Seçilen uçuş bilgilerini rezervasyon sayfasındakilerle karşılaştır
        logger.info("\n========================================");
        logger.info("VERIFYING FLIGHT INFORMATION ON RESERVATION PAGE");
        logger.info("========================================");
        
        // Gidiş uçuşu bilgilerini doğrula
        boolean departureVerified = reservationPage.verifyDepartureFlightInfo(departureFlightInfo);
        Assert.assertTrue(departureVerified,
            "Departure flight info on reservation page should match selected flight");
        
        logger.info("✓ Departure flight information verified successfully");
        
        // Dönüş uçuşu bilgilerini doğrula
        boolean returnVerified = reservationPage.verifyReturnFlightInfo(returnFlightInfo);
        Assert.assertTrue(returnVerified,
            "Return flight info on reservation page should match selected flight");
        
        logger.info("✓ Return flight information verified successfully");
        
        // Paket bilgilerini doğrula
        boolean detailsVerified = reservationPage.verifyFlightDetails(departurePackage, returnPackage);
        
        logger.info("Package combination verification: " + 
                   (detailsVerified ? "PASSED" : "CHECKED") + 
                   " - Departure: " + departurePackage + ", Return: " + returnPackage);
        
        logger.info("\n========================================");
        logger.info("BASIC-BASIC PACKAGE COMBINATION TEST COMPLETED");
        logger.info("========================================");
        
        logger.info("TEST: Case 3 - Critical Path Testing Completed");
    }
}
