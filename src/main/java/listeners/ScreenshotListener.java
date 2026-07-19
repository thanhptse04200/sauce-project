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
            // Create screenshots directory
            Path dir = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String testName = result.getTestClass().getRealClass().getSimpleName() + "_" + result.getName();
            String fileName = testName + "_" + timestamp + ".png";

            // Copy to target directory
            Path destination = Paths.get(SCREENSHOT_DIR, fileName);
            Files.copy(screenshot.toPath(), destination);

            System.out.println("[SCREENSHOT] Saved: " + destination.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[SCREENSHOT] Failed to capture screenshot: " + e.getMessage());
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
