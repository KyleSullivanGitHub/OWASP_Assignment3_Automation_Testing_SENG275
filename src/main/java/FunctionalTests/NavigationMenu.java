package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;


public class NavigationMenu implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;
    //Xpath to the navigation menu
    static String xPathNavMenu = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[1]";
    //common string part of all navigation element xpaths
    static String xPathNavMenuCommon = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[";
    //end of all navigation menu element xpaths
    static String xPathNavMenuEnd = "]";
    //Addition to navigation element xpath string to find the name of the element
    static String xPathNavMenuDesc = "/div/span";

    static Object[] menuOptions; //options in the menu to compare results against
    static Object[] URLConfirm; //URL extensions for all menu option to compare results against


    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Kyle Sullivan
     */
    @BeforeClass
    public void SetUp() throws IOException, InterruptedException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

    /**
     * Smoke test to open the navigation menu, ensure all proper options are there, and quickly check a link works
     * Programmer: Kyle Sullivan
     * @param chosenBrowser Chosen browser for this test
     * @throws InterruptedException Thrown if the test iss interrupted during a thread sleeping period
     * @throws IOException Thrown if no browser is selected
     */
    @Test(
            groups = {"Smoke", "Navigation_Menu", "hasDataProvider"},
            priority = 10,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void NM1_Nav_Menu_Basic_Functionality(String chosenBrowser) throws InterruptedException, IOException
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
            // set the menu options to the logged out options
            menuOptions = TestFunctions.navbarLoggedOut;
            //check teh navigation menu
            checkNav(browserWindow);
            //go to customer feedback via the menu, and confirm the link has taken the user to the right page
            browserWindow.findElement(By.xpath(xPathNavMenuCommon + 1 + xPathNavMenuEnd)).click();
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"contact");
        }
        finally
        {
            //end test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }


    /**
     * Sanity test to confirm all options are present on nav menu when logged out, and all links work
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if the test iss interrupted during a thread sleeping period
     */
    @Test(
            groups = {"Sanity", "Navigation_Menu", "noDataProvider"},
            priority = 60,
            enabled = true
    )
    public void NM2_Nav_Menu_Logged_Out() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);


        try
        {
            //Set the menu options to the logged out versions
            menuOptions = TestFunctions.navbarLoggedOut;
            //set the URL links to the logged out versions
            URLConfirm = new Object[]{"","contact","about","photo-wall"};
            //Check all options and links
            checkNavLinks(browserWindow);
        }
        finally
        {
            //end test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test to check all navigation menu options and links while logged in.
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if the test iss interrupted during a thread sleeping period
     */
    @Test(
            groups = {"Sanity", "Navigation_Menu", "noDataProvider"},
            priority = 60,
            enabled = true
    )
    public void NM3_Nav_Menu_Logged_In() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Set the menu options to the logged in sequence
            menuOptions = TestFunctions.navbarLoggedIn;
            //set the URLs to to the logged in sequence
            URLConfirm = new Object[]{"","contact","complain","chatbot","about","photo-wall","deluxe-membership"};
            //Login
            TestFunctions.login(browserWindow);
            //Check the nav menu
            checkNavLinks(browserWindow);
        }
        finally
        {
            //End Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Method to check that each nav option present in the nav menu correlates to the expected values of the nav menu
     * Programmer: Kyle Sullivan
     * @param browserWindow Test window to work in
     * @throws InterruptedException Thrown if the test was interrupted during a waiting period
     */
    public static void checkNav(WebDriver browserWindow) throws InterruptedException
    {
        //wait for and open the nav menu
        TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu,true);

        //for every option within the nav menu, compare to the correlating option in the menuOptions object.
        int limit = menuOptions.length;
        for(int i = 1; i < limit; i++)
            assertEquals(browserWindow.findElement(By.xpath(xPathNavMenuCommon + i + xPathNavMenuEnd + xPathNavMenuDesc)).getText(),menuOptions[i]);
    }

    /**
     * Method to check that each link in the navmenu takes you to the expected page. Also checks each pages nav menu for consistency.
     * Programmer: Kyle Sullivan
     * @param browserWindow Test window to work in
     * @throws InterruptedException Thrown if the test was interrupted during a waiting period
     */
    public void checkNavLinks(WebDriver browserWindow) throws InterruptedException
    {
        //for each option within the URLconfirm object
        int limit = URLConfirm.length;
        for(int i = 1; i < limit; i++)
        {
            //check the pages navmenu
            checkNav(browserWindow);
            //go to the page
            TestFunctions.waitForSiteXpath(browserWindow, xPathNavMenuCommon + i + xPathNavMenuEnd,true);
            //check that it is the right page
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website + URLConfirm[i]);
        }
        //check the nav menu of the last page.
        checkNav(browserWindow);
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
