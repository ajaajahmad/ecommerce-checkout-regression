package com.quantumqa.checkout.pages.components;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.checkout.config.TestConfig;
import com.quantumqa.checkout.pages.CartPage;

public class HeaderComponent {

	private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
	private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public HeaderComponent(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public String cartBadgeCount() {
		return wait.until(visibilityOfElementLocated(cartBadge)).getText();
	}

	public boolean isCartBadgeAbsent() {
		try {
			return wait.until(invisibilityOfElementLocated(cartBadge));
		} catch (TimeoutException e) {
			return false;
		}
	}

	public CartPage openCart() {
		wait.until(elementToBeClickable(cartLink)).click();
		return new CartPage(driver);
	}
}
