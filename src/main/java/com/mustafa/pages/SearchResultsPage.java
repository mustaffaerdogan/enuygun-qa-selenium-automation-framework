package com.mustafa.pages;

import com.mustafa.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * SearchResultsPage - Uçuş arama sonuçları sayfası için Page Object
 */
public class SearchResultsPage extends BasePage {
    
    // Locators - Gidiş kalkış / varış saatleri filtresi
    private final By departureTimeFilterCard = By.cssSelector(".ctx-filter-departure-return-time.card-header");
    private final By departureTimeExpandIcon = By.cssSelector(".ctx-filter-departure-return-time.ei-expand-more");
    private final By departureTimeCollapseIcon = By.cssSelector(".ctx-filter-departure-return-time.ei-expand-less");
    private final By departureTimeCollapseDiv = By.xpath("//div[contains(@class, 'ctx-filter-departure-return-time')]/following-sibling::div[contains(@class, 'collapse')]");
    
    // Slider locators - Gidiş Kalkış saati için (departureDepartureTimeSlider içindeki)
    private final By departureTimeSliderContainer = By.cssSelector("[data-testid='departureDepartureTimeSlider']");
    private final By departureTimeSliderLeft = By.cssSelector("[data-testid='departureDepartureTimeSlider'] .rc-slider-handle.rc-slider-handle-1");
    private final By departureTimeSliderRight = By.cssSelector("[data-testid='departureDepartureTimeSlider'] .rc-slider-handle.rc-slider-handle-2");
    
    // Flight results locators
    private final By flightCards = By.cssSelector(".flight-list-body .body-row, .flight-card");
    private final By flightDepartureTimes = By.cssSelector("[data-testid='departureTime']");
    private final By flightDetailButtons = By.cssSelector(".action-detail-btn");
    private final By flightDetailWrappers = By.cssSelector(".flight-detail-wrapper");
    private final By flightDepartureAirports = By.cssSelector("[data-testid='undefinedFlightAirportInfo']");
    private final By flightArrivalAirports = By.cssSelector("[data-testid='flightAirportInfo']");
    
    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Sonuç sayfasının yüklendiğini doğrula
     */
    @Step("Verify search results page is loaded")
    public boolean isResultsPageLoaded() {
        try {
            logger.info("Waiting for results page to load (extended wait for loading)...");
            Thread.sleep(8000); // Loading uzun sürebilir - 8 saniye bekle
            
            logger.info("Checking for filter card presence");
            
            // Gidiş kalkış filtre kartı var mı kontrol et (URL doğrulaması yok)
            boolean hasFilter = driver.findElements(departureTimeFilterCard).size() > 0;
            logger.info("Departure time filter card found: " + hasFilter);
            
            if (hasFilter) {
                logger.info("Results page loaded successfully with filters visible");
            } else {
                logger.warn("Results page loaded but filters not visible yet");
            }
            
            return hasFilter;
            
        } catch (Exception e) {
            logger.warn("Results page load verification failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gidiş kalkış/varış saatleri filtresini aç
     */
    @Step("Expand departure time filter")
    public SearchResultsPage expandDepartureTimeFilter() {
        try {
            logger.info("Checking departure time filter status");
            
            // Filtre kartına scroll et
            scrollToElement(departureTimeFilterCard);
            Thread.sleep(500);
            
            // Collapse div'i kontrol et - "show" class'ı var mı?
            WebElement collapseDiv = driver.findElement(departureTimeCollapseDiv);
            String collapseClass = collapseDiv.getAttribute("class");
            logger.info("Collapse div classes: " + collapseClass);
            
            boolean isExpanded = collapseClass != null && collapseClass.contains("show");
            
            if (!isExpanded) {
                logger.info("Filter is collapsed (no 'show' class), clicking card to expand");
                click(departureTimeFilterCard);
                Thread.sleep(1000); // Slider'ların görünmesi için bekle
                logger.info("Departure time filter expanded successfully");
                
                // Tekrar kontrol et
                String newClass = driver.findElement(departureTimeCollapseDiv).getAttribute("class");
                logger.info("After click, collapse div classes: " + newClass);
            } else {
                logger.info("Filter is already expanded ('show' class present)");
            }
            
        } catch (Exception e) {
            logger.error("Error expanding departure time filter: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Gidiş saati filtresini ayarla (slider ile)
     * @param startHour Başlangıç saati (örn: 10 → 10:00)
     * @param endHour Bitiş saati (örn: 18 → 18:00)
     */
    @Step("Set departure time filter: {startHour}:00 - {endHour}:00")
    public SearchResultsPage setDepartureTimeFilter(int startHour, int endHour) {
        try {
            logger.info("Setting departure time filter: " + startHour + ":00 - " + endHour + ":00");
            
            // Saat → Dakika dönüşümü
            int startMinutes = startHour * 60;  // 10 saat = 600 dakika
            int endMinutes = endHour * 60;      // 18 saat = 1080 dakika
            
            // Slider container'ın görünür olmasını bekle
            Thread.sleep(1000);
            
            // Slider container'ı kontrol et
            boolean sliderContainerExists = driver.findElements(departureTimeSliderContainer).size() > 0;
            logger.info("Departure time slider container exists: " + sliderContainerExists);
            
            if (!sliderContainerExists) {
                logger.error("Slider container not found! Filter may not be expanded.");
                return this;
            }
            
            // Slider elementlerini bul
            WebElement leftSlider = driver.findElement(departureTimeSliderLeft);
            WebElement rightSlider = driver.findElement(departureTimeSliderRight);
            
            logger.info("=== BEFORE ADJUSTMENT ===");
            logger.info("Left slider - Current: " + leftSlider.getAttribute("aria-valuenow"));
            logger.info("Right slider - Current: " + rightSlider.getAttribute("aria-valuenow"));
            
            // ÖNCE SAĞ SLIDER'I AYARLA (daha geniş aralık için)
            logger.info("=== ADJUSTING RIGHT SLIDER FIRST ===");
            setSliderValue(rightSlider, endMinutes);
            Thread.sleep(1000); // Daha uzun bekleme - sonuçlar güncellensin
            
            logger.info("Right slider after adjustment: " + rightSlider.getAttribute("aria-valuenow"));
            
            // SONRA SOL SLIDER'I AYARLA
            logger.info("=== ADJUSTING LEFT SLIDER ===");
            setSliderValue(leftSlider, startMinutes);
            Thread.sleep(1000); // Daha uzun bekleme - sonuçlar güncellensin
            
            logger.info("Left slider after adjustment: " + leftSlider.getAttribute("aria-valuenow"));
            
            logger.info("=== FINAL VALUES ===");
            logger.info("Left slider final: " + leftSlider.getAttribute("aria-valuenow") + " (target: " + startMinutes + ")");
            logger.info("Right slider final: " + rightSlider.getAttribute("aria-valuenow") + " (target: " + endMinutes + ")");
            
            logger.info("Departure time filter sliders adjusted - waiting for results to update...");
            
        } catch (Exception e) {
            logger.error("Error setting departure time filter: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Slider değerini ayarla
     * @param slider Slider WebElement
     * @param targetMinutes Hedef dakika değeri (0-1439 arası)
     */
    private void setSliderValue(WebElement slider, int targetMinutes) {
        try {
            // Slider'ın mevcut değeri
            int currentValue = Integer.parseInt(slider.getAttribute("aria-valuenow"));
            int maxValue = Integer.parseInt(slider.getAttribute("aria-valuemax")); // 1439 (23:59)
            
            logger.info("Setting slider from " + currentValue + " to " + targetMinutes);
            
            // Slider track'i bul (slider'ın parent container'ındaki .rc-slider)
            WebElement sliderContainer = slider.findElement(By.xpath("./ancestor::div[contains(@class, 'rc-slider')]"));
            int containerWidth = sliderContainer.getSize().getWidth();
            
            logger.info("Slider container width: " + containerWidth + " pixels");
            
            // Hedef pozisyonu hesapla (piksel cinsinden - container'a göre)
            double targetPercentage = (double) targetMinutes / maxValue;
            int targetPixels = (int) (targetPercentage * containerWidth);
            
            double currentPercentage = (double) currentValue / maxValue;
            int currentPixels = (int) (currentPercentage * containerWidth);
            
            int offsetX = targetPixels - currentPixels;
            
            logger.info("Target: " + targetMinutes + " minutes = " + (targetPercentage * 100) + "% = " + targetPixels + " pixels");
            logger.info("Current: " + currentValue + " minutes = " + (currentPercentage * 100) + "% = " + currentPixels + " pixels");
            logger.info("Moving slider by " + offsetX + " pixels");
            
            // Slider'ı sürükle
            Actions actions = new Actions(driver);
            actions.clickAndHold(slider)
                   .moveByOffset(offsetX, 0)
                   .release()
                   .build()
                   .perform();
            
            Thread.sleep(300);
            
            // Yeni değeri logla ve doğrula
            String newValue = slider.getAttribute("aria-valuenow");
            logger.info("Slider moved to: " + newValue + " minutes");
            
            // Hedef değere yakın mı kontrol et (±30 dakika tolerans)
            int actualValue = Integer.parseInt(newValue);
            int difference = Math.abs(actualValue - targetMinutes);
            if (difference > 30) {
                logger.warn("Slider value difference: " + difference + " minutes (expected: " + targetMinutes + ", actual: " + actualValue + ")");
            }
            
        } catch (Exception e) {
            logger.error("Error setting slider value: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Filtrelerin uygulandığını bekle ve sonuçların yenilenmesini kontrol et
     */
    @Step("Wait for filters to apply")
    public SearchResultsPage waitForFiltersToApply() {
        try {
            logger.info("Waiting for filters to apply and results to update...");
            
            // Filtrelerin uygulanması için bekle
            Thread.sleep(2000);
            
            // Sonuçların yenilenmesini bekle (loading indicator kaybolsun)
            logger.info("Checking if loading indicator is gone...");
            Thread.sleep(3000);
            
            // Toplam bekleme: 5 saniye
            logger.info("Filters applied, results should be updated now");
            
            // Kaç uçuş kaldığını kontrol et
            int flightCount = driver.findElements(flightDepartureTimes).size();
            logger.info("Flight count after filtering: " + flightCount);
            
        } catch (Exception e) {
            logger.error("Error waiting for filters: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Uçuş kalkış saatlerinin belirtilen aralıkta olduğunu doğrula
     * Her uçuş kartından SADECE İLK departureTime elementini kontrol eder (aktarmalı uçuşlar için)
     * @param startHour Başlangıç saati (örn: 10)
     * @param endHour Bitiş saati (örn: 18)
     * @return Tüm uçuşlar aralıkta mı?
     */
    @Step("Verify all departure times are between {startHour}:00 - {endHour}:00")
    public boolean verifyDepartureTimesInRange(int startHour, int endHour) {
        try {
            logger.info("Verifying ALL departure times are between " + startHour + ":00 - " + endHour + ":00");
            
            // Her uçuş kartını ayrı ayrı kontrol et
            java.util.List<WebElement> flightCardElements = driver.findElements(flightCards);
            
            if (flightCardElements.isEmpty()) {
                logger.warn("No flight cards found! Trying alternative selector...");
                // Alternatif: Tüm departureTime'ları al ama her 2'de bir al (gidiş + dönüş)
                java.util.List<WebElement> allTimes = driver.findElements(flightDepartureTimes);
                logger.info("Found " + allTimes.size() + " total departure time elements");
                return verifyTimesFromList(allTimes, startHour, endHour);
            }
            
            logger.info("Found " + flightCardElements.size() + " flight cards");
            
            int validCount = 0;
            int invalidCount = 0;
            
            // Her uçuş kartından SADECE İLK departureTime'ı al
            for (int i = 0; i < flightCardElements.size(); i++) {
                try {
                    WebElement card = flightCardElements.get(i);
                    
                    // Bu karttaki İLK departureTime elementini bul
                    java.util.List<WebElement> cardDepartureTimes = card.findElements(flightDepartureTimes);
                    
                    if (cardDepartureTimes.isEmpty()) {
                        logger.warn("Flight card " + (i + 1) + ": No departure time found");
                        continue;
                    }
                    
                    // İLK segment'in kalkış saati (aktarmalı uçuşlarda ilk bacak)
                    String timeText = cardDepartureTimes.get(0).getText().trim();
                    
                    // Saati parse et
                    String[] parts = timeText.split(":");
                    int hour = Integer.parseInt(parts[0]);
                    int minute = Integer.parseInt(parts[1]);
                    
                    // Aralıkta mı kontrol et
                    boolean inRange = (hour >= startHour && hour < endHour) || 
                                     (hour == endHour && minute == 0);
                    
                    if (inRange) {
                        validCount++;
                        logger.info("✓ Flight " + (i + 1) + ": " + timeText + " - IN RANGE");
                        
                        // Aktarmalı mı?
                        if (cardDepartureTimes.size() > 1) {
                            logger.info("  (Aktarmalı uçuş - " + cardDepartureTimes.size() + " segment, sadece ilk kontrol edildi)");
                        }
                    } else {
                        invalidCount++;
                        logger.warn("✗ Flight " + (i + 1) + ": " + timeText + " - OUT OF RANGE (Expected: " + startHour + ":00 - " + endHour + ":00)");
                    }
                    
                } catch (Exception e) {
                    logger.error("Error checking flight card " + (i + 1) + ": " + e.getMessage());
                    invalidCount++;
                }
            }
            
            logger.info("=== VERIFICATION SUMMARY ===");
            logger.info("Valid flights: " + validCount);
            logger.info("Invalid flights: " + invalidCount);
            logger.info("Total checked: " + flightCardElements.size());
            
            if (flightCardElements.size() > 0) {
                logger.info("Success rate: " + (validCount * 100 / flightCardElements.size()) + "%");
            }
            
            return invalidCount == 0;
            
        } catch (Exception e) {
            logger.error("Error verifying departure times: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Alternatif: departureTime listesinden doğrula
     */
    private boolean verifyTimesFromList(java.util.List<WebElement> times, int startHour, int endHour) {
        int validCount = 0;
        int invalidCount = 0;
        
        for (int i = 0; i < times.size(); i++) {
            try {
                String timeText = times.get(i).getText().trim();
                String[] parts = timeText.split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                
                boolean inRange = (hour >= startHour && hour < endHour) || (hour == endHour && minute == 0);
                
                if (inRange) {
                    validCount++;
                    logger.info("✓ Time " + (i + 1) + ": " + timeText + " - IN RANGE");
                } else {
                    invalidCount++;
                    logger.warn("✗ Time " + (i + 1) + ": " + timeText + " - OUT OF RANGE");
                }
            } catch (Exception e) {
                invalidCount++;
            }
        }
        
        logger.info("Valid: " + validCount + ", Invalid: " + invalidCount + ", Total: " + times.size());
        return invalidCount == 0;
    }
    
    /**
     * Uçuş destinasyonlarının (kalkış ve varış) config'le eşleştiğini doğrula
     * Aktarmalı uçuşlarda: İLK segment'in kalkışı ve SON segment'in varışı kontrol edilir
     * @param expectedOrigin Beklenen kalkış şehri (örn: "İstanbul")
     * @param expectedDestination Beklenen varış şehri (örn: "Ankara")
     * @return Tüm uçuşlar doğru destinasyonlara mı?
     */
    @Step("Verify all flights match origin: {expectedOrigin} and destination: {expectedDestination}")
    public boolean verifyFlightDestinations(String expectedOrigin, String expectedDestination) {
        try {
            logger.info("Verifying flight destinations - Origin: " + expectedOrigin + ", Destination: " + expectedDestination);
            
            // ÖNCE: Tüm detay butonlarını aç (destinasyon bilgileri görünsün)
            logger.info("Opening all flight detail panels...");
            java.util.List<WebElement> detailButtons = driver.findElements(flightDetailButtons);
            logger.info("Found " + detailButtons.size() + " detail buttons");
            
            int openedCount = 0;
            for (int i = 0; i < detailButtons.size(); i++) {
                try {
                    WebElement button = detailButtons.get(i);
                    
                    if (button.isDisplayed()) {
                        String buttonHtml = button.getAttribute("innerHTML");
                        if (buttonHtml.contains("ei-expand-more")) {
                            button.click();
                            openedCount++;
                            Thread.sleep(100);
                            logger.info("Opened detail panel " + (i + 1));
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Could not click detail button " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            logger.info("Opened " + openedCount + " detail panels");
            Thread.sleep(1000);
            
            // SONRA: Her uçuş kartını ayrı ayrı kontrol et (aktarmalı uçuşlar için)
            java.util.List<WebElement> flightWrappers = driver.findElements(flightDetailWrappers);
            
            if (flightWrappers.isEmpty()) {
                logger.warn("No flight detail wrappers found!");
                return false;
            }
            
            logger.info("Found " + flightWrappers.size() + " flights to verify");
            
            int validFlights = 0;
            int invalidFlights = 0;
            
            for (int i = 0; i < flightWrappers.size(); i++) {
                try {
                    WebElement flightWrapper = flightWrappers.get(i);
                    
                    // Bu uçuş için tüm kalkış ve varış noktalarını bul
                    java.util.List<WebElement> departures = flightWrapper.findElements(By.cssSelector("[data-testid='undefinedFlightAirportInfo']"));
                    java.util.List<WebElement> arrivals = flightWrapper.findElements(By.cssSelector("[data-testid='flightAirportInfo']"));
                    
                    if (departures.isEmpty() || arrivals.isEmpty()) {
                        logger.warn("Flight " + (i + 1) + ": No departure/arrival info found");
                        invalidFlights++;
                        continue;
                    }
                    
                    // İLK kalkış noktası (ilk segment)
                    String firstDeparture = departures.get(0).getText().trim();
                    
                    // SON varış noktası (son segment)
                    String lastArrival = arrivals.get(arrivals.size() - 1).getText().trim();
                    
                    // Kontrol et
                    boolean originMatches = firstDeparture.contains(expectedOrigin);
                    boolean destinationMatches = lastArrival.contains(expectedDestination);
                    
                    if (originMatches && destinationMatches) {
                        validFlights++;
                        logger.info("✓ Flight " + (i + 1) + ": " + firstDeparture + " → " + lastArrival + " - MATCH");
                        if (departures.size() > 1) {
                            logger.info("  (Aktarmalı uçuş - " + departures.size() + " segment)");
                        }
                    } else {
                        invalidFlights++;
                        logger.warn("✗ Flight " + (i + 1) + ": MISMATCH");
                        if (!originMatches) {
                            logger.warn("  Origin: " + firstDeparture + " (Expected: " + expectedOrigin + ")");
                        }
                        if (!destinationMatches) {
                            logger.warn("  Destination: " + lastArrival + " (Expected: " + expectedDestination + ")");
                        }
                    }
                    
                } catch (Exception e) {
                    logger.error("Error verifying flight " + (i + 1) + ": " + e.getMessage());
                    invalidFlights++;
                }
            }
            
            logger.info("=== DESTINATION VERIFICATION SUMMARY ===");
            logger.info("Valid flights: " + validFlights);
            logger.info("Invalid flights: " + invalidFlights);
            logger.info("Total checked: " + flightWrappers.size());
            
            if (flightWrappers.size() > 0) {
                logger.info("Success rate: " + (validFlights * 100 / flightWrappers.size()) + "%");
            }
            
            return invalidFlights == 0;
            
        } catch (Exception e) {
            logger.error("Error verifying flight destinations: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
