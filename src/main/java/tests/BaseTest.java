package tests;

import drivers.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

@Listeners(listeners.ScreenshotListener.class)
public class BaseTest {

    protected WebDriver driver;

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser) {
        DriverFactory.setDriver(browser);
        driver = DriverFactory.getDriver();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
