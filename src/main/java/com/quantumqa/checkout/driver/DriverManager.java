package com.quantumqa.checkout.driver;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.quantumqa.checkout.config.TestConfig;

public final class DriverManager {

	// One browser per test thread, so parallel tests never share a cart or session.
	private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

	private DriverManager() {
	}

	public static void start() {
		DRIVER.set(new ChromeDriver(chromeOptions()));
	}

	public static WebDriver driver() {
		return DRIVER.get();
	}

	public static void quit() {
		WebDriver driver = DRIVER.get();
		if (driver != null) {
			driver.quit();
			DRIVER.remove();
		}
	}

	private static ChromeOptions chromeOptions() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--window-size=1440,900");
		if (TestConfig.headless()) {
			options.addArguments("--headless=new");
		}
		// The demo password appears in breach lists, and Chrome's warning popup would block the page.
		options.setExperimentalOption("prefs",
				Map.of("credentials_enable_service", false, "profile.password_manager_enabled", false,
						"profile.password_manager_leak_detection", false));
		return options;
	}
}
