package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.pages.HomePage;
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
    public void testCriticalPath() {
        logger.info("TEST: Case 3 - Critical Path Testing Started");
        
        // Navigate to Enuygun.com
        HomePage homePage = new HomePage(driver);
        
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageLoaded(), 
            "Enuygun.com home page should be loaded");
        
        // Accept cookies if present
        homePage.acceptCookies();
        
        logger.info("TEST: Successfully navigated to Enuygun.com");
        
        // TODO: Critical path steps will be added here
        
        logger.info("TEST: Case 3 - Critical Path Testing Completed");
    }
}
