package com.mustafa.pages;

import com.mustafa.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ProductsPage - SauceDemo ürünler sayfası için Page Object
 */
public class ProductsPage extends BasePage {
    
    // Locators
    private final By pageTitle = By.className("title");
    private final By productItems = By.className("inventory_item");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By shoppingCartLink = By.className("shopping_cart_link");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By productSortDropdown = By.className("product_sort_container");
    
    // Constructor
    public ProductsPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Ürünler sayfasında olduğunu doğrula
     */
    @Step("Verify products page is displayed")
    public boolean isProductsPageDisplayed() {
        boolean displayed = isElementVisible(pageTitle) && 
                          getText(pageTitle).equals("Products");
        logger.info("Products page displayed: " + displayed);
        return displayed;
    }
    
    /**
     * Sayfa başlığını al
     */
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    /**
     * Ürün sayısını al
     */
    @Step("Get number of products")
    public int getProductCount() {
        List<WebElement> products = findElements(productItems);
        int count = products.size();
        logger.info("Product count: " + count);
        return count;
    }
    
    /**
     * İlk ürünü sepete ekle
     */
    @Step("Add first product to cart")
    public ProductsPage addFirstProductToCart() {
        By firstAddToCartButton = By.id("add-to-cart-sauce-labs-backpack");
        click(firstAddToCartButton);
        logger.info("First product added to cart");
        return this;
    }
    
    /**
     * Belirli bir ürünü sepete ekle (isim ile)
     */
    @Step("Add product to cart: {productName}")
    public ProductsPage addProductToCart(String productName) {
        // Ürün ismine göre dinamik locator oluştur
        String formattedName = productName.toLowerCase().replace(" ", "-");
        By addToCartButton = By.id("add-to-cart-" + formattedName);
        click(addToCartButton);
        logger.info("Product added to cart: " + productName);
        return this;
    }
    
    /**
     * Sepetteki ürün sayısını al
     */
    @Step("Get cart item count")
    public int getCartItemCount() {
        if (isElementVisible(shoppingCartBadge)) {
            String count = getText(shoppingCartBadge);
            logger.info("Cart item count: " + count);
            return Integer.parseInt(count);
        }
        logger.info("Cart is empty");
        return 0;
    }
    
    /**
     * Alışveriş sepetine git
     */
    @Step("Navigate to shopping cart")
    public void goToShoppingCart() {
        click(shoppingCartLink);
        logger.info("Navigated to shopping cart");
    }
    
    /**
     * Logout yap
     */
    @Step("Logout from application")
    public void logout() {
        click(burgerMenuButton);
        waitForElement(logoutLink);
        click(logoutLink);
        logger.info("User logged out");
    }
    
    /**
     * Ürün sıralama dropdown'u görünür mü
     */
    public boolean isSortDropdownDisplayed() {
        return isElementVisible(productSortDropdown);
    }
    
    /**
     * Ürünleri sırala
     */
    @Step("Sort products by: {sortOption}")
    public ProductsPage sortProducts(String sortOption) {
        selectByVisibleText(productSortDropdown, sortOption);
        logger.info("Products sorted by: " + sortOption);
        return this;
    }
}

