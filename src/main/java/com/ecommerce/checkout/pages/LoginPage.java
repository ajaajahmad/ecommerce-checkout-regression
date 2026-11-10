package com.ecommerce.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ecommerce.checkout.config.TestConfig;

public class LoginPage {

	private static final Logger LOG = LogManager.getLogger(LoginPage.class);

	private final By usernameInput = By.cssSelector("[data-test='username']");
	private final By passwordInput = By.cssSelector("[data-test='password']");
	private final By loginButton = By.cssSelector("[data-test='login-button']");
	private final By errorMessage = By.cssSelector("[data-test='error']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public LoginPage open() {
		LOG.info("Opening {}", TestConfig.baseUrl());
		driver.get(TestConfig.baseUrl());
		wait.until(visibilityOfElementLocated(loginButton));
		return this;
	}

	public InventoryPage signInAs(String username, String password) {
		submitCredentials(username, password);
		return new InventoryPage(driver);
	}

	// The password is never logged, even for demo accounts, so the habit carries over to real ones.
	public void submitCredentials(String username, String password) {
		LOG.info("Signing in as '{}'", username);
		driver.findElement(usernameInput).sendKeys(username);
		driver.findElement(passwordInput).sendKeys(password);
		wait.until(elementToBeClickable(loginButton)).click();
	}

	public String errorMessage() {
		String text = wait.until(visibilityOfElementLocated(errorMessage)).getText();
		LOG.debug("Login error shown: {}", text);
		return text;
	}
}
