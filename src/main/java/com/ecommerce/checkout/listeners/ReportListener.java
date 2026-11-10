package com.ecommerce.checkout.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.ecommerce.checkout.config.TestConfig;
import com.ecommerce.checkout.driver.DriverManager;

public class ReportListener implements ITestListener {

	private static final Logger LOG = LogManager.getLogger(ReportListener.class);

	private static final Path EVIDENCE_DIR = Path.of("target", "test-evidence");
	// log4j2.xml writes one log per test here, named after the test method.
	private static final Path TEST_LOG_DIR = EVIDENCE_DIR.resolve("logs");

	private final ExtentReports extent = createReport();
	private final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

	@Override
	public void onStart(ITestContext context) {
		LOG.info("Running '{}' with {}", context.getName(), TestConfig.summary());
	}

	@Override
	public void onTestStart(ITestResult result) {
		currentTest.set(extent.createTest(result.getMethod().getDescription()));
		LOG.info("START {}", result.getMethod().getDescription());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		LOG.info("PASS  {} in {} ms", result.getMethod().getDescription(), duration(result));
		currentTest.get().pass("Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		LOG.error("FAIL  {} in {} ms: {}", result.getMethod().getDescription(), duration(result),
				firstLine(result.getThrowable()));

		ExtentTest test = currentTest.get();
		WebDriver driver = DriverManager.driver();
		if (driver == null) {
			test.fail(result.getThrowable());
		} else {
			String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
			test.fail(result.getThrowable(),
					MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
			test.info("Failed on URL: " + driver.getCurrentUrl());
			savePageSource(testName, driver.getPageSource());
		}
		attachTestLog(test, testName);

		// After the attachment, so the report shows the steps without repeating the stack trace.
		LOG.debug("Stack trace for {}", testName, result.getThrowable());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// A skip means setup failed before the test started, so onTestStart may not have run.
		if (currentTest.get() == null) {
			currentTest.set(extent.createTest(result.getMethod().getDescription()));
		}
		LOG.warn("SKIP  {}: {}", result.getMethod().getDescription(), firstLine(result.getThrowable()));
		currentTest.get().skip(result.getThrowable());
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
		LOG.info("Report: {}", EVIDENCE_DIR.resolve("checkout-report.html").toAbsolutePath());
	}

	private static ExtentReports createReport() {
		ExtentReports report = new ExtentReports();
		ExtentSparkReporter reporter = new ExtentSparkReporter(EVIDENCE_DIR.resolve("checkout-report.html").toString());
		reporter.config().setDocumentTitle("Checkout Regression");
		reporter.config().setReportName("SauceDemo checkout regression (demo application)");
		report.attachReporter(reporter);
		return report;
	}

	private static long duration(ITestResult result) {
		return result.getEndMillis() - result.getStartMillis();
	}

	// Selenium messages run to many lines of session detail; the first line says what went wrong.
	private static String firstLine(Throwable throwable) {
		if (throwable == null || throwable.getMessage() == null) {
			return String.valueOf(throwable);
		}
		return throwable.getMessage().lines().findFirst().orElse("");
	}

	private static void savePageSource(String testName, String pageSource) {
		Path file = EVIDENCE_DIR.resolve(testName + ".html");
		try {
			Files.createDirectories(EVIDENCE_DIR);
			Files.writeString(file, pageSource);
			LOG.info("Saved page source to {}", file);
		} catch (IOException e) {
			throw new IllegalStateException("Could not save page source for " + testName, e);
		}
	}

	private static void attachTestLog(ExtentTest test, String testName) {
		Path log = TEST_LOG_DIR.resolve(testName + ".log");
		if (!Files.exists(log)) {
			return;
		}
		try {
			test.info(MarkupHelper.createCodeBlock(Files.readString(log)));
		} catch (IOException e) {
			LOG.warn("Could not attach {} to the report", log, e);
		}
	}
}
