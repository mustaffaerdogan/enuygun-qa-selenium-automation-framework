package com.mustafa.exceptions;

/**
 * ElementNotFoundException - Element bulunamadığında fırlatılan exception
 */
public class ElementNotFoundException extends FrameworkException {
    
    private static final long serialVersionUID = 1L;
    
    public ElementNotFoundException(String message) {
        super(message);
    }
    
    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

