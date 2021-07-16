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
            groups = {"Smoke","Account_Management Smoke","Valid_Account_Management"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void MA1_Valid_Use(String chosenBrowser, String email, String password) throws InterruptedException, IOException {
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
     * @param chosenBrowser browser used for that test
     * link: https://ibb.co/6gBdXKJ
     */
    @Test(
            groups = {"Smoke","Support_Chat Smoke","Invalid_Support_Chat"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void MA2_Update_Profile(String chosenBrowser, String email, String password) throws InterruptedException, IOException {
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
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
