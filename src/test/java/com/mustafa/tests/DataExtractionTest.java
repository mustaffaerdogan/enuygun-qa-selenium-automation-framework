package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.pages.HomePage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

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
    public void testDataExtractionAndAnalysis() {
        logger.info("TEST: Case 4 - Data Extraction and Analysis Started");
        
        // Navigate to Enuygun.com
        HomePage homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageLoaded(), 
            "Enuygun.com home page should be loaded");
        
        // Accept cookies if present
        homePage.acceptCookies();
        
        logger.info("TEST: Successfully navigated to Enuygun.com");
        
        // TODO: Data extraction and analysis steps will be added here
        
        logger.info("TEST: Case 4 - Data Extraction and Analysis Completed");
    }
}
