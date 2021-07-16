package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static org.testng.Assert.*;


import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Tests for verifying the full functionality of the logout Feature
 */
public class Logout implements ITest
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
     * Smoke Test to confirm the logout button is hidden while logged out, and performs a manual logout to confirm functionality.
     * Programmer: Kyle Sullivan
     * @param chosenBrowser Browser type for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Logout Smoke", "Logout", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void LO1_Manual_Logout(String chosenBrowser) throws IOException, InterruptedException
    {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        try
        {
            //See method for full details
            verifyLoggedOut(browserWindow);
            browserWindow.findElement(By.cssSelector("#navbarLogoutButton")).click();//Logout

            verifyLoggedOut(browserWindow);
            assertTrue(browserWindow.findElement(By.cssSelector("#navbarLogoutButton")).isDisplayed());//Confirm user is logged out.
        }
        finally
        {
            //End Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test to verify a user is not logged out when going back through browser history,
     * and is logged out of all accounts when a single browser logs out
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown when the test is interrupted during a thread sleep period
     */
    @Test(
            groups = {"Sanity", "Logout Sanity", "Logout","noDataProvider"},
            priority = 0,
            enabled = true
    )
    public void LO2_Logout_Sanity() throws InterruptedException
    {
        //Create two environments
        WebDriver browserWindow = environment.makeDriver();
        WebDriver browserSecondary = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserSecondary.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);
        browserSecondary.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);
        TestFunctions.waitForSite(browserSecondary);

        try
        {
            //Login using google account
            TestFunctions.login(browserWindow);
            TestFunctions.login(browserSecondary);

            //Browse back through history and return to website
            browserWindow.navigate().back();
            browserWindow.get(TestFunctions.website);

            //Check if the account menu has the logout button displayed. If it is displayed, then the test was passed as the user was not logged out
            TestFunctions.waitForSite(browserWindow, TestFunctions.navPath); //Wait for site to be ready
            browserWindow.findElement(By.cssSelector(TestFunctions.navPath)).click();//Open account menu
            Thread.sleep(100);
            WebElement browserWindowLogout = browserWindow.findElement(By.cssSelector("#navbarLogoutButton"));
            boolean userIsNotLoggedOut = browserWindowLogout.isDisplayed();
            assertTrue(userIsNotLoggedOut);

            //Log out on the primary test window
            browserWindowLogout.click();
            //Wait for the site to catch up
            Thread.sleep(10000);

            //Refresh the page on the secondary test window
            browserSecondary.navigate().refresh();
            //Check if logged out. If the logout button is present, then the user was not logged out, and the test was failed.
            TestFunctions.waitForSite(browserSecondary, TestFunctions.navPath);
            browserSecondary.findElement(By.cssSelector(TestFunctions.navPath)).click();
            Thread.sleep(100);

            boolean secondaryIsLoggedOut = browserSecondary.findElement(By.cssSelector("#navbarLogoutButton")).isDisplayed();
            assertFalse(secondaryIsLoggedOut);

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
            browserSecondary.quit();
        }
    }

    /**
     * Performs a sanity test of the automated logout system.
     * WARNING: Test will take roughly 30 minutes. Only activate if you intend to run tests for that long
     * Additionally this test will always fail as logout automation does not actual work on site
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if Test was interrupted during a thread waiting period
     */
    @Test(
            groups = {"Sanity", "Registration Sanity", "Registration","noDataProvider"},
            priority = 1,
            enabled = false
    )
    public void LO3_Logout_Automated() throws InterruptedException
    {
        //Amount of minutes for the test
        int timeoutTime = 30;

        //Make the test environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            TestFunctions.login(browserWindow);

            //Perform the following loop a number of times equal to timeoutTime.
            for (int minutesPassed = 1; minutesPassed < timeoutTime; minutesPassed++)
            {
                //Wait one minute
                Thread.sleep(60000);
                //Print out a reminder message
                System.out.println("System has waited " + minutesPassed + " minutes.\nSystem has " + (timeoutTime - minutesPassed) + " minutes remaining.");
            }
            //Check if the logout button is visible
            verifyLoggedOut(browserWindow);
        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Method used by several test methods in Logout.java.
     * Performs the following functions
     * Checks if the user is logged out by checking for the visibility of the logout button
     * Logs the user back in via google
     * Opens the account menu
     * Programmer: Kyle Sullivan
     * @param browserWindow Test window to perform actions in
     * @throws InterruptedException Thrown if the test is interrupted during a thread waiting period
     */
    private void verifyLoggedOut(WebDriver browserWindow) throws InterruptedException
    {
        //Waits for the account menu to be accessible then opens it
        TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);
        Thread.sleep(100);
        browserWindow.findElement(By.cssSelector(TestFunctions.navPath)).click();
        Thread.sleep(100);

        boolean logoutVisible = false; //Whether the logout button is visible
        try
        {
            //Find the logout button
            WebElement element = browserWindow.findElement(By.cssSelector("#navbarLogoutButton"));
            //If the element is present...
            if (element.isDisplayed())
                logoutVisible = true;
        }
        catch (Exception NoSuchElementException) {}
        finally
        {
            assertFalse(logoutVisible);//Ensure that while logged out, the logout button is not visible
        }

        //Find the overlay blocking the account menu button and remove it
        browserWindow.findElement(By.cssSelector("body > div.cdk-overlay-container.bluegrey-lightgreen-theme > div.cdk-overlay-backdrop.cdk-overlay-transparent-backdrop.cdk-overlay-backdrop-showing")).click();

        //Log the user back in via google
        TestFunctions.login(browserWindow);
        //Open the account menu
        browserWindow.findElement(By.cssSelector(TestFunctions.navPath)).click();
        Thread.sleep(500);
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
