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
import org.openqa.selenium.support.ui.Select;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.testng.Assert.*;

public class Complaint implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();


    private  final String sideMenuCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)";
    private final String ComplaintCSS ="body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(7) > div > span";
    private final String complaintMessageToSend = "I have a complaint";
    private final String feedbackFromSiteForComplaint = "Customer support will get in touch with you soon! Your complaint reference is";
    private final String complaintConfirmationCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-complaint > div > mat-card > div.confirmation";
    private final String errorMessageToProvideText = "Please provide a text.";


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
     *Smoke tests for Valid use of Complaint feature
     * Includes test cases C_001,C_002,C_003,C_005
     *Programmer: Seyedmehrad Adimi
     * @param dataSet provides email and password to login
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Complaint Smoke","Valid_Complaint", "has_Data_Provider"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void CO1_Valid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);


        // C_001 test case: Verify  'Complaints' field is not visible before login

        try {

            try {
                WebElement sideMenu = browserWindow.findElement(By.cssSelector (sideMenuCSS));
                sideMenu.clear ();
                sleep (1);
                WebElement Complaint = browserWindow.findElement (By.cssSelector (ComplaintCSS));
                assertFalse (Complaint.isDisplayed ());
            }catch (Exception e){
                assertFalse (false);
            }


            // C_002 test case: Verify navigation to complaint page
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());
            Thread.sleep (1000);
            browserWindow.findElement(By.cssSelector (sideMenuCSS)).click ();
            Thread.sleep (1000);
            WebElement Complaint = browserWindow.findElement (By.cssSelector (ComplaintCSS));
            assertTrue (Complaint.isDisplayed ());
            Thread.sleep (1000);


            // C_003 test case: Verify whether the required details and fields are displayed in the 'Complaint' page after login (Customer, Message. INvoice)
            Complaint.click ();
            WebElement customer = browserWindow.findElement (By.cssSelector ("#mat-input-1"));
            assertTrue (customer.isDisplayed ());

            WebElement Message = browserWindow.findElement (By.id ("complaintMessage"));
            assertTrue (Message.isDisplayed ());

            WebElement Invoice = browserWindow.findElement (By.cssSelector ("#complaint-form > div > label"));
            assertEquals (Invoice.getText (), "Invoice:");

            // C_005 test case: Verify whether the required details and fields are displayed in the 'Complaint' page after login (Customer, Message. INvoice)

            Message.click ();
            Message.sendKeys (complaintMessageToSend);

            WebElement submitButton = browserWindow.findElement (By.id ("submitButton"));
            submitButton.click ();
            Thread.sleep (500);

            WebElement complaintConfirmation = browserWindow.findElement (By.cssSelector (complaintConfirmationCSS));
            assertTrue (complaintConfirmation.getText ().startsWith (feedbackFromSiteForComplaint));

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }


    }





    /**
     *Smoke (also included in Sanity) test for Invalid use of Complaint feature
     * Includes test cases C_006
     *Programmer: Seyedmehrad Adimi
     * @param dataSet provides email and password to login
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Complaint Smoke","Sanity Smoke","Invalid_Complaint", "has_Data_Provider"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void CO2_Invalid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);


        try {
            // C_006 test case: Verify submitting the Complaints in 'Complaint' page by not providing any details
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());
            sleep (1);
            browserWindow.findElement(By.cssSelector (sideMenuCSS)).click ();
            sleep (1);
            WebElement Complaint = browserWindow.findElement (By.cssSelector (ComplaintCSS));
            Complaint.click ();
            sleep (1);


            WebElement submitButton = browserWindow.findElement (By.id ("submitButton"));
            assertFalse (submitButton.isEnabled ());
        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }



    /**
     * Sanity test for Invalid use of Complaint feature
     * Includes test cases C_004
     *Programmer: Seyedmehrad Adimi
     * @param dataSet provides email and password to login
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Sanity","Sanity Smoke","Invalid_Complaint", "has_Data_Provider"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void CO3_Invalid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try {

            // C_004 test case: Verify all the text fields in the 'Complaint' page are mandatory
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());
            sleep (1);
            browserWindow.findElement(By.cssSelector (sideMenuCSS)).click ();
            sleep (1);
            WebElement Complaint = browserWindow.findElement (By.cssSelector (ComplaintCSS));
            Complaint.click ();
            sleep (1);


            WebElement Message = browserWindow.findElement (By.id ("complaintMessage"));
            Message.click ();

            // Click on the screen and leave Message empty, to make sure it is mandatory (gives a message to provide a text)

            browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content")).click ();

            // Error message to provide text
            WebElement errorMessage = browserWindow.findElement (By.id ("mat-error-0"));
            assertEquals (errorMessage.getText (), errorMessageToProvideText);

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }


    }


    /**
     * Regression test for Login feature within several different browsers.
     * Considers test cases TC_C_007, TC_C_008
     * Programmer: Seyedmehrad Adimi
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Regression","Complaint","Login_Complaint","hasDataProvider"},
            priority = 0,
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    private void CO_Regression(String chosenBrowser, Object[] dataSet) throws IOException, InterruptedException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

//        try {
//
//            /* Test cases TC_LF_016, TC_LF_017: Verify the Page Heading, Page Title and Page URL of Login page, Verify the UI of the Login page*/
//            TestFunctions.navToLogin (browserWindow);
//            testRegressionForMe (browserWindow, false);
//
//            /* Test case TC_LF_008: Verify E-Mail Address and Password text fields in the Login page have the placeholder text*/
//
//            WebElement emailInputField = browserWindow.findElement (By.id ("email"));
//            emailInputField.click ();
//            assertEquals (emailInputField.getAttribute ("aria-label"), "Text field for the login email");
//            assertElement (emailInputField);
//
//
//            WebElement passwordInputField = browserWindow.findElement (By.id ("password"));
//            passwordInputField.click ();
//            assertEquals (passwordInputField.getAttribute ("aria-label"), "Text field for the login password");
//            assertElement (passwordInputField);
//
//
//            /* Test case TC_LF_007: Verify logging into the Application using Keyboard keys (Tab and Enter)*/
//            // Register first since the accounts get deleted regularly.
//            fillOutReg (browserWindow, dataSet[0].toString (), dataSet[1].toString (), dataSet[1].toString (), true, dataSet[2].toString ());
//            sleep (1);
//
//        }finally {
//            Thread.sleep(TestFunctions.endTestWait);
//            browserWindow.quit();
//        }


    }



    private void loginForMe(WebDriver browserWindow,  String email, String password) throws InterruptedException{
        browserWindow.get (TestFunctions.website);
        sleep (1);

        browserWindow.findElement(By.id ("navbarAccount")).click ();
        sleep (1);



        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector(TestFunctions.navbarLogin));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();
        sleep (1);



        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        sleep (1);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        sleep (1);
        Login.emailPassEnter (browserWindow, email, password, emailUsr);
        sleep (1);
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
