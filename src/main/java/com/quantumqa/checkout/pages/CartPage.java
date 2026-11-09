package com.quantumqa.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.checkout.config.TestConfig;
import com.quantumqa.checkout.pages.components.HeaderComponent;
import com.quantumqa.checkout.utils.Prices;

public class CartPage {

	private static final Logger LOG = LogManager.getLogger(CartPage.class);

	private final By cartList = By.cssSelector("[data-test='cart-list']");
	private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
	private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
	private final By checkoutButton = By.cssSelector("[data-test='checkout']");

	public final HeaderComponent header;

	private final WebDriver driver;
	private final WebDriverWait wait;

	public CartPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
		this.header = new HeaderComponent(driver);
	}

	public List<String> itemNames() {
		wait.until(visibilityOfElementLocated(cartList));
		List<String> names = driver.findElements(itemName).stream().map(WebElement::getText).toList();
		LOG.debug("Cart items: {}", names);
		return names;
	}

	public List<BigDecimal> itemPrices() {
		wait.until(visibilityOfElementLocated(cartList));
		List<BigDecimal> prices = Prices.parseAll(driver.findElements(itemPrice));
		LOG.debug("Cart prices: {}", prices);
		return prices;
	}

	public CartPage remove(String productName) {
		LOG.info("Removing '{}' from the cart", productName);
		wait.until(elementToBeClickable(By.xpath(cartRowXpath(productName) + "//button"))).click();
		wait.until(invisibilityOfElementLocated(By.xpath(cartRowXpath(productName))));
		return this;
	}

	public CheckoutInformationPage proceedToCheckout() {
		LOG.info("Proceeding to checkout");
		wait.until(elementToBeClickable(checkoutButton)).click();
		return new CheckoutInformationPage(driver);
	}

	// Scoped to the cart list because inventory rows use the same attributes and also show a
	// Remove button, so an unscoped locator can match the previous page during navigation.
	private String cartRowXpath(String productName) {
		return "//div[@data-test='cart-list']//div[@data-test='inventory-item']"
				+ "[.//div[@data-test='inventory-item-name'][normalize-space()='" + productName + "']]";
	}
}
