package com.mustafa.listeners;

import com.mustafa.driver.DriverManager;
import com.mustafa.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.*;

/**
 * TestListener - TestNG test lifecycle'ını dinler ve loglama yapar
 */
public class TestListener implements ITestListener, ISuiteListener {
    
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    
    @Override
    public void onStart(ISuite suite) {
        logger.info("========================================");
        logger.info("Suite Started: " + suite.getName());
        logger.info("========================================");
    }
    
    @Override
    public void onFinish(ISuite suite) {
        logger.info("========================================");
        logger.info("Suite Finished: " + suite.getName());
        logger.info("========================================");
    }
    
    @Override
    public void onStart(ITestContext context) {
        logger.info("----------------------------------------");
        logger.info("Test Context Started: " + context.getName());
        logger.info("----------------------------------------");
    }
    
    @Override
    public void onFinish(ITestContext context) {
        logger.info("----------------------------------------");
        logger.info("Test Context Finished: " + context.getName());
        logger.info("Total Tests Run: " + context.getAllTestMethods().length);
        logger.info("Passed: " + context.getPassedTests().size());
        logger.info("Failed: " + context.getFailedTests().size());
        logger.info("Skipped: " + context.getSkippedTests().size());
        logger.info("----------------------------------------");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info(">>> Test Started: " + getTestName(result));
        logger.info("Description: " + result.getMethod().getDescription());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("✓ Test PASSED: " + getTestName(result));
        logger.info("Duration: " + getDuration(result) + "ms");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("✗ Test FAILED: " + getTestName(result));
        logger.error("Error Message: " + result.getThrowable().getMessage());
        logger.error("Duration: " + getDuration(result) + "ms");
        
        // Screenshot al
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                String screenshotPath = ScreenshotUtils.takeScreenshot(driver, 
                    "FAILED_" + getTestName(result));
                logger.info("Failure Screenshot: " + screenshotPath);
                
                // Allure'a ekle
                ScreenshotUtils.attachScreenshotToAllure(driver, 
                    "FAILED_" + getTestName(result));
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot on test failure", e);
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⊘ Test SKIPPED: " + getTestName(result));
        if (result.getThrowable() != null) {
            logger.warn("Skip Reason: " + result.getThrowable().getMessage());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test Failed But Within Success Percentage: " + getTestName(result));
    }
    
    /**
     * Test adını al
     */
    private String getTestName(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getMethod().getMethodName();
    }
    
    /**
     * Test süresini hesapla (milliseconds)
     */
    private long getDuration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}

