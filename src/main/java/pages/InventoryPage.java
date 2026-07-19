package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnInventoryPage() {
        return waitForUrlContains("inventory");
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getInventoryItemCount() {
        return inventoryItems.size();
    }

    public void addItemToCartByIndex(int index) {
        WebElement item = inventoryItems.get(index);
        WebElement addButton = item.findElement(
                org.openqa.selenium.By.cssSelector("button[id^='add-to-cart']"));
        click(addButton);
    }

    public void goToCart() {
        click(cartLink);
    }

    public int getCartBadgeCount() {
        try {
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public void logout() {
        click(menuButton);
        waitUntilVisible(logoutLink);
        click(logoutLink);
    }
}
