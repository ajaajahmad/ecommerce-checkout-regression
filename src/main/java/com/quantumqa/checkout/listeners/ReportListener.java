package com.quantumqa.checkout.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.quantumqa.checkout.driver.DriverManager;

public class ReportListener implements ITestListener {

	private static final Path EVIDENCE_DIR = Path.of("target", "test-evidence");

	private final ExtentReports extent = createReport();
	private final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

	@Override
	public void onTestStart(ITestResult result) {
		currentTest.set(extent.createTest(result.getMethod().getDescription()));
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		currentTest.get().pass("Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		ExtentTest test = currentTest.get();
		WebDriver driver = DriverManager.driver();
		if (driver == null) {
			test.fail(result.getThrowable());
			return;
		}
		String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		test.fail(result.getThrowable(),
				MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		test.info("Failed on URL: " + driver.getCurrentUrl());
		savePageSource(result.getMethod().getMethodName(), driver.getPageSource());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// A skip means setup failed before the test started, so onTestStart may not have run.
		if (currentTest.get() == null) {
			currentTest.set(extent.createTest(result.getMethod().getDescription()));
		}
		currentTest.get().skip(result.getThrowable());
	}

	@Override
	public void onFinish(org.testng.ITestContext context) {
		extent.flush();
	}

	private static ExtentReports createReport() {
		ExtentReports report = new ExtentReports();
		ExtentSparkReporter reporter = new ExtentSparkReporter(EVIDENCE_DIR.resolve("checkout-report.html").toString());
		reporter.config().setDocumentTitle("Checkout Regression");
		reporter.config().setReportName("SauceDemo checkout regression (demo application)");
		report.attachReporter(reporter);
		return report;
	}

	private static void savePageSource(String testName, String pageSource) {
		try {
			Files.createDirectories(EVIDENCE_DIR);
			Files.writeString(EVIDENCE_DIR.resolve(testName + ".html"), pageSource);
		} catch (IOException e) {
			throw new IllegalStateException("Could not save page source for " + testName, e);
		}
	}
}
