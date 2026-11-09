package com.quantumqa.checkout.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.quantumqa.checkout.config.TestConfig;

public class LoginPage {

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
		driver.get(TestConfig.baseUrl());
		wait.until(visibilityOfElementLocated(loginButton));
		return this;
	}

	public InventoryPage signInAs(String username, String password) {
		submitCredentials(username, password);
		return new InventoryPage(driver);
	}

	public void submitCredentials(String username, String password) {
		driver.findElement(usernameInput).sendKeys(username);
		driver.findElement(passwordInput).sendKeys(password);
		wait.until(elementToBeClickable(loginButton)).click();
	}

	public String errorMessage() {
		return wait.until(visibilityOfElementLocated(errorMessage)).getText();
	}
}
