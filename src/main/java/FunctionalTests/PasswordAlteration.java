package FunctionalTests;

import Setup.*;
import org.openqa.selenium.*;
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

    /**
     * Smoke test for valid password recovery from log in page
     * Programmer: Salam Fazil
     * @param chosenBrowser browser being used for test
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

        try {
            // Navigate to 'forgot password' page:
            navigateToForgotPassword(browserWindow);

            // Fill in form with data and invalid new password
            Thread.sleep(1000);
            fillForgotPasswordData(browserWindow, newValidPassword);

            // If fields were successfully filled out, proceed to reset
            assertTrue(browserWindow.findElement(By.id("resetButton")).isEnabled());
            browserWindow.findElement(By.id("resetButton")).click();

            // Login with new password to validate that it was reset
            logIn(browserWindow, constEmail, constPassword);

            // Validate logged in successfully
            validateLoggedIn(browserWindow);

        } finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Smoke test for invalid password recovery from log in page
     * Programmer: Salam Fazil
     */
    @Test(
            groups = {"Smoke","PasswordAlteration","PA_Smoke","noDataProvider"}
    )

    public void PA2_invalidPasswordReset() throws InterruptedException {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try {
            // Navigate to 'forgot password' page:
            navigateToForgotPassword(browserWindow);

            // Fill in form with data and invalid new password
            fillForgotPasswordData(browserWindow, newInvalidPassword);

            // If fields were successfully filled out, verify that rest button is disabled
            assertFalse(browserWindow.findElement(By.id("resetButton")).isEnabled());

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test for valid password reset from change password page
     * Programmer: Salam Fazil
     */
    @Test(
            groups = {"Sanity","PasswordAlteration","PA_Sanity","noDataProvider"}
    )

    public void PA3_validPasswordReset() throws InterruptedException {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try {
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

        } finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test for invalid password reset from change password page
     * Programmer: Salam Fazil
     */
    @Test(
            groups = {"Sanity","PasswordAlteration","PA_Sanity","noDataProvider"}
    )

    public void PA4_invalidPasswordReset() throws InterruptedException {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try {
            // Log in
            logIn(browserWindow, constEmail, constPassword);

            // Change password and store whether it was successful or not in result
            boolean result = changePassword(browserWindow, constPassword, newInvalidPassword);
            assertFalse(result); // Should not be successful since entered invalid password

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    @Test(
            groups = {"Regression","PasswordAlteration","PA_Regression","noDataProvider"}
    )

    public void PA_regressionForgetPasswordPage() throws InterruptedException {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try {
            // Go to forgot password page
            navigateToForgotPassword(browserWindow);

            // Verify URL, title, and page heading
            String url = browserWindow.getCurrentUrl();
            String title = browserWindow.getTitle();
            String heading = browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > " +
                    "mat-sidenav-content > app-forgot-password > div > mat-card > h1")).getText();

            assertEquals(url, "https://juice-shop.herokuapp.com/#/forgot-password");
            assertEquals(title, "OWASP Juice Shop");
            assertEquals(heading, "Forgot Password");

            // Verify top bar elements exist (logged out)
            verifyTopBarElements(browserWindow, false);

            // Verify place holder text
            WebElement emailContainer = browserWindow.findElement(By.id("email"));
            emailContainer.click();
            String emailPlaceholder = emailContainer.getAttribute("placeholder");

            WebElement securityQuestionContainer = browserWindow.findElement(By.id("securityAnswer"));
            securityQuestionContainer.click();
            String securityQuestionPlaceholder = securityQuestionContainer.getAttribute("placeholder");

            WebElement newPasswordContainer = browserWindow.findElement(By.id("newPassword"));
            newPasswordContainer.click();
            String newPasswordPlaceholder = newPasswordContainer.getAttribute("placeholder");

            WebElement newPasswordRepeatContainer = browserWindow.findElement(By.id("newPasswordRepeat"));
            newPasswordRepeatContainer.click();
            String newPasswordRepeatPlaceholder = newPasswordRepeatContainer.getAttribute("placeholder");

            assertEquals(emailPlaceholder, "Enter your email");
            assertEquals(securityQuestionPlaceholder, "Enter Security Question");
            assertEquals(newPasswordPlaceholder, "Enter a New Password");
            assertEquals(newPasswordRepeatPlaceholder, "Repeat New Password");

            // Verify password entered is hidden
            assertEquals(newPasswordContainer.getAttribute("type"), "password");
            assertEquals(newPasswordRepeatContainer.getAttribute("type"), "password");

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    @Test(
            groups = {"Regression","PasswordAlteration","PA_Regression","noDataProvider"}
    )

    public void PA_regressionChangePasswordPage() throws InterruptedException {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try {
            // Log in then go to change password page
            logIn(browserWindow, constEmail, constPassword);
            navigateToChangePassword(browserWindow);

            // Verify URL, title, and page heading
            String url = browserWindow.getCurrentUrl();
            String title = browserWindow.getTitle();
            String heading = browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > " +
                    "mat-sidenav-content > app-privacy-security > mat-sidenav-container > mat-sidenav-content > " +
                    "app-change-password > div > mat-card > h1")).getText();

            assertEquals(url, "https://juice-shop.herokuapp.com/#/privacy-security/change-password");
            assertEquals(title, "OWASP Juice Shop");
            assertEquals(heading, "Change Password");

            // Verify top bar elements exist (logged in)
            verifyTopBarElements(browserWindow, true);

            // Verify place holder text
            WebElement currentPasswordContainer = browserWindow.findElement(By.id("currentPassword"));
            currentPasswordContainer.click();
            String currentPasswordPlaceholder = currentPasswordContainer.getAttribute("placeholder");

            WebElement newPasswordContainer = browserWindow.findElement(By.id("newPassword"));
            newPasswordContainer.click();
            String newPasswordPlaceholder = newPasswordContainer.getAttribute("placeholder");

            WebElement newPasswordRepeatContainer = browserWindow.findElement(By.id("newPasswordRepeat"));
            newPasswordRepeatContainer.click();
            String newPasswordRepeatPlaceholder = newPasswordRepeatContainer.getAttribute("placeholder");

            assertEquals(currentPasswordPlaceholder, "Please provide your current password.");
            assertEquals(newPasswordPlaceholder, "");
            assertEquals(newPasswordRepeatPlaceholder, "");

            // Verify password entered is hidden
            assertEquals(newPasswordContainer.getAttribute("type"), "password");
            assertEquals(newPasswordRepeatContainer.getAttribute("type"), "password");

        } finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /* Start of helper methods */

    public static void verifyTopBarElements(WebDriver browserWindow, boolean loggedIn){
        boolean logoExists = !browserWindow.findElements(By.cssSelector("body > app-root > div > mat-sidenav-container > " +
                "mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(2) > " +
                "span.mat-button-wrapper > img")).isEmpty();

        boolean searchBtnExists = !browserWindow.findElements(By.cssSelector("#searchQuery")).isEmpty();
        boolean accountBtnExists = !browserWindow.findElements(By.cssSelector("#navbarAccount")).isEmpty();

        boolean languageBtnExists = !browserWindow.findElements(By.cssSelector("body > app-root > div > " +
                "mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > " +
                "button.mat-focus-indicator.mat-tooltip-trigger.mat-menu-trigger.buttons.mat-button.mat-button-base")).isEmpty();

        boolean openSideNavBtnExists = !browserWindow.findElements(By.cssSelector("body > app-root > div > " +
                "mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row " +
                "> button:nth-child(1)")).isEmpty();

        assertTrue(logoExists);
        assertTrue(searchBtnExists);
        assertTrue(accountBtnExists);
        assertTrue(languageBtnExists);
        assertTrue(openSideNavBtnExists);

        if (loggedIn){
            boolean yourBasketBtnExists = !browserWindow.findElements(By.cssSelector("body > app-root > div > " +
                    "mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > " +
                    "button.mat-focus-indicator.buttons.mat-button.mat-button-base.ng-star-inserted")).isEmpty();

            assertTrue(yourBasketBtnExists);
        }
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

    private static void navigateToForgotPassword(WebDriver browserWindow){

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

    /**
     * Method for changing the name of tests performed multiple times by adding the first value in their data provider to the end of their names
     * Taken from: https://www.swtestacademy.com/change-test-name-testng-dataprovider/
     * Programmer: Canberk Akduygu
     * @param method Test method whose name is to be changed
     * @param testData The data parameters for the method
     */
    @BeforeMethod(onlyForGroups = {"hasDataProvider"})
    public void BeforeMethod(Method method, Object[] testData)
    {
        //Set name to (method name)_(first value in data provider)
        testName.set(method.getName() + "_" + testData[0]);
    }

    @BeforeMethod(onlyForGroups = {"noDataProvider"})
    public void BeforeMethod(Method method)
    {
        //Set name to (method name)
        testName.set(method.getName());
    }

    @Override
    public String getTestName()
    {
        return testName.get();
    }
}
