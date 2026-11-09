package com.quantumqa.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.math.BigDecimal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.checkout.config.TestConfig;
import com.quantumqa.checkout.utils.Prices;

public class CheckoutOverviewPage {

	private final By subtotalLabel = By.cssSelector("[data-test='subtotal-label']");
	private final By finishButton = By.cssSelector("[data-test='finish']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public CheckoutOverviewPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public BigDecimal subtotal() {
		return Prices.parse(wait.until(visibilityOfElementLocated(subtotalLabel)).getText());
	}

	public CheckoutCompletePage finish() {
		wait.until(elementToBeClickable(finishButton)).click();
		return new CheckoutCompletePage(driver);
	}
}
