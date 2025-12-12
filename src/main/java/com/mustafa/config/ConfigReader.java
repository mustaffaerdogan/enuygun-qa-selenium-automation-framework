package com.mustafa.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - config.properties dosyasını okur
 * Merkezi konfigürasyon yönetimi sağlar
 */
public class ConfigReader {
    
    private static Properties properties;
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    
    // Static block - Class yüklendiğinde properties dosyasını oku
    static {
        loadProperties();
    }
    
    /**
     * Properties dosyasını yükle
     */
    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
            logger.info("Configuration file loaded successfully: " + CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to load configuration file: " + CONFIG_FILE_PATH, e);
            throw new RuntimeException("Configuration file not found: " + CONFIG_FILE_PATH, e);
        }
    }
    
    /**
     * Key'e göre property değerini al
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found for key: " + key);
            throw new RuntimeException("Property not found: " + key);
        }
        return value.trim();
    }
    
    /**
     * Key'e göre property değerini al, bulunamazsa default değer dön
     */
    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        return value.trim();
    }
    
    /**
     * Integer property değeri al
     */
    public static int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid integer value for key: " + key + ", value: " + value, e);
            throw new RuntimeException("Invalid integer property: " + key);
        }
    }
    
    /**
     * Boolean property değeri al
     */
    public static boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Properties dosyasını yeniden yükle
     */
    public static void reloadProperties() {
        logger.info("Reloading configuration file");
        loadProperties();
    }
}

