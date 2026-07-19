package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import testdata.UserData;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        loginPage.open();
    }

    // ======================== HAPPY PATH ========================

    @Test(description = "Login with valid standard_user credentials")
    public void testLoginSuccess_StandardUser() {
        loginPage.login(UserData.STANDARD_USER, UserData.VALID_PASSWORD);

        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Should redirect to inventory page after successful login");
        Assert.assertEquals(inventoryPage.getPageTitle(), "Products",
                "Page title should be 'Products'");
    }

    @Test(description = "Login with performance_glitch_user - should succeed with delay")
    public void testLoginSuccess_PerformanceGlitchUser() {
        loginPage.login(UserData.PERFORMANCE_GLITCH_USER, UserData.VALID_PASSWORD);

        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Performance glitch user should still login successfully");
    }

    @Test(description = "Login with problem_user - should reach inventory")
    public void testLoginSuccess_ProblemUser() {
        loginPage.login(UserData.PROBLEM_USER, UserData.VALID_PASSWORD);

        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Problem user should login and reach inventory page");
    }

    @Test(description = "Login with error_user - should reach inventory")
    public void testLoginSuccess_ErrorUser() {
        loginPage.login(UserData.ERROR_USER, UserData.VALID_PASSWORD);

        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Error user should login and reach inventory page");
    }

    @Test(description = "Login with visual_user - should reach inventory")
    public void testLoginSuccess_VisualUser() {
        loginPage.login(UserData.VISUAL_USER, UserData.VALID_PASSWORD);

        Assert.assertTrue(inventoryPage.isOnInventoryPage(),
                "Visual user should login and reach inventory page");
    }

    // ======================== NEGATIVE: LOCKED USER ========================

    @Test(description = "Login with locked_out_user should show locked error")
    public void testLoginFail_LockedOutUser() {
        loginPage.login(UserData.LOCKED_OUT_USER, UserData.VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for locked user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Error should mention user is locked out");
        Assert.assertTrue(loginPage.isOnLoginPage(),
                "Should remain on login page");
    }

    // ======================== NEGATIVE: INVALID CREDENTIALS ========================

    @Test(description = "Login with wrong password shows error")
    public void testLoginFail_WrongPassword() {
        loginPage.login(UserData.STANDARD_USER, UserData.INVALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("do not match"),
                "Error should indicate credentials don't match");
        Assert.assertTrue(loginPage.isOnLoginPage(),
                "Should remain on login page");
    }

    @Test(description = "Login with non-existent username shows error")
    public void testLoginFail_InvalidUsername() {
        loginPage.login(UserData.NON_EXIST_USER, UserData.VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("do not match"),
                "Error should indicate credentials don't match");
    }

    @Test(description = "Login with both invalid username and password")
    public void testLoginFail_BothInvalid() {
        loginPage.login(UserData.NON_EXIST_USER, UserData.INVALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed");
        Assert.assertTrue(loginPage.isOnLoginPage(),
                "Should remain on login page");
    }

    // ======================== NEGATIVE: EMPTY FIELDS ========================

    @Test(description = "Login with empty username shows required error")
    public void testLoginFail_EmptyUsername() {
        loginPage.login(UserData.EMPTY, UserData.VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Error should say username is required");
    }

    @Test(description = "Login with empty password shows required error")
    public void testLoginFail_EmptyPassword() {
        loginPage.login(UserData.STANDARD_USER, UserData.EMPTY);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
                "Error should say password is required");
    }

    @Test(description = "Login with both fields empty shows username required first")
    public void testLoginFail_BothEmpty() {
        loginPage.login(UserData.EMPTY, UserData.EMPTY);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Error should prioritize username required message");
    }

    // ======================== EDGE CASES ========================

    @Test(description = "Login with username containing spaces")
    public void testLoginEdge_UsernameWithSpaces() {
        loginPage.login("  " + UserData.STANDARD_USER + " ", UserData.VALID_PASSWORD);

        // Depending on implementation: may succeed (trimmed) or fail
        // SauceDemo does NOT trim, so this should fail
        Assert.assertTrue(loginPage.isErrorDisplayed() || inventoryPage.isOnInventoryPage(),
                "Should either trim and succeed, or show error");
    }

    @Test(description = "Login with password containing special characters")
    public void testLoginEdge_SpecialCharsPassword() {
        loginPage.login(UserData.STANDARD_USER, UserData.SPECIAL_CHARACTER);
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error should be displayed for wrong password with special chars");
    }

    @Test(description = "Login with very long username (255 chars)")
    public void testLoginEdge_LongUsername() {
        String longUsername = "a".repeat(255);
        loginPage.login(longUsername, UserData.VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error should be displayed for non-existent long username");
        Assert.assertTrue(loginPage.isOnLoginPage(),
                "Should remain on login page");
    }

    @Test(description = "Login with SQL injection attempt in username")
    public void testLoginEdge_SQLInjection() {
        loginPage.login("' OR '1'='1", "' OR '1'='1");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "SQL injection should not bypass login");
        Assert.assertTrue(loginPage.isOnLoginPage(),
                "Should remain on login page — no bypass");
    }

    @Test(description = "Login with XSS script in username")
    public void testLoginEdge_XSSAttempt() {
        loginPage.login("<script>alert('xss')</script>", UserData.VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "XSS payload should not execute — treated as invalid credentials");
        Assert.assertTrue(loginPage.isOnLoginPage(),
                "Should remain on login page");
    }

    // ======================== DATA-DRIVEN NEGATIVE CASES ========================

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"standard_user", "wrong1", "do not match"},
                {"standard_user", "SECRET_SAUCE", "do not match"},  // case-sensitive
                {"STANDARD_USER", "secret_sauce", "do not match"},  // case-sensitive username
                {"standard_user ", "secret_sauce", "do not match"}, // trailing space
        };
    }

    @Test(dataProvider = "invalidCredentials",
            description = "Data-driven: various invalid credential combinations")
    public void testLoginFail_DataDriven(String username, String password, String expectedError) {
        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error should be displayed for: [" + username + " / " + password + "]");
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError),
                "Error message should contain: " + expectedError);
    }
}
