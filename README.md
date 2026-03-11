# Enuygun.com Test Automation Framework

Enuygun.com için geliştirilmiş **production-ready** Selenium WebDriver tabanlı test otomasyon framework'ü. Page Object Model (POM) design pattern kullanılarak geliştirilmiştir.

**Proje Durumu:** 3/4 test case tamamlandı (%75 coverage)  
**Öne Çıkan Özellikler:** Data Analysis, CSV Export, Executive HTML Reports  
**Son Güncelleme:** Mart 2026

---

## İçindekiler

- [Test Case'leri](#test-caseleri)
- [Özellikler](#özellikler)
- [Teknolojiler](#teknolojiler)
- [Proje Yapısı](#proje-yapısı)
- [Kurulum](#kurulum)
- [Test Çalıştırma](#test-çalıştırma)
- [Raporlama](#raporlama)
- [Best Practices](#best-practices)

---

## Test Case'leri

Bu projede Enuygun.com için 4 ana test case geliştirilmiştir. Tüm test case'leri başarıyla tamamlanmış ve production-ready durumda.

### Case 1: Basic Flight Search and Time Filter
**Test Sınıfı:** `FlightSearchTest.java`  
**Durum:** Tamamlandı  
**Öncelik:** BLOCKER  
**Açıklama:** İstanbul-Ankara rotası için temel uçuş arama ve zaman filtresi testi

**Test Adımları:**
1. Enuygun.com ana sayfasına git ve çerezleri kabul et
2. Gidiş-dönüş (round trip) seçeneğini seç
3. Kalkış şehri: İstanbul, Varış şehri: Ankara seç
4. Gidiş ve dönüş tarihlerini dinamik takvimden seç
5. Otel checkbox'ının işaretli olmadığını doğrula
6. "Ucuz Bilet Bul" butonuna tıkla ve arama yap
7. Arama sonuçları sayfasının yüklendiğini doğrula
8. Gidiş kalkış saati filtresini aç
9. Zaman filtresini 10:00 - 18:00 aralığına ayarla (slider ile)
10. Filtrelerin uygulanmasını bekle ve sayfanın güncellenmesini kontrol et
11. **Tüm listelenen uçuşların** kalkış saatlerinin 10:00-18:00 arasında olduğunu doğrula
12. **Tüm listelenen uçuşların** kalkış/varış şehirlerinin config ile eşleştiğini doğrula
13. Uçuş listesinin düzgün görüntülendiğini doğrula

**Özel Özellikler:**
- Dinamik takvim navigasyonu (ay ileri/geri)
- Aktarmalı uçuşlar için sadece ilk segment'in kalkış saati kontrol edilir
- UTF-8 encoding ile Türkçe karakter desteği (İstanbul, Ankara)
- Detay butonlarına tıklanarak uçuş bilgileri görüntülenir

---

### Case 2: Price Sorting for Turkish Airlines
**Test Sınıfı:** `PriceSortingTest.java`  
**Durum:** Tamamlandı  
**Öncelik:** CRITICAL  
**Açıklama:** Türk Hava Yolları için fiyat sıralama ve filtreleme testi

**Test Adımları:**
1. Case 1'in 1-7 adımlarını tekrarla (ayrı config parametreleri ile)
2. Zaman filtresini 10:00 - 18:00 aralığına ayarla
3. Filtrelerin uygulanmasını bekle ve doğrula
4. Havayolları filtre kartını aç
5. "Türk Hava Yolları" (TK) checkbox'ını seç
6. Havayolu filtresinin uygulanmasını bekle
7. **Tüm listelenen uçuşların** Türk Hava Yolları olduğunu doğrula
8. "En ucuz" (cheapest) sıralama butonuna tıkla
9. Sıralamanın uygulanmasını bekle
10. **Tüm uçuş fiyatlarının** artan sırada (ucuzdan pahalıya) olduğunu doğrula

**Özel Özellikler:**
- Ayrı config parametreleri: `price.sorting.origin/destination/dates`
- Havayolu filtre kartı expand/collapse kontrolleri
- Checkbox seçim state yönetimi
- data-price attribute'tan fiyat çekme
- Fiyat sıralaması validation (ascending order)

---

### Case 3: Critical Path Testing
**Test Sınıfı:** `CriticalPathTest.java`  
**Durum:** Tamamlandı  
**Öncelik:** BLOCKER  
**Açıklama:** Gidiş–dönüş kritik kullanıcı yolunu, paket seçimleri ve rezervasyon sayfası doğrulamaları ile birlikte end-to-end test eder.

**Test Adımları:**
1. Enuygun.com ana sayfasına git ve çerezleri kabul et
2. Gidiş-dönüş (round trip) seçeneğini seç
3. Config'ten okunan `critical.path.origin/destination` ve tarih bilgileri ile arama yap
4. Arama sonuçları sayfasının yüklendiğini doğrula
5. İlk gidiş uçuşu için detayları aç ve uçuş bilgisini oku (havayolu, uçuş no, kalkış/varış saati, sınıf, süre, fiyat)
6. Gidiş uçuşu için \"Seç\" butonuna tıkla ve BASIC paketini seç, onayla
7. Dönüş uçuşu listesinden ilk dönüş uçuşunun detaylarını aç ve uçuş bilgisini oku
8. Dönüş uçuşu için \"Seç\" butonuna tıkla ve BASIC paketini seç (otomatik olarak rezervasyon sayfasına yönlenir)
9. Rezervasyon sayfasının yüklendiğini doğrula
10. Rezervasyon sayfasındaki gidiş ve dönüş uçuşu kartlarının, seçilen uçuşların kalkış/varış saatleri ile eşleştiğini doğrula
11. Rezervasyon sayfasında BASIC/BASIC paket kombinasyonunun doğru görüntülendiğini kontrol et

---

### Case 4: Data Extraction and Analysis
**Test Sınıfı:** `DataExtractionTest.java`  
**Durum:** Tamamlandı  
**Öncelik:** NORMAL  
**Açıklama:** Uçuş verilerini çıkarma, CSV'ye kaydetme ve veri analizi

**Test Adımları:**
1. Case 1'in 1-7 adımlarını tekrarla (ayrı config parametreleri ile)
2. Tüm action-detail-btn butonlarını bul (her biri bir uçuş kartı)
3. Her uçuş kartından aşağıdaki verileri çıkar:
   - Kalkış saati (`departureTime`)
   - Varış saati (`arrivalTime`)
   - Havayolu adı (`airlineName`)
   - Fiyat (`price` - data-price attribute)
   - Bağlantı bilgisi (`connectionInfo` - Direkt/Aktarma)
   - Uçuş süresi (`flightDuration`)
4. Çıkarılan verileri CSV dosyasına kaydet (UTF-8 BOM ile)
5. CSV dosyasının oluşturulduğunu doğrula
6. **Veri Analizi Yap:**
   - Havayolu bazında fiyat istatistikleri (min, max, avg)
   - Zaman dilimi bazında fiyat dağılımı (sabah, öğle, akşam, gece)
   - En uygun maliyetli uçuşları belirle (cost-effectiveness algoritması)
7. **Profesyonel HTML Rapor Oluştur:**
   - Executive Summary
   - KPI kartları (toplam uçuş, ortalama fiyat, min/max)
   - Havayolu fiyat karşılaştırma tablosu
   - Bar chart görselleştirme (ortalama fiyatlar)
   - Heatmap (zaman dilimi analizi)
   - Top 5 en uygun maliyetli uçuş listesi
8. Raporu konsola ve HTML dosyasına kaydet

**Özel Özellikler:**
- **CSV Export:** UTF-8 BOM encoding ile Excel uyumlu
- **Veri Analizi:**
  - Min/Max/Avg price by airline
  - Price distribution by time slot
  - Direct vs Transfer flight counts
- **Cost-Effectiveness Algorithm:**
  ```
  Score = (10000 / Price) + DirectBonus(50) + (500 / Duration)
  ```
  - Düşük fiyat = Yüksek skor
  - Direkt uçuş = +50 bonus puan
  - Kısa süre = Yüksek skor
- **HTML Rapor:** C-level/Executive sunuma hazır, modern, profesyonel tasarım
- **Çıktılar:**
  - `target/test-data/flight_data_TIMESTAMP.csv`
  - `target/test-reports/flight_analysis_report_TIMESTAMP.html`

---

## Özellikler

### Core Framework Features
- **Page Object Model (POM)** - Sürdürülebilir ve okunabilir test yapısı
- **Selenium 4.25.0** - En güncel Selenium WebDriver
- **WebDriverManager** - Otomatik driver yönetimi
- **TestNG** - Güçlü test yönetimi ve execution
- **Allure Reports** - Profesyonel test raporları
- **Log4j2** - Detaylı loglama sistemi
- **JavaFaker** - Dinamik test verisi üretimi
- **Multi-browser Support** - Chrome, Firefox, Edge desteği
- **Headless Mode** - CI/CD için headless çalıştırma
- **Screenshot on Failure** - Hata durumunda otomatik ekran görüntüsü
- **Thread-safe Driver** - Parallel test execution desteği
- **Config Management** - Merkezi konfigürasyon yönetimi
- **Comprehensive Utilities** - Hazır yardımcı metodlar
- **Retry Mechanism** - Başarısız testleri yeniden çalıştırma

### Advanced Test Features
- **Dynamic Calendar Navigation** - Ay/yıl bazında takvim navigasyonu
- **Slider Manipulation** - Piksel hesaplamalı hassas slider kontrolü
- **UTF-8 Support** - Türkçe karakter desteği (config.properties için)
- **Multi-Segment Flight Handling** - Aktarmalı uçuşlar için özel logic
- **Dynamic Element Verification** - Visibility ve state kontrolü
- **Bulk Data Validation** - Liste halindeki tüm elementleri doğrulama
- **Time Range Filtering** - Saat aralığı filtreleme ve validation
- **Price Sorting Validation** - Ascending/descending sıralama kontrolü
- **Airline Filtering** - Havayolu checkbox seçimi ve doğrulama

### Data Analysis & Reporting Features
- **CSV Data Export** - UTF-8 BOM encoding ile Excel uyumlu
- **Statistical Analysis** - Min/Max/Avg hesaplamaları
- **Time Slot Analysis** - Zaman dilimi bazında fiyat dağılımı
- **Cost-Effectiveness Algorithm** - Optimal uçuş önerisi algoritması
- **Professional HTML Reports** - Executive/C-level sunuma hazır raporlar
- **Data Visualization** - Bar chart ve heatmap görselleştirme
- **KPI Dashboard** - Anahtar metrik göstergeleri
- **Multi-Format Output** - Console logs + CSV + HTML rapor

---

## Teknolojiler

| Teknoloji | Versiyon | Açıklama |
|-----------|----------|----------|
| Java | 17 | Programlama dili |
| Selenium | 4.25.0 | Web otomasyon |
| TestNG | 7.10.2 | Test framework |
| Maven | 3.x | Build tool |
| Allure | 2.21.0 | Raporlama |
| Log4j2 | 2.22.0 | Loglama |
| WebDriverManager | 5.8.0 | Driver yönetimi |
| JavaFaker | 1.0.2 | Test data |
| Jackson | 2.17.1 | JSON işlemleri |
| Commons IO | 2.15.1 | Dosya işlemleri |

---

## Proje Yapısı

```
qa-selenium-automation-framework/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mustafa/
│   │   │       ├── base/              # Base sınıflar
│   │   │       │   └── BasePage.java
│   │   │       ├── config/            # Konfigürasyon
│   │   │       │   └── ConfigReader.java
│   │   │       ├── constants/         # Sabitler
│   │   │       │   └── FrameworkConstants.java
│   │   │       ├── driver/            # Driver yönetimi
│   │   │       │   └── DriverManager.java
│   │   │       ├── listeners/         # TestNG listeners
│   │   │       │   ├── TestListener.java
│   │   │       │   └── RetryListener.java
│   │   │       ├── pages/             # Page Objects
│   │   │       │   ├── HomePage.java            # Enuygun.com ana sayfa
│   │   │       │   └── SearchResultsPage.java   # Arama sonuçları sayfası
│   │   │       ├── exceptions/        # Custom exceptions
│   │   │       │   ├── FrameworkException.java
│   │   │       │   ├── ConfigurationException.java
│   │   │       │   └── ElementNotFoundException.java
│   │   │       └── utils/             # Yardımcı sınıflar
│   │   │           ├── WaitHelper.java
│   │   │           ├── ElementHelper.java
│   │   │           ├── ScreenshotUtils.java
│   │   │           ├── DateUtils.java
│   │   │           └── TestDataGenerator.java
│   │   │
│   │   └── resources/
│   │       ├── config.properties      # Ana konfigürasyon
│   │       ├── log4j2.xml            # Log konfigürasyonu
│   │       └── allure.properties     # Allure konfigürasyonu
│   │
│   └── test/
│       ├── java/
│       │   └── com/mustafa/
│       │       ├── base/
│       │       │   └── BaseTest.java # Base test sınıfı
│       │       └── tests/            # Test sınıfları
│       │           ├── FlightSearchTest.java      # Case 1
│       │           ├── PriceSortingTest.java      # Case 2
│       │           ├── CriticalPathTest.java      # Case 3
│       │           └── DataExtractionTest.java    # Case 4
│       │
│       └── resources/
│           ├── testng.xml            # TestNG suite
│           └── testdata/             # Test verileri
│
├── target/                           # Build çıktıları
│   ├── allure-results/              # Allure sonuçları
│   ├── screenshots/                 # Ekran görüntüleri
│   ├── test-data/                   # CSV veri export'ları
│   ├── test-reports/                # HTML analiz raporları
│   └── logs/                        # Log dosyaları
│
├── .gitignore                       # Git ignore
├── pom.xml                          # Maven dependencies
├── README.md                        # Bu dosya
```

---

## Kurulum

### Ön Gereksinimler

- **Java 17** veya üzeri
- **Maven 3.6+**
- **IDE** (Eclipse, IntelliJ IDEA, VS Code)
- **Git**

### Kurulum Adımları

1. **Projeyi klonlayın:**
```bash
git clone <repository-url>
cd qa-selenium-automation-framework
```

2. **Maven bağımlılıklarını indirin:**
```bash
mvn clean install
```

3. **IDE'ye import edin:**
   - Eclipse: File > Import > Maven > Existing Maven Projects
   - IntelliJ: File > Open > Proje klasörünü seçin

---

## Konfigürasyon

### config.properties

Ana konfigürasyon dosyası: `src/main/resources/config.properties`

**Test case'leri için ayrı parametreler:**

```properties
# Base URL
base.url=https://www.enuygun.com

# Browser Configuration
browser=chrome          # chrome, firefox, edge
headless=false          # true: headless mode

# Timeouts (seconds)
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# Screenshots
screenshot.on.failure=true
screenshot.path=target/screenshots

# ==========================================
# FLIGHT SEARCH TEST DATA (Case 1)
# ==========================================
flight.origin=İstanbul
flight.destination=Ankara
flight.departure.date=28.05.2026
flight.return.date=23.05.2026

# ==========================================
# PRICE SORTING TEST DATA (Case 2)
# ==========================================
price.sorting.origin=İstanbul
price.sorting.destination=Ankara
price.sorting.departure.date=13.03.2026
price.sorting.return.date=15.03.2026

# ==========================================
# DATA EXTRACTION TEST DATA (Case 4)
# ==========================================
data.extraction.origin=İstanbul
data.extraction.destination=Ankara
data.extraction.departure.date=20.06.2026
data.extraction.return.date=27.06.2026
```

**Not:** Her test case'in kendi config parametreleri vardır. Bu sayede testler bağımsız çalışır ve farklı tarih/rota kombinasyonları test edilebilir.

### testng.xml

Test suite konfigürasyonu: `src/test/resources/testng.xml`

**Tüm testleri çalıştır (varsayılan):**
```xml
<test name="Enuygun All Tests" enabled="true">
    <classes>
        <class name="com.mustafa.tests.FlightSearchTest"/>
        <class name="com.mustafa.tests.PriceSortingTest"/>
        <class name="com.mustafa.tests.CriticalPathTest"/>
        <class name="com.mustafa.tests.DataExtractionTest"/>
    </classes>
</test>
```

**Tek bir test çalıştır:**
Her test case'in `enabled="false"` olan kendi suite'i vardır. İstediğiniz test'i çalıştırmak için ilgili suite'i `enabled="true"` yapın.

---

## Test Çalıştırma

### Tüm Testleri Çalıştır

**Maven ile (4 test case birlikte):**
```bash
mvn clean test
```

**TestNG XML ile:**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Tek Bir Test Case Çalıştır

**Case 1 - Flight Search:**
```bash
mvn test -Dtest=FlightSearchTest
```

**Case 2 - Price Sorting:**
```bash
mvn test -Dtest=PriceSortingTest
```

**Case 3 - Critical Path:**
```bash
mvn test -Dtest=CriticalPathTest
```

**Case 4 - Data Extraction:**
```bash
mvn test -Dtest=DataExtractionTest
```

### Farklı Browser ile Çalıştır

```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Headless Mode

```bash
mvn test -Dheadless=true
```

### IDE'den Çalıştırma

1. **Tüm testler:** `testng.xml` > Sağ tık > Run As > TestNG Suite
2. **Tek test sınıfı:** Test class > Sağ tık > Run As > TestNG Test
3. **Tek test metodu:** Test metodu > Sağ tık > Run As > TestNG Test

### Parallel Execution (Hızlı Çalıştırma)

`testng.xml` içinde "Parallel Execution" suite'ini `enabled="true"` yapın:
```xml
<test name="Parallel Execution" enabled="true" parallel="classes" thread-count="4">
```

Sonra çalıştırın:
```bash
mvn clean test
```

---

## Raporlama

### Allure Reports

Framework'ün ana raporlama sistemi. Test execution detayları, step-by-step loglar, screenshot'lar içerir.

**Rapor oluştur ve aç:**
```bash
mvn clean test
mvn allure:serve
```

**Rapor oluştur (statik):**
```bash
mvn allure:report
```

Rapor lokasyonu: `target/allure-report/index.html`

**Allure Özellikleri:**
- Test execution timeline
- Step-by-step test adımları (@Step annotations)
- Screenshot'lar (failure durumunda)
- Test kategorileri (@Epic, @Feature, @Story)
- Severity levels (@Severity)
- Detaylı log çıktıları

---

### Data Analysis HTML Reports (Case 4)

DataExtractionTest çalıştığında otomatik olarak profesyonel HTML rapor oluşturulur.

**Rapor Lokasyonu:** `target/test-reports/flight_analysis_report_TIMESTAMP.html`

**Rapor İçeriği:**
- **Executive Summary** - Genel özet ve bulgular
- **KPI Dashboard** - Anahtar metrikler (toplam uçuş, ortalama fiyat, min/max)
- **Airline Price Statistics** - Havayolu bazında fiyat analizi (min, max, avg, variance)
- **Price Comparison Bar Chart** - Görsel karşılaştırma
- **Time Slot Heatmap** - Zaman dilimi bazında fiyat dağılımı (sabah, öğle, akşam, gece)
- **Top 5 Cost-Effective Flights** - En uygun maliyetli uçuşlar listesi
- **Methodology & Disclaimer** - Algoritma açıklaması

**Tasarım Özellikleri:**
- C-level/Executive sunuma hazır
- Modern, profesyonel, temiz tasarım
- Responsive (mobil uyumlu)
- UTF-8 encoding (Türkçe karakter desteği)
- Print-friendly

---

### CSV Data Export (Case 4)

Tüm uçuş verileri CSV formatında export edilir.

**CSV Lokasyonu:** `target/test-data/flight_data_TIMESTAMP.csv`

**CSV Kolonları:**
- Departure Time
- Arrival Time
- Airline Name
- Price
- Connection Info (Direkt/Aktarma)
- Flight Duration

**Özellikler:**
- UTF-8 BOM encoding (Excel uyumlu)
- Türkçe karakter desteği
- Doğrudan Excel'de açılabilir
- Virgülle ayrılmış (comma-separated)

---

### TestNG Reports

Test sonrası otomatik oluşur:
- `target/surefire-reports/index.html`
- `test-output/index.html`

TestNG native HTML raporları. Temel test pass/fail durumları ve execution süreleri.

---

### Loglar

Log dosyaları: `target/logs/`
- `automation.log` - Tüm loglar
- `error.log` - Sadece hatalar
- `test-execution.log` - Test execution logları

**Log Levels:**
- INFO: Genel bilgi mesajları
- WARN: Uyarılar (önemli değil ama dikkat edilmeli)
- ERROR: Hatalar (test başarısızlıkları)
- DEBUG: Detaylı debug bilgileri (ihtiyaç halinde)

---

## Test Yazma Rehberi

### Mevcut Test Case'leri Geliştirme

4 test case'in boş iskeletleri hazır durumda. Her test case için adımları eklemek için:

1. **Page Object'leri genişletin** (`src/main/java/com/mustafa/pages/`)
   - `HomePage.java` - Ana sayfa elemanları
   - Yeni page'ler ekleyin (SearchResultsPage, FlightDetailsPage, vb.)

2. **Test adımlarını ekleyin** (`src/test/java/com/mustafa/tests/`)
   - `FlightSearchTest.java` - Uçuş arama ve filtreleme
   - `PriceSortingTest.java` - Fiyat sıralama
   - `CriticalPathTest.java` - E2E akış
   - `DataExtractionTest.java` - Veri çıkarma

### Yeni Page Object Oluşturma

```java
package com.mustafa.pages;

import com.mustafa.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchResultsPage extends BasePage {
    
    // Locators
    private final By flightList = By.cssSelector(".flight-list");
    private final By priceFilter = By.id("price-filter");
    
    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }
    
    @Step("Apply price filter")
    public SearchResultsPage applyPriceFilter() {
        click(priceFilter);
        logger.info("Price filter applied");
        return this;
    }
}
```

### Test Case Geliştirme Örneği

```java
@Test
public void testFlightSearch() {
    logger.info("TEST: Flight Search Started");
    
    // Arrange
    HomePage homePage = new HomePage(driver);
    homePage.acceptCookies();
    
    // Act
    SearchResultsPage resultsPage = homePage
        .selectDeparture("Istanbul")
        .selectArrival("Ankara")
        .selectDate("2026-04-01")
        .clickSearch();
    
    // Assert
    Assert.assertTrue(resultsPage.isResultsDisplayed(), 
        "Search results should be displayed");
    
    logger.info("TEST: Flight Search Completed");
}
```

---

## Best Practices

### 1. Locator Stratejisi
- **ID** kullan (mümkünse)
- **CSS Selector** tercih et
- **XPath** son çare olsun
- Dinamik XPath'lerden kaçın

### 2. Wait Stratejisi
- **Explicit Wait** kullan
- **Thread.sleep()** kullanma
- Element görünür/tıklanabilir olana kadar bekle

### 3. Test Bağımsızlığı
- Her test bağımsız çalışmalı
- Test execution sırası önemli olmamalı
- Test datası paylaşımından kaçın

### 4. Assertion Mesajları
- Her assertion'a açıklayıcı mesaj ekle
```java
Assert.assertTrue(condition, "Login should be successful with valid credentials");
```

### 5. Logging
- Önemli adımlarda log at
- Test başlangıç/bitiş logla
- Hata durumlarını detaylı logla

### 6. Screenshot
- Hata durumunda otomatik alınır
- Manuel almak için: `takeScreenshot("test-name")`

### 7. Code Organization
- Page Object per page
- Bir test sınıfı bir feature'ı test etmeli
- Utility metodları tekrar kullanılabilir olmalı

---

## Troubleshooting

### Driver Bulunamıyor
```bash
# WebDriverManager otomatik indirir, manuel gerek yok
# Eğer sorun varsa cache'i temizle:
rm -rf ~/.m2/repository/webdriver
```

### Test Başarısız Oluyor
1. Log dosyalarını kontrol et: `target/logs/`
2. Screenshot'ları incele: `target/screenshots/`
3. Allure raporunu oluştur: `mvn allure:serve`

### Browser Açılmıyor
- Browser yüklü mü kontrol et
- Headless mode'u kapat: `headless=false`
- Driver versiyonu browser versiyonu ile uyumlu mu

---

## İletişim

**Proje:** Enuygun.com Test Automation Framework  
**Geliştirici:** Mustafa Erdoğan
**GitHub:** [mustaffaerdogan](https://github.com/mustaffaerdogan)

---

## Proje Durumu

### Tamamlananlar

**Framework Altyapısı:**
- Page Object Model (POM) mimarisi
- Base page ve test sınıfları
- Driver manager ve WebDriver setup
- TestNG konfigürasyonu ve suite'ler
- Allure reporting entegrasyonu
- Log4j2 logging sistemi
- Config management (properties)
- Screenshot utilities
- Wait helpers ve element utilities
- Exception handling

**Page Objects:**
- **HomePage** - Ana sayfa interactions
  - Cookie kabul
  - Round trip/One way seçimi
  - Hotel checkbox yönetimi
  - Origin/destination autocomplete
  - Dinamik takvim navigasyonu (ay/yıl bazında)
  - Tarih seçimi (departure/return)
  - Search button
  
- **SearchResultsPage** - Arama sonuçları interactions
  - Sayfa yükleme doğrulama
  - Departure time filter (slider manipulation)
  - Airline filter (checkbox seçimi)
  - Price sorting (cheapest first)
  - Flight data extraction
  - Bulk verification (time range, destinations, prices)

**Test Cases:**
- **Case 1: FlightSearchTest** - TAMAMLANDI
  - Uçuş arama end-to-end
  - Zaman filtresi (10:00-18:00)
  - Kalkış saati validation (tüm uçuşlar)
  - Destinasyon validation (multi-segment flights)
  
- **Case 2: PriceSortingTest** - TAMAMLANDI
  - Turkish Airlines filtreleme
  - En ucuz sıralama
  - Fiyat sıralaması validation
  - Havayolu doğrulama (tüm uçuşlar TK)
  
- **Case 3: CriticalPathTest** - TAMAMLANDI
  - Gidiş–dönüş kritik kullanıcı yolu (search → select → reservation)
  - Gidiş ve dönüş için uçuş/paket seçimi (BASIC/BASIC)
  - Rezervasyon sayfasında gidiş/dönüş saatlerinin doğrulanması
  - Paket bilgilerinin (BASIC/BASIC) kontrolü
  
- **Case 4: DataExtractionTest** - TAMAMLANDI
  - CSV data export (UTF-8 BOM)
  - Statistical analysis (min/max/avg by airline)
  - Time slot analysis (morning/afternoon/evening/night)
  - Cost-effectiveness algorithm
  - Professional HTML report generation
  - Bar chart ve heatmap visualizations

**Özel Özellikler:**
- UTF-8 encoding support (Türkçe karakterler)
- Dynamic calendar navigation (optimize edilmiş)
- Slider pixel calculation ve manipulation
- Multi-segment flight handling
- Data analysis ve visualization
- Executive-level reporting

---

### Test Coverage

| Test Case | Durum | Öncelik | Test Adımları | Assertions |
|-----------|-------|---------|---------------|------------|
| Case 1 | Tamamlandı | BLOCKER | 13 adım | 6+ assertion |
| Case 2 | Tamamlandı | CRITICAL | 10 adım | 4+ assertion |
| Case 3 | Tamamlandı | BLOCKER | 11 adım | 4+ assertion |
| Case 4 | Tamamlandı | NORMAL | 8 adım | 3+ assertion |

**Toplam:**
- Test Case'leri: 4 (4 aktif, 0 beklemede)
- Test Metodları: 4
- Page Objects: 3
- Toplam Kod Satırı: ~2000+
- Test Coverage: %100'e yakın (tüm ana akışlar kapsanıyor)

---

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

---


