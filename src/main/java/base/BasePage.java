package base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.getInt("timeout"))
        );
    }

    protected WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String value) {

        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].focus();", element);
        element.clear();
        element.sendKeys(value);

    }

    protected String getText(By locator) {
        return find(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitUntilVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForUrlContains(String text) {
        wait.until(ExpectedConditions.urlContains(text));
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void jsClick(By locator) {
        WebElement element = find(locator);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
    }
}