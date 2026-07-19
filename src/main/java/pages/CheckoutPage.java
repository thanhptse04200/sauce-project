package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage extends BasePage {


    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

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
    }

    public String getTotalPrice() {
        return getText(totalLabel);
    }

    public void clickFinish() {
        click(finishButton);
    }

    public String getCompleteHeader() {
        return getText(completeHeader);
    }

    public void backToProducts() {
        click(backToProductsButton);
    }
    public boolean isOrderComplete() {
        try {
            return getCompleteHeader().contains("Thank you for your order");
        } catch (Exception e) {
            return false;
        }
    }
}
