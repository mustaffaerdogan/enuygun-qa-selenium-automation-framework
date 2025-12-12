package com.mustafa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * DateUtils - Tarih/zaman işlemleri için yardımcı sınıf
 */
public class DateUtils {
    
    private static final Logger logger = LogManager.getLogger(DateUtils.class);
    
    /**
     * Bugünün tarihini belirtilen formatta al
     */
    public static String getCurrentDate(String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.now().format(formatter);
        } catch (Exception e) {
            logger.error("Failed to get current date with format: " + format, e);
            return LocalDate.now().toString();
        }
    }
    
    /**
     * Bugünün tarihini default formatta al (yyyy-MM-dd)
     */
    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd");
    }
    
    /**
     * Şu anki tarih ve saati belirtilen formatta al
     */
    public static String getCurrentDateTime(String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.now().format(formatter);
        } catch (Exception e) {
            logger.error("Failed to get current datetime with format: " + format, e);
            return LocalDateTime.now().toString();
        }
    }
    
    /**
     * Şu anki tarih ve saati default formatta al (yyyy-MM-dd HH:mm:ss)
     */
    public static String getCurrentDateTime() {
        return getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * Timestamp al (yyyyMMddHHmmss formatında)
     */
    public static String getTimestamp() {
        return getCurrentDateTime("yyyyMMddHHmmss");
    }
    
    /**
     * Gelecek/geçmiş bir tarihi al (gün bazında)
     */
    public static String getDateWithDayOffset(int days, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.now().plusDays(days).format(formatter);
        } catch (Exception e) {
            logger.error("Failed to get date with offset: " + days, e);
            return LocalDate.now().plusDays(days).toString();
        }
    }
    
    /**
     * Gelecek/geçmiş bir tarihi al - default format (yyyy-MM-dd)
     */
    public static String getDateWithDayOffset(int days) {
        return getDateWithDayOffset(days, "yyyy-MM-dd");
    }
    
    /**
     * Legacy Date nesnesinden String'e çevir
     */
    public static String formatDate(Date date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        } catch (Exception e) {
            logger.error("Failed to format date", e);
            return date.toString();
        }
    }
}

