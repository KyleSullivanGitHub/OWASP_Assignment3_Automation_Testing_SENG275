package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.testng.Assert.*;

/**
 * Tests for verifying the full functionality of the registration feature
 */
public class Registration implements ITest
{
    private final ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;


    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
     * @exception IOException Thrown if no browser is chosen for a test
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

    /**
     * Smoke test for valid inputs to Registration within several different browsers
     * Programmer: Kyle Sullivan
     * @param chosenBrowser Browser used for that test
     * @param dataSet Object containing a data set for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Registration", "Registration Smoke", "hasDataProvider"},
            priority = 0,
            dataProvider = "RF1_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RF1_Valid_Input(String chosenBrowser, Object[] dataSet) throws IOException, InterruptedException
    {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        //Navigate to Site
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        try
        {
            //Navigate to registration.
            TestFunctions.navToReg(browserWindow);

            //Fill out registration with a valid data set
            fillOutReg(browserWindow, dataSet);

            browserWindow.findElement(By.cssSelector(TestFunctions.regButton)).click();//click register button

            //Check that the user is taken back to the login page
            Thread.sleep(1000);
            assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website + "/#/login");
            Thread.sleep(500);
        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Smoke tests a single invalid registration attempt.
     * Programmer: Kyle Sullivan
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Registration Smoke", "Registration"},
            priority = 1,
            enabled = true
    )
    public void RF2_Invalid_Input() throws InterruptedException
    {
        //Create the Test Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Navigate to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Navigate to registration.
            TestFunctions.navToReg(browserWindow);
            //Fill out registration form with an invalid data set
            fillOutReg(browserWindow, new Object[] {"email", "pswrd", "password", false, ""});

            //Check that the registration button cannot be clicked.
            assertFalse(browserWindow.findElement(By.cssSelector(TestFunctions.regButton)).isEnabled());
        }
        finally
        {
            //End Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }


    /**
     * purpose
     * Programmer
     *
     * @param
     */
    @Test(
            groups = {"", ""},
            priority = 2,
            enabled = false
    )
    public void RF3_Validation_Email() throws InterruptedException
    {
        //TODO Validation Email
        try
        {

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
        }
    }

    /**
     * Smoke tests several invalid cases for Registration, which can be found in the data provider class.
     * Programmer: Kyle Sullivan
     * @param testing Invalid case being tested
     * @param dataSet An Object containing a data set for this test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Sanity", "Registration Sanity", "Registration", "hasDataProvider"},
            priority = 3,
            dataProvider = "RF4_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RF4_Invalid_Input(String testing, Object[] dataSet) throws InterruptedException
    {

        boolean disabledButton; //Whether the Registration button is disabled

        //Create Test Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Navigate to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Navigate to registration.
            TestFunctions.navToReg(browserWindow);
            //Fill out Registration form
            fillOutReg(browserWindow, dataSet);
            //Check if the registration button is disabled.
            disabledButton = browserWindow.findElement(By.cssSelector(TestFunctions.regButton)).isEnabled();

            //If the button is enabled...
            if (disabledButton)
            {
                //...Ensure it does not accept the account details.
                browserWindow.findElement(By.cssSelector(TestFunctions.regButton)).click();//click register button
                Thread.sleep(1000);
                assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website + "/#/register");//confirm that we have not left the page.
            }
            else
            {
                //If the registration button cannot be accessed, confirm the test.
                assertFalse(disabledButton);
            }
        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * purpose
     * Programmer
     *
     * @param
     */
    @Test(
            groups = {"", ""},
            priority = 4,
            enabled = false
    )
    public void RF_Regression() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        try
        {
            TestFunctions.navToReg(browserWindow);
            Object[] UI = new Object[]{"get URL", "Get Header", "Get title"};

            TestFunctions.commonRegression(browserWindow, UI, true);
            assertEquals(browserWindow.findElement(By.className("mat-slide-toggle-thumb")).getAttribute("aria-checked"), "false");

            //check placeholder fields
            //check normal color

            //mandatory field check
            fillOutReg(browserWindow, new Object[]{"","","",false,""});
            //touch security question
            //Check mandatory fields
            //check out password advice
            TestFunctions.commonRegression(browserWindow, UI, true);

            //clear current fields

            fillOutReg(browserWindow, new Object[]{"testUser" + new Random().nextInt(100) +"@gmail.com","aB3!aB3!", "aB3!aB3!", true, "answer"});
            TestFunctions.commonRegression(browserWindow, UI, true);
            //check out password advice

            //check password is hidden
            WebElement passwordField = browserWindow.findElement(By.cssSelector("#password"));
            assertEquals(passwordField.getAttribute("type"), "password");
            WebElement repPasswordField = browserWindow.findElement(By.cssSelector("#repeatPasswordControl"));
            assertEquals(repPasswordField.getAttribute("type"), "password");

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
        }
    }

    /**
     * Method used to fill out registration form with passed values. Universal for all tests within registration.java
     * Programmer: Kyle Sullivan
     * @param browserWindow  Browser window used for this test
     * @param dataSet an object containing:
     *  Email string used for test
     *  Password string used for test
     *  Repeat password string used for test
     *  True/false to whether to use a security question
     *  String for security question.
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    public void fillOutReg(WebDriver browserWindow, Object[] dataSet) throws InterruptedException
    {
        boolean notFound = true;//Whether the correct value for a security question has been found
        int optionTry = 0;//Current list element of the security question options being tried for the security question
        int optionTryLimit = 50;//Maximum amount of attempts

        //Ensure the test is on the registration page
        assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website + "/#/register");

        browserWindow.findElement(By.cssSelector("#emailControl")).sendKeys((String)dataSet[0]); //Enter email
        browserWindow.findElement(By.cssSelector("#passwordControl")).sendKeys((String)dataSet[1]); //Enter password
        browserWindow.findElement(By.cssSelector("#repeatPasswordControl")).sendKeys((String)dataSet[2]); //Re-enter password
        browserWindow.findElement(By.cssSelector(".mat-select-trigger")).click(); //Select security question

        //If a security question is being selected for this test
        if ((boolean) dataSet[3])
        {
            Thread.sleep(500);
            /*
            Due to the nature of the security question answer box, there is potentially dozens of ids for the several answers.
            The catch is that only a few of these are true for any given instance of a test, but we dont know which ones will be active.
            As such, we are forced to just check for many different possible answers and use the first valid one we find.
            This is not an error on the site's part, just a limitation of the automation.
            */
            while (notFound && optionTry < optionTryLimit)
            {
                try
                {
                    //Try an potential option
                    browserWindow.findElement(By.cssSelector("#mat-option-" + optionTry)).click();
                    notFound = false;
                }
                catch (Exception NoSuchElementException)
                {
                    //If the option was invalid, prepare to try the next one
                    notFound = true;
                    optionTry++;
                }
            }
        }
        //give security question answer
        browserWindow.findElement(By.cssSelector("#securityAnswerControl")).sendKeys((String)dataSet[4]); //Enter answer
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
