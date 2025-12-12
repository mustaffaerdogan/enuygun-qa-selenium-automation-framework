package com.mustafa.pages;

import com.mustafa.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage - SauceDemo login sayfası için Page Object
 */
public class LoginPage extends BasePage {
    
    // Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By errorButton = By.cssSelector(".error-button");
    
    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Username gir
     */
    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        logger.info("Username entered: " + username);
        return this;
    }
    
    /**
     * Password gir
     */
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        logger.info("Password entered");
        return this;
    }
    
    /**
     * Login butonuna tıkla
     */
    @Step("Click login button")
    public void clickLoginButton() {
        click(loginButton);
        logger.info("Login button clicked");
    }
    
    /**
     * Tam login işlemi - username, password gir ve login'e tıkla
     */
    @Step("Login with username: {username}")
    public ProductsPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("Login completed for user: " + username);
        return new ProductsPage(driver);
    }
    
    /**
     * Hata mesajı görünür mü kontrol et
     */
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }
    
    /**
     * Hata mesajını al
     */
    @Step("Get error message")
    public String getErrorMessage() {
        String error = getText(errorMessage);
        logger.info("Error message: " + error);
        return error;
    }
    
    /**
     * Hata mesajını kapat
     */
    public LoginPage closeErrorMessage() {
        if (isElementVisible(errorButton)) {
            click(errorButton);
            logger.info("Error message closed");
        }
        return this;
    }
    
    /**
     * Login sayfasında olduğunu doğrula
     */
    public boolean isLoginPageDisplayed() {
        return isElementVisible(usernameInput) && 
               isElementVisible(passwordInput) && 
               isElementVisible(loginButton);
    }
    
    /**
     * Login formunu temizle
     */
    public LoginPage clearLoginForm() {
        if (isElementPresent(usernameInput)) {
            driver.findElement(usernameInput).clear();
        }
        if (isElementPresent(passwordInput)) {
            driver.findElement(passwordInput).clear();
        }
        logger.info("Login form cleared");
        return this;
    }
}

