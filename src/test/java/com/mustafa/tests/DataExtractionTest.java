package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.config.ConfigReader;
import com.mustafa.pages.HomePage;
import com.mustafa.pages.SearchResultsPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * DataExtractionTest - Veri çıkarma ve analiz testleri
 * Case 4: Data Extraction and Analysis
 */
@Epic("Data Analysis")
@Feature("Data Extraction")
public class DataExtractionTest extends BaseTest {
    
    @Test(priority = 1, description = "Case 4 - Data Extraction and Analysis")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Veri çıkarma ve analiz testi")
    @Story("As a tester, I should be able to extract and analyze flight data")
    public void testDataExtractionAndAnalysis() throws InterruptedException {
        logger.info("TEST: Case 4 - Data Extraction and Analysis Started");
        
        // Arrange - Get test data from config
        String origin = ConfigReader.getProperty("data.extraction.origin");
        String destination = ConfigReader.getProperty("data.extraction.destination");
        String departureDate = ConfigReader.getProperty("data.extraction.departure.date");
        String returnDate = ConfigReader.getProperty("data.extraction.return.date");
        
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
        
        // Extract flight data from all search results
        logger.info("TEST: Extracting flight data from all search results");
        List<SearchResultsPage.FlightData> flightDataList = resultsPage.extractAllFlightData();
        
        Assert.assertFalse(flightDataList.isEmpty(), 
            "Flight data list should not be empty");
        
        logger.info("TEST: Successfully extracted data from " + flightDataList.size() + " flights");
        
        // Save extracted data to CSV file
        String csvFilePath = saveFlightDataToCSV(flightDataList);
        logger.info("TEST: Flight data saved to CSV file: " + csvFilePath);
        
        Assert.assertNotNull(csvFilePath, "CSV file path should not be null");
        Assert.assertTrue(Files.exists(Paths.get(csvFilePath)), 
            "CSV file should exist at: " + csvFilePath);
        
        logger.info("TEST: Data extraction and CSV creation completed successfully");
        
        logger.info("TEST: Case 4 - Data Extraction and Analysis Completed");
    }
    
    /**
     * Uçuş verilerini CSV dosyasına kaydet (UTF-8 BOM ile Excel uyumlu)
     */
    private String saveFlightDataToCSV(List<SearchResultsPage.FlightData> flightDataList) {
        String csvDirectory = "target/test-data";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "flight_data_" + timestamp + ".csv";
        String csvFilePath = csvDirectory + "/" + fileName;
        
        try {
            // Dizini oluştur (yoksa)
            Files.createDirectories(Paths.get(csvDirectory));
            
            // CSV dosyasına UTF-8 BOM ile yaz (Excel için gerekli)
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(csvFilePath), 
                        StandardCharsets.UTF_8))) {
                
                // UTF-8 BOM (Byte Order Mark) yaz - Excel için gerekli
                writer.write('\ufeff');
                
                // CSV başlıklarını yaz
                writer.write("Departure Time,Arrival Time,Airline Name,Price,Connection Info,Flight Duration\n");
                
                // Her uçuş verisini yaz
                for (SearchResultsPage.FlightData flightData : flightDataList) {
                    writer.write(flightData.toCsvRow());
                    writer.write("\n");
                }
                
                logger.info("Successfully wrote " + flightDataList.size() + " flight records to CSV with UTF-8 BOM encoding");
            }
            
        } catch (IOException e) {
            logger.error("Error writing CSV file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return csvFilePath;
    }
}
