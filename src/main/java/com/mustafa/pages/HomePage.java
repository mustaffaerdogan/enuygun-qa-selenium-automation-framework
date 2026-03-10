package com.mustafa.pages;

import com.mustafa.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * HomePage - Enuygun.com ana sayfa için Page Object
 */
public class HomePage extends BasePage {
    
    // Locators
    private final By cookieAcceptButton = By.id("onetrust-accept-btn-handler");
    
    // Flight Search Form Locators
    private final By roundTripRadioButton = By.cssSelector("span[name='flightTrip']");
    private final By originInput = By.cssSelector("[data-testid='endesign-flight-origin-autosuggestion-input']");
    private final By originFirstOption = By.cssSelector("[data-testid='endesign-flight-origin-autosuggestion-option-item-0']");
    private final By destinationInput = By.cssSelector("[data-testid='endesign-flight-destination-autosuggestion-input']");
    private final By destinationFirstOption = By.cssSelector("[data-testid='endesign-flight-destination-autosuggestion-option-item-0']");
    
    // Date Picker Locators
    private final By departureDateInput = By.cssSelector("[data-testid='enuygun-homepage-flight-departureDate-datepicker-input']");
    private final By returnDateInput = By.cssSelector("[data-testid='enuygun-homepage-flight-returnDate-datepicker']");
    private final By departureMonthForwardButton = By.cssSelector("[data-testid='enuygun-homepage-flight-departureDate-month-forward-button']");
    private final By returnMonthForwardButton = By.cssSelector("[data-testid='enuygun-homepage-flight-returnDate-month-forward-button']");
    
    // Search Button
    private final By searchButton = By.cssSelector("[data-testid='enuygun-homepage-flight-submitButton']");
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Ana sayfanın yüklendiğini doğrula
     */
    @Step("Verify home page is loaded")
    public boolean isHomePageLoaded() {
        logger.info("Verifying home page is loaded");
        return driver.getCurrentUrl().contains("enuygun.com");
    }
    
    /**
     * Cookie kabul et (varsa)
     * OneTrust cookie consent dialog'ındaki "KABUL ET" butonuna tıklar
     */
    @Step("Accept cookies if present")
    public HomePage acceptCookies() {
        try {
            if (isElementVisible(cookieAcceptButton)) {
                click(cookieAcceptButton);
                logger.info("Cookies accepted via OneTrust dialog");
                Thread.sleep(1000); // Cookie dialog'un kapanması için kısa bir bekleme
            }
        } catch (Exception e) {
            logger.info("Cookie dialog not found or already accepted");
        }
        return this;
    }
    
    /**
     * Gidiş-Dönüş (Round Trip) seçeneğini seç
     */
    @Step("Select round trip option")
    public HomePage selectRoundTrip() {
        try {
            click(roundTripRadioButton);
            logger.info("Round trip option selected");
            Thread.sleep(500);
        } catch (Exception e) {
            logger.warn("Could not click round trip button, might be already selected: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Kalkış noktası gir ve ilk seçeneği seç
     */
    @Step("Enter origin city: {origin}")
    public HomePage enterOrigin(String origin) {
        try {
            click(originInput);
            Thread.sleep(300);
            type(originInput, origin);
            logger.info("Origin entered: " + origin);
            Thread.sleep(1000);
            
            waitForElement(originFirstOption);
            click(originFirstOption);
            logger.info("First origin option selected");
            Thread.sleep(500);
        } catch (Exception e) {
            logger.error("Error entering origin: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Varış noktası gir ve ilk seçeneği seç
     */
    @Step("Enter destination city: {destination}")
    public HomePage enterDestination(String destination) {
        try {
            click(destinationInput);
            Thread.sleep(300);
            type(destinationInput, destination);
            logger.info("Destination entered: " + destination);
            Thread.sleep(1000);
            
            waitForElement(destinationFirstOption);
            click(destinationFirstOption);
            logger.info("First destination option selected");
            Thread.sleep(500);
        } catch (Exception e) {
            logger.error("Error entering destination: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Uçuş arama formu görünür mü kontrol et
     */
    public boolean isFlightSearchFormVisible() {
        return isElementVisible(originInput) && isElementVisible(destinationInput);
    }
    
    /**
     * Gidiş tarihini seç (dd.MM.yyyy formatında)
     * @param dateString dd.MM.yyyy formatında tarih (örn: 18.03.2026)
     */
    @Step("Select departure date: {dateString}")
    public HomePage selectDepartureDate(String dateString) {
        try {
            logger.info("Selecting departure date: " + dateString);
            
            // Tarihi parse et
            String[] dateParts = dateString.split("\\.");
            String day = dateParts[0]; // Gün (örn: "18")
            String month = dateParts[1]; // Ay (örn: "03")
            String year = dateParts[2]; // Yıl (örn: "2026")
            
            // Tarih input'una tıkla - takvim açılsın
            click(departureDateInput);
            Thread.sleep(800); // Takvimin açılması için kısa bekleme
            
            // Takvimde günü seç - spesifik ay ve gün ile (gidiş için forward button kullan)
            selectDateInCalendarByMonthAndDay(year, month, day, departureMonthForwardButton);
            
            logger.info("Departure date selected: " + dateString);
            Thread.sleep(300); // Kısa stabilizasyon
        } catch (Exception e) {
            logger.error("Error selecting departure date: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Dönüş tarihini seç (dd.MM.yyyy formatında)
     * NOT: Gidiş tarihi seçildikten sonra dönüş tarihine tıklanır
     * Dönüş takviminde gidiş tarihine kadar olan kısım pasif (disabled) olur
     * @param dateString dd.MM.yyyy formatında tarih (örn: 25.03.2026)
     */
    @Step("Select return date: {dateString}")
    public HomePage selectReturnDate(String dateString) {
        try {
            logger.info("Selecting return date: " + dateString);
            
            // Tarihi parse et
            String[] dateParts = dateString.split("\\.");
            String day = dateParts[0];
            String month = dateParts[1];
            String year = dateParts[2];
            
            // Gidiş tarihi seçildikten sonra kısa bir bekleme
            Thread.sleep(1000);
            
            // Dönüş tarihi input'a tıkla - dönüş takvimi açılır
            // NOT: returnDateInput locator'ı: enuygun-homepage-flight-returnDate-datepicker
            logger.info("Clicking return date picker");
            click(returnDateInput);
            Thread.sleep(1000); // Takvimin açılması için bekle
            
            logger.info("Return date calendar opened");
            
            // Takvimde günü seç - ay navigasyonu ve gün seçimi
            // Dönüş takviminde gidiş öncesi günler disabled olacak
            selectDateInCalendarByMonthAndDay(year, month, day, returnMonthForwardButton);
            
            logger.info("Return date selected: " + dateString);
            Thread.sleep(300);
        } catch (Exception e) {
            logger.error("Error selecting return date: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Takvimde belirli ay ve güne göre tarihi seç
     * Ay navigasyonu yaparak doğru aya ulaşır
     * @param year Yıl (örn: "2026")
     * @param month Ay (örn: "03")
     * @param day Gün (örn: "18")
     * @param forwardButton İleri butonu (gidiş veya dönüş için farklı)
     */
    private void selectDateInCalendarByMonthAndDay(String year, String month, String day, By forwardButton) {
        try {
            // Ay container ID'sini oluştur: calendar-month-2026-03
            String monthContainerId = "calendar-month-" + year + "-" + month;
            
            logger.info("Target date: " + day + "/" + month + "/" + year);
            logger.info("Looking for calendar month container: " + monthContainerId);
            
            // Doğru aya ulaşana kadar ileri butonuna tıkla
            int maxAttempts = 24; // Maksimum 24 ay ileri gidebilir
            int attempts = 0;
            
            while (attempts < maxAttempts) {
                // Hedef ay container'ı var mı kontrol et
                By monthContainer = By.id(monthContainerId);
                
                if (isElementPresent(monthContainer)) {
                    logger.info("Target month found: " + monthContainerId);
                    break;
                }
                
                // Hedef ay yoksa, ileri butonuna tıkla
                if (isElementVisible(forwardButton)) {
                    click(forwardButton);
                    logger.info("Clicked forward button, attempt: " + (attempts + 1));
                    Thread.sleep(300); // Hızlı kaydırma için kısa bekleme
                    attempts++;
                } else {
                    logger.warn("Forward button not visible");
                    break;
                }
            }
            
            if (attempts >= maxAttempts) {
                logger.error("Could not find target month after " + maxAttempts + " attempts");
            }
            
            // Doğru ayda olduğumuza göre günü seç
            // NOT: Disabled olmayan (aktif) günü seç
            // Gün değerini integer'a çevirip tekrar string'e çevir (leading zero'suz)
            int dayInt = Integer.parseInt(day);
            String dayForXPath = String.valueOf(dayInt); // "02" → "2", "25" → "25"
            
            By dateButton = By.xpath("//div[@id='" + monthContainerId + "']//button[@data-day='" + dayForXPath + "' and not(@disabled)]");
            
            logger.info("Selecting day button: day=" + dayForXPath + " (active/enabled days only)");
            
            waitForElement(dateButton);
            click(dateButton);
            logger.info("Date button clicked successfully: " + dayForXPath + "/" + month + "/" + year);
            
        } catch (Exception e) {
            logger.error("Error selecting date in calendar: " + e.getMessage());
            
            // Alternatif deneme: Sadece data-day ile (görünen ilk eşleşen)
            try {
                int dayInt = Integer.parseInt(day);
                String dayForXPath = String.valueOf(dayInt);
                By alternativeDateButton = By.cssSelector("button[data-day='" + dayForXPath + "'][data-testid='datepicker-active-day']");
                if (isElementPresent(alternativeDateButton)) {
                    click(alternativeDateButton);
                    logger.info("Date selected using alternative selector");
                }
            } catch (Exception ex) {
                logger.error("Could not select date with alternative method", ex);
            }
        }
    }
    
    /**
     * Ucuz Bilet Bul butonuna tıkla - Uçuş araması yap
     */
    @Step("Click search button to find flights")
    public void clickSearchButton() {
        try {
            logger.info("Clicking search button");
            
            // Search butonunun görünür ve tıklanabilir olduğundan emin ol
            waitForElement(searchButton);
            click(searchButton);
            
            logger.info("Search button clicked - Flight search initiated");
            
            // Sonuç sayfasının yüklenmesi için daha uzun bekleme
            Thread.sleep(5000); // 5 saniye - sonuç sayfası yavaş yüklenebilir
            
            logger.info("Waiting for search results page to load");
            
        } catch (Exception e) {
            logger.error("Error clicking search button: " + e.getMessage());
        }
    }
}
