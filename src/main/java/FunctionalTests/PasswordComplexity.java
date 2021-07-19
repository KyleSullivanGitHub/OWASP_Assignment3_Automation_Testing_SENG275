package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;

/**
 * Tests for verifying the functionality of the password Complexity feature present wherever the user needs to create passwords
 */
public class PasswordComplexity implements ITest
{
    private final ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;

    String validPassword = "aB3!aB3!";//Valid password for any complexity test

    String pass = "primary";//Used to determine if a password has passed a specific complexity standard
    String fail = "warn";//Used to determine if a password has failed a specific complexity standard.


    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
     *
     * @throws IOException          Thrown if no browser is chosen for a test
     * @throws InterruptedException Thrown if the test is interrupted during a wait period
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
     * Smoke test for valid inputs for Password Complexity within several different browsers
     * Programmer: Kyle Sullivan
     *
     * @param chosenBrowser Browser used for that test
     * @throws IOException          Thrown if no browser is chosen for a test
     * @throws InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Password Complexity", "Password Complexity Smoke", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PCS1_Password_Complexity_Valid(String chosenBrowser) throws IOException, InterruptedException
    {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);

        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);
        try
        {
            //Test Password
            testPassAdviceReg(browserWindow, new Object[]{validPassword, pass, pass, pass, pass, pass});
        } finally
        {
            //End Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }


    /**
     * Smoke test for invalid input for Password Complexity
     * Programmer: Kyle Sullivan
     *
     * @throws InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Password Complexity", "Password Complexity Smoke","noDataProvider"},
            priority = 0,
            enabled = true
    )
    public void PCS2_Password_Complexity_Invalid() throws InterruptedException
    {
        //Create the Test Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Navigate to the website
        browserWindow.get(TestFunctions.website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Try the password
            testPassAdviceReg(browserWindow, new Object[]{"1234567!", fail, fail, pass, pass, pass});
        } finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test for all types of invalid input for Password Complexity
     * Programmer: Kyle Sullivan
     *
     * @param type    Type of invalid input, used for naming tests
     * @param dataSet An object containing the data set for this test
     * @throws InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Sanity", "Password Complexity", "Password Complexity Sanity", "hasDataProvider"},
            priority = 0,
            dataProvider = "PC_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PCS3_Password_Complexity_Invalid_Reg(String type, Object[] dataSet) throws InterruptedException
    {
        //Create Test Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Test password
            testPassAdviceReg(browserWindow, dataSet);
        } finally
        {
            //End Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test for valid inputs in Forgot Password for Password Complexity
     * Programmer: Kyle Sullivan
     *
     * @throws InterruptedException Thrown if the test is interrupted during a thread waiting period
     */
    @Test(
            groups = {"Sanity", "Password Complexity", "Password Complexity Sanity","noDataProvider"},
            priority = 0,
            enabled = true
    )
    public void PCS4_Password_Complexity_Valid_FP() throws InterruptedException
    {
        //Create the Test Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Test a valid password within Forgot Password
            testPassAdviceFP(browserWindow, new Object[]{validPassword, pass, pass, pass, pass, pass});
        } finally
        {
            //End Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test for Invalid Inputs in Forgot Password for Password Complexity
     * Programmer: Kyle Sullivan
     *
     * @param type    Type of invalid input for test, used in naming the test.
     * @param dataSet An object containing a data set for this test
     * @throws InterruptedException Thrown if the test is interrupted during a thread waiting period
     */
    @Test(
            groups = {"Sanity", "Password Complexity", "Password Complexity Sanity", "hasDataProvider"},
            priority = 0,
            dataProvider = "PC_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PCS5_Password_Complexity_Invalid_FP(String type, Object[] dataSet) throws InterruptedException
    {
        //Create the Test Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Test the Invalid Input
            testPassAdviceFP(browserWindow, dataSet);
        } finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Setup for Password complexity tests within Registration
     * Programmer: Kyle Sullivan
     *
     * @param browserWindow Browser used for that test
     * @param dataSet       An object contaning the following values:
     *                      Password string for test
     *                      Whether the password should pass the lowercase test
     *                      Whether the password should pass the uppercase test
     *                      Whether the password should pass the number test
     *                      Whether the password should pass the special character test
     *                      Whether the password should pass the length test
     * @throws InterruptedException Thrown if the test was interrupted during a waiting period
     */
    public void testPassAdviceReg(WebDriver browserWindow, Object[] dataSet) throws InterruptedException
    {
        //xPath to common part of icon elements on password advice element
        String xPathLoc = "register";
        //Navigate to the Registration Page
        TestFunctions.navToReg(browserWindow);

        //Activate password complexity advice by toggling the slider
        browserWindow.findElement(By.className("mat-slide-toggle-thumb")).click();
        browserWindow.findElement(By.cssSelector("#passwordControl")).sendKeys((String) dataSet[0]); //enter password

        //Test the Password
        testPassAdvice(browserWindow, dataSet, xPathLoc);

    }

    /**
     * Setup for Password complexity tests within Forgot Password
     * Programmer: Kyle Sullivan
     *
     * @param browserWindow Browser used for that test
     * @param dataSet       An object contaning the following values:
     *                      Password string for test
     *                      Whether the password should pass the lowercase test
     *                      Whether the password should pass the uppercase test
     *                      Whether the password should pass the number test
     *                      Whether the password should pass the special character test
     *                      Whether the password should pass the length test
     * @throws InterruptedException Thrown if the test was interrupted during a thread waiting period
     */
    public void testPassAdviceFP(WebDriver browserWindow, Object[] dataSet) throws InterruptedException
    {
        //xPath to common part of icon elements on password advice element
        String xPathLoc = "forgot-password";

        //Navigate to Forgot Password
        TestFunctions.navToLogin(browserWindow);
        browserWindow.get(TestFunctions.website+"forgot-password");
        try
        {
            //Fill out form
            browserWindow.findElement(By.cssSelector("#email")).sendKeys(TestFunctions.constEmail);//Fill out email
            browserWindow.findElement(By.cssSelector("#securityAnswer")).sendKeys(TestFunctions.constAnswer);//Fill out security answer

            //Activate Password Advice by toggling the slider.
            browserWindow.findElement(By.className("mat-slide-toggle")).click();
            browserWindow.findElement(By.cssSelector("#newPassword")).sendKeys((String) dataSet[0]); //enter password
        }
        catch (ElementNotInteractableException badCondition)
        {
            assertTrue(false);
        }

        //Test the password
        testPassAdvice(browserWindow, dataSet, xPathLoc);
    }

    /**
     * Password complexity test for all Password Complexity class test methods
     * Programmer: Kyle Sullivan
     *
     * @param browserWindow Browser used for that test
     * @param dataSet An object contaning the following values:
     *      Password string for test
     *      Whether the password should pass the lowercase test
     *      Whether the password should pass the uppercase test
     *      Whether the password should pass the number test
     *      Whether the password should pass the special character test
     *      Whether the password should pass the length test
     * @param xPathLoc      common xpath to the password advice icons.
     */
    public static void testPassAdvice(WebDriver browserWindow, Object[] dataSet, String xPathLoc)
    {
        //common HTML element containing results of password complexity standards
        String iconPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-" + xPathLoc + "/div/mat-card/div[2]/mat-password-strength-info/mat-card/mat-card-content/";

        /*
         * To test whether a password has passed or failed a standards test, the automation looks at the icon for each standard.
         * Each Icon can be in one of two states, pass or fail, indicated by the color attribute of the icon in question.
         * If the icon is red, its color attribute is warn, which is the fail state for a standard test
         * If the icon is green, its color attribute is primary, which is the pass state for a standard test.
         * There are 5 standard tests to check.
         */
        for(int i = 1; i <= 5; i++)
        {
            assertEquals(browserWindow.findElement(By.xpath(iconPath + "div[" + i + "]/mat-icon")).getAttribute("color"), (String)dataSet[i]);//Check standard test
        }
    }

    /**
     * Method for changing the name of tests performed multiple times by adding the first value in their data provider to the end of their names
     * Taken from: https://www.swtestacademy.com/change-test-name-testng-dataprovider/
     * Programmer: Canberk Akduygu
     *
     * @param method   Test method whose name is to be changed
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
     *
     * @return Name of test
     */
    @Override
    public String getTestName()
    {
        return testName.get();
    }
}
