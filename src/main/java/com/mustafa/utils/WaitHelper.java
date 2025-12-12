package com.mustafa.utils;

import com.mustafa.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * WaitHelper - Explicit wait işlemleri için yardımcı sınıf
 */
public class WaitHelper {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(WaitHelper.class);
    
    /**
     * Constructor - Wait süresini config'den alır
     */
    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        int explicitWait = ConfigReader.getIntProperty("explicit.wait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.debug("WaitHelper initialized with " + explicitWait + " seconds timeout");
    }
    
    /**
     * Constructor - Özel wait süresi ile
     */
    public WaitHelper(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        logger.debug("WaitHelper initialized with custom timeout: " + timeoutInSeconds + " seconds");
    }
    
    /**
     * Element görünür olana kadar bekle
     */
    public WebElement waitForVisibility(By locator) {
        try {
            logger.debug("Waiting for element to be visible: " + locator);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not visible within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element görünür olana kadar bekle (WebElement)
     */
    public WebElement waitForVisibility(WebElement element) {
        try {
            logger.debug("Waiting for WebElement to be visible");
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("WebElement not visible within timeout", e);
            throw e;
        }
    }
    
    /**
     * Element tıklanabilir olana kadar bekle
     */
    public WebElement waitForClickable(By locator) {
        try {
            logger.debug("Waiting for element to be clickable: " + locator);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            logger.error("Element not clickable within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element tıklanabilir olana kadar bekle (WebElement)
     */
    public WebElement waitForClickable(WebElement element) {
        try {
            logger.debug("Waiting for WebElement to be clickable");
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("WebElement not clickable within timeout", e);
            throw e;
        }
    }
    
    /**
     * Element mevcut olana kadar bekle
     */
    public WebElement waitForPresence(By locator) {
        try {
            logger.debug("Waiting for element presence: " + locator);
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not present within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Tüm elementler görünür olana kadar bekle
     */
    public List<WebElement> waitForAllVisible(By locator) {
        try {
            logger.debug("Waiting for all elements to be visible: " + locator);
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (Exception e) {
            logger.error("Elements not visible within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element görünmez olana kadar bekle
     */
    public boolean waitForInvisibility(By locator) {
        try {
            logger.debug("Waiting for element to be invisible: " + locator);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element still visible after timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element'in text içermesini bekle
     */
    public boolean waitForTextToBePresentInElement(By locator, String text) {
        try {
            logger.debug("Waiting for text '" + text + "' in element: " + locator);
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (Exception e) {
            logger.error("Text not present in element within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element'in belirli bir attribute değeri içermesini bekle
     */
    public boolean waitForAttributeToBe(By locator, String attribute, String value) {
        try {
            logger.debug("Waiting for attribute '" + attribute + "' to be '" + value + "' in element: " + locator);
            return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
        } catch (Exception e) {
            logger.error("Attribute value not matched within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Alert görünene kadar bekle
     */
    public void waitForAlert() {
        try {
            logger.debug("Waiting for alert to be present");
            wait.until(ExpectedConditions.alertIsPresent());
        } catch (Exception e) {
            logger.error("Alert not present within timeout", e);
            throw e;
        }
    }
    
    /**
     * Frame'e geçiş yapılabilene kadar bekle
     */
    public void waitForFrameAndSwitch(By locator) {
        try {
            logger.debug("Waiting for frame and switching: " + locator);
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
        } catch (Exception e) {
            logger.error("Frame not available within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Sayfa başlığının belirli bir değer olmasını bekle
     */
    public boolean waitForTitle(String title) {
        try {
            logger.debug("Waiting for page title: " + title);
            return wait.until(ExpectedConditions.titleIs(title));
        } catch (Exception e) {
            logger.error("Page title not matched within timeout. Expected: " + title, e);
            throw e;
        }
    }
    
    /**
     * Sayfa başlığının belirli bir text içermesini bekle
     */
    public boolean waitForTitleContains(String titlePart) {
        try {
            logger.debug("Waiting for page title to contain: " + titlePart);
            return wait.until(ExpectedConditions.titleContains(titlePart));
        } catch (Exception e) {
            logger.error("Page title doesn't contain expected text within timeout: " + titlePart, e);
            throw e;
        }
    }
    
    /**
     * URL'nin belirli bir değer olmasını bekle
     */
    public boolean waitForUrl(String url) {
        try {
            logger.debug("Waiting for URL: " + url);
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (Exception e) {
            logger.error("URL not matched within timeout. Expected: " + url, e);
            throw e;
        }
    }
    
    /**
     * URL'nin belirli bir text içermesini bekle
     */
    public boolean waitForUrlContains(String urlPart) {
        try {
            logger.debug("Waiting for URL to contain: " + urlPart);
            return wait.until(ExpectedConditions.urlContains(urlPart));
        } catch (Exception e) {
            logger.error("URL doesn't contain expected text within timeout: " + urlPart, e);
            throw e;
        }
    }
    
    /**
     * Element seçili olana kadar bekle (checkbox/radio)
     */
    public boolean waitForElementToBeSelected(By locator) {
        try {
            logger.debug("Waiting for element to be selected: " + locator);
            return wait.until(ExpectedConditions.elementToBeSelected(locator));
        } catch (Exception e) {
            logger.error("Element not selected within timeout: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Özel wait instance'ı al (özel koşullar için)
     */
    public WebDriverWait getWait() {
        return wait;
    }
}

