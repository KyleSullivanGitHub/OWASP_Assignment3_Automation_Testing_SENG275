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

public class AccountSafety implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console
    String website = "https://juice-shop.herokuapp.com"; //default website URL

    TestBrowser environment;
    CreateEnvironment passBrowser;


    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Kyle Sullivan
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
     * @param chosenBrowser browser used for that test
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
        String toggleXpath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-login/div/mat-card/div/mat-form-field[2]/div/div[1]/div[4]/button";

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);

        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        //Quickly fill out login form
        TestFunctions.quickLogFill(browserWindow,TestFunctions.constPassword);
        //Confirm that the password box is of type password, thus only visible as dots
        WebElement passwordField = browserWindow.findElement(By.cssSelector("#password"));
        assertEquals(passwordField.getAttribute("type"),"password");
        //Click on the toggle setting
        passwordField.findElement(By.xpath(toggleXpath)).click();
        //Confirm that the password box has been switched to type text, and now is fully visible
        assertEquals(passwordField.getAttribute("type"),"text");
        //toggle again
        passwordField.findElement(By.xpath(toggleXpath)).click();

        //Confirm that the user cannot copy and paste the password from it's section.
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        if(TestFunctions.OS.contains("win"))
        {
            passwordField.sendKeys(Keys.CONTROL + "a");
            passwordField.sendKeys(Keys.CONTROL + "c");
        }
        else if(TestFunctions.OS.contains("mac"))
        {
            passwordField.sendKeys(Keys.COMMAND + "a");
            passwordField.sendKeys(Keys.COMMAND + "c");
        }
        assertNotEquals(cb.getData(DataFlavor.stringFlavor),TestFunctions.constPassword);
        browserWindow.findElement(By.id ("loginButton")).click (); //click on login


        //password not visible within page description
        //TODO Password visibility in source code

        browserWindow.quit();
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
