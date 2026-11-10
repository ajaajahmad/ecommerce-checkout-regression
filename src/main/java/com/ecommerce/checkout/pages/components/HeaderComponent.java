package com.ecommerce.checkout.pages.components;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ecommerce.checkout.config.TestConfig;
import com.ecommerce.checkout.pages.CartPage;

public class HeaderComponent {

	private static final Logger LOG = LogManager.getLogger(HeaderComponent.class);

	private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
	private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public HeaderComponent(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public String cartBadgeCount() {
		String count = wait.until(visibilityOfElementLocated(cartBadge)).getText();
		LOG.debug("Cart badge shows {}", count);
		return count;
	}

	public boolean isCartBadgeAbsent() {
		try {
			return wait.until(invisibilityOfElementLocated(cartBadge));
		} catch (TimeoutException e) {
			LOG.debug("Cart badge still visible after {}s", TestConfig.waitTimeout().toSeconds());
			return false;
		}
	}

	public CartPage openCart() {
		LOG.info("Opening the cart");
		wait.until(elementToBeClickable(cartLink)).click();
		return new CartPage(driver);
	}
}
