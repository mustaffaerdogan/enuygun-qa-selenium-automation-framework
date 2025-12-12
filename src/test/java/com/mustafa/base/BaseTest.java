package com.mustafa.base;

import com.mustafa.config.ConfigReader;
import com.mustafa.driver.DriverManager;
import com.mustafa.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * BaseTest - Tüm test sınıflarının kalıtım alacağı temel test sınıfı
 * Test lifecycle yönetimi burada yapılır
 */
public class BaseTest {
    
    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    
    /**
     * Her test metodundan önce çalışır
     * Browser'ı başlatır ve test URL'ine gider
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional String browser) {
        logger.info("========== Test Starting ==========");
        
        // Browser parametresi testng.xml'den gelmezse config'den oku
        if (browser == null || browser.isEmpty()) {
            browser = ConfigReader.getProperty("browser");
        }
        
        // Driver'ı başlat
        driver = DriverManager.initDriver(browser);
        logger.info("Browser started: " + browser);
        
        // Test URL'ine git
        String baseUrl = ConfigReader.getProperty("base.url");
        driver.get(baseUrl);
        logger.info("Navigated to: " + baseUrl);
        
        // Browser'ı maximize et
        driver.manage().window().maximize();
        logger.info("Browser window maximized");
    }
    
    /**
     * Her test metodundan sonra çalışır
     * Test başarısız olursa screenshot alır
     * Browser'ı kapatır
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Test başarısız olduysa screenshot al
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test FAILED: " + result.getName());
            logger.error("Failure reason: " + result.getThrowable());
            
            String screenshotPath = ScreenshotUtils.takeScreenshot(driver, result.getName());
            logger.info("Screenshot saved: " + screenshotPath);
            
            // Allure'a screenshot ekle
            ScreenshotUtils.attachScreenshotToAllure(driver, result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test PASSED: " + result.getName());
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("Test SKIPPED: " + result.getName());
        }
        
        // Driver'ı kapat
        if (driver != null) {
            DriverManager.quitDriver();
            logger.info("Browser closed");
        }
        
        logger.info("========== Test Finished ==========\n");
    }
    
    /**
     * Test sırasında screenshot almak için kullanılabilir
     */
    protected String takeScreenshot(String screenshotName) {
        return ScreenshotUtils.takeScreenshot(driver, screenshotName);
    }
}

