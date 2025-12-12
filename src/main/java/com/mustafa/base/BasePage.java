package com.mustafa.base;

import com.mustafa.utils.ElementHelper;
import com.mustafa.utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * BasePage - Tüm Page sınıflarının kalıtım alacağı temel sınıf
 * Ortak sayfa işlemleri burada tanımlanır
 */
public class BasePage {
    
    protected WebDriver driver;
    protected WaitHelper waitHelper;
    protected ElementHelper elementHelper;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    
    /**
     * Constructor - Her page oluşturulduğunda driver'ı alır ve helper'ları başlatır
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        this.elementHelper = new ElementHelper(driver);
        PageFactory.initElements(driver, this);
        logger.info(this.getClass().getSimpleName() + " initialized");
    }
    
    /**
     * Element'e güvenli tıklama - Wait ve scroll içerir
     */
    protected void click(By locator) {
        elementHelper.safeClick(locator);
        logger.info("Clicked on element: " + locator);
    }
    
    /**
     * Element'e güvenli text gönderme
     */
    protected void type(By locator, String text) {
        elementHelper.safeSendKeys(locator, text);
        logger.info("Typed '" + text + "' into element: " + locator);
    }
    
    /**
     * Element'in text değerini al
     */
    protected String getText(By locator) {
        String text = waitHelper.waitForVisibility(locator).getText();
        logger.info("Got text '" + text + "' from element: " + locator);
        return text;
    }
    
    /**
     * Element'in görünür olmasını bekle
     */
    protected WebElement waitForElement(By locator) {
        logger.debug("Waiting for element: " + locator);
        return waitHelper.waitForVisibility(locator);
    }
    
    /**
     * Element'e scroll yap
     */
    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        logger.info("Scrolled to element: " + locator);
    }
    
    /**
     * Element'in tıklanabilir olmasını bekle
     */
    protected WebElement waitForClickable(By locator) {
        logger.debug("Waiting for element to be clickable: " + locator);
        return waitHelper.waitForClickable(locator);
    }
    
    /**
     * Element'in görünmez olmasını bekle
     */
    protected boolean waitForInvisibility(By locator) {
        logger.debug("Waiting for element to be invisible: " + locator);
        return waitHelper.waitForInvisibility(locator);
    }
    
    /**
     * Element'in mevcut olup olmadığını kontrol et
     */
    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            logger.debug("Element is present: " + locator);
            return true;
        } catch (Exception e) {
            logger.debug("Element is not present: " + locator);
            return false;
        }
    }
    
    /**
     * Element'in görünür olup olmadığını kontrol et
     */
    protected boolean isElementVisible(By locator) {
        try {
            boolean visible = driver.findElement(locator).isDisplayed();
            logger.debug("Element visibility: " + visible + " for: " + locator);
            return visible;
        } catch (Exception e) {
            logger.debug("Element is not visible: " + locator);
            return false;
        }
    }
    
    /**
     * Tüm elementleri bul
     */
    protected List<WebElement> findElements(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        logger.debug("Found " + elements.size() + " elements for: " + locator);
        return elements;
    }
    
    /**
     * Sayfanın başlığını al
     */
    protected String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: " + title);
        return title;
    }
    
    /**
     * Mevcut URL'i al
     */
    protected String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: " + url);
        return url;
    }
    
    /**
     * JavaScript ile tıklama
     */
    protected void clickWithJS(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        logger.info("Clicked with JavaScript on element: " + locator);
    }
    
    /**
     * Belirtilen süre kadar bekle
     */
    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.debug("Waited for " + seconds + " seconds");
        } catch (InterruptedException e) {
            logger.error("Wait interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Dropdown'dan text ile seçim yap
     */
    protected void selectByVisibleText(By locator, String text) {
        elementHelper.selectDropdownByText(locator, text);
        logger.info("Selected '" + text + "' from dropdown: " + locator);
    }
    
    /**
     * Sayfayı yenile
     */
    protected void refreshPage() {
        driver.navigate().refresh();
        logger.info("Page refreshed");
    }
    
    /**
     * Geri git
     */
    protected void navigateBack() {
        driver.navigate().back();
        logger.info("Navigated back");
    }
    
    /**
     * İleri git
     */
    protected void navigateForward() {
        driver.navigate().forward();
        logger.info("Navigated forward");
    }
}

