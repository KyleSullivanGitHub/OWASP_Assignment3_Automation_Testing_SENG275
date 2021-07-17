package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
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
    String xPathNavMenuCommon = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/";
    String xPathNavMenu2 = "]";
    String xPathNavMenu3 = "/div/span";

    String xPathAboutButtons1 = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-about/div/mat-card/section/div/a[";
    String xPathAboutButtons2 = "]/button";
    Dimension smallScreen = new Dimension(500,1000);

    Object[] menuOptions;
    Object[] URLConfirm;


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
       // browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        menuOptions = new Object[]{"","Customer Feedback","About Us","Photo Wall"};
        try
        {

            TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu,true);//click register button
            checkNav(browserWindow);
            TestFunctions.waitForSiteXpath(browserWindow, xPathNavMenuCommon +1+ xPathNavMenu2,true);//click register button
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
        browserWindow.manage().window().setSize(smallScreen);
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        menuOptions = new Object[]{"Login","Customer Feedback","About Us","Photo Wall"};
        URLConfirm = new Object[]{"login","contact","about","photo-wall"};

        try
        {

            TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu,true);
            checkNav(browserWindow);
            TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/a",true);
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website + URLConfirm[0]);
            checkNavLinks(browserWindow);

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
        browserWindow.manage().window().setSize(smallScreen);
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        menuOptions = new Object[]{"","Customer Feedback","About Us","Photo Wall"};
        URLConfirm = new Object[]{"","contact","about","photo-wall"};

        //xPathNavMenu2 + xPathNavMenu3
        String loginElement = "/div/a";
        String loggedInElement = "/div/a["; // 1,2
        String catagoryElement = "/div/mat-list-item["; //1 2
        String listElement1 = "/div/div/a["; // 1, 5
        String listElement2 = "/div/div[2]/a["; //1,6


            //set 1: State
                //check login " state
            // set 2: State
                for(int set2 = 1; set2 <= 3; set2++)
                {

                }


            for(int element = 1; element < setLength; element++ )
            {
                if(//set 2 is active)
            }



        /*
         * Type 1: !LG | !PR !LO | !OP !PS | !OH !RE !MSA !MPO !DW | !PP !RDE1 !RDE2 !CP !SF !LLIP | CF !CM !SC AU PW !DM
         * Type 2:  LG | !PR !LO | !OP !PS | !OH !RE !MSA !MPO !DW | !PP !RDE1 !RDE2 !CP !SF !LLIP | CF !CM !SC AU PW !DM
         * Type 3: !LG |  PR  LO .  OP  PS .  OH  RE  MSA  MPO  DW .  PP  RDE1  RDE2  CP  SF  LLIP | CF  CM  SC AU PW  DM
         * Type 4: !LG | !PR !LO | !OP !PS | !OH !RE !MSA !MPO !DW | !PP !RDE1 !RDE2 !CP !SF !LLIP | CF !CM !SC AU PW !DM
         * Type 5: !LG | !PR !LO | !OP !PS | !OH !RE !MSA !MPO !DW | !PP !RDE1 !RDE2 !CP !SF !LLIP | CF  CM  SC AU PW  DM
         *
         */



        try
        {              //login       div/a/div/span
            //profile                div/a[1]/div/span
            //orders and payment     div/mat-list-item[1]/div/span
                //order History      div/div/a[1]/div/span
                //Recycle            div/div/a[2]/div/span
                //My saved addresses div/div/a[3]/div/span
                //my payment options div/div/a[4]/div/span
                //digital wallet     div/div/a[5]/div/span
            //privacy and security   div/mat-list-item[2]/div/span
                //privacy Policy     div/div[2]/a[1]/div/span
               //request Data export div/div[2]/a[2]/div/span
             //request data Erasure  div/div[2]/a[3]/div/span
                //chagne password    div/div[2]/a[4]/div/span
                //SFA configuration  div/div[2]/a[5]/div/span
                //Last Login IP      div/div[2]/a[6]/div/span
            //logout                 div/a[2]/div/span


            //customer feedback      a[1]/div/span
            //complaint
            //Support Chat
            //about us
            //photo wall
            // deluxe membership
            TestFunctions.login(browserWindow);
            TestFunctions.waitForSiteXpath(browserWindow,xPathNavMenu,true);
            checkNav(browserWindow);
            TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/a",true);
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website + URLConfirm[0]);
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
    public void NM4_Nav_Menu_Logged_Out_FullScreen() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        menuOptions = new Object[]{"","Customer Feedback","About Us","Photo Wall"};
        URLConfirm = new Object[]{"","contact","about","photo-wall"};
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
        menuOptions = new Object[]{"","Customer Feedback","Complaint","Support Chat","About Us","Photo Wall","Deluxe Membership"};
        URLConfirm = new Object[]{"","contact","complain","chatbot","about","photo-wall","deluxe-membership"};
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
