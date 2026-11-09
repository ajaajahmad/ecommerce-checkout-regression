package com.quantumqa.checkout.tests;

import java.lang.reflect.Method;

import org.apache.logging.log4j.ThreadContext;
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
	public void startBrowser(Method method) {
		// Tags every log line from this thread; log4j2.xml routes tagged lines to a per-test file.
		ThreadContext.put("test", method.getName());
		DriverManager.start();
	}

	@AfterMethod(alwaysRun = true)
	public void stopBrowser() {
		try {
			DriverManager.quit();
		} finally {
			ThreadContext.remove("test");
		}
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
