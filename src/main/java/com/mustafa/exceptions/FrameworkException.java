package com.mustafa.exceptions;

/**
 * FrameworkException - Framework genelinde kullanılan özel exception sınıfı
 * Tüm framework exception'ları bu sınıftan türetilir
 */
public class FrameworkException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Sadece mesaj ile exception oluştur
     */
    public FrameworkException(String message) {
        super(message);
    }
    
    /**
     * Mesaj ve cause ile exception oluştur
     */
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Sadece cause ile exception oluştur
     */
    public FrameworkException(Throwable cause) {
        super(cause);
    }
}

