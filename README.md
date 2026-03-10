# 🚀 Enuygun.com Test Automation Framework

Enuygun.com için geliştirilmiş Selenium WebDriver tabanlı test otomasyon framework'ü. Page Object Model (POM) design pattern kullanılarak geliştirilmiştir.

---

## 📋 İçindekiler

- [Test Case'leri](#-test-caseleri)
- [Özellikler](#-özellikler)
- [Teknolojiler](#-teknolojiler)
- [Proje Yapısı](#-proje-yapısı)
- [Kurulum](#-kurulum)
- [Test Çalıştırma](#-test-çalıştırma)
- [Raporlama](#-raporlama)
- [Best Practices](#-best-practices)

---

## 🎯 Test Case'leri

Bu projede Enuygun.com için 4 ana test case bulunmaktadır:

### Case 1: Basic Flight Search and Time Filter 🔍
**Test Sınıfı:** `FlightSearchTest.java`  
**Açıklama:** Temel uçuş arama ve zaman filtresi testi

**Test Adımları:**
- Enuygun.com ana sayfasına git
- Uçuş arama formunu doldur (kalkış-varış, tarih)
- Uçuş ara
- Zaman filtrelerini uygula
- Sonuçları doğrula

---

### Case 2: Price Sorting for Turkish Airlines 💰
**Test Sınıfı:** `PriceSortingTest.java`  
**Açıklama:** Turkish Airlines için fiyat sıralama testi

**Test Adımları:**
- Enuygun.com ana sayfasına git
- Uçuş ara
- Turkish Airlines filtresi uygula
- Fiyata göre sırala (artan/azalan)
- Sıralamanın doğru çalıştığını doğrula

---

### Case 3: Critical Path Testing 🛤️
**Test Sınıfı:** `CriticalPathTest.java`  
**Açıklama:** Kritik kullanıcı yolu end-to-end testi

**Test Adımları:**
- Enuygun.com ana sayfasına git
- Uçuş ara
- Uçuş seç
- Yolcu bilgilerini doldur
- Ödeme adımına kadar ilerle
- Kritik yolun tamamını doğrula

---

### Case 4: Data Extraction and Analysis 📊
**Test Sınıfı:** `DataExtractionTest.java`  
**Açıklama:** Uçuş verilerini çıkarma ve analiz testi

**Test Adımları:**
- Enuygun.com ana sayfasına git
- Uçuş ara
- Sonuç sayfasından verileri çıkar (fiyat, zaman, havayolu)
- Verileri analiz et ve raporla
- Log ve screenshot ile kaydet

---

## ✨ Özellikler

- ✅ **Page Object Model (POM)** - Sürdürülebilir ve okunabilir test yapısı
- ✅ **Selenium 4.25.0** - En güncel Selenium WebDriver
- ✅ **WebDriverManager** - Otomatik driver yönetimi
- ✅ **TestNG** - Güçlü test yönetimi ve execution
- ✅ **Allure Reports** - Profesyonel test raporları
- ✅ **Log4j2** - Detaylı loglama sistemi
- ✅ **JavaFaker** - Dinamik test verisi üretimi
- ✅ **Multi-browser Support** - Chrome, Firefox, Edge desteği
- ✅ **Headless Mode** - CI/CD için headless çalıştırma
- ✅ **Screenshot on Failure** - Hata durumunda otomatik ekran görüntüsü
- ✅ **Thread-safe Driver** - Parallel test execution desteği
- ✅ **Config Management** - Merkezi konfigürasyon yönetimi
- ✅ **Comprehensive Utilities** - Hazır yardımcı metodlar
- ✅ **Retry Mechanism** - Başarısız testleri yeniden çalıştırma

---

## 🛠 Teknolojiler

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

## 📁 Proje Yapısı

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
│   │   │       │   └── HomePage.java  # Enuygun.com ana sayfa
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
│   └── logs/                        # Log dosyaları
│
├── .gitignore                       # Git ignore
├── pom.xml                          # Maven dependencies
├── README.md                        # Bu dosya
```

---

## 🔧 Kurulum

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

## ⚙️ Konfigürasyon

### config.properties

Ana konfigürasyon dosyası: `src/main/resources/config.properties`

```properties
# Test URL
base.url=https://www.enuygun.com

# Browser
browser=chrome          # chrome, firefox, edge
headless=false          # true: headless mode

# Timeouts (seconds)
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# Screenshots
screenshot.on.failure=true
screenshot.path=target/screenshots
```

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

## 🎯 Test Çalıştırma

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

## 📊 Raporlama

### Allure Reports

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

### TestNG Reports

Test sonrası otomatik oluşur:
- `target/surefire-reports/index.html`
- `test-output/index.html`

### Loglar

Log dosyaları: `target/logs/`
- `automation.log` - Tüm loglar
- `error.log` - Sadece hatalar
- `test-execution.log` - Test execution logları

---

## 📝 Test Yazma Rehberi

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

## 🎨 Best Practices

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

## 🐛 Troubleshooting

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

## 📞 İletişim

**Proje:** Enuygun.com Test Automation Framework  
**Geliştirici:** Mustafa Erdoğan
**GitHub:** [mustaffaerdogan](https://github.com/mustaffaerdogan)

---

## 📈 Proje Durumu

### ✅ Tamamlananlar
- Framework altyapısı hazır
- 4 test case iskeleti oluşturuldu
- Base page ve test sınıfları hazır
- TestNG konfigürasyonu tamamlandı
- Reporting sistemi aktif (Allure)

### 🚧 Devam Eden
- Case 1: Basic Flight Search and Time Filter (TODO)
- Case 2: Price Sorting for Turkish Airlines (TODO)
- Case 3: Critical Path Testing (TODO)
- Case 4: Data Extraction and Analysis (TODO)

### 📝 Sonraki Adımlar
1. HomePage locator'larını tamamla
2. SearchResultsPage oluştur
3. Her test case için adımları ekle
4. Test verilerini hazırla
5. Testleri çalıştır ve doğrula

---

## 📜 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

---


