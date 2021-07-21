package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;


public class ProductSearching implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;

    //valid search input
    String validInput = "juice";
    //invalid search input
    String invalidInput = "jarbagard";
    //path to amount of products on page
    String productAmountPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/";
    //path to product element in product list
    String listElement = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[";
    //path to expanded product container
    String productContainer = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details";
    //path to product description
    String descXpath = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details/mat-dialog-content/div/div[1]/div[2]/div[1]";
    //common path to page navigation
    String pageNav = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/div[2]/button[";


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
     * Smoke test of valid searching
     * Programmer: Kyle Sullivan
     * @param chosenBrowser chosen browser for test
     * @throws InterruptedException Thrown if test is interrupted during thread waiting period
     * @throws IOException Thrown if the browser is not set for each test
     */
    @Test(
            groups = {"Smoke", "Product_Searching", "hasDataProvider"},
            priority = 12,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void SC1_Valid_Search(String chosenBrowser) throws InterruptedException, IOException
    {
        //set up environment
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        JavascriptExecutor scroll = (JavascriptExecutor) browserWindow;
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        try
        {
            int resultAmount;

            //fill out search method
            searching(browserWindow,validInput);
            //chech that the search value is correct
            TestFunctions.waitForSite(browserWindow,"#searchValue");
            assertEquals(validInput, browserWindow.findElement(By.id("searchValue")).getText());
            //check we are on the propper page
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"search?q="+validInput);

            //set to 36 products per page
            TestFunctions.waitForSiteXpath(browserWindow, productAmountPath+"div[1]/mat-form-field/div/div[1]/div/mat-select/div",true);
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"mat-option-"+5+"\"]",true);

            //find how many search results are present
            resultAmount = Integer.parseInt( browserWindow.findElement(By.className("mat-paginator-range-label")).getText().substring(5,6));

            //check each product matches the search criterai
            String validSearch = "Product contains text: " +validInput;
            String actualSearch;
            for (int i = 1; i <= resultAmount; i++)
            {
                actualSearch = "Product does not contain text: "+validInput;
                WebElement productDisplay = browserWindow.findElement(By.xpath(listElement+i+"]"));
                //scroll to product
                scroll.executeScript("arguments[0].scrollIntoView();",productDisplay);
                //check name is is title
                if(productDisplay.findElement(By.className("item-name")).getText().toLowerCase().contains(validInput))
                    actualSearch = validSearch;
                else
                {
                    //if not, check it is in expanded product description
                    productDisplay.click();
                    TestFunctions.waitForSiteXpath(browserWindow, productContainer);
                    WebElement product = browserWindow.findElement(By.xpath(productContainer));
                    try
                    {
                        if(product.findElement(By.xpath(descXpath)).getText().toLowerCase().contains(validInput))
                            actualSearch = validSearch;
                    } catch (NoSuchElementException invalidSearchResult) { assertEquals("Bad Product Format", "Valid Product Format"); }
                    //close box
                    product.findElement(By.className("close-dialog")).click();
                }
                assertEquals(actualSearch, validSearch);
            }
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Smoke test for invalid result, blank values
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if test is interrupted during thread waiting period
     */
    @Test(
            groups = {"Smoke", "Product_Searching", "noDataProvider"},
            priority = 13,
            enabled = true
    )
    public void SC2_Invalid_Search() throws InterruptedException
    {
        //set up test environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //fill out serach
            searching(browserWindow, invalidInput);
            TestFunctions.waitForSite(browserWindow,"#searchValue");
            //check the correct input is being searched
            assertEquals(invalidInput, browserWindow.findElement(By.id("searchValue")).getText());
            //confirm we are on the correct page
            assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website+"search?q="+ invalidInput);
            //check the error message is present
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-card/mat-card-title/span")).getText(),"No results found");
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test for a comprehensive test of the search function
     * Programmer: Kyle Sullivan
     * @param testing what we are testing
     * @param input what to input
     * @throws InterruptedException thrown if the test is interrupted during a thread waiting period
     */
    @Test(
            groups = {"Sanity", "Product_Searching", "hasDataProvider"},
            priority = 62,
            dataProvider = "SC3_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void SC3_Invalid_Search_Comprehensive(String testing, String input) throws InterruptedException
    {
        //set up test environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //use search function
            searching(browserWindow, input);
            //if blank input
            if(input == "")
            {
                //confirm that the search has taken us to the main search page
                TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[1]/div[1]");
                assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"search");
            }
            else
            {
                //same as SC2
                TestFunctions.waitForSite(browserWindow,"#searchValue");
                assertEquals(invalidInput, browserWindow.findElement(By.id("searchValue")).getText());
                assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website+"search?q="+ invalidInput);
                assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-card/mat-card-title/span")).getText(),"No results found");
            }
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Regression test for search feature
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if test is interrupted during a thread waiting period
     * @throws IOException Thrown if no browser is selected for test
     */
    @Test(
            groups = {"Regression","Product_Searching", "noDataProvider"},
            priority = 78,
            enabled = true
    )
    public void SC_Regression() throws InterruptedException, IOException
    {
        //Setup Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        try
        {
            //for a valid input and an invalid input
            for(int isValid = 1; isValid <= 2; isValid++)
            {
                //set input for test
                String input = validInput;
                if(isValid == 2)
                    input = invalidInput;

                //use search feature
                searching(browserWindow, input);
                //wait until page is ready
                TestFunctions.waitForSite(browserWindow, "#searchValue");


                //check how many products have been found
                int select;
                TestFunctions.waitForSiteXpath(browserWindow, productAmountPath + "div[1]/mat-form-field/div/div[1]/div/mat-select/div", true);
                TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"mat-option-" + 5 + "\"]", true);

                int resultAmount = Integer.parseInt(browserWindow.findElement(By.className("mat-paginator-range-label")).getText().substring(5, 6));

                //repeat test for each potential amount of set products per page
                int[] productPerPage = new int[]{12, 24, 36};
                for (int perPage : productPerPage)
                {
                    //set product per page selecter value and set products per page
                    select = perPage == 12 ? 3 : perPage == 24 ? 4 : perPage == 36 ? 5 : null;
                    TestFunctions.waitForSiteXpath(browserWindow, productAmountPath + "div[1]/mat-form-field/div/div[1]/div/mat-select/div", true);
                    TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"mat-option-" + select + "\"]", true);

                    //check how many pages are present
                    int temp = resultAmount / perPage + (resultAmount % perPage > 0 ? 1 : 0);
                    int pages = 2 * temp - 1;

                    //loop through all pages, and then back in reverse order
                    for (int i = 1; i <= pages; i++)
                    {
                        //test common regression features.
                        TestFunctions.commonRegression(browserWindow, TestFunctions.website + "search?q=" + input, false);
                        //checke every product on every page, and make sure they are present. if you are on the last page of the forward run, check the remainder products
                        int limit = i == temp ? resultAmount % perPage : perPage;
                        for (int j = 1; j <= limit; j++)
                            assertTrue(browserWindow.findElement(By.xpath(listElement + j + "]")).isDisplayed());
                        if (i < temp) //go to the next page if we have not reached the end
                            TestFunctions.waitForSiteXpath(browserWindow, pageNav + "2]", true);
                        else if (i >= temp && i != pages) //go to the previous page if we have reached the end
                            TestFunctions.waitForSiteXpath(browserWindow, pageNav + "1]", true);
                    }
                }
                browserWindow.findElement(By.className("mat-search_icon-close")).click();
            }
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Fillout and use the search function
     * Programmer: Kyle Sullivan
     * @param browserWindow window to perform tests in
     * @param toSearchFor search inptu
     * @throws InterruptedException Thrown if interrupted during a thread waiting period
     */
    public void searching(WebDriver browserWindow, String toSearchFor) throws InterruptedException
    {
        //activate the search bar
        TestFunctions.waitForSite(browserWindow,"mat-icon.mat-icon:nth-child(2)",true);
        //wait until the input box is ready
        TestFunctions.waitForSite(browserWindow,"#mat-input-0");
        //enter the search value and search
        browserWindow.findElement(By.id("mat-input-0")).sendKeys(toSearchFor + Keys.ENTER);
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
