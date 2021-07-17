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

//TODO UTTERLY FUCKED


public class NavigationMenu implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;
    String xPathNavMenu = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[1]";
    String xPathNavMenuCommon = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/";
    String xPathNavMenuEnd = "]";
    String xPathNavMenuDesc = "/div/span";

    String xPathAboutButtons1 = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-about/div/mat-card/section/div/a[";
    String xPathAboutButtons2 = "]/button";
    Dimension smallScreen = new Dimension(500,1000);

    Object[][] menuOptions = new Object[][]{
            {"Login"},
            {"",TestFunctions.googleEmail,"Logout"},
            {"","Orders & Payment","Privacy & Security"},
            {"","Order History","Recycle","My saved addresses","My Payment Options","Digital Wallet"},
            {"","Privacy Policy","Request Data Export","Request Data Erasure","Change Password","2FA Configuration","Last Login IP"},
            {"","Customer Feedback","Complaint","Support Chat","About Us","Photo Wall","Deluxe Membership"}
    };
    Object[] URLConfirm;

    Object[] paths = new Object[]{
            "div/a",
            "div/a[",
            "div/mat-list-item[",
            "div/div/a[",
            "div/div[2]/a[",
            "a[",
    };

    public static Object[][] state;


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
            enabled = false
    )

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

        state = new Object[][]{
                {false},
                {false},
                {null,true,false,false,true,true,false},
        };

        try
        {
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
            enabled = false
    )
    public void NM2_Nav_Menu_Logged_Out_Small() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().setSize(smallScreen);
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        state = new Object[][]{
                {true},
                {false},
                {null,true,false,false,true,true,false},
        };

        try
        {

            checkNav(browserWindow);

            /*
            Object[] links = new Object[]{"","","","","","",};
            TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu,true);
            TestFunctions.waitForSiteXpath(browserWindow, xPathNavMenuCommon + 2 + xPathNavMenu2,true);
            for(int j = 1; j <= 5; j++)
            {
                TestFunctions.waitForSiteXpath(browserWindow,xPathAboutButtons1+j+xPathAboutButtons2,true);
                Thread.sleep(1500);
                assertEquals(browserWindow.getCurrentUrl(),links[j]);
                browserWindow.get(TestFunctions.website + "about");
                TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu);
            }

             */
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
            enabled = false
    )
    public void NM3_Nav_Menu_Logged_In_Small() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        state = new Object[][]{
                {false},
                {true},
                {null,true,true,true,true,true,true},
        };



        try
        {
            TestFunctions.login(browserWindow);
            browserWindow.manage().window().setSize(smallScreen);
            checkNav(browserWindow);

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
            enabled = false
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

        state = new Object[][]{
                {false},
                {false},
                {null,true,false,false,true,true,false},
        };

        try
        {
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
            enabled = false
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

        state = new Object[][]{
                {false},
                {false},
                {null,true,true,true,true,true,true},
        };

        try
        {
            TestFunctions.login(browserWindow);
            checkNavLinks(browserWindow);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    public void checkNavLinks(WebDriver browserWindow) throws InterruptedException
    {
        checkNav(browserWindow);
    }

    public void checkNav(WebDriver browserWindow) throws InterruptedException
    {

        WebElement navElement;
        TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu,true);
        if((boolean)state[0][0])
        {
            try
            {
                navElement = browserWindow.findElement(By.xpath(xPathNavMenuCommon + paths[0] +xPathNavMenuDesc));
                if (navElement.isDisplayed())
                    assertEquals(navElement.getText(), menuOptions[0][0]);
            } catch (NoSuchElementException exception)
            {
                assertFalse((Boolean) state[0][0]);
            }
        }
        if(!(boolean)state[1][0])
        {
            try
            {
                for(int section = 2; section <= menuOptions.length-1;section++)
                {
                    int limit = menuOptions[section].length;
                    for (int i = 1; i <= limit; i++)
                    {
                        navElement = browserWindow.findElement(By.xpath(xPathNavMenuCommon + paths[section] + i + xPathNavMenuEnd + xPathNavMenuDesc));
                        if (navElement.isDisplayed())
                            assertEquals(menuOptions[section][i], navElement.getText());
                        if(section == 2)
                            TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenuCommon + paths[section] + i + xPathNavMenuEnd,true);
                    }
                }
            } catch (NoSuchElementException exception)
            {
                assertFalse((Boolean) state[1][0]);
            }
        }
        for(int temp = 1; temp <=6;temp++)
        {
            if ((boolean) state[2][temp])
            {
                try
                {
                        navElement = browserWindow.findElement(By.xpath(xPathNavMenuCommon + paths[5] + temp + xPathNavMenuEnd + xPathNavMenuDesc));
                        if (navElement.isDisplayed())
                            assertEquals(navElement.getText(),menuOptions[5][temp]);
                } catch (NoSuchElementException exception)
                {
                    assertFalse((Boolean) state[2][temp]);
                }
            }
        }

/*
        int stateIndex = 0;
        if((boolean)state[1][0] && !browserWindow.getCurrentUrl().equals(TestFunctions.website+"about"))
        {
            TestFunctions.waitForSiteXpath(browserWindow, xPathNavMenuCommon+paths[2]+1+xPathNavMenuEnd,true);
            TestFunctions.waitForSiteXpath(browserWindow, xPathNavMenuCommon+paths[2]+2+xPathNavMenuEnd,true);
        }
        for(int section = 0; section < 6; section++)
        {
            int index = 0;
            WebElement navElement;
            if(section == 0)
            {
                try
                {
                    navElement = browserWindow.findElement(By.xpath(xPathNavMenuCommon + paths[section]+xPathNavMenuDesc));
                    if(navElement.isDisplayed())
                        assertEquals(navElement.getText(),menuOptions[section][index]);
                }
            }
            else
            {
                int limit = menuOptions[section].length;
                for (int element = 1; element < limit; element++)
                {
                    index = element == 5 ? element : 0;
                    try
                    {
                        navElement = browserWindow.findElement(By.xpath(xPathNavMenuCommon + paths[section] + element +xPathNavMenuEnd+xPathNavMenuDesc));
                        if(navElement.isDisplayed())
                            assertEquals(navElement.getText(), menuOptions[stateIndex][element]);
                    }
                    catch (NoSuchElementException exception) { assertFalse((boolean)state[stateIndex][index]); }
                }
            }
            if(section == 0 || section >= 4)
                stateIndex++;
        }

 */
    }

    /*
    public void checkNav(WebDriver browserWindow)
    {
        if(menuOptions[0].equals("Login"))
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/a/div/span")).getText(),menuOptions[0]);
        int limit = menuOptions.length-1;
        for(int i = 1; i <= limit; i++)
        {
            assertEquals(browserWindow.findElement(By.xpath(xPathNavMenuCommon + i + xPathNavMenu2 + xPathNavMenu3)).getText(),menuOptions[i]);
        }
    }

    public void checkNavLinks(WebDriver browserWindow) throws InterruptedException
    {
        int limit = URLConfirm.length-1;
        for(int i = 1; i <= limit; i++)
        {
            TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu,true);
            checkNav(browserWindow);
            TestFunctions.waitForSiteXpath(browserWindow, xPathNavMenuCommon + i + xPathNavMenu2,true);
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website + URLConfirm[i]);
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
