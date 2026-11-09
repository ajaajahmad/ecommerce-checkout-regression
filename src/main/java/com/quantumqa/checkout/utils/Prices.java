package com.quantumqa.checkout.utils;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.WebElement;

public final class Prices {

	private Prices() {
	}

	// BigDecimal keeps money comparisons exact, which double would not.
	public static BigDecimal parse(String displayedPrice) {
		return new BigDecimal(displayedPrice.replaceAll("[^0-9.]", ""));
	}

	public static List<BigDecimal> parseAll(List<WebElement> priceElements) {
		return priceElements.stream().map(WebElement::getText).map(Prices::parse).toList();
	}
}
