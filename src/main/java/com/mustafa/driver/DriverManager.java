package com.mustafa.driver;

import com.mustafa.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

/**
 * DriverManager - WebDriver yönetimini sağlar
 * Thread-safe driver instance yönetimi
 */
public class DriverManager {
    
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    
    // Thread-safe driver instance
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * Driver'ı başlat ve döndür
     */
    public static WebDriver initDriver(String browser) {
        if (browser == null || browser.isEmpty()) {
            browser = ConfigReader.getProperty("browser");
        }
        
        logger.info("Initializing driver for browser: " + browser);
        
        WebDriver webDriver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                webDriver = createChromeDriver();
                break;
            case "firefox":
                webDriver = createFirefoxDriver();
                break;
            case "edge":
                webDriver = createEdgeDriver();
                break;
            default:
                logger.warn("Unknown browser: " + browser + ". Defaulting to Chrome");
                webDriver = createChromeDriver();
        }
        
        // Driver'ı ThreadLocal'e kaydet
        driver.set(webDriver);
        
        // Timeouts ayarla
        setTimeouts(webDriver);
        
        logger.info("Driver initialized successfully");
        return webDriver;
    }
    
    /**
     * Chrome driver oluştur
     */
    private static WebDriver createChromeDriver() {
        logger.info("Setting up Chrome driver");
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Headless mode
        if (ConfigReader.getBooleanProperty("headless")) {
            options.addArguments("--headless=new");
            logger.info("Chrome running in headless mode");
        }
        
        // Chrome argümanları
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");
        
        // Prefs ayarları
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        return new ChromeDriver(options);
    }
    
    /**
     * Firefox driver oluştur
     */
    private static WebDriver createFirefoxDriver() {
        logger.info("Setting up Firefox driver");
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        
        // Headless mode
        if (ConfigReader.getBooleanProperty("headless")) {
            options.addArguments("--headless");
            logger.info("Firefox running in headless mode");
        }
        
        // Firefox ayarları
        options.addArguments("--start-maximized");
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("geo.enabled", false);
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Edge driver oluştur
     */
    private static WebDriver createEdgeDriver() {
        logger.info("Setting up Edge driver");
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        
        // Headless mode
        if (ConfigReader.getBooleanProperty("headless")) {
            options.addArguments("--headless");
            logger.info("Edge running in headless mode");
        }
        
        // Edge argümanları
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        
        return new EdgeDriver(options);
    }
    
    /**
     * Timeout'ları ayarla
     */
    private static void setTimeouts(WebDriver driver) {
        int implicitWait = ConfigReader.getIntProperty("implicit.wait");
        int pageLoadTimeout = ConfigReader.getIntProperty("page.load.timeout");
        int scriptTimeout = ConfigReader.getIntProperty("script.timeout");
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));
        
        logger.info("Timeouts configured - Implicit: " + implicitWait + 
                   "s, PageLoad: " + pageLoadTimeout + "s, Script: " + scriptTimeout + "s");
    }
    
    /**
     * Mevcut driver instance'ı al
     */
    public static WebDriver getDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            logger.warn("Driver is null. Initializing with default browser.");
            return initDriver(ConfigReader.getProperty("browser"));
        }
        return webDriver;
    }
    
    /**
     * Driver'ı kapat ve ThreadLocal'den temizle
     */
    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver", e);
            } finally {
                driver.remove();
            }
        }
    }
}

