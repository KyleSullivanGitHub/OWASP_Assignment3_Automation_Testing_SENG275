package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.testng.Assert.*;

public class Account_Management implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Seyedmehrad Adimi
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }


    /**
     *Smoke tests for Valid use of Account Management
     * Includes test cases MA_001, MA_002, MA_005
     *Programmer: Seyedmehrad Adimi
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    //TODO Check MA_005 and how to apply it
    @Test(
            groups = {"Smoke","Account_Management","hasDataProvider"},
            dataProvider = "LG3_Input",
            priority = 35,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void MA1_Valid_Use(String chosenBrowser, String email, String password) throws InterruptedException, IOException {
        //TODO remove all paramaters except for chosen browser. Use hard coded inputs for smoke tests.
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);

        // MA_001 test case: Verify that the Profile page is hidden while logged out

        browserWindow.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep(200);


        try {
            WebElement AccountManagement = browserWindow.findElement (By.cssSelector ("#mat-menu-panel-0 > div > button:nth-child(1)"));
            assertTrue (AccountManagement.isDisplayed ());
        }catch (Exception e){
            assertFalse (false);
        }

        // MA_002 test case: Verify navigating to 'Profile' page
        loginForMe (browserWindow,email,password);
        Thread.sleep (1000);
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep (1000);

        WebElement AccountManagement = browserWindow.findElement (By.cssSelector ("#mat-menu-panel-0 > div > button:nth-child(1)"));
        AccountManagement.click ();
        Thread.sleep (1000);

        WebElement profileTitle = browserWindow.findElement (By.cssSelector ("#card > div > h1"));
        assertEquals (profileTitle.getText (),"User Profile");


        Thread.sleep (1000);
        browserWindow.quit();
    }






    // TODO How to upload a picture and how to check if the pic is uploaded
    /**
     *Smoke tests for Invalid use of support chat
     * Includes test case SC_006
     *Programmer: Seyedmehrad Adimi
     * @param email email text for test
     * @param password password text for test
     * link: https://ibb.co/6gBdXKJ
     */
    @Test(
            groups = {"Smoke","Account_Management","noDataProvider"},
            dataProvider = "LG3_Input",
            priority = 36,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void MA2_Update_Profile(String email, String password) throws InterruptedException, IOException {
        //TODO remove all paramaters. Smoke tests use hard coded inptus
        //Browser setup
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);


        loginForMe (browserWindow,email,password);
        Thread.sleep (1000);
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep (1000);

        WebElement AccountManagement = browserWindow.findElement (By.cssSelector ("#mat-menu-panel-0 > div > button:nth-child(1)"));
        AccountManagement.click ();
        Thread.sleep (1000);

        WebElement profileUserName = browserWindow.findElement (By.id ("username"));
        Thread.sleep (1000);
        profileUserName.sendKeys ("IamHelloWorld");
        WebElement setUsernameBtn = browserWindow.findElement (By.cssSelector ("#submit"));
        Thread.sleep (1000);
        setUsernameBtn.click ();
        Thread.sleep (1000);
        WebElement checkUsername = browserWindow.findElement (By.cssSelector ("#card > div > div:nth-child(2) > p"));
        assertEquals (checkUsername.getText (), "\\IamHelloWorld");



        WebElement profilePicUrl = browserWindow.findElement (By.id ("url"));
        Thread.sleep (1000);
        profilePicUrl.sendKeys ("https://ibb.co/6gBdXKJ");
        Thread.sleep (1000);

        WebElement setPicture = browserWindow.findElement (By.cssSelector ("#submitUrl"));
        Thread.sleep (1000);
        setPicture.click ();
        Thread.sleep (1000);





        Thread.sleep(2500);
        browserWindow.quit();
    }


    @Test(
            groups = {"Regression","Account_Management","noDataProvider"},
            priority = 87,
            enabled = true
    )

    public void MA3_Update(String email, String password) throws InterruptedException, IOException {
        //TODO one regression per test class

    }

    @Test(
            groups = {"Regression","Account_Management","hasDataProvider"},
            priority =87 ,
            enabled = true
    )

    public void MA_Regression(String email, String password) throws InterruptedException, IOException {
        //TODO no paramaters on regressions. It runs once, and does all types of inputs sequentially.
    }

    private void loginForMe(WebDriver browserWindow,  String email, String password) throws InterruptedException{
        browserWindow.get (TestFunctions.website);
        Thread.sleep(500);
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep(500);



        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector(TestFunctions.navbarLogin));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        Thread.sleep(1000);



        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        Thread.sleep(1000);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        Thread.sleep(1000);
        Login.emailPassEnter (browserWindow, email, password, emailUsr);
        Thread.sleep(1000);
    }

    private static void sleep(int a) throws InterruptedException {

        switch (a) {
            case 1:
                Thread.sleep (1000);
                break;
            case 2:
                Thread.sleep (2000);
                break;
            case 3:
                Thread.sleep (3000);
                break;
            case 4:
                Thread.sleep (4000);
                break;
            case 5:
                Thread.sleep (5000);
                break;

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
    @BeforeMethod(onlyForGroups = {"noDataProvider"})
    public void BeforeMethod(Method method)
    {
        //Set name to (method name)
        testName.set(method.getName());
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
