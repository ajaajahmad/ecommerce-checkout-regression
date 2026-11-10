package com.ecommerce.checkout.tests;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ecommerce.checkout.data.TestData;
import com.ecommerce.checkout.pages.InventoryPage;
import com.ecommerce.checkout.pages.LoginPage;

public class LoginTest extends BaseTest {

	@Test(description = "TC01 Valid login opens the inventory")
	public void validLoginOpensInventory() {
		InventoryPage inventory = signInAsShopper();

		assertTrue(inventory.isInventoryVisible(), "Inventory should be visible");
		assertTrue(driver().getCurrentUrl().endsWith("/inventory.html"),
				"Unexpected URL: " + driver().getCurrentUrl());
	}

	@Test(description = "TC02 Locked account is denied login")
	public void lockedAccountIsDenied() {
		LoginPage login = openLoginPage();

		login.submitCredentials(TestData.LOCKED_OUT_USER, TestData.PASSWORD);

		String error = login.errorMessage();
		assertTrue(error.contains("locked out"), "Unexpected error: " + error);
		assertFalse(driver().getCurrentUrl().contains("inventory"),
				"Locked user reached: " + driver().getCurrentUrl());
	}

	@Test(description = "TC03 Empty username is rejected")
	public void emptyUsernameIsRejected() {
		LoginPage login = openLoginPage();

		login.submitCredentials("", TestData.PASSWORD);

		String error = login.errorMessage();
		assertTrue(error.contains("Username is required"), "Unexpected error: " + error);
		assertFalse(driver().getCurrentUrl().contains("inventory"),
				"Signed-out user reached: " + driver().getCurrentUrl());
	}
}
