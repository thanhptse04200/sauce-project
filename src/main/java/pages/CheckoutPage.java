package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.className("complete-header");
    private final By backToProductsButton = By.id("back-to-products");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);

    }

    public void clickContinue() {
        click(continueButton);
        waitForUrlContains("checkout-step-two");
    }

    public String getTotalPrice() {
        return getText(totalLabel);
    }

    public void clickFinish() {
        click(finishButton);
        waitForUrlContains("checkout-complete");
    }

    public String getCompleteHeader() {
        return getText(completeHeader);
    }

    public boolean isOrderComplete() {
        return getCompleteHeader().contains("Thank you for your order");
    }
}