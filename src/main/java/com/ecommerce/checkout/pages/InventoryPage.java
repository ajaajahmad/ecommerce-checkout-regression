package com.ecommerce.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ecommerce.checkout.config.TestConfig;
import com.ecommerce.checkout.pages.components.HeaderComponent;
import com.ecommerce.checkout.utils.Prices;

public class InventoryPage {

	private static final Logger LOG = LogManager.getLogger(InventoryPage.class);

	private final By inventoryList = By.cssSelector("[data-test='inventory-list']");
	private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
	private final By sortDropdown = By.cssSelector("[data-test='product-sort-container']");

	public final HeaderComponent header;

	private final WebDriver driver;
	private final WebDriverWait wait;

	public InventoryPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
		this.header = new HeaderComponent(driver);
	}

	public boolean isInventoryVisible() {
		return wait.until(visibilityOfElementLocated(inventoryList)).isDisplayed();
	}

	public InventoryPage addToCart(String productName) {
		LOG.info("Adding '{}' to the cart", productName);
		wait.until(elementToBeClickable(addToCartButton(productName))).click();
		return this;
	}

	public InventoryPage sortBy(String visibleOption) {
		LOG.info("Sorting products by '{}'", visibleOption);
		new Select(wait.until(visibilityOfElementLocated(sortDropdown))).selectByVisibleText(visibleOption);
		return this;
	}

	public List<BigDecimal> displayedPrices() {
		wait.until(visibilityOfElementLocated(inventoryList));
		List<BigDecimal> prices = Prices.parseAll(driver.findElements(itemPrice));
		LOG.debug("Displayed prices: {}", prices);
		return prices;
	}

	// Scoped to the product's own row so the test never depends on its position in the list.
	private By addToCartButton(String productName) {
		return By.xpath("//div[@data-test='inventory-item'][.//div[@data-test='inventory-item-name'][normalize-space()='"
				+ productName + "']]//button");
	}
}
