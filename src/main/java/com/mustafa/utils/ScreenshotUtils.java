package com.mustafa.utils;

import com.mustafa.config.ConfigReader;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils - Screenshot alma işlemleri için yardımcı sınıf
 */
public class ScreenshotUtils {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = ConfigReader.getProperty("screenshot.path", "target/screenshots");
    
    /**
     * Screenshot al ve dosya olarak kaydet
     * @return Screenshot dosya yolu
     */
    public static String takeScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Screenshot dizinini oluştur
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
                logger.debug("Screenshot directory created: " + SCREENSHOT_DIR);
            }
            
            // Timestamp ekle
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            
            // Screenshot al
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
            
            // Hedef dosya yolu
            String filePath = SCREENSHOT_DIR + File.separator + fileName;
            File destinationFile = new File(filePath);
            
            // Dosyayı kaydet
            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot saved: " + filePath);
            
            return filePath;
            
        } catch (IOException e) {
            logger.error("Failed to save screenshot: " + screenshotName, e);
            return null;
        }
    }
    
    /**
     * Screenshot al ve byte array olarak dön
     */
    public static byte[] takeScreenshotAsBytes(WebDriver driver) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
            logger.debug("Screenshot taken as bytes");
            return screenshotBytes;
        } catch (Exception e) {
            logger.error("Failed to take screenshot as bytes", e);
            return new byte[0];
        }
    }
    
    /**
     * Screenshot'ı Allure raporuna ekle
     */
    public static void attachScreenshotToAllure(WebDriver driver, String screenshotName) {
        try {
            byte[] screenshotBytes = takeScreenshotAsBytes(driver);
            Allure.addAttachment(screenshotName, "image/png", 
                               new ByteArrayInputStream(screenshotBytes), ".png");
            logger.debug("Screenshot attached to Allure report: " + screenshotName);
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Allure: " + screenshotName, e);
        }
    }
    
    /**
     * Full page screenshot al (varsa)
     */
    public static String takeFullPageScreenshot(WebDriver driver, String screenshotName) {
        // Not: Full page screenshot browser ve driver versiyonuna göre değişebilir
        // Şimdilik normal screenshot alıyoruz
        logger.warn("Full page screenshot not implemented, taking regular screenshot");
        return takeScreenshot(driver, screenshotName);
    }
}

