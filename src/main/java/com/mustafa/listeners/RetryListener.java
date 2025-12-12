package com.mustafa.listeners;

import com.mustafa.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryListener - Başarısız testleri otomatik olarak yeniden çalıştırır
 */
public class RetryListener implements IRetryAnalyzer {
    
    private static final Logger logger = LogManager.getLogger(RetryListener.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = FrameworkConstants.MAX_RETRY_COUNT;
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("Retrying test '" + result.getName() + "' - Attempt " + retryCount + 
                       " of " + MAX_RETRY_COUNT);
            return true;
        }
        
        logger.error("Test '" + result.getName() + "' failed after " + MAX_RETRY_COUNT + 
                    " retry attempts");
        return false;
    }
}

