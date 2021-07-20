package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;

/**
 * Test Class for tests relating to the main product display page.
 */
public class ProductViewing implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;

    //Path to main header button
    String headerButton = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[2]";
    //Path to product list elements
    String listElement = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[";
    //Path to the container of a product
    String productContainer = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details";
    //Path to products per page menu
    String productAmountPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/";
    //Path to the page navigation buttons
    String pageNav = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/div[2]/button[";
    //path to the page numbers on a page
    String pageNumPath = "div[2]/div";


    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
     */
    @BeforeClass
    public void SetUp() throws IOException, InterruptedException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

    /**
     * Smoke test to check basic product page functionality, such as the product expanded view, and page navigation.
     * Programmer: Kyle Sullivan
     * @param chosenBrowser Browser for this particular test
     * @throws IOException Thrown if a browser is not set for the test
     * @throws InterruptedException Thrown if the test is interrupted during a thread sleep period
     */
    @Test(
            groups = {"Smoke", "Product_View", "hasDataProvider"},
            priority = 11,
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
            TestFunctions.waitForSiteXpath(browserWindow,headerButton,true);
            Thread.sleep(500);
            //confirm user is on the product page
            assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website+"search");


            //check product
            TestFunctions.waitForSiteXpath(browserWindow, listElement + "1]",true);
            //confirm the expanded view is on display
            TestFunctions.waitForSiteXpath(browserWindow, productContainer);
            WebElement product = browserWindow.findElement(By.xpath(productContainer));
            //check that a product name is present
            assertFalse(product.findElement(By.xpath(productContainer +"/mat-dialog-content/div/div[1]/div[2]/h1")).getText().isEmpty());
            //check that a product description is present
            assertFalse(product.findElement(By.xpath(productContainer + "/mat-dialog-content/div/div[1]/div[2]/div[1]")).getText().isEmpty());
            //check taht a product price is present
            assertTrue(product.findElement(By.className("item-price")).isDisplayed());
            //Close the product window
            product.findElement(By.className("close-dialog")).click();

            //open products per page sub menu to test feature
            TestFunctions.waitForSiteXpath(browserWindow, productAmountPath+"div[1]/mat-form-field/div/div[1]/div/mat-select/div",true);

            //specific path can switch between 0 and 3 randomly, but regardless, select one of the product per page amounts
            try
            {
                //Try an potential option
                browserWindow.findElement(By.xpath("//*[@id=\"mat-option-"+0+"\"]")).click();
            }
            catch (NoSuchElementException exception)
            {
                //If the option was invalid, prepare to try the next one
                browserWindow.findElement(By.xpath("//*[@id=\"mat-option-" + 3 + "\"]")).click();
            }

            //navigate to next page of products
            TestFunctions.waitForSiteXpath(browserWindow, pageNav + "2]", true);
            assertEquals(browserWindow.findElement(By.xpath(productAmountPath+pageNumPath)).getText(),"13 – 24 of 36");
            //navigate to previous product page
            TestFunctions.waitForSiteXpath(browserWindow, pageNav+"1]", true);
            assertEquals(browserWindow.findElement(By.xpath(productAmountPath+pageNumPath)).getText(),"1 – 12 of 36");

        } finally
        {
            //end test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Confirm full functionality of product grid features, such as control of number of products displayed, and page navigation
     * @param testing What is being tested. Only used by test name
     * @param browserWindow Browser window to use for all tests
     * @param itemPerPage Amount of products per page to test
     * @throws IOException Thrown if a browser is not set for the test, or if no product amount was set
     * @throws InterruptedException Thrown if the test is interrupted during a thread sleep period,
     */
    @Test(
            groups = {"Sanity", "Product_View", "hasDataProvider"},
            priority = 61,
            dataProvider = "PV2_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PV2_Grid_Functionality(String testing, WebDriver browserWindow, int itemPerPage) throws InterruptedException, IOException
    {
        try
        {
            int select;
            //determine which web element to pick for the test
            try { select = itemPerPage == 12 ? 0 : itemPerPage == 24 ? 1 : itemPerPage == 36 ? 2 : null;}
            catch (NullPointerException exception) { throw new IOException("No Product per page Amount Set");}

            //amount of pages available
            int pageAmount = 36/itemPerPage + (itemPerPage == 24 ? 1 : 0);
            //amount of pages we will test, going forwards and then back through in reverse
            int pagesToCheck = 2*pageAmount-1;

            //select the amount of product per page to display
            TestFunctions.waitForSiteXpath(browserWindow, productAmountPath+"div[1]/mat-form-field/div/div[1]/div/mat-select/div",true);
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"mat-option-"+select+"\"]",true);

            //check each page available
            for(int page = 1; page <= pagesToCheck; page++)
            {
                //check each product in the product grid to confirm the correct amount is present
                for (int i = 1; i <= itemPerPage; i++)
                {
                    assertTrue(browserWindow.findElement(By.xpath(listElement + i + "]")).isDisplayed());
                    if(itemPerPage == 24 && page == 2 && i == 12)
                        break;
                }
                if(page < pageAmount) //go to the next page if we have not reached the end
                    TestFunctions.waitForSiteXpath(browserWindow, pageNav + "2]", true);
                else if(page >= pageAmount && page != pagesToCheck) //go to the previous page if we have reached the end
                    TestFunctions.waitForSiteXpath(browserWindow, pageNav + "1]", true);
            }

        } finally
        {
            if(itemPerPage == 36)
            {
                //end test
                Thread.sleep(TestFunctions.endTestWait);
                browserWindow.quit();
            }
        }
    }

    @Test(
            groups = {"Regression", "Product_View", "noDataProvider"},
            priority = 77,
            enabled = true
    )
    public void PV_Regression() throws InterruptedException, IOException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
                int[] temp = new int[]{5, 2, 1};
                for (int pages : temp)
                {
                    int select;
                    try
                    {
                        select = pages == 5 ? 0 : pages == 2 ? 1 : pages == 1 ? 2 : null;
                    } catch (NullPointerException exception)
                    {
                        try
                        {
                            select = pages == 5 ? 3 : pages == 2 ? 4 : pages == 1 ? 5 : null;
                        }
                        catch (NullPointerException badTest){ throw new IOException("No Product per page Amount Set");}
                        throw new IOException("No Product per page Amount Set");
                    }

                    //select the amount of product per page to display
                    TestFunctions.waitForSiteXpath(browserWindow, productAmountPath + "div[1]/mat-form-field/div/div[1]/div/mat-select/div", true);
                    TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"mat-option-" + select + "\"]", true);

                    for (int page = 1; page <= pages; page++)
                    {
                        TestFunctions.commonRegression(browserWindow, TestFunctions.website, false);
                        TestFunctions.waitForSiteXpath(browserWindow, listElement + "1]", true);
                        while(true)
                        {
                            try
                            {
                                browserWindow.findElement(By.className("mat-expansion-panel-header"));
                                break;
                            }
                            catch(NoSuchElementException ignore){}
                        }
                        ProductReviews.testReview(browserWindow);

                        WebElement product = browserWindow.findElement(By.xpath(productContainer));
                        product.findElement(By.className("close-dialog")).click();

                        TestFunctions.commonRegression(browserWindow, TestFunctions.website,  false);

                        if (page < pages) //go to the next page if we have not reached the end
                            TestFunctions.waitForSiteXpath(browserWindow, pageNav + "2]", true);
                        else if (page >= pages && page != pages) //go to the previous page if we have reached the end
                            TestFunctions.waitForSiteXpath(browserWindow, pageNav + "1]", true);
                    }

                }


        } finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
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
