package com.mustafa.constants;

/**
 * FrameworkConstants - Framework genelinde kullanılan sabitler
 */
public final class FrameworkConstants {
    
    // Private constructor - Utility class
    private FrameworkConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // File Paths
    public static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    public static final String TEST_DATA_PATH = "src/test/resources/testdata";
    public static final String SCREENSHOT_PATH = "target/screenshots";
    public static final String ALLURE_RESULTS_PATH = "target/allure-results";
    
    // Browser Types
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String EDGE = "edge";
    
    // Wait Times (in seconds)
    public static final int SHORT_WAIT = 5;
    public static final int MEDIUM_WAIT = 10;
    public static final int LONG_WAIT = 20;
    public static final int EXTRA_LONG_WAIT = 30;
    
    // Retry Configuration
    public static final int MAX_RETRY_COUNT = 2;
    
    // Test Data
    public static final String DEFAULT_PASSWORD = "Test@123";
    public static final String DEFAULT_USERNAME = "testuser";
    
    // File Extensions
    public static final String EXCEL_EXTENSION = ".xlsx";
    public static final String JSON_EXTENSION = ".json";
    public static final String PROPERTIES_EXTENSION = ".properties";
    public static final String SCREENSHOT_EXTENSION = ".png";
    
    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    
    // Success/Failure Messages
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGIN_FAILURE = "Login failed";
    public static final String ELEMENT_NOT_FOUND = "Element not found";
    public static final String PAGE_NOT_LOADED = "Page not loaded";
    
    // HTTP Status Codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_SERVER_ERROR = 500;
}

