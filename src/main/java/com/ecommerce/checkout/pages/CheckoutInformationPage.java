package com.ecommerce.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ecommerce.checkout.config.TestConfig;

public class CheckoutInformationPage {

	private static final Logger LOG = LogManager.getLogger(CheckoutInformationPage.class);

	private final By firstNameInput = By.cssSelector("[data-test='firstName']");
	private final By lastNameInput = By.cssSelector("[data-test='lastName']");
	private final By postalCodeInput = By.cssSelector("[data-test='postalCode']");
	private final By continueButton = By.cssSelector("[data-test='continue']");
	private final By errorMessage = By.cssSelector("[data-test='error']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public CheckoutInformationPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public CheckoutOverviewPage continueWith(String firstName, String lastName, String postalCode) {
		submitDetails(firstName, lastName, postalCode);
		return new CheckoutOverviewPage(driver);
	}

	// Quoted so an empty field shows as '' rather than disappearing from the line.
	public void submitDetails(String firstName, String lastName, String postalCode) {
		LOG.info("Submitting checkout details: first name '{}', last name '{}', postal code '{}'", firstName,
				lastName, postalCode);
		wait.until(visibilityOfElementLocated(firstNameInput)).sendKeys(firstName);
		driver.findElement(lastNameInput).sendKeys(lastName);
		driver.findElement(postalCodeInput).sendKeys(postalCode);
		wait.until(elementToBeClickable(continueButton)).click();
	}

	public String errorMessage() {
		String text = wait.until(visibilityOfElementLocated(errorMessage)).getText();
		LOG.debug("Checkout error shown: {}", text);
		return text;
	}
}
