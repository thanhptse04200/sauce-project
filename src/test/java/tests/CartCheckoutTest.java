package tests;

import constants.Products;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;

public class CartCheckoutTest extends BaseTest {

    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void initPages() {
        LoginPage loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "Login failed — cannot proceed");
    }

    @Test(description = "Add single item to cart and verify cart badge")
    public void testAddSingleItemToCart() {
        inventoryPage.addItemToCart((Products.BACKPACK));
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Cart badge should show 1 after adding 1 item");
    }

    @Test(description = "Add multiple items to cart and verify count")
    public void testAddMultipleItemsToCart() {
        inventoryPage.addItemToCart((Products.BACKPACK));
        inventoryPage.addItemToCart((Products.BIKE_LIGHT));
        inventoryPage.addItemToCart((Products.BOLT_TSHIRT));

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 3, "Cart badge should show 3 after adding 3 items");
    }

    @Test(description = "Add items, go to cart, verify items present")
    public void testCartContainsAddedItems() {
        inventoryPage.addItemToCart((Products.BACKPACK));
        inventoryPage.addItemToCart((Products.BIKE_LIGHT));
        inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isOnCartPage(), "Should be on cart page");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items");
    }

    @Test(description = "Full E2E: Complete shopping")
    public void testFullCheckoutFlow() {

        inventoryPage.addItemToCart((Products.BACKPACK));
        inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isOnCartPage(), "Should be on cart page");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item");

        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInfo("Phung", "Thanh", "100000");
        checkoutPage.clickContinue();

        String total = checkoutPage.getTotalPrice();
        Assert.assertNotNull(total, "Total price should be displayed");
        Assert.assertTrue(total.contains("Total:"), "Should show Total label");

        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderComplete(), "Notification should be displayed");
    }

    @Test(description = "Checkout with multiple items — verify total includes all")
    public void testCheckoutMultipleItems() {
        inventoryPage.addItemToCart((Products.BACKPACK));
        inventoryPage.addItemToCart((Products.BIKE_LIGHT));

        inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");

        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInfo("Phung", "Thanh", "100000");
        checkoutPage.clickContinue();

        String total = checkoutPage.getTotalPrice();
        Assert.assertNotNull(total, "Total should be displayed for multiple items");

        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderComplete(),
                "Order should complete successfully with multiple items");
    }

}
