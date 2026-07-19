package listeners;

import drivers.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotListener implements ITestListener {

    private static final String SCREENSHOT_DIR = "target/screenshots/";

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            return;
        }

        try {
            Path dir = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String testName = result.getTestClass().getRealClass().getSimpleName() + "_" + result.getName();
            String fileName = testName + "_" + timestamp + ".png";

            Path destination = Paths.get(SCREENSHOT_DIR, fileName);
            Files.copy(screenshot.toPath(), destination);

            System.out.println("Screenshot: " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[TEST START] " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[TEST PASS] " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[TEST SKIP] " + result.getMethod().getMethodName());
    }
}
