package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;

public class CartCheckoutTest extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        // Pre-condition: login as standard_user
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isOnInventoryPage(), "Login failed — cannot proceed");
    }

    @Test(description = "Add single item to cart and verify cart badge")
    public void testAddSingleItemToCart() {
        inventoryPage.addItemToCartByIndex(0);

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1,
                "Cart badge should show 1 after adding 1 item");
    }

    @Test(description = "Add multiple items to cart and verify count")
    public void testAddMultipleItemsToCart() {
        inventoryPage.addItemToCartByIndex(0);
        inventoryPage.addItemToCartByIndex(1);
        inventoryPage.addItemToCartByIndex(2);

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 3,
                "Cart badge should show 3 after adding 3 items");
    }

    @Test(description = "Add items, go to cart, verify items present")
    public void testCartContainsAddedItems() {
        inventoryPage.addItemToCartByIndex(0);
        inventoryPage.addItemToCartByIndex(1);
        inventoryPage.goToCart();

        Assert.assertTrue(cartPage.isOnCartPage(), "Should be on cart page");
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "Cart should contain 2 items");
    }

    @Test(description = "Full E2E: Add item → Cart → Checkout → Complete")
    public void testFullCheckoutFlow() {
        // Add item
        inventoryPage.addItemToCartByIndex(0);

        // Go to cart
        inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isOnCartPage(), "Should be on cart page");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item");

        // Checkout step 1: fill info
        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Checkout step 2: verify total exists
        String total = checkoutPage.getTotalPrice();
        Assert.assertNotNull(total, "Total price should be displayed");
        Assert.assertTrue(total.contains("Total:"), "Should show Total label");

        // Finish
        checkoutPage.clickFinish();

        // Verify order complete
        Assert.assertTrue(checkoutPage.isOrderComplete(),
                "Order confirmation 'Thank you for your order' should be displayed");
    }

    @Test(description = "Checkout with multiple items — verify total includes all")
    public void testCheckoutMultipleItems() {
        inventoryPage.addItemToCartByIndex(0);
        inventoryPage.addItemToCartByIndex(1);

        inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");

        cartPage.clickCheckout();
        checkoutPage.fillCheckoutInfo("Jane", "Smith", "67890");
        checkoutPage.clickContinue();

        String total = checkoutPage.getTotalPrice();
        Assert.assertNotNull(total, "Total should be displayed for multiple items");

        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderComplete(),
                "Order should complete successfully with multiple items");
    }
}
