package com.quantumqa.checkout.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.testng.annotations.Test;

import com.quantumqa.checkout.data.TestData;
import com.quantumqa.checkout.pages.InventoryPage;

public class InventoryTest extends BaseTest {

	@Test(description = "TC06 Sorting by price orders products low to high")
	public void sortingByPriceOrdersLowToHigh() {
		InventoryPage inventory = signInAsShopper();

		// Fails if the list already started in price order, because the sort would then prove nothing.
		List<BigDecimal> before = inventory.displayedPrices();
		assertTrue(before.size() > 1, "Sorting needs more than one product");
		assertNotEquals(before, sorted(before), "Default order should not already be price order");

		List<BigDecimal> after = inventory.sortBy(TestData.SORT_PRICE_LOW_TO_HIGH).displayedPrices();

		assertEquals(after, sorted(before), "Prices should be ascending");
	}

	private List<BigDecimal> sorted(List<BigDecimal> prices) {
		return prices.stream().sorted().toList();
	}
}
