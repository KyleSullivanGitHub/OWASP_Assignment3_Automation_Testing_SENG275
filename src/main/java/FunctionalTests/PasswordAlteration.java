package FunctionalTests;

import Setup.*;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.v85.browser.Browser;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static FunctionalTests.TestFunctions.*;
import static org.testng.Assert.*;

public class PasswordAlteration implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();

    TestBrowser environment;
    CreateEnvironment passBrowser;

    String newValidPassword;
    String newInvalidPassword;

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Salam Fazil
     */
    @BeforeSuite
    public void SetUp() throws IOException, InterruptedException {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();

        TestFunctions.createAccount();

        newValidPassword = "123456";
        newInvalidPassword = "1234";
    }

    /*
    TODO:
    PA1 - Smoke - Valid password recovery from log in - STATUS: COMPLETED (needs review)
    PA2 - Smoke - Invalid password recovery from log in - STATUS: COMPLETED (needs review)
    PA3 - Sanity - Valid password reset (from change password page) - STATUS: COMPLETED (needs review)
    PA4 - Sanity - Invalid password reset (from change password page) - STATUS: COMPLETED (needs review)
    PA - Regression - STATUS: Not done, complete at end
     */

    /**
     * Smoke test for valid password recovery from log in page
     * Programmer: Salam Fazil
     * @param chosenBrowser
     */
    @Test(
            groups = {"Smoke","PasswordAlteration","PA_Smoke","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void PA1_validPasswordReset(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Navigate to 'forgot password' page:
        navigateToForgotPassword(browserWindow);

        // Fill in form with data and invalid new password
        fillForgotPasswordData(browserWindow, newValidPassword);

        // If fields were successfully filled out, proceed to reset
        assertTrue(browserWindow.findElement(By.id("resetButton")).isEnabled());
        browserWindow.findElement(By.id("resetButton")).click();

        // Login with new password to validate that it was reset
        logIn(browserWindow, constEmail, constPassword);

        // Validate logged in successfully
        validateLoggedIn(browserWindow);
    }

    /**
     * Smoke test for invalid password recovery from log in page
     * Programmer: Salam Fazil
     * @param chosenBrowser browser being used for test
     */
    @Test(
            groups = {"Smoke","PasswordAlteration","PA_Smoke","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void PA2_invalidPasswordReset(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Navigate to 'forgot password' page:
        navigateToForgotPassword(browserWindow);

        // Fill in form with data and invalid new password
        fillForgotPasswordData(browserWindow, newInvalidPassword);

        // If fields were successfully filled out, verify that rest button is disabled
        assertFalse(browserWindow.findElement(By.id("resetButton")).isEnabled());
    }

    /**
     * Sanity test for valid password reset from change password page
     * Programmer: Salam Fazil
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Sanity","PasswordAlteration","PA_Sanity","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void PA3_validPasswordReset(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Log in
        logIn(browserWindow, constEmail, constPassword);

        // Change password and store whether it was successful or not in result
        boolean result = changePassword(browserWindow, constPassword, newValidPassword);
        assertTrue(result); // Should be successful since entered valid password

        // Log out then log back in with new password
        logOut(browserWindow);
        logIn(browserWindow, constEmail, newValidPassword);
        Thread.sleep(1000);

        // Validate logged in successfully
        validateLoggedIn(browserWindow);
        Thread.sleep(1000);

        // Reset password back to original for next browser
        changePassword(browserWindow, newValidPassword, constPassword);
    }

    /**
     * Sanity test for invalid password reset from change password page
     * Programmer: Salam Fazil
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Sanity","PasswordAlteration","PA_Sanity","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void PA4_invalidPasswordReset(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Log in
        logIn(browserWindow, constEmail, constPassword);

        // Change password and store whether it was successful or not in result
        boolean result = changePassword(browserWindow, constPassword, newInvalidPassword);
        assertFalse(result); // Should not be successful since entered invalid password
    }

    private static boolean changePassword(WebDriver browserWindow, String currPassword, String newPassword) throws InterruptedException {

        navigateToChangePassword(browserWindow);

        browserWindow.findElement(By.id("currentPassword")).sendKeys(currPassword);
        browserWindow.findElement(By.id("newPassword")).sendKeys(newPassword);
        browserWindow.findElement(By.id("newPasswordRepeat")).sendKeys(newPassword);

        WebElement submitBtn = browserWindow.findElement(By.id("changeButton"));

        if (submitBtn.isEnabled()){
            submitBtn.click();
            return true;
        }else{
            browserWindow.navigate().refresh();
            return false;
        }

    }

    private static void logIn(WebDriver browserWindow, String email, String password){
        navigateToLogin(browserWindow);

        browserWindow.findElement(By.id("email")).sendKeys(email);
        browserWindow.findElement(By.id("password")).sendKeys(password);

        browserWindow.findElement(By.id("loginButton")).click();

    }

    private static void fillForgotPasswordData(WebDriver browserWindow, String password){

        try {
            browserWindow.findElement(By.id("email")).sendKeys(constEmail);
            browserWindow.findElement(By.id("securityAnswer")).sendKeys(constAnswer);
            browserWindow.findElement(By.id("newPassword")).sendKeys(password);
            browserWindow.findElement(By.id("newPasswordRepeat")).sendKeys(password);
        }catch (ElementNotInteractableException e){
            // If any of the fields do not allow us to enter text, test fails
            fail();
        }

    }

    private static void validateLoggedIn(WebDriver browserWindow){
        browserWindow.findElement(By.id("navbarAccount")).click();

        WebElement emailContainer = browserWindow.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(1) > span"));
        assertEquals(emailContainer.getText(), constEmail);

        browserWindow.navigate().refresh();
    }

    private static void navigateToForgotPassword(WebDriver browserWindow) throws InterruptedException {

        // Go to log in page
        navigateToLogin(browserWindow);

        // Click on 'forgot your password' (Need to do it this way because it is being overlapped)
        WebElement forgotPasswordLinkContainer = browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/" +
                "mat-sidenav-content/app-login/div/mat-card/div/a"));

        JavascriptExecutor jse = (JavascriptExecutor)browserWindow;
        jse.executeScript("arguments[0].click();", forgotPasswordLinkContainer);
    }

    private static void navigateToLogin(WebDriver browserWindow){
        browserWindow.findElement(By.id("navbarAccount")).click();
        browserWindow.findElement(By.id("navbarLoginButton")).click();
    }

    private static void logOut(WebDriver browserWindow){
        browserWindow.findElement(By.id("navbarAccount")).click();
        browserWindow.findElement(By.id("navbarLogoutButton")).click();
    }

    private static void navigateToChangePassword(WebDriver browserWindow) throws InterruptedException {
        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        Actions action = new Actions(browserWindow);
        WebElement pAndSTab;
        try {
            pAndSTab = browserWindow.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(3)"));
        }catch (NoSuchElementException e){
            pAndSTab = browserWindow.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div/button[3]/span"));
        }
        action.moveToElement(pAndSTab).perform();
        Thread.sleep(1000);

        browserWindow.findElement(By.cssSelector("#mat-menu-panel-2 > div > button:nth-child(5)")).click();
    }

    @BeforeMethod(onlyForGroups = {"hasDataProvider"})
    public void BeforeMethod(Method method, Object[] testData)
    {
        testName.set(method.getName()+"_"+testData[0]);
    }

    @Override
    public String getTestName()
    {
        return testName.get();
    }
}
