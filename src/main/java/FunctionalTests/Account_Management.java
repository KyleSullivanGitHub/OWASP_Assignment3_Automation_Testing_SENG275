package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
     * @param dataSet provides email and password text for test
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

        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            // MA_001 test case: Verify that the Profile page is hidden while logged out

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (TestFunctions.navPath)));
            browserWindow.findElement(By.cssSelector (TestFunctions.navPath)).click ();


            try {
                WebElement AccountManagement = browserWindow.findElement (By.cssSelector ("#mat-menu-panel-0 > div > button:nth-child(1)"));
                assertTrue (AccountManagement.isDisplayed ());
            }catch (Exception e){
                assertFalse (false);
            }

            Actions ESCAPE = new Actions (browserWindow);
            ESCAPE.sendKeys (Keys.ESCAPE).perform ();

            // MA_002 test case: Verify navigating to 'Profile' page

            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());

            navToAccountManagement (browserWindow, wait, By.cssSelector (TestFunctions.navPath));

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#card > div > h1")));
            WebElement profileTitle = browserWindow.findElement (By.cssSelector ("#card > div > h1"));
            assertEquals (profileTitle.getText (),"User Profile");
        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }


    }


    // TODO How to upload a picture and how to check if the pic is uploaded
    /**
     *Smoke tests for Invalid use of support chat
     * Includes test case SC_006
     *Programmer: Seyedmehrad Adimi
     * @param dataSet provides email and password text for test
     * @param chosenBrowser browser used for that test
     * @param email email text for test
     * @param password password text for test
     * link: https://ibb.co/6gBdXKJ
     */
    @Test(
            groups = {"Smoke","Account_Management Smoke","Invalid_Account_Manegement"},
            dataProvider = "LG3_Input",
            priority = 36,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void MA2_Update_Profile(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);


        try {
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());

            //Navigate to Account Management
            navToAccountManagement (browserWindow, wait, By.id ("navbarAccount"));

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("username")));
            WebElement profileUserName = browserWindow.findElement (By.id ("username"));

            profileUserName.clear ();
            profileUserName.sendKeys ("IamHelloWorld");

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#submit")));
            WebElement setUsernameBtn = browserWindow.findElement (By.cssSelector ("#submit"));
            setUsernameBtn.click ();

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#card > div > div:nth-child(2) > p")));
            WebElement checkUsername = browserWindow.findElement (By.cssSelector ("#card > div > div:nth-child(2) > p"));
            assertEquals (checkUsername.getText (), "\\IamHelloWorld");



            WebElement profilePicUrl = browserWindow.findElement (By.id ("url"));
            Thread.sleep (1000);
            profilePicUrl.sendKeys ("https://ibb.co/6gBdXKJ");


            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#submitUrl")));
            WebElement setPicture = browserWindow.findElement (By.cssSelector ("#submitUrl"));

            setPicture.click ();

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }


    }

    private void navToAccountManagement(WebDriver browserWindow, WebDriverWait wait, By navbarAccount) {
        wait.until (ExpectedConditions.visibilityOfElementLocated (navbarAccount));
        browserWindow.findElement (navbarAccount).click ();

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#mat-menu-panel-0 > div > button:nth-child(1)")));
        WebElement AccountManagement = browserWindow.findElement (By.cssSelector ("#mat-menu-panel-0 > div > button:nth-child(1)"));
        AccountManagement.click ();
    }


    @Test(
            groups = {"Regression","Account_Management","noDataProvider"},
            priority = 87,
            enabled = true
    )

    public void MA3_Update(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
    public void MA3_Update(String email, String password) throws InterruptedException, IOException {
        //TODO one regression per test class

    }

    @Test(
            groups = {"Regression","Account_Management","hasDataProvider"},
            priority =87 ,
            enabled = true
    )

    public void MA_Regression(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            Login.testRegressionForMe (browserWindow,false);

            // Login
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());

            // Navigate to Account Management
            navToAccountManagement (browserWindow, wait, By.id ("navbarAccount"));

            //

        }finally {
            Thread.sleep (TestFunctions.endTestWait);
            browserWindow.quit ();
        }
    }

    private void loginForMe(WebDriver browserWindow,  String email, String password) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(browserWindow,10);
        browserWindow.get (TestFunctions.website);
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (Complaint.titleCSS)));
        TestFunctions.navToLogin (browserWindow);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("loginButtonGoogle")));
        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        sleep (1);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        sleep (1);
        Login.emailPassEnter (browserWindow, email, password, emailUsr);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (Complaint.titleCSS)));
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
