package locators;

import org.openqa.selenium.By;

public class LoginPageLocators {

    private LoginPageLocators() {
        // Prevent instantiate
    }

    public static final By TXT_USERNAME =
            By.id("user-name");

    public static final By TXT_PASSWORD =
            By.id("password");

    public static final By BTN_LOGIN =
            By.id("login-button");

    public static final By TXT_ERROR =
            By.cssSelector("[data-test='error']");
}