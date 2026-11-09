package com.quantumqa.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.checkout.config.TestConfig;

public class CheckoutCompletePage {

	private static final Logger LOG = LogManager.getLogger(CheckoutCompletePage.class);

	private final By completeHeader = By.cssSelector("[data-test='complete-header']");

	private final WebDriverWait wait;

	public CheckoutCompletePage(WebDriver driver) {
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public String confirmationMessage() {
		String message = wait.until(visibilityOfElementLocated(completeHeader)).getText();
		LOG.debug("Confirmation shown: {}", message);
		return message;
	}
}
