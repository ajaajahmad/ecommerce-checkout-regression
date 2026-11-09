package com.quantumqa.checkout.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import com.quantumqa.checkout.data.TestData;
import com.quantumqa.checkout.pages.CartPage;
import com.quantumqa.checkout.pages.CheckoutInformationPage;
import com.quantumqa.checkout.pages.CheckoutOverviewPage;

public class CheckoutTest extends BaseTest {

	@Test(description = "TC07 Empty first name blocks checkout")
	public void emptyFirstNameBlocksCheckout() {
		CheckoutInformationPage information = signInAsShopper().addToCart(TestData.BACKPACK).header.openCart()
				.proceedToCheckout();

		information.submitDetails("", TestData.LAST_NAME, TestData.POSTAL_CODE);

		String error = information.errorMessage();
		assertTrue(error.contains("First Name is required"), "Unexpected error: " + error);
		assertFalse(driver().getCurrentUrl().contains("checkout-step-two"),
				"Checkout moved on to: " + driver().getCurrentUrl());
	}

	@Test(description = "TC08 Valid checkout completes with the correct subtotal")
	public void validCheckoutCompletesWithCorrectSubtotal() {
		CartPage cart = signInAsShopper().addToCart(TestData.BACKPACK).header.openCart();
		BigDecimal cartTotal = cart.itemPrices().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

		CheckoutOverviewPage overview = cart.proceedToCheckout().continueWith(TestData.FIRST_NAME, TestData.LAST_NAME,
				TestData.POSTAL_CODE);

		assertEquals(overview.subtotal(), cartTotal, "Subtotal should match the cart");
		assertEquals(overview.finish().confirmationMessage(), "Thank you for your order!", "Confirmation message");
	}
}
