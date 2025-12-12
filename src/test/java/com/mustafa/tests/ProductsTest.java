package com.mustafa.tests;

import com.mustafa.base.BaseTest;
import com.mustafa.pages.LoginPage;
import com.mustafa.pages.ProductsPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ProductsTest - Ürünler sayfası için test senaryoları
 */
@Epic("E-Commerce")
@Feature("Products")
public class ProductsTest extends BaseTest {
    
    private ProductsPage productsPage;
    
    /**
     * Her testten önce login ol
     */
    @BeforeMethod
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.login("standard_user", "secret_sauce");
        logger.info("User logged in successfully before test");
    }
    
    @Test(priority = 1, description = "Verify products page displays correctly")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test Description: Verify products page is displayed after login")
    @Story("As a user, I should see products page after successful login")
    public void testProductsPageDisplay() {
        logger.info("TEST: Products Page Display Test Started");
        
        // Assert
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), 
            "Products page should be displayed");
        
        Assert.assertEquals(productsPage.getPageTitle(), "Products", 
            "Page title should be 'Products'");
        
        logger.info("TEST: Products Page Display Test Completed Successfully");
    }
    
    @Test(priority = 2, description = "Verify product count is correct")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Verify the number of products displayed on the page")
    @Story("As a user, I should see all available products")
    public void testProductCount() {
        logger.info("TEST: Product Count Test Started");
        
        // Act
        int productCount = productsPage.getProductCount();
        
        // Assert
        Assert.assertTrue(productCount > 0, 
            "At least one product should be displayed");
        
        Assert.assertEquals(productCount, 6, 
            "Should display 6 products on the page");
        
        logger.info("TEST: Product Count Test Completed Successfully - Count: " + productCount);
    }
    
    @Test(priority = 3, description = "Verify adding product to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description: Add a product to cart and verify cart count increases")
    @Story("As a user, I should be able to add products to cart")
    public void testAddProductToCart() {
        logger.info("TEST: Add Product To Cart Test Started");
        
        // Act
        productsPage.addFirstProductToCart();
        
        // Assert
        int cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, 1, 
            "Cart should contain 1 item after adding a product");
        
        logger.info("TEST: Add Product To Cart Test Completed Successfully");
    }
    
    @Test(priority = 4, description = "Verify adding multiple products to cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description: Add multiple products and verify cart count")
    @Story("As a user, I should be able to add multiple products to cart")
    public void testAddMultipleProductsToCart() {
        logger.info("TEST: Add Multiple Products Test Started");
        
        // Act
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.addProductToCart("sauce-labs-bike-light");
        productsPage.addProductToCart("sauce-labs-bolt-t-shirt");
        
        // Assert
        int cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, 3, 
            "Cart should contain 3 items after adding three products");
        
        logger.info("TEST: Add Multiple Products Test Completed Successfully");
    }
    
    @Test(priority = 5, description = "Verify sort dropdown is displayed")
    @Severity(SeverityLevel.MINOR)
    @Description("Test Description: Verify sort dropdown exists on products page")
    @Story("As a user, I should be able to sort products")
    public void testSortDropdownDisplay() {
        logger.info("TEST: Sort Dropdown Display Test Started");
        
        // Assert
        Assert.assertTrue(productsPage.isSortDropdownDisplayed(), 
            "Sort dropdown should be displayed on products page");
        
        logger.info("TEST: Sort Dropdown Display Test Completed Successfully");
    }
    
    @Test(priority = 6, description = "Verify logout functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description: Logout from products page and verify redirect to login")
    @Story("As a user, I should be able to logout from the application")
    public void testLogout() {
        logger.info("TEST: Logout Test Started");
        
        // Act
        productsPage.logout();
        
        // Wait a bit for navigation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("Sleep interrupted", e);
            Thread.currentThread().interrupt();
        }
        
        // Assert - Login page'e dönüldüğünü kontrol et
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
            "Should be redirected to login page after logout");
        
        logger.info("TEST: Logout Test Completed Successfully");
    }
}

