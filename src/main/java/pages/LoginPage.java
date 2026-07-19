package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import base.BasePage;
import utils.ConfigReader;

public class LoginPage extends BasePage {




    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {

        driver.get(
                ConfigReader.get("base.url")
        );

        waitUntilVisible(loginButton);

    }

    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void clickLogin() {
        click(loginButton);
    }


    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isErrorDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().equals(ConfigReader.get("base.url")) || driver.getCurrentUrl().equals(ConfigReader.get("base.url") + "/");
    }
}
