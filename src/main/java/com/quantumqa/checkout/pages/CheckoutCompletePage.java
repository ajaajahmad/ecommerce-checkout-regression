package com.quantumqa.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.checkout.config.TestConfig;

public class CheckoutCompletePage {

	private final By completeHeader = By.cssSelector("[data-test='complete-header']");

	private final WebDriverWait wait;

	public CheckoutCompletePage(WebDriver driver) {
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public String confirmationMessage() {
		return wait.until(visibilityOfElementLocated(completeHeader)).getText();
	}
}
