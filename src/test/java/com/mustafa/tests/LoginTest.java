package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.pages.LoginPage;
import com.mustafa.pages.ProductsPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest - Login fonksiyonalitesi için test senaryoları
 */
@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {
    
    @Test(priority = 1, description = "Verify login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Login with valid username and password, verify successful login")
    @Story("As a user, I should be able to login with valid credentials")
    public void testValidLogin() {
        // Test başlığı
        logger.info("TEST: Valid Login Test Started");
        
        // Arrange - Test verilerini hazırla
        String username = "standard_user";
        String password = "secret_sauce";
        
        // Act - Login işlemini gerçekleştir
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.login(username, password);
        
        // Assert - Sonuçları doğrula
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), 
            "Products page should be displayed after successful login");
        
        String pageTitle = productsPage.getPageTitle();
        Assert.assertEquals(pageTitle, "Products", 
            "Page title should be 'Products'");
        
        logger.info("TEST: Valid Login Test Completed Successfully");
    }
    
    @Test(priority = 2, description = "Verify login fails with invalid username")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description: Login with invalid username, verify error message is displayed")
    @Story("As a user, I should see error message when login with invalid username")
    public void testInvalidUsername() {
        logger.info("TEST: Invalid Username Test Started");
        
        // Arrange
        String invalidUsername = "invalid_user";
        String password = "secret_sauce";
        
        // Act
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(invalidUsername)
                 .enterPassword(password)
                 .clickLoginButton();
        
        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
            "Error message should be displayed for invalid username");
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Username and password do not match"), 
            "Error message should indicate username/password mismatch");
        
        logger.info("TEST: Invalid Username Test Completed Successfully");
    }
    
    @Test(priority = 3, description = "Verify login fails with invalid password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description: Login with valid username but invalid password")
    @Story("As a user, I should see error message when login with invalid password")
    public void testInvalidPassword() {
        logger.info("TEST: Invalid Password Test Started");
        
        // Arrange
        String username = "standard_user";
        String invalidPassword = "wrong_password";
        
        // Act
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(username)
                 .enterPassword(invalidPassword)
                 .clickLoginButton();
        
        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
            "Error message should be displayed for invalid password");
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Username and password do not match"), 
            "Error message should indicate username/password mismatch");
        
        logger.info("TEST: Invalid Password Test Completed Successfully");
    }
    
    @Test(priority = 4, description = "Verify login fails with empty credentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Click login button without entering credentials")
    @Story("As a user, I should see error message when login with empty fields")
    public void testEmptyCredentials() {
        logger.info("TEST: Empty Credentials Test Started");
        
        // Act
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButton();
        
        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
            "Error message should be displayed for empty credentials");
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Username is required"), 
            "Error message should indicate username is required");
        
        logger.info("TEST: Empty Credentials Test Completed Successfully");
    }
    
    @Test(priority = 5, description = "Verify login with locked out user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Login with a locked out user account")
    @Story("As a system, I should prevent locked out users from logging in")
    public void testLockedOutUser() {
        logger.info("TEST: Locked Out User Test Started");
        
        // Arrange
        String lockedUsername = "locked_out_user";
        String password = "secret_sauce";
        
        // Act
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(lockedUsername, password);
        
        // Assert
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
            "Error message should be displayed for locked out user");
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Sorry, this user has been locked out"), 
            "Error message should indicate user is locked out");
        
        logger.info("TEST: Locked Out User Test Completed Successfully");
    }
    
    @Test(priority = 6, description = "Verify login page elements are displayed")
    @Severity(SeverityLevel.MINOR)
    @Description("Test Description: Verify all login page elements are visible")
    @Story("As a user, I should see all necessary login page elements")
    public void testLoginPageElements() {
        logger.info("TEST: Login Page Elements Test Started");
        
        // Act
        LoginPage loginPage = new LoginPage(driver);
        
        // Assert
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
            "Login page elements should be displayed");
        
        Assert.assertEquals(driver.getTitle(), "Swag Labs", 
            "Page title should be 'Swag Labs'");
        
        logger.info("TEST: Login Page Elements Test Completed Successfully");
    }
}

