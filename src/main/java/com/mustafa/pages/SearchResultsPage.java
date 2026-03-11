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
    
    // Locators - Havayolları filtresi
    private final By airlineFilterCard = By.cssSelector(".ctx-filter-airline.card-header");
    private final By airlineExpandIcon = By.cssSelector(".ctx-filter-airline.ei-expand-more");
    private final By airlineCollapseIcon = By.cssSelector(".ctx-filter-airline.ei-expand-less");
    private final By turkishAirlinesCheckbox = By.id("TKairlines");
    private final By turkishAirlinesLabel = By.cssSelector("label[for='TKairlines']");
    
    // Locators - Fiyat sıralama
    private final By sortButtonCheapest = By.cssSelector("[data-testid='sortButtons0'], .search__filter_sort-PRICE_ASC");
    private final By flightPrices = By.cssSelector("[data-testid='flightInfoPrice']");
    
    // Locators - Veri çıkarma için
    private final By airlineNames = By.cssSelector(".summary-marketing-airlines");
    private final By connectionInfo = By.cssSelector(".summary-transit");
    private final By flightDurations = By.cssSelector("[data-testid='departureFlightTime']");
    private final By arrivalTimes = By.cssSelector("[data-testid='arrivalTime']");
    
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
    
    /**
     * Havayolları filtresini aç
     */
    @Step("Expand airline filter")
    public SearchResultsPage expandAirlineFilter() {
        try {
            logger.info("Checking airline filter status");
            
            // Filtre kartına scroll et
            scrollToElement(airlineFilterCard);
            Thread.sleep(500);
            
            // Expand icon var mı kontrol et (kapalı mı?)
            boolean isClosed = driver.findElements(airlineExpandIcon).size() > 0;
            
            if (isClosed) {
                logger.info("Airline filter is closed (ei-expand-more found), clicking to expand");
                click(airlineExpandIcon); // Icon'a tıkla
                Thread.sleep(1000);
                logger.info("Airline filter expanded successfully");
            } else {
                logger.info("Airline filter is already expanded (ei-expand-less)");
            }
            
        } catch (Exception e) {
            logger.error("Error expanding airline filter: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Türk Hava Yolları filtresini seç
     */
    @Step("Select Turkish Airlines filter")
    public SearchResultsPage selectTurkishAirlines() {
        try {
            logger.info("Selecting Turkish Airlines filter");
            
            // Checkbox zaten seçili mi kontrol et
            WebElement checkbox = driver.findElement(turkishAirlinesCheckbox);
            boolean isChecked = checkbox.isSelected();
            
            if (isChecked) {
                logger.info("Turkish Airlines is already selected");
                return this;
            }
            
            // Label'a tıkla (checkbox'ı seçmek için)
            logger.info("Clicking Turkish Airlines checkbox");
            click(turkishAirlinesLabel);
            Thread.sleep(1000); // Filtrenin uygulanması için bekle
            
            // Tekrar kontrol et
            boolean nowChecked = driver.findElement(turkishAirlinesCheckbox).isSelected();
            logger.info("Turkish Airlines checkbox selected: " + nowChecked);
            
            if (nowChecked) {
                logger.info("Turkish Airlines filter selected successfully");
            } else {
                logger.warn("Turkish Airlines checkbox may not be selected properly");
            }
            
        } catch (Exception e) {
            logger.error("Error selecting Turkish Airlines: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Havayolu filtrelerin uygulanmasını bekle ve sonuçların güncellenmesini kontrol et
     */
    @Step("Wait for airline filters to apply")
    public SearchResultsPage waitForAirlineFiltersToApply() {
        try {
            logger.info("Waiting for airline filters to apply and results to update...");
            
            Thread.sleep(2000); // Filtrelerin uygulanması
            Thread.sleep(3000); // Sonuçların yenilenmesi
            
            logger.info("Airline filters applied, results should be updated");
            
            // Kaç uçuş kaldığını kontrol et
            int flightCount = driver.findElements(By.cssSelector(".flight-item")).size();
            logger.info("Flight count after airline filtering: " + flightCount);
            
        } catch (Exception e) {
            logger.error("Error waiting for airline filters: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Tüm listelenen uçuşların Türk Hava Yolları olduğunu doğrula
     * @return Tüm uçuşlar Türk Hava Yolları mı?
     */
    @Step("Verify all flights are Turkish Airlines")
    public boolean verifyAllFlightsAreTurkishAirlines() {
        try {
            logger.info("Verifying all listed flights are Turkish Airlines");
            
            // Tüm havayolu elementlerini bul
            By turkishAirlinesElements = By.cssSelector("[data-testid='Türk Hava Yolları']");
            java.util.List<WebElement> airlines = driver.findElements(turkishAirlinesElements);
            
            if (airlines.isEmpty()) {
                logger.warn("No airline information found in flight list!");
                return false;
            }
            
            logger.info("Found " + airlines.size() + " airline elements to verify");
            
            int turkishAirlinesCount = 0;
            int otherAirlinesCount = 0;
            
            // Tüm uçuş kartlarını kontrol et
            java.util.List<WebElement> flightItems = driver.findElements(By.cssSelector(".flight-item"));
            logger.info("Total flight items: " + flightItems.size());
            
            for (int i = 0; i < flightItems.size(); i++) {
                try {
                    WebElement flight = flightItems.get(i);
                    
                    // Bu uçuşta Türk Hava Yolları elementi var mı?
                    java.util.List<WebElement> tkElements = flight.findElements(turkishAirlinesElements);
                    
                    if (tkElements.size() > 0) {
                        turkishAirlinesCount++;
                        String airlineName = tkElements.get(0).getText().trim();
                        logger.info("✓ Flight " + (i + 1) + ": " + airlineName + " - MATCH");
                    } else {
                        otherAirlinesCount++;
                        // Hangi havayolu olduğunu bul
                        java.util.List<WebElement> otherAirlines = flight.findElements(By.cssSelector(".summary-marketing-airlines"));
                        String airlineName = otherAirlines.isEmpty() ? "Unknown" : otherAirlines.get(0).getText().trim();
                        logger.warn("✗ Flight " + (i + 1) + ": " + airlineName + " - NOT Turkish Airlines");
                    }
                    
                } catch (Exception e) {
                    logger.error("Error checking flight " + (i + 1) + ": " + e.getMessage());
                    otherAirlinesCount++;
                }
            }
            
            logger.info("=== TURKISH AIRLINES FILTER VERIFICATION ===");
            logger.info("Turkish Airlines flights: " + turkishAirlinesCount);
            logger.info("Other airlines: " + otherAirlinesCount);
            logger.info("Total flights: " + flightItems.size());
            
            if (flightItems.size() > 0) {
                logger.info("Success rate: " + (turkishAirlinesCount * 100 / flightItems.size()) + "%");
            }
            
            return otherAirlinesCount == 0 && turkishAirlinesCount > 0;
            
        } catch (Exception e) {
            logger.error("Error verifying Turkish Airlines filter: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * "En ucuz" butonuna tıklayarak fiyata göre sırala (ucuzdan pahalıya)
     */
    @Step("Sort flights by price (cheapest first)")
    public SearchResultsPage sortByPriceCheapestFirst() {
        try {
            logger.info("Sorting flights by price (cheapest first)");
            
            // En ucuz butonunu bul ve tıkla
            WebElement sortButton = driver.findElement(sortButtonCheapest);
            
            // Zaten aktif mi kontrol et
            String buttonClass = sortButton.getAttribute("class");
            if (buttonClass != null && buttonClass.contains("active")) {
                logger.info("Price sort (cheapest) is already active");
                return this;
            }
            
            // Butona tıkla
            logger.info("Clicking 'En ucuz' (cheapest) sort button");
            click(sortButtonCheapest);
            
            // Sıralama uygulanması için bekle
            Thread.sleep(2000);
            logger.info("Price sorting applied - waiting for results to update");
            Thread.sleep(2000);
            
            logger.info("Flights sorted by price (cheapest first) successfully");
            
        } catch (Exception e) {
            logger.error("Error sorting by price: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Fiyatların ucuzdan pahalıya doğru sıralandığını doğrula
     * @return Fiyatlar doğru sıralı mı?
     */
    @Step("Verify flights are sorted by price (cheapest to most expensive)")
    public boolean verifyPricesSortedCheapestFirst() {
        try {
            logger.info("Verifying flights are sorted by price (cheapest to most expensive)");
            
            // Tüm fiyatları al
            java.util.List<WebElement> priceElements = driver.findElements(flightPrices);
            
            if (priceElements.isEmpty()) {
                logger.warn("No flight prices found!");
                return false;
            }
            
            logger.info("Found " + priceElements.size() + " flight prices to verify");
            
            java.util.List<Double> prices = new java.util.ArrayList<>();
            
            // Her fiyatı parse et
            for (int i = 0; i < priceElements.size(); i++) {
                try {
                    WebElement priceElement = priceElements.get(i);
                    String priceStr = priceElement.getAttribute("data-price");
                    
                    if (priceStr != null && !priceStr.isEmpty()) {
                        double price = Double.parseDouble(priceStr);
                        prices.add(price);
                        logger.info("Flight " + (i + 1) + ": " + price + " TL");
                    }
                    
                } catch (Exception e) {
                    logger.error("Error parsing price for flight " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            if (prices.isEmpty()) {
                logger.warn("No valid prices found to verify!");
                return false;
            }
            
            // Sıralamayı kontrol et (her fiyat bir öncekinden büyük veya eşit olmalı)
            boolean isSorted = true;
            int wrongOrderCount = 0;
            
            for (int i = 1; i < prices.size(); i++) {
                double previousPrice = prices.get(i - 1);
                double currentPrice = prices.get(i);
                
                if (currentPrice < previousPrice) {
                    isSorted = false;
                    wrongOrderCount++;
                    logger.warn("✗ Wrong order at position " + (i + 1) + ": " + 
                               previousPrice + " TL > " + currentPrice + " TL (should be <=)");
                } else {
                    logger.info("✓ Correct order at position " + (i + 1) + ": " + 
                               previousPrice + " TL <= " + currentPrice + " TL");
                }
            }
            
            logger.info("=== PRICE SORTING VERIFICATION ===");
            logger.info("Total flights checked: " + prices.size());
            logger.info("Correctly ordered: " + (prices.size() - wrongOrderCount - 1));
            logger.info("Wrong order: " + wrongOrderCount);
            logger.info("Is sorted correctly: " + isSorted);
            
            if (prices.size() > 1) {
                logger.info("Cheapest: " + prices.get(0) + " TL");
                logger.info("Most expensive: " + prices.get(prices.size() - 1) + " TL");
            }
            
            return isSorted;
            
        } catch (Exception e) {
            logger.error("Error verifying price sorting: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Uçuş verilerini temsil eden sınıf
     */
    public static class FlightData {
        private String departureTime;
        private String arrivalTime;
        private String airlineName;
        private String price;
        private String connectionInfo;
        private String flightDuration;
        
        public FlightData(String departureTime, String arrivalTime, String airlineName, 
                         String price, String connectionInfo, String flightDuration) {
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.airlineName = airlineName;
            this.price = price;
            this.connectionInfo = connectionInfo;
            this.flightDuration = flightDuration;
        }
        
        public String getDepartureTime() { return departureTime; }
        public String getArrivalTime() { return arrivalTime; }
        public String getAirlineName() { return airlineName; }
        public String getPrice() { return price; }
        public String getConnectionInfo() { return connectionInfo; }
        public String getFlightDuration() { return flightDuration; }
        
        public String toCsvRow() {
            return String.format("%s,%s,%s,%s,%s,%s",
                departureTime, arrivalTime, airlineName, price, connectionInfo, flightDuration);
        }
    }
    
    /**
     * Tüm uçuşlardan veri çek
     */
    @Step("Extract flight data from all search results")
    public java.util.List<FlightData> extractAllFlightData() {
        java.util.List<FlightData> flightDataList = new java.util.ArrayList<>();
        
        try {
            logger.info("Starting to extract flight data from search results");
            
            // Ekstra bekleme - sayfa tam yüklensin
            Thread.sleep(3000);
            
            // Detay butonlarını bul - her buton bir uçuş kartına ait
            By detailButtonLocator = By.cssSelector(".action-detail-btn");
            java.util.List<WebElement> detailButtons = driver.findElements(detailButtonLocator);
            logger.info("Found " + detailButtons.size() + " detail buttons (flights) to extract data from");
            
            if (detailButtons.isEmpty()) {
                logger.warn("No flight detail buttons found on the page!");
                return flightDataList;
            }
            
            // Her detay butonunun parent elementinden (uçuş kartı) veri çek
            for (int i = 0; i < detailButtons.size(); i++) {
                try {
                    // Her iterasyonda tekrar bul (DOM değişebilir)
                    java.util.List<WebElement> currentButtons = driver.findElements(detailButtonLocator);
                    if (i >= currentButtons.size()) {
                        logger.warn("Button index out of bounds at " + i);
                        break;
                    }
                    
                    WebElement detailButton = currentButtons.get(i);
                    
                    // Butonun parent element'ini bul (uçuş kartı)
                    // Birkaç level yukarı çık - flight-item-wrapper veya flight-item div'i
                    WebElement flightCard = detailButton.findElement(By.xpath("./ancestor::div[contains(@class, 'flight-item')]"));
                    
                    // Departure time
                    String departureTime = "N/A";
                    java.util.List<WebElement> depTimes = flightCard.findElements(By.cssSelector("[data-testid='departureTime']"));
                    if (!depTimes.isEmpty()) {
                        departureTime = depTimes.get(0).getText().trim();
                    }
                    
                    // Arrival time
                    String arrivalTime = "N/A";
                    java.util.List<WebElement> arrTimes = flightCard.findElements(By.cssSelector("[data-testid='arrivalTime']"));
                    if (!arrTimes.isEmpty()) {
                        arrivalTime = arrTimes.get(0).getText().trim();
                    }
                    
                    // Airline name
                    String airlineName = "N/A";
                    java.util.List<WebElement> airlines = flightCard.findElements(By.cssSelector(".summary-marketing-airlines"));
                    if (!airlines.isEmpty()) {
                        airlineName = airlines.get(0).getText().trim();
                    }
                    
                    // Price (data-price attribute'tan al)
                    String price = "N/A";
                    java.util.List<WebElement> priceElements = flightCard.findElements(By.cssSelector("[data-testid='flightInfoPrice']"));
                    if (!priceElements.isEmpty()) {
                        String priceData = priceElements.get(0).getAttribute("data-price");
                        String currency = priceElements.get(0).getAttribute("data-currency");
                        if (priceData != null && !priceData.isEmpty()) {
                            price = priceData + " " + (currency != null ? currency : "TRY");
                        }
                    }
                    
                    // Connection info (Direkt Uçuş / 1 Aktarma vb.)
                    String connectionInfo = "N/A";
                    java.util.List<WebElement> connections = flightCard.findElements(By.cssSelector(".summary-transit"));
                    if (!connections.isEmpty()) {
                        connectionInfo = connections.get(0).getText().trim();
                    }
                    
                    // Flight duration
                    String flightDuration = "N/A";
                    java.util.List<WebElement> durations = flightCard.findElements(By.cssSelector("[data-testid='departureFlightTime']"));
                    if (!durations.isEmpty()) {
                        flightDuration = durations.get(0).getText().trim();
                    }
                    
                    // FlightData objesi oluştur ve listeye ekle
                    FlightData flightData = new FlightData(
                        departureTime, arrivalTime, airlineName, 
                        price, connectionInfo, flightDuration
                    );
                    
                    flightDataList.add(flightData);
                    
                    logger.info("Flight " + (i + 1) + ": " + 
                               airlineName + " | " + 
                               departureTime + " - " + arrivalTime + " | " + 
                               price + " | " + 
                               connectionInfo + " | " + 
                               flightDuration);
                    
                } catch (Exception e) {
                    logger.warn("Error extracting data from flight " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            logger.info("=== DATA EXTRACTION SUMMARY ===");
            logger.info("Total flights found: " + detailButtons.size());
            logger.info("Successfully extracted: " + flightDataList.size());
            
        } catch (Exception e) {
            logger.error("Error extracting flight data: " + e.getMessage());
            e.printStackTrace();
        }
        
        return flightDataList;
    }
    
    /**
     * Dönüş uçuşu listesinden belirli bir index'teki uçuşun detay butonuna tıkla
     */
    @Step("Expand return flight details for flight at index {flightIndex}")
    public SearchResultsPage expandReturnFlightDetails(int flightIndex) {
        try {
            logger.info("Expanding return flight details for flight at index: " + flightIndex);
            
            // Dönüş uçuşları container'ını bul
            WebElement returnFlightList = driver.findElement(By.cssSelector(".flight-list-return"));
            
            // Bu container içindeki detay butonlarını bul
            java.util.List<WebElement> detailButtons = returnFlightList.findElements(By.cssSelector(".action-detail-btn"));
            
            if (flightIndex >= detailButtons.size()) {
                logger.error("Return flight index " + flightIndex + " is out of bounds. Total return flights: " + detailButtons.size());
                return this;
            }
            
            WebElement detailButton = detailButtons.get(flightIndex);
            scrollToElement(By.cssSelector(".flight-list-return"));
            Thread.sleep(500);
            
            detailButton.click();
            Thread.sleep(1000); // Detayların açılmasını bekle
            
            logger.info("Return flight details expanded successfully");
            
        } catch (Exception e) {
            logger.error("Error expanding return flight details: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Dönüş uçuşu bilgilerini oku
     */
    @Step("Get return flight info for flight at index {flightIndex}")
    public FlightInfo getReturnFlightInfo(int flightIndex) {
        try {
            logger.info("Getting return flight info for index: " + flightIndex);
            
            // Dönüş uçuşları container'ını bul
            WebElement returnFlightList = driver.findElement(By.cssSelector(".flight-list-return"));
            
            // Bu container içindeki uçuş kartlarını bul
            java.util.List<WebElement> flightCards = returnFlightList.findElements(By.cssSelector(".flight-item"));
            
            if (flightIndex >= flightCards.size()) {
                logger.error("Return flight index out of bounds");
                return null;
            }
            
            WebElement flightCard = flightCards.get(flightIndex);
            
            // Basic bilgileri al
            String airline = flightCard.findElements(By.cssSelector(".summary-marketing-airlines")).get(0).getText().trim();
            String departureTime = flightCard.findElements(By.cssSelector("[data-testid='departureTime']")).get(0).getText().trim();
            String arrivalTime = flightCard.findElements(By.cssSelector("[data-testid='arrivalTime']")).get(0).getText().trim();
            String duration = flightCard.findElements(By.cssSelector("[data-testid='returnFlightTime']")).get(0).getText().trim();
            String price = flightCard.findElements(By.cssSelector("[data-testid='flightInfoPrice']")).get(0).getAttribute("data-price");
            
            // Detayları aç ve segment bilgilerini al
            expandReturnFlightDetails(flightIndex);
            Thread.sleep(1000);
            
            // Segment detaylarını al
            WebElement flightDetailWrapper = flightCard.findElement(By.cssSelector(".flight-detail-wrapper"));
            String flightNumber = flightDetailWrapper.findElement(By.cssSelector("[data-testid='flightNumber']")).getText().trim();
            String flightClass = flightDetailWrapper.findElement(By.cssSelector("[data-testid='flightClass']")).getText().trim();
            
            FlightInfo info = new FlightInfo(airline, departureTime, arrivalTime, duration, price, flightNumber, flightClass);
            logger.info("Return flight info: " + airline + " - " + flightNumber + " (" + departureTime + " - " + arrivalTime + ")");
            
            return info;
            
        } catch (Exception e) {
            logger.error("Error getting return flight info: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Dönüş uçuşunun "Seç" butonuna tıkla
     */
    @Step("Click select button for return flight at index {flightIndex}")
    public SearchResultsPage clickSelectReturnFlight(int flightIndex) {
        try {
            logger.info("Clicking select button for return flight at index: " + flightIndex);
            
            // Dönüş uçuşları container'ını bul
            WebElement returnFlightList = driver.findElement(By.cssSelector(".flight-list-return"));
            
            // Bu container içindeki seç butonlarını bul
            java.util.List<WebElement> selectButtons = returnFlightList.findElements(By.cssSelector(".action-select-btn"));
            
            if (flightIndex >= selectButtons.size()) {
                logger.error("Return flight index out of bounds");
                return this;
            }
            
            WebElement selectButton = selectButtons.get(flightIndex);
            scrollToElement(By.cssSelector(".flight-list-return"));
            Thread.sleep(500);
            
            selectButton.click();
            Thread.sleep(2000); // Paket seçim modalının açılmasını bekle
            
            logger.info("Return flight select button clicked - Package selection modal should be visible");
            
        } catch (Exception e) {
            logger.error("Error clicking return flight select button: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Belirli bir index'teki uçuşun detay butonuna tıkla
     */
    @Step("Expand flight details for flight at index {flightIndex}")
    public SearchResultsPage expandFlightDetails(int flightIndex) {
        try {
            logger.info("Expanding flight details for flight at index: " + flightIndex);
            
            By detailButtonLocator = By.cssSelector(".action-detail-btn");
            java.util.List<WebElement> detailButtons = driver.findElements(detailButtonLocator);
            
            if (flightIndex >= detailButtons.size()) {
                logger.error("Flight index " + flightIndex + " is out of bounds. Total flights: " + detailButtons.size());
                return this;
            }
            
            WebElement detailButton = detailButtons.get(flightIndex);
            scrollToElement(By.cssSelector(".action-detail-btn"));
            Thread.sleep(500);
            
            detailButton.click();
            Thread.sleep(1000); // Detayların açılmasını bekle
            
            logger.info("Flight details expanded successfully");
            
        } catch (Exception e) {
            logger.error("Error expanding flight details: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Belirli bir index'teki uçuşun bilgilerini oku
     */
    @Step("Get flight info for flight at index {flightIndex}")
    public FlightInfo getFlightInfo(int flightIndex) {
        try {
            logger.info("Getting flight info for index: " + flightIndex);
            
            By detailButtonLocator = By.cssSelector(".action-detail-btn");
            java.util.List<WebElement> detailButtons = driver.findElements(detailButtonLocator);
            
            if (flightIndex >= detailButtons.size()) {
                logger.error("Flight index out of bounds");
                return null;
            }
            
            WebElement detailButton = detailButtons.get(flightIndex);
            WebElement flightCard = detailButton.findElement(By.xpath("./ancestor::div[contains(@class, 'flight-item')]"));
            
            // Basic bilgileri al
            String airline = flightCard.findElements(By.cssSelector(".summary-marketing-airlines")).get(0).getText().trim();
            String departureTime = flightCard.findElements(By.cssSelector("[data-testid='departureTime']")).get(0).getText().trim();
            String arrivalTime = flightCard.findElements(By.cssSelector("[data-testid='arrivalTime']")).get(0).getText().trim();
            String duration = flightCard.findElements(By.cssSelector("[data-testid='departureFlightTime']")).get(0).getText().trim();
            String price = flightCard.findElements(By.cssSelector("[data-testid='flightInfoPrice']")).get(0).getAttribute("data-price");
            
            // Detayları aç ve segment bilgilerini al
            expandFlightDetails(flightIndex);
            Thread.sleep(1000);
            
            // Segment detaylarını al
            WebElement flightDetailWrapper = flightCard.findElement(By.cssSelector(".flight-detail-wrapper"));
            String flightNumber = flightDetailWrapper.findElement(By.cssSelector("[data-testid='flightNumber']")).getText().trim();
            String flightClass = flightDetailWrapper.findElement(By.cssSelector("[data-testid='flightClass']")).getText().trim();
            
            FlightInfo info = new FlightInfo(airline, departureTime, arrivalTime, duration, price, flightNumber, flightClass);
            logger.info("Flight info: " + airline + " - " + flightNumber + " (" + departureTime + " - " + arrivalTime + ")");
            
            return info;
            
        } catch (Exception e) {
            logger.error("Error getting flight info: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Belirli bir index'teki uçuşun "Seç" butonuna tıkla
     */
    @Step("Click select button for flight at index {flightIndex}")
    public SearchResultsPage clickSelectFlight(int flightIndex) {
        try {
            logger.info("Clicking select button for flight at index: " + flightIndex);
            
            By selectButtonLocator = By.cssSelector(".action-select-btn");
            java.util.List<WebElement> selectButtons = driver.findElements(selectButtonLocator);
            
            if (flightIndex >= selectButtons.size()) {
                logger.error("Flight index out of bounds");
                return this;
            }
            
            WebElement selectButton = selectButtons.get(flightIndex);
            scrollToElement(selectButtonLocator);
            Thread.sleep(500);
            
            selectButton.click();
            Thread.sleep(2000); // Paket seçim modalının açılmasını bekle
            
            logger.info("Select button clicked - Package selection modal should be visible");
            
        } catch (Exception e) {
            logger.error("Error clicking select button: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Gidiş uçuşu için paket seç (BASIC, FLEX, PREMIUM)
     */
    @Step("Select departure package: {packageType}")
    public SearchResultsPage selectDeparturePackage(String packageType) {
        try {
            logger.info("Selecting departure package: " + packageType);
            
            String testId = "departureProviderPackageItem" + packageType.toUpperCase();
            By packageLocator = By.cssSelector("[data-testid='" + testId + "']");
            
            Thread.sleep(1000); // Modal'ın tam yüklenmesini bekle
            
            WebElement packageElement = driver.findElement(packageLocator);
            packageElement.click();
            Thread.sleep(500);
            
            logger.info("Departure package selected: " + packageType);
            
        } catch (Exception e) {
            logger.error("Error selecting departure package: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Dönüş uçuşu için paket seç (BASIC, FLEX, PREMIUM)
     */
    @Step("Select return package: {packageType}")
    public SearchResultsPage selectReturnPackage(String packageType) {
        try {
            logger.info("Selecting return package: " + packageType);
            
            String testId = "returnProviderPackageItem" + packageType.toUpperCase();
            By packageLocator = By.cssSelector("[data-testid='" + testId + "']");
            
            Thread.sleep(1000); // Modal'ın tam yüklenmesini bekle
            
            WebElement packageElement = driver.findElement(packageLocator);
            packageElement.click();
            Thread.sleep(500);
            
            logger.info("Return package selected: " + packageType);
            
        } catch (Exception e) {
            logger.error("Error selecting return package: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Paket seçimini onayla ve devam et
     */
    @Step("Confirm package selection")
    public SearchResultsPage confirmPackageSelection() {
        try {
            logger.info("Confirming package selection");
            
            By confirmButtonLocator = By.cssSelector("[data-testid='providerSelectBtn']");
            Thread.sleep(1000);
            
            WebElement confirmButton = driver.findElement(confirmButtonLocator);
            confirmButton.click();
            Thread.sleep(3000); // Sayfanın yüklenmesini bekle
            
            logger.info("Package selection confirmed - Navigating to next page");
            
        } catch (Exception e) {
            logger.error("Error confirming package selection: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Uçuş bilgisi için inner class
     */
    public static class FlightInfo {
        private String airline;
        private String departureTime;
        private String arrivalTime;
        private String duration;
        private String price;
        private String flightNumber;
        private String flightClass;
        
        public FlightInfo(String airline, String departureTime, String arrivalTime, 
                         String duration, String price, String flightNumber, String flightClass) {
            this.airline = airline;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.duration = duration;
            this.price = price;
            this.flightNumber = flightNumber;
            this.flightClass = flightClass;
        }
        
        public String getAirline() { return airline; }
        public String getDepartureTime() { return departureTime; }
        public String getArrivalTime() { return arrivalTime; }
        public String getDuration() { return duration; }
        public String getPrice() { return price; }
        public String getFlightNumber() { return flightNumber; }
        public String getFlightClass() { return flightClass; }
    }
}
