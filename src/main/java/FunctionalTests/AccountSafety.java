package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static org.testng.Assert.*;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.spi.ToolProvider;

/**
 * Test classes for the privacy of users during login.
 */
public class AccountSafety implements ITest
{
    private final ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console
    String website = "https://juice-shop.herokuapp.com"; //default website URL

    TestBrowser environment;
    CreateEnvironment passBrowser;


    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @BeforeSuite
    public void SetUp() throws IOException, InterruptedException
    {
        //Create an environment to set up browser specific test environments
        passBrowser = new CreateEnvironment();
        //Create a test environment of the default browser
        environment = passBrowser.createBrowser();
        //Create a constant account to use in tests
        TestFunctions.createAccount();
    }


    /**
     * Smoke test for Login Security within several different browsers
     * Programmer: Kyle Sullivan
     * @param chosenBrowser Browser used for that test
     * @exception IOException Thrown if no browser is selected for the test
     * @exception InterruptedException Thrown if Test is interrupted during a thread waiting period
     * @exception UnsupportedFlavorException Thrown if the clipboard cannot be read
     */
    @Test(
            groups = {"Smoke","Password Security","Password Security Smoke","hasDataProvider"},
            priority = 0,
            dataProvider = "AS1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void AS1_Login(String chosenBrowser) throws IOException, InterruptedException, UnsupportedFlavorException
    {
        //Path to password visibility toggle
        String toggleXpath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-login/div/mat-card/div/mat-form-field[2]/div/div[1]/div[4]/button";

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        //Navigate to Website
        browserWindow.get(website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Quickly fill out login form
            TestFunctions.quickLogFill(browserWindow, TestFunctions.constPassword);
            //Confirm that the password box is of type password, thus only visible as dots
            WebElement passwordField = browserWindow.findElement(By.cssSelector("#password"));
            assertEquals(passwordField.getAttribute("type"), "password");
            //Click on the toggle setting
            passwordField.findElement(By.xpath(toggleXpath)).click();
            //Confirm that the password box has been switched to type text, and now is fully visible
            assertEquals(passwordField.getAttribute("type"), "text");
            //Toggle again
            passwordField.findElement(By.xpath(toggleXpath)).click();

            //Confirm that the user cannot copy and paste the password from it's section.
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            //Try to copy the contents of the password field
            if (TestFunctions.OS.contains("win"))//For copying via Windows
            {
                passwordField.sendKeys(Keys.CONTROL + "a");
                passwordField.sendKeys(Keys.CONTROL + "c");
            }
            else if (TestFunctions.OS.contains("mac"))//For Copying via Mac
            {
                passwordField.sendKeys(Keys.COMMAND + "a");
                passwordField.sendKeys(Keys.COMMAND + "c");
            }
            //Confirm that the clipboard does not contain the password
            assertNotEquals(cb.getData(DataFlavor.stringFlavor), TestFunctions.constPassword);

            browserWindow.findElement(By.id("loginButton")).click(); //click on login

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
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

    /**
     * Returns the name of the test. Used to alter the name of tests performed multiple times
     * Taken from: https://www.swtestacademy.com/change-test-name-testng-dataprovider/
     * Programmer: Canberk Akduygu
     * @return Name of test
     */
    @Override
    public String getTestName()
    {
        return testName.get();
    }
}
