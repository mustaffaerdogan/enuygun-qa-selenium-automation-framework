package com.mustafa.utils;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Random;

/**
 * TestDataGenerator - Faker kullanarak test verisi üretir
 */
public class TestDataGenerator {
    
    private static final Faker faker = new Faker(new Locale("en-US"));
    private static final Random random = new Random();
    private static final Logger logger = LogManager.getLogger(TestDataGenerator.class);
    
    /**
     * Rastgele isim üret
     */
    public static String generateFirstName() {
        String name = faker.name().firstName();
        logger.debug("Generated first name: " + name);
        return name;
    }
    
    /**
     * Rastgele soyisim üret
     */
    public static String generateLastName() {
        String lastName = faker.name().lastName();
        logger.debug("Generated last name: " + lastName);
        return lastName;
    }
    
    /**
     * Rastgele tam isim üret
     */
    public static String generateFullName() {
        String fullName = faker.name().fullName();
        logger.debug("Generated full name: " + fullName);
        return fullName;
    }
    
    /**
     * Rastgele email üret
     */
    public static String generateEmail() {
        String email = faker.internet().emailAddress();
        logger.debug("Generated email: " + email);
        return email;
    }
    
    /**
     * Rastgele telefon numarası üret
     */
    public static String generatePhoneNumber() {
        String phone = faker.phoneNumber().phoneNumber();
        logger.debug("Generated phone: " + phone);
        return phone;
    }
    
    /**
     * Rastgele adres üret
     */
    public static String generateAddress() {
        String address = faker.address().fullAddress();
        logger.debug("Generated address: " + address);
        return address;
    }
    
    /**
     * Rastgele şehir üret
     */
    public static String generateCity() {
        String city = faker.address().city();
        logger.debug("Generated city: " + city);
        return city;
    }
    
    /**
     * Rastgele eyalet/il üret
     */
    public static String generateState() {
        String state = faker.address().state();
        logger.debug("Generated state: " + state);
        return state;
    }
    
    /**
     * Rastgele posta kodu üret
     */
    public static String generateZipCode() {
        String zipCode = faker.address().zipCode();
        logger.debug("Generated zip code: " + zipCode);
        return zipCode;
    }
    
    /**
     * Rastgele şirket adı üret
     */
    public static String generateCompanyName() {
        String company = faker.company().name();
        logger.debug("Generated company: " + company);
        return company;
    }
    
    /**
     * Rastgele password üret
     */
    public static String generatePassword() {
        return generatePassword(8, 15);
    }
    
    /**
     * Belirtilen uzunlukta rastgele password üret
     */
    public static String generatePassword(int minLength, int maxLength) {
        String password = faker.internet().password(minLength, maxLength, true, true);
        logger.debug("Generated password with length: " + password.length());
        return password;
    }
    
    /**
     * Rastgele username üret
     */
    public static String generateUsername() {
        String username = faker.name().username();
        logger.debug("Generated username: " + username);
        return username;
    }
    
    /**
     * Rastgele sayı üret (0-max arası)
     */
    public static int generateRandomNumber(int max) {
        int number = random.nextInt(max);
        logger.debug("Generated random number: " + number);
        return number;
    }
    
    /**
     * Rastgele sayı üret (min-max arası)
     */
    public static int generateRandomNumber(int min, int max) {
        int number = random.nextInt(max - min + 1) + min;
        logger.debug("Generated random number between " + min + " and " + max + ": " + number);
        return number;
    }
    
    /**
     * Rastgele boolean üret
     */
    public static boolean generateRandomBoolean() {
        return random.nextBoolean();
    }
    
    /**
     * Rastgele metin üret
     */
    public static String generateRandomText(int wordCount) {
        String text = faker.lorem().sentence(wordCount);
        logger.debug("Generated random text: " + text);
        return text;
    }
    
    /**
     * Rastgele paragraf üret
     */
    public static String generateRandomParagraph() {
        String paragraph = faker.lorem().paragraph();
        logger.debug("Generated random paragraph");
        return paragraph;
    }
    
    /**
     * Rastgele UUID üret
     */
    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }
    
    /**
     * Timestamp ile benzersiz email üret
     */
    public static String generateUniqueEmail() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String email = "user" + timestamp + "@test.com";
        logger.debug("Generated unique email: " + email);
        return email;
    }
    
    /**
     * Timestamp ile benzersiz username üret
     */
    public static String generateUniqueUsername() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = "user" + timestamp;
        logger.debug("Generated unique username: " + username);
        return username;
    }
}

