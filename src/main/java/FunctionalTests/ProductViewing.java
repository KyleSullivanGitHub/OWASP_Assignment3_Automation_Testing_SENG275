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

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;


public class ProductViewing implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;

    String listElement = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[";
    String pageNav = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/div[2]/button[";

    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
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
    public void PV1_Product_Viewing(String chosenBrowser) throws IOException, InterruptedException
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
            //click on main page button
            TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[2]",true);
            Thread.sleep(500);
            //confirm user is on the product page
            assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website+"search");

            //check product
            TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[1]/figure/mat-card/div[1]",true);
            Thread.sleep(500);
            TestFunctions.waitForSiteXpath(browserWindow,"/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details");
            WebElement product = browserWindow.findElement(By.xpath("/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details"));
            assertFalse(product.findElement(By.xpath("app-product-details/mat-dialog-content/div/div[1]/div[2]/h1")).getText().isEmpty());

            //close product window


            TestFunctions.waitForSiteXpath(browserWindow, "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/div[1]/mat-form-field/div/div[1]/div/mat-select/div",true);
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"mat-option-"+0+"\"]",true);
            //check that you can navigate across pages
            TestFunctions.waitForSiteXpath(browserWindow, pageNav + "2]", true);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/div[2]/div")).getText(),"13 - 24 of 36");
            TestFunctions.waitForSiteXpath(browserWindow, pageNav+"1]", true);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/div[2]/div")).getText(),"1 - 12 of 36");



        } finally
        {
            Thread.sleep(TestFunctions.endTestWait);
           // browserWindow.quit();
        }
    }

    @Test(
            groups = {"Sanity", "Product View Sanity", "Product View", "hasDataProvider"},
            priority = 0,
            dataProvider = "PV2_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PV2_Grid_Functionality(String testing, WebDriver browserWindow, int itemPerPage) throws InterruptedException, IOException
    {
        try
        {
            int select;
            try { select = itemPerPage == 12 ? 0 : itemPerPage == 24 ? 1 : itemPerPage == 36 ? 2 : null;}
            catch (NullPointerException exception){ throw new IOException("No Product per page Amount Set");}

            int pageAmount = 36/itemPerPage + (itemPerPage == 24 ? 1 : 0);
            int pagesToCheck = 2*pageAmount-1;

            TestFunctions.waitForSiteXpath(browserWindow, "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/div[1]/mat-form-field/div/div[1]/div/mat-select/div",true);
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"mat-option-"+select+"\"]",true);

            for(int page = 1; page <= pagesToCheck; page++)
            {
                for (int i = 1; i <= itemPerPage; i++)
                {
                    assertTrue(browserWindow.findElement(By.xpath(listElement + i + "]")).isDisplayed());
                    if(itemPerPage == 24 && page == 2 && i == 12)
                        break;
                }
                if(page < pageAmount)
                    TestFunctions.waitForSiteXpath(browserWindow, pageNav + "2]", true);
                else if(page >= pageAmount && page != pagesToCheck)
                    TestFunctions.waitForSiteXpath(browserWindow, pageNav + "1]", true);
            }

        } finally
        {
            if(itemPerPage == 36)
            {
                Thread.sleep(TestFunctions.endTestWait);
                browserWindow.quit();
            }
        }
    }


    public void PV_Regression() throws InterruptedException
    {

        //TODO PV Regression
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //try twice, once logged in, once logged out
            //check reviews
            //check add to basket
            //move to next page, and check again
            //check for ribbons

        } finally
        {

        }
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
