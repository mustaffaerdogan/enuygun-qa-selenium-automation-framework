# 🚀 QA Selenium Automation Framework

Modern ve profesyonel bir Selenium test otomasyon framework'ü. Page Object Model (POM) design pattern kullanılarak geliştirilmiştir.

---

## 📋 İçindekiler

- [Özellikler](#-özellikler)
- [Teknolojiler](#-teknolojiler)
- [Proje Yapısı](#-proje-yapısı)
- [Kurulum](#-kurulum)
- [Konfigürasyon](#-konfigürasyon)
- [Test Çalıştırma](#-test-çalıştırma)
- [Raporlama](#-raporlama)
- [Test Yazma Rehberi](#-test-yazma-rehberi)
- [Best Practices](#-best-practices)

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
│   │   │       │   ├── AllureListener.java
│   │   │       │   └── RetryListener.java
│   │   │       ├── pages/             # Page Objects
│   │   │       │   ├── LoginPage.java
│   │   │       │   └── ProductsPage.java
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
│       │           ├── LoginTest.java
│       │           └── ProductsTest.java
│       │
│       └── resources/
│           └── testng.xml            # TestNG suite
│
├── target/                           # Build çıktıları
│   ├── allure-results/              # Allure sonuçları
│   ├── screenshots/                 # Ekran görüntüleri
│   └── logs/                        # Log dosyaları
│
├── .gitignore                       # Git ignore
├── pom.xml                          # Maven dependencies
└── README.md                        # Bu dosya
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
base.url=https://www.saucedemo.com

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

```xml
<suite name="QA Selenium Automation Test Suite">
    <parameter name="browser" value="chrome"/>
    <test name="Smoke Tests">
        <classes>
            <class name="com.mustafa.tests.LoginTest"/>
        </classes>
    </test>
</suite>
```

---

## 🎯 Test Çalıştırma

### Maven ile

**Tüm testleri çalıştır:**
```bash
mvn clean test
```

**Belirli bir test sınıfı:**
```bash
mvn test -Dtest=LoginTest
```

**Belirli bir test metodu:**
```bash
mvn test -Dtest=LoginTest#testValidLogin
```

**TestNG XML ile:**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

**Farklı browser ile:**
```bash
mvn test -Dbrowser=firefox
```

**Headless mode:**
```bash
mvn test -Dheadless=true
```

### IDE'den

1. **Tek test çalıştırma:** Test metoduna sağ tıklayın > Run As > TestNG Test
2. **Test sınıfı:** Class'a sağ tıklayın > Run As > TestNG Test
3. **Test suite:** testng.xml'e sağ tıklayın > Run As > TestNG Suite

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

### 1. Page Object Oluşturma

```java
package com.mustafa.pages;

import com.mustafa.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MyPage extends BasePage {
    
    // Locators
    private final By usernameInput = By.id("username");
    private final By submitButton = By.id("submit");
    
    // Constructor
    public MyPage(WebDriver driver) {
        super(driver);
    }
    
    // Actions
    public MyPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }
    
    public void clickSubmit() {
        click(submitButton);
    }
}
```

### 2. Test Sınıfı Oluşturma

```java
package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.pages.MyPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MyTest extends BaseTest {
    
    @Test
    public void testExample() {
        MyPage page = new MyPage(driver);
        page.enterUsername("testuser")
            .clickSubmit();
        
        Assert.assertTrue(condition, "Message");
    }
}
```

### 3. Test Data Üretme

```java
import com.mustafa.utils.TestDataGenerator;

String email = TestDataGenerator.generateEmail();
String username = TestDataGenerator.generateUsername();
String password = TestDataGenerator.generatePassword();
int randomNumber = TestDataGenerator.generateRandomNumber(1, 100);
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

**Mustafa** - [GitHub Profile](https://github.com/mustaffaerdogan)

Project Link: [https://github.com/mustafa/qa-selenium-automation-framework](https://github.com/mustafa/qa-selenium-automation-framework)

---

## 📜 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

---


