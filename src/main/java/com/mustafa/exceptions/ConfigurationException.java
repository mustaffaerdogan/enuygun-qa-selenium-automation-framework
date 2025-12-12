package com.mustafa.exceptions;

/**
 * ConfigurationException - Konfigürasyon hatalarında fırlatılan exception
 */
public class ConfigurationException extends FrameworkException {
    
    private static final long serialVersionUID = 1L;
    
    public ConfigurationException(String message) {
        super(message);
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

