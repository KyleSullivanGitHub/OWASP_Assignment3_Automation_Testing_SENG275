package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Salam Fazil
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }

    /*
    TODO:
    PA1
    PA2
    PA3
    (password recovery tests)
     */


    /**
     * Sanity test for valid password reset
     * Programmer: Salam Fazil
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Sanity","PasswordAlteration","PA_Sanity","hasDataProvider"},
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3
    )

    public void PA4_validReset(String chosenBrowser, String email, String password, String answer) throws IOException, InterruptedException
    {
        // Reset password to this
        String newPass = "123456";

        // Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(website);
        Thread.sleep(1000);

        // Dismiss the initial pop up
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > " +
                "app-welcome-banner > div > div:nth-child(3) > " +
                "button.mat-focus-indicator.close-dialog." +
                "mat-raised-button.mat-button-base.mat-primary.ng-star-inserted")).click();
        Thread.sleep(1000);

        // Create an account with random email and password
        //register(browserWindow, email, password, answer);
        Thread.sleep(1000);

        // Log in to created account
        //logIn(browserWindow, email, password);
        Thread.sleep(1000);

        // Initiate password reset
        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        Actions action = new Actions(browserWindow);
        WebElement privacyAndSecurityTab = browserWindow.findElement(By.xpath(
                "/html/body/div[3]/div[2]/div/div/div/button[3]"));
        action.moveToElement(privacyAndSecurityTab).perform();
        Thread.sleep(1000);

        browserWindow.findElement(By.cssSelector("#mat-menu-panel-2 > div > button:nth-child(5)")).click();
        Thread.sleep(1000);

        //addText(browserWindow, "currentPassword", password);

        //addText(browserWindow, "newPassword", newPass);

        //addText(browserWindow, "newPasswordRepeat", newPass);
        Thread.sleep(1000);

        browserWindow.findElement(By.id("changeButton")).click();
        Thread.sleep(1000);

        // Log out after changing password (so we can log in again to see if it works)
        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        browserWindow.findElement(By.id("navbarLogoutButton")).click();
        Thread.sleep(1000);

        // Try to log in with new password
        //logIn(browserWindow, email, newPass);
        Thread.sleep(1000);

        // Check if logged in email = same email. If log in fails, test will fail
        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        WebElement emailContainer = browserWindow.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(1) > span"));

        assertEquals(emailContainer.getText(), email);
    }

    /**
     * Sanity test for invalid password reset
     * Programmer: Salam Fazil
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Sanity","PasswordAlteration","PA_Sanity","hasDataProvider"},
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3
    )

    public void PA5_invalidReset(String chosenBrowser, String email, String password, String answer) throws IOException, InterruptedException
    {
        // Reset password to this
        String newPass = "1234";

        // Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(website);
        Thread.sleep(1000);

        // Dismiss the initial pop up
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > " +
                "app-welcome-banner > div > div:nth-child(3) > " +
                "button.mat-focus-indicator.close-dialog." +
                "mat-raised-button.mat-button-base.mat-primary.ng-star-inserted")).click();
        Thread.sleep(1000);

        // Create an account with random email and password
        //register(browserWindow, email, password, answer);
        Thread.sleep(1000);

        // Log in to created account
        //logIn(browserWindow, email, password);
        Thread.sleep(1000);

        // Initiate password reset
        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        Actions action = new Actions(browserWindow);
        WebElement privacyAndSecurityTab = browserWindow.findElement(By.xpath(
                "/html/body/div[3]/div[2]/div/div/div/button[3]"));
        action.moveToElement(privacyAndSecurityTab).perform();
        Thread.sleep(1000);

        browserWindow.findElement(By.cssSelector("#mat-menu-panel-2 > div > button:nth-child(5)")).click();
        Thread.sleep(1000);

        //addText(browserWindow, "currentPassword", password);

        //addText(browserWindow, "newPassword", newPass);

        browserWindow.findElement(By.id("newPasswordRepeat")).click();
        Thread.sleep(1000);

        WebElement invalidPassErrorMsgContainer = browserWindow.findElement(By.id("mat-error-16"));
        assertEquals(invalidPassErrorMsgContainer.getText(), "Password must be 5-20 characters long.");
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
