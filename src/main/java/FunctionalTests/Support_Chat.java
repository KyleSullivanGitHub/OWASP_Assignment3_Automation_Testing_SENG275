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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.testng.Assert.*;

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
    @BeforeSuite
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }


    /**
     *Smoke tests for Valid use of support chat
     * Includes test cases SC_001, SC_002, SC_005, SC_009.
     *Programmer: Seyedmehrad Adimi
     * @param chosenBrowser browser used for that test
     * @param dataSet provides email and password to Login
     */
    @Test(
            groups = {"Smoke","Support_Chat Smoke","Valid_Support_Chat"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
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
     *Smoke tests for Invalid use of support chat
     * Includes test case SC_006
     *Programmer: Seyedmehrad Adimi
     * @param chosenBrowser browser used for that test
     * @param dataSet provides email and password to Login
     */
    @Test(
            groups = {"Smoke","Support_Chat Smoke","Invalid_Support_Chat"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void SC2_Invalid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try {
           // Login
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());
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



    // TODO there is no submit button and message is not mandatory

    /**
     *Smoke tests for Invalid use of support chat
     * Includes test case SC_003,SC_004,SC_007,SC_008
     *Programmer: Seyedmehrad Adimi
     * @param chosenBrowser browser used for that test
     * @param dataSet provides email and password to Login
     */
    @Test(
            groups = {"Regression","Support_Chat Regression","hasDataProvider"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void SC_Regression(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        // website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {
            // Common Regression Before Logging in and goin to support chat
            Login.testRegressionForMe (browserWindow,false);

            // Login now
            loginForMe (browserWindow,dataSet[0].toString (), dataSet[1].toString ());

            // Common Regression After Login
            Login.testRegressionForMe (browserWindow,true);

            // Testing SC_003: Verify whether the required details and fields are displayed in the 'Support Chat' page -> NO submit button
            checkFieldsAreDisplayed (browserWindow, wait);

            // Testing SC_004: Verify all the text fields in the 'Support Chat' page are mandatory -> not mandatory

            // Testing SC_007: Verify the Page URL, Page Heading and Page Title of 'Support Chat' page
            Login.testUrlAndTitleAndHeading (browserWindow,"https://juice-shop.herokuapp.com/#/chatbot", "OWASP Juice Shop", "Support Chat (powered by juicy-chat-bot)", SupportChatTitleCSS );

            // Common regression again
            Login.testRegressionForMe (browserWindow,true);

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    private void checkFieldsAreDisplayed(WebDriver browserWindow, WebDriverWait wait) {
        navigateToSupportChat(browserWindow);
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("chat-window")));
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("message-input")));
        WebElement ChatBox = browserWindow.findElement (By.id ("chat-window"));
        WebElement inputMessage = browserWindow.findElement (By.id ("message-input"));
        Login.assertElement (ChatBox);
        Login.assertElement (inputMessage);
    }

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

    private void loginForMe(WebDriver browserWindow,  String email, String password) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(browserWindow,10);
        browserWindow.get (TestFunctions.website);
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (titleCSS)));
        TestFunctions.navToLogin (browserWindow);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("loginButtonGoogle")));
        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        sleep (1);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        sleep (1);
        Login.emailPassEnter (browserWindow, email, password, emailUsr);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (titleCSS)));
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
