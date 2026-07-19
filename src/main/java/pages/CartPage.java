package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return findAll(cartItems).size();
    }

    public void clickCheckout() {
        jsClick(checkoutButton);
        waitForUrlContains("checkout-step-one");
    }

    public boolean isOnCartPage() {
        return getCurrentUrl().contains("cart");
    }

}