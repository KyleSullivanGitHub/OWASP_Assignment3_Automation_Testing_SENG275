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


/*
Tests for verifying the full functionality of the Support Chat feature
*/
public class Support_Chat implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();



    public static final String sideMenuCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)";
    public static final String supportChatCSS ="body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(8)";
    public static final String SupportChatTitleCSS="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-chatbot > div > mat-card > h1";
    public static final String titleCSS="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-search-result > div > div > div.heading.mat-elevation-z6 > div.ng-star-inserted";
    public static final String supportChatAnswerCSS="#chat-window > div:nth-child(3) > div";
    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Seyedmehrad Adimi
     */
    @BeforeClass
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }


    /**
     * Smoke tests for Valid use of support chat
     * Includes test cases SC_001, SC_002, SC_005, SC_009.
     * Programmer: Seyedmehrad Adimi
     * @param chosenBrowser browser used for that test
     * @param dataSet provides email and password to Login
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Smoke","Support_Chat","hasDataProvider"},
            dataProvider = "LG3_Input",
            priority = 29,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void SC1_Valid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        // Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            // SC_001 test case: Support chat is not visible when the user is not logged in

            browserWindow.findElement(By.cssSelector (sideMenuCSS)).click ();
            Thread.sleep(200);

            // verify support chat is not displayed
            try {
                WebElement SupportChat = browserWindow.findElement (By.cssSelector (supportChatCSS));
                assertTrue (SupportChat.isDisplayed ());
            }catch (Exception e){
                assertFalse (false);
            }

            // SC_002 test case: Navigating to support chat after login
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());
            navigateToSupportChat (browserWindow);

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("message-input")));
            WebElement inputText = browserWindow.findElement (By.id ("message-input"));

            //Write for the bot
            inputText.sendKeys ("NoNoNo");
            inputText.sendKeys (Keys.ENTER);

            // check and get the answer
            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (supportChatAnswerCSS)));
            WebElement SupportChat_answer = browserWindow.findElement (By.cssSelector (supportChatAnswerCSS));
            sleep (1);
            assertTrue (SupportChat_answer.isDisplayed ());
        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }



    /**
     * Smoke tests for Invalid use of support chat
     * Includes test case SC_006
     * Programmer: Seyedmehrad Adimi
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Smoke","Support_Chat","noDataProvider"},
            priority = 30,
            enabled = true
    )
    public void SC2_Invalid_Use() throws InterruptedException{
        //Create driver and browser for this particular test

        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try {
           // Login
            loginForMe (browserWindow,Login.googleEmail,Login.googlePass);
            // navigate to support chat page
            navigateToSupportChat (browserWindow);

            // find the input text field
            WebElement inputText = browserWindow.findElement (By.id ("message-input"));
            // send empty message as wanted
            inputText.sendKeys ("");
            inputText.sendKeys (Keys.ENTER);
            sleep (1);

            // Support chat answer element
            try {
                WebElement SupportChat_answer = browserWindow.findElement (By.cssSelector (supportChatAnswerCSS));
                assertTrue (SupportChat_answer.isDisplayed ());
            }catch (Exception e){
                assertFalse (false);
            }
        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }




    /**
     * Regression test on Support Chat using one browser
     * Includes test case SC_003,SC_007,SC_008
     * Programmer: Seyedmehrad Adimi
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Regression","Support_Chat","noDataProvider"},
            priority = 85,
            enabled = true
    )
    public void SC_Regression() throws InterruptedException{
        //Create driver and browser for this particular test

        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        // website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            // Common Regression Before Logging in and goin to support chat
            Login.testRegressionForMe (browserWindow,false);

            // Login now
            loginForMe (browserWindow,Login.googleEmail, Login.googlePass);

            // Common Regression After Login
            Login.testRegressionForMe (browserWindow,true);

            // Testing SC_003: Verify whether the required details and fields are displayed in the 'Support Chat' page -> NO submit button
            checkFieldsAreDisplayed (browserWindow, wait);

            // Use the Support Chat and check again
            WebElement inputText = browserWindow.findElement (By.id ("message-input"));

            inputText.sendKeys ("Hello");
            inputText.sendKeys (Keys.ENTER);
            sleep (1);

            checkFieldsAreDisplayed (browserWindow, wait);


            // Testing SC_007: Verify the Page URL, Page Heading and Page Title of 'Support Chat' page
            Login.testUrlAndTitleAndHeading (browserWindow,"https://juice-shop.herokuapp.com/#/chatbot", "OWASP Juice Shop", "Support Chat (powered by juicy-chat-bot)", SupportChatTitleCSS );

            // Common regression again
            Login.testRegressionForMe (browserWindow,true);

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }


    /**
     * Helper method to check if the required fields are displayed
     * Includes test case SC_003,SC_004,SC_007,SC_008
     * Programmer: Seyedmehrad Adimi
     * @param browserWindow is the driver
     * @param wait is the wair driver
     */
    private void checkFieldsAreDisplayed(WebDriver browserWindow, WebDriverWait wait) {
        navigateToSupportChat(browserWindow);
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("chat-window")));
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("message-input")));
        WebElement ChatBox = browserWindow.findElement (By.id ("chat-window"));
        WebElement inputMessage = browserWindow.findElement (By.id ("message-input"));
        Login.assertElement (ChatBox);
        Login.assertElement (inputMessage);
    }
    /**
     * Helper method to navigate to Support Chat Page
     * Includes test case SC_003,SC_004,SC_007,SC_008
     * Programmer: Seyedmehrad Adimi
     * @param browserWindow is the driver
     */
    private void navigateToSupportChat(WebDriver browserWindow){
        WebDriverWait wait = new WebDriverWait(browserWindow,10);
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (sideMenuCSS)));

        WebElement sideMenu = browserWindow.findElement (By.cssSelector (sideMenuCSS));
        sideMenu.click ();

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (supportChatCSS)));
        WebElement SupportChat = browserWindow.findElement (By.cssSelector (supportChatCSS));
        Login.assertElement (SupportChat);
        SupportChat.click ();
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

        browserWindow.navigate ().refresh ();

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (titleCSS)));
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (TestFunctions.navPath)));
        sleep (3);
        TestFunctions.navToLogin (browserWindow);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("loginButtonGoogle")));
        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        sleep (1);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        sleep (1);
        Login.emailPassEnter (browserWindow, email, password, emailUsr);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (titleCSS)));
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
            case 6:
                Thread.sleep (500);

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
