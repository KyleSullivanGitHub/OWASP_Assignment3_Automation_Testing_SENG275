package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class AccountSafety
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
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(website);
        TestFunctions.waitForSite(browserWindow);

        TestFunctions.quickLogFill(browserWindow,TestFunctions.constPassword);
        WebElement passwordField = browserWindow.findElement(By.cssSelector("#password"));
        assertEquals(passwordField.getAttribute("type"),"password");
        passwordField.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-login/div/mat-card/div/mat-form-field[2]/div/div[1]/div[4]/button")).click();
        assertEquals(passwordField.getAttribute("type"),"text");
        passwordField.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-login/div/mat-card/div/mat-form-field[2]/div/div[1]/div[4]/button")).click();
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();



        //passowrd copying impossible
        if(TestFunctions.OS.indexOf("win") >=0)
        {
            passwordField.sendKeys(Keys.CONTROL + "a");
            passwordField.sendKeys(Keys.CONTROL + "c");
        }
        else if(TestFunctions.OS.indexOf("mac") >=0)
        {
            passwordField.sendKeys(Keys.COMMAND + "a");
            passwordField.sendKeys(Keys.COMMAND + "c");
        }
        assertNotEquals(cb.getData(DataFlavor.stringFlavor),TestFunctions.constPassword);


        //password not visible within page description

    }

    @Test
    public void AS2_Registration()
    {
        //Password visibility toggled

    }

    @Test
    public void AS3_PasswordRecovery()
    {
        //Passowrd Visibility toggled
    }

    public void passwordVisibility()
    {

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
