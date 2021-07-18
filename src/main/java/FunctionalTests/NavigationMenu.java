package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;


public class NavigationMenu implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;
    static String xPathNavMenu = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[1]";
    static String xPathNavMenuCommon = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[";
    static String xPathNavMenuEnd = "]";
    static String xPathNavMenuDesc = "/div/span";

    String xPathAboutButtons1 = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-about/div/mat-card/section/div/a[";
    String xPathAboutButtons2 = "]/button";

    static Object[] menuOptions;
    static Object[] URLConfirm;


    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Kyle Sullivan
     */
    @BeforeSuite
    public void SetUp() throws IOException, InterruptedException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

    @Test(
            groups = {"Smoke", "Navigation Menu Smoke", "Navigation menu", "hasDataProvider"},
            priority = 0,
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
            menuOptions = new Object[]{"","Customer Feedback","About Us","Photo Wall"};
            checkNav(browserWindow);
            browserWindow.findElement(By.xpath(xPathNavMenuCommon + 1 + xPathNavMenuEnd)).click();
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"contact");
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }


    @Test(
            groups = {"Sanity", "Navigation Menu Sanity", "Navigation menu", "noDataProvider"},
            priority = 0,
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
            menuOptions = new Object[]{"","Customer Feedback","About Us","Photo Wall"};
            URLConfirm = new Object[]{"","contact","about","photo-wall"};
            checkNavLinks(browserWindow);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    @Test(
            groups = {"Sanity", "Navigation Menu Sanity", "Navigation menu", "noDataProvider"},
            priority = 0,
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
            menuOptions = new Object[]{"","Customer Feedback","Complaint","Support Chat","About Us","Photo Wall", "Deluxe Membership"};
            URLConfirm = new Object[]{"","contact","complain","chatbot","about","photo-wall","deluxe-membership"};
            TestFunctions.login(browserWindow);
            checkNavLinks(browserWindow);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    public static void checkNav(WebDriver browserWindow) throws InterruptedException
    {
        TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu,true);
        int limit = menuOptions.length;
        for(int i = 1; i < limit; i++)
            assertEquals(browserWindow.findElement(By.xpath(xPathNavMenuCommon + i + xPathNavMenuEnd + xPathNavMenuDesc)).getText(),menuOptions[i]);
    }

    public void checkNavLinks(WebDriver browserWindow) throws InterruptedException
    {
        int limit = URLConfirm.length;
        for(int i = 1; i < limit; i++)
        {
            checkNav(browserWindow);
            TestFunctions.waitForSiteXpath(browserWindow, xPathNavMenuCommon + i + xPathNavMenuEnd,true);
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website + URLConfirm[i]);
        }
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
