package com.ecommerce.checkout.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.ecommerce.checkout.data.TestData;
import com.ecommerce.checkout.pages.CartPage;
import com.ecommerce.checkout.pages.InventoryPage;

public class CartTest extends BaseTest {

	@Test(description = "TC04 Adding a product updates the badge and the cart")
	public void addingProductUpdatesBadgeAndCart() {
		InventoryPage inventory = signInAsShopper().addToCart(TestData.BACKPACK);

		assertEquals(inventory.header.cartBadgeCount(), "1", "Cart badge count");
		assertEquals(inventory.header.openCart().itemNames(), List.of(TestData.BACKPACK), "Cart contents");
	}

	@Test(description = "TC05 Removing the only product empties the cart")
	public void removingOnlyProductEmptiesCart() {
		CartPage cart = signInAsShopper().addToCart(TestData.BACKPACK).header.openCart();

		cart.remove(TestData.BACKPACK);

		assertTrue(cart.itemNames().isEmpty(), "Cart should have no rows");
		assertTrue(cart.header.isCartBadgeAbsent(), "Cart badge should be gone");
	}
}
