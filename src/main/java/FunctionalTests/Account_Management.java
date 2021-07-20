package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v85.log.Log;
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



/*
Tests for verifying the full functionality of the Account Management feature
*/
public class Account_Management implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();


    public static final String submitButtonCSS = "#card > div > div:nth-child(2) > form:nth-child(3) > button";
    public static final String inputImageUrlCSS = "#url";
    public static final String headingProfileCSS = "#card > div > h1";
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
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Smoke","Account_Management","hasDataProvider"},
            dataProvider = "LG3_Input",
            priority = 35,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void MA1_Valid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        // Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            // MA_001 test case: Verify that the Profile page is hidden while logged out

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (TestFunctions.navPath)));
            browserWindow.findElement(By.cssSelector (TestFunctions.navPath)).click ();

            // Assert Account Management does not exist
            assertFalse (browserWindow.getPageSource ().contains (Login.googleEmail));



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


    /**
     *Smoke tests for Invalid use of support chat
     * Includes test case SC_006
     *Programmer: Seyedmehrad Adimi
     * link: https://ibb.co/6gBdXKJ
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Smoke","Account_Management Smoke","Invalid_Account_Manegement", "hasNoDataProvider"},
            priority = 36
    )
    public void MA2_Update_Profile() throws InterruptedException{
        //Create driver and browser for this particular test

        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();


        //Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);


        // Updating all the fields except photo, cant be uploaded, as discussed with Dr. Popli.
        // Website problem

        try {
            loginForMe (browserWindow,Login.googleEmail,Login.googlePass);

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




    /**
     * Sanity tests for updating Profile page fields -> 1st one
     *Programmer: Seyedmehrad Adimi
     * link: https://ibb.co/6gBdXKJ
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Sanity","Account_Management","noDataProvider"},
            priority = 87,
            enabled = true
    )

    public void MA3_1_Update() throws InterruptedException{
        //Create driver and browser for this particular test

        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();


        //Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

       try {
           /*TC_MA_007 = Verify Uploading image without a file selected*/

           loginForMe (browserWindow,Login.googleEmail,Login.googlePass);

           //Navigate to Account Management
           navToAccountManagement (browserWindow, wait, By.id ("navbarAccount"));

           wait.until (ExpectedConditions.elementToBeClickable (By.cssSelector (submitButtonCSS)));

           // Assert that no file is chosen -> No file is chosen because we are not able to upload any picture
           assertEquals (browserWindow.findElement (By.id("picture")).getAttribute ("aria-label"), "Input for selecting the profile picture");


           // Assert that upload button is greyed out and clickable -> or error message shows up when clicking on the button
           // The test should FAIL
           assertFalse (browserWindow.findElement (By.cssSelector (submitButtonCSS)).isEnabled ());

       }finally {
           Thread.sleep(TestFunctions.endTestWait);
           browserWindow.quit();
       }

    }


    /**
     * Sanity tests for updating Profile page fields -> 2nd one
     *Programmer: Seyedmehrad Adimi
     * link: https://ibb.co/6gBdXKJ
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Sanity","Account_Management_Sanity","hasNoDataProvider"},
            priority = 1
    )

    public void MA3_2_Update() throws InterruptedException{
        //Create driver and browser for this particular test

        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();


        //Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            /*TC_MA_007 = Verify Uploading image without a file selected*/

            loginForMe (browserWindow,Login.googleEmail,Login.googlePass);

            //Navigate to Account Management
            navToAccountManagement (browserWindow, wait, By.id ("navbarAccount"));

            wait.until (ExpectedConditions.elementToBeClickable (By.cssSelector (submitButtonCSS)));


            /*TC_MA_008 = Verify Uploading image without an Image URL set*/
            assertEquals (browserWindow.findElement (By.cssSelector (inputImageUrlCSS)).getText (),"");
            // Assert that upload button is greyed out and clickable -> or error message shows up when clicking on the button
            // The test should FAIL
            assertFalse (browserWindow.findElement (By.cssSelector (submitButtonCSS)).isEnabled ());


        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    /**
     * Sanity tests for updating Profile page fields -> 3rd one
     * Programmer: Seyedmehrad Adimi
     * link: https://ibb.co/6gBdXKJ
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Sanity","Account_Management_Sanity","hasNoDataProvider"},
            priority = 1
    )

    public void MA3_3_Update() throws InterruptedException{
        //Create driver and browser for this particular test

        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();


        //Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            /*TC_MA_007 = Verify Uploading image without a file selected*/

            loginForMe (browserWindow,Login.googleEmail,Login.googlePass);

            //Navigate to Account Management
            navToAccountManagement (browserWindow, wait, By.id ("navbarAccount"));

            wait.until (ExpectedConditions.elementToBeClickable (By.cssSelector (submitButtonCSS)));


            /*TC_MA_009 = Verify uploading image with bad image URL*/
            browserWindow.findElement (By.cssSelector ("#url")).sendKeys ("hadvkadjbc");
            browserWindow.findElement (By.cssSelector ("#submitUrl")).click ();
            wait.until (ExpectedConditions.urlToBe ("https://juice-shop.herokuapp.com/profile/image/url"));
            assertEquals (browserWindow.getCurrentUrl (),"https://juice-shop.herokuapp.com/profile/image/url");
            assertTrue (browserWindow.getPageSource ().contains ("Invalid URI"));



        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }




    /**
     * Regression test for Account Management feature within one browser.
     * Programmer: Seyedmehrad Adimi
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Regression","Account_Management","hasNoDataProvider"},
            priority =87,
            enabled = true
    )

    public void MA_Regression() throws InterruptedException{
        //Create driver and browser for this particular test

        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();


        //Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            Login.testRegressionForMe (browserWindow,false);

            // Login
            loginForMe (browserWindow,Login.googleEmail,Login.googlePass);

            // Navigate to Account Management
            navToAccountManagement (browserWindow, wait, By.id ("navbarAccount"));

            // Login Heading, title and URL
            Login.testUrlAndTitleAndHeading (browserWindow,"https://juice-shop.herokuapp.com/profile","OWASP Juice Shop","User Profile", headingProfileCSS);

            //Checking place holders and buttons
            wait.until (ExpectedConditions.visibilityOfElementLocated (By.id("email")));
            WebElement emailPlaceHolder = browserWindow.findElement (By.id("email"));

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.id("username")));
            WebElement username = browserWindow.findElement (By.id("username"));

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#submit")));
            WebElement submitUsernameBtn = browserWindow.findElement (By.cssSelector ("#submit"));

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#picture")));
            WebElement choosePicButton = browserWindow.findElement (By.cssSelector ("#picture"));

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#card > div > div:nth-child(2) > form:nth-child(3) > button")));
            WebElement UploadPicBtn = browserWindow.findElement (By.cssSelector ("#card > div > div:nth-child(2) > form:nth-child(3) > button"));


            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#url")));
            WebElement linkInput = browserWindow.findElement (By.cssSelector ("#url"));

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#submitUrl")));
            WebElement LinkImageBtn = browserWindow.findElement (By.cssSelector ("#submitUrl"));

            assertTrue (emailPlaceHolder.isDisplayed ());
            Login.assertElement (username);
            Login.assertElement (submitUsernameBtn);
            Login.assertElement (choosePicButton);
            Login.assertElement (linkInput);
            Login.assertElement (LinkImageBtn);
            Login.assertElement (UploadPicBtn);



        }finally {
            Thread.sleep (TestFunctions.endTestWait);
            browserWindow.quit ();
        }
    }
    /**
     * Helper method to navigate to Profile page
     * Programmer: Seyedmehrad Adimi
     * @param browserWindow is the driver
     * @param wait is the wait driver
     * @param navbarAccount is the locator
     */

    private void navToAccountManagement(WebDriver browserWindow, WebDriverWait wait, By navbarAccount) {
        wait.until (ExpectedConditions.visibilityOfElementLocated (navbarAccount));
        browserWindow.findElement (navbarAccount).click ();

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#mat-menu-panel-0 > div > button:nth-child(1)")));
        WebElement AccountManagement = browserWindow.findElement (By.cssSelector ("#mat-menu-panel-0 > div > button:nth-child(1)"));
        AccountManagement.click ();
    }


    /**
     * Helper method to Login
     * Programmer: Seyedmehrad Adimi
     * @param browserWindow is the driver
     * @param email is the email to login
     * @param password is the password to login
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */

    private void loginForMe(WebDriver browserWindow,  String email, String password) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(browserWindow,10);
        browserWindow.get (TestFunctions.website);
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (Complaint.titleCSS)));
        TestFunctions.navToLogin (browserWindow);
        sleep (1);
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("loginButtonGoogle")));
        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        sleep (1);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        sleep (1);
        Login.emailPassEnter (browserWindow, email, password, emailUsr);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (Complaint.titleCSS)));
    }



    /**
     * This is a helper method that helps use Thread.sleep method easily
     * Programmer: Seyedmehrad Adimi
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     **/
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
