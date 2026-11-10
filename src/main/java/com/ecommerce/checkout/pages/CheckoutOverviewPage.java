package com.ecommerce.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ecommerce.checkout.config.TestConfig;
import com.ecommerce.checkout.utils.Prices;

public class CheckoutOverviewPage {

	private static final Logger LOG = LogManager.getLogger(CheckoutOverviewPage.class);

	private final By subtotalLabel = By.cssSelector("[data-test='subtotal-label']");
	private final By finishButton = By.cssSelector("[data-test='finish']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public CheckoutOverviewPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public BigDecimal subtotal() {
		BigDecimal subtotal = Prices.parse(wait.until(visibilityOfElementLocated(subtotalLabel)).getText());
		LOG.debug("Overview subtotal: {}", subtotal);
		return subtotal;
	}

	public CheckoutCompletePage finish() {
		LOG.info("Placing the order");
		wait.until(elementToBeClickable(finishButton)).click();
		return new CheckoutCompletePage(driver);
	}
}
