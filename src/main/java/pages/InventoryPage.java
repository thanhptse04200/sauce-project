package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InventoryPage extends BasePage {

    private final By inventoryItems = By.className("inventory_item");
    private final By cartLink = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private static final By ITEM_NAME = By.className("inventory_item_name");
    private static final By ADD_BUTTON = By.cssSelector("button[id^='add-to-cart']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnInventoryPage() {
        return getCurrentUrl().contains("inventory");
    }

    public void addItemToCart(String productName) {

        for (WebElement item : findAll(inventoryItems)) {
            String currentName = item.findElement(ITEM_NAME).getText();
            if (currentName.equals(productName)) {
                item.findElement(ADD_BUTTON).click();
                return;
            }
        }
        throw new IllegalArgumentException(
                "Cannot find product: " + productName
        );
    }

    public void goToCart() {
        click(cartLink);
        waitForUrlContains("cart");

    }

    public int getCartBadgeCount() {
        if (!isDisplayed(cartBadge)) {
            return 0;
        }
        return Integer.parseInt(getText(cartBadge));
    }

}