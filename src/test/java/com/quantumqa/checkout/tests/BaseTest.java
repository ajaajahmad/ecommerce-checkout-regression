package com.quantumqa.checkout.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.quantumqa.checkout.config.TestConfig;
import com.quantumqa.checkout.data.TestData;
import com.quantumqa.checkout.driver.DriverManager;
import com.quantumqa.checkout.pages.InventoryPage;
import com.quantumqa.checkout.pages.LoginPage;

public abstract class BaseTest {

	@BeforeMethod(alwaysRun = true)
	public void startBrowser() {
		DriverManager.start();
	}

	@AfterMethod(alwaysRun = true)
	public void stopBrowser() {
		DriverManager.quit();
	}

	protected WebDriver driver() {
		return DriverManager.driver();
	}

	protected LoginPage openLoginPage() {
		return new LoginPage(driver()).open();
	}

	protected InventoryPage signInAsShopper() {
		return openLoginPage().signInAs(TestConfig.shopper(), TestData.PASSWORD);
	}
}
