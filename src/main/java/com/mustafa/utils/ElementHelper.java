package com.mustafa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/**
 * ElementHelper - Web element işlemleri için yardımcı sınıf
 * Güvenli ve tekrar kullanılabilir element işlemleri
 */
public class ElementHelper {
    
    private final WebDriver driver;
    private final WaitHelper waitHelper;
    private final Actions actions;
    private static final Logger logger = LogManager.getLogger(ElementHelper.class);
    
    public ElementHelper(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        this.actions = new Actions(driver);
        logger.debug("ElementHelper initialized");
    }
    
    /**
     * Güvenli tıklama - Wait içerir, scroll yapar, tıklar
     */
    public void safeClick(By locator) {
        try {
            WebElement element = waitHelper.waitForClickable(locator);
            scrollToElement(element);
            element.click();
            logger.debug("Successfully clicked on element: " + locator);
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, trying with JavaScript: " + locator);
            clickWithJS(locator);
        } catch (Exception e) {
            logger.error("Failed to click on element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Güvenli text gönderme - Önce temizler, sonra yazar
     */
    public void safeSendKeys(By locator, String text) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            element.clear();
            element.sendKeys(text);
            logger.debug("Successfully sent keys to element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Text gönderme (temizlemeden)
     */
    public void sendKeysWithoutClear(By locator, String text) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            element.sendKeys(text);
            logger.debug("Successfully sent keys without clear to element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * JavaScript ile tıklama
     */
    public void clickWithJS(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.debug("Successfully clicked with JavaScript: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click with JavaScript: " + locator, e);
            throw e;
        }
    }
    
    /**
     * JavaScript ile text gönderme
     */
    public void sendKeysWithJS(By locator, String text) {
        try {
            WebElement element = driver.findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value='" + text + "';", element);
            logger.debug("Successfully sent keys with JavaScript: " + locator);
        } catch (Exception e) {
            logger.error("Failed to send keys with JavaScript: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element'e scroll yap
     */
    public void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(300); // Scroll animasyonu için kısa bekleme
            logger.debug("Successfully scrolled to element");
        } catch (Exception e) {
            logger.error("Failed to scroll to element", e);
        }
    }
    
    /**
     * Element'e scroll yap (By locator ile)
     */
    public void scrollToElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            scrollToElement(element);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Sayfanın en altına scroll yap
     */
    public void scrollToBottom() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            logger.debug("Scrolled to bottom of page");
        } catch (Exception e) {
            logger.error("Failed to scroll to bottom", e);
        }
    }
    
    /**
     * Sayfanın en üstüne scroll yap
     */
    public void scrollToTop() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, 0)");
            logger.debug("Scrolled to top of page");
        } catch (Exception e) {
            logger.error("Failed to scroll to top", e);
        }
    }
    
    /**
     * Dropdown'dan text ile seçim yap
     */
    public void selectDropdownByText(By locator, String text) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            logger.debug("Selected '" + text + "' from dropdown: " + locator);
        } catch (Exception e) {
            logger.error("Failed to select from dropdown: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Dropdown'dan value ile seçim yap
     */
    public void selectDropdownByValue(By locator, String value) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.debug("Selected value '" + value + "' from dropdown: " + locator);
        } catch (Exception e) {
            logger.error("Failed to select from dropdown: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Dropdown'dan index ile seçim yap
     */
    public void selectDropdownByIndex(By locator, int index) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            Select select = new Select(element);
            select.selectByIndex(index);
            logger.debug("Selected index " + index + " from dropdown: " + locator);
        } catch (Exception e) {
            logger.error("Failed to select from dropdown: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Mouse hover (Actions ile)
     */
    public void hover(By locator) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            actions.moveToElement(element).perform();
            logger.debug("Hovered over element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to hover over element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Double click
     */
    public void doubleClick(By locator) {
        try {
            WebElement element = waitHelper.waitForClickable(locator);
            actions.doubleClick(element).perform();
            logger.debug("Double clicked on element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to double click on element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Right click (context menu)
     */
    public void rightClick(By locator) {
        try {
            WebElement element = waitHelper.waitForClickable(locator);
            actions.contextClick(element).perform();
            logger.debug("Right clicked on element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to right click on element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Drag and drop
     */
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        try {
            WebElement source = waitHelper.waitForVisibility(sourceLocator);
            WebElement target = waitHelper.waitForVisibility(targetLocator);
            actions.dragAndDrop(source, target).perform();
            logger.debug("Dragged element from " + sourceLocator + " to " + targetLocator);
        } catch (Exception e) {
            logger.error("Failed to drag and drop", e);
            throw e;
        }
    }
    
    /**
     * Element'in text değerini al
     */
    public String getText(By locator) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            String text = element.getText();
            logger.debug("Got text: '" + text + "' from element: " + locator);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element'in attribute değerini al
     */
    public String getAttribute(By locator, String attribute) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            String value = element.getAttribute(attribute);
            logger.debug("Got attribute '" + attribute + "' value: '" + value + "' from element: " + locator);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute from element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element'in CSS property değerini al
     */
    public String getCssValue(By locator, String property) {
        try {
            WebElement element = waitHelper.waitForVisibility(locator);
            String value = element.getCssValue(property);
            logger.debug("Got CSS property '" + property + "' value: '" + value + "' from element: " + locator);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get CSS value from element: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Element'in görünür olup olmadığını kontrol et
     */
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            logger.debug("Element not displayed: " + locator);
            return false;
        }
    }
    
    /**
     * Element'in enabled olup olmadığını kontrol et
     */
    public boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (Exception e) {
            logger.debug("Element not enabled: " + locator);
            return false;
        }
    }
    
    /**
     * Element'in seçili olup olmadığını kontrol et
     */
    public boolean isSelected(By locator) {
        try {
            return driver.findElement(locator).isSelected();
        } catch (Exception e) {
            logger.debug("Element not selected: " + locator);
            return false;
        }
    }
}

