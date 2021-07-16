package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
    String xPathNavMenu = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[1]";
    String xPathNavMenu1 = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[";
    String xPathNavMenu2 = "]";
    String xPathNavMenu3 = "/div/span";
    String xPathAboutButtons1 = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-about/div/mat-card/section/div/a[";
    String xPathAboutButtons2 = "]/button";


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
    /*
    public void NM1_Nav_Menu_Basic_Functionality(String chosenBrowser) throws InterruptedException, IOException
    {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
       // browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        Object[] menuOptions = new Object[]{"","Customer Feedback","About Us","Photo Wall"};

        try
        {
            browserWindow.findElement(By.xpath(xPathNavMenu)).click();
            checkNav(browserWindow,menuOptions);
            browserWindow.findElement(By.xpath(xPathNavMenu1 +1+ xPathNavMenu2)).click();
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website + "contact");
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
    public void NM2_Nav_Menu_Logged_Out_Small() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
       //browserWindow.manage().window().setSize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        try
        {

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
    public void NM3_Nav_Menu_Logged_In_Small() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        //browserWindow.manage().window().setSize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        try
        {

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
    public void NM4_Nav_Menu_Logged_Out_FullScreen() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        Object[] menuOptions = new Object[]{"","Customer Feedback","About Us","Photo Wall"};
        Object[] URLConfirm = new Object[]{"","contact","about","photo-wall"};
        try
        {
            checkNavLinks(browserWindow,menuOptions,URLConfirm);
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
    public void NM5_Nav_Menu_Logged_In_FullScreen() throws InterruptedException
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

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    public void checkNav(WebDriver browserWindow, Object[] menuOptions)
    {
        int limit = menuOptions.length-1;
        for(int i = 1; i <= limit; i++)
            assertEquals(menuOptions[i], browserWindow.findElement(By.xpath(xPathNavMenu1 + i + xPathNavMenu2 + xPathNavMenu3)).getText());
    }

    public void checkNavLinks(WebDriver browserWindow, Object[] menuOptions, Object[] URLConfirm) throws InterruptedException
    {
        int limit = URLConfirm.length-1;
        for(int i = 1; i <= limit; i++)
        {
            Thread.sleep(1500);
            browserWindow.findElement(By.xpath(xPathNavMenu)).click();
            checkNav(browserWindow, menuOptions);
            browserWindow.findElement(By.xpath(xPathNavMenu1 + i + xPathNavMenu2)).click();
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website + URLConfirm[i]);
            if(browserWindow.getCurrentUrl().equals(TestFunctions.website+"about"))
            {
                Object[] links = new Object[]{"","","","","","",};
                for(int j = 1; j <= 5; j++)
                {
                    browserWindow.findElement(By.xpath(xPathAboutButtons1+j+xPathAboutButtons2)).click();
                    Thread.sleep(500);
                    assertEquals(browserWindow.getCurrentUrl(),links[j]);
                    browserWindow.get(TestFunctions.website + "about");
                    TestFunctions.waitForSite(browserWindow, TestFunctions.navPath);
                }
            }
        }
    }

     */

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
