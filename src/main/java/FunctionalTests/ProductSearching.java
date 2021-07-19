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


public class ProductSearching implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;
    String validInput = "juice";
    String invalidInput = "jarbagard";
    String productAmountPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-paginator/div/div/";
    String listElement = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[";
    String productContainer = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details";
    String descXpath = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details/mat-dialog-content/div/div[1]/div[2]/div[1]";
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
            groups = {"Smoke", "Product_Searching", "hasDataProvider"},
            priority = 12,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void SC1_Valid_Search(String chosenBrowser) throws InterruptedException, IOException
    {
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

            searching(browserWindow,validInput);
            TestFunctions.waitForSite(browserWindow,"#searchValue");
            assertEquals(validInput, browserWindow.findElement(By.id("searchValue")).getText());
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"search?q="+validInput);

            TestFunctions.waitForSiteXpath(browserWindow, productAmountPath+"div[1]/mat-form-field/div/div[1]/div/mat-select/div",true);
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"mat-option-"+5+"\"]",true);

            resultAmount = Integer.parseInt( browserWindow.findElement(By.className("mat-paginator-range-label")).getText().substring(5,6));

            String validSearch = "Product contains text: " +validInput;
            String actualSearch;
            for (int i = 1; i <= resultAmount; i++)
            {
                actualSearch = "Product does not contain text: "+validInput;
                WebElement productDisplay = browserWindow.findElement(By.xpath(listElement+i+"]"));
                scroll.executeScript("arguments[0].scrollIntoView();",productDisplay);
                if(productDisplay.findElement(By.className("item-name")).getText().toLowerCase().contains(validInput))
                    actualSearch = validSearch;
                else
                {
                    productDisplay.click();
                    TestFunctions.waitForSiteXpath(browserWindow, productContainer);
                    WebElement product = browserWindow.findElement(By.xpath(productContainer));
                    try
                    {
                        if(product.findElement(By.xpath(descXpath)).getText().toLowerCase().contains(validInput))
                            actualSearch = validSearch;
                    } catch (NoSuchElementException invalidSearchResult) { assertEquals("Bad Product Format", "Valid Product Format"); }
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

    @Test(
            groups = {"Smoke", "Product_Searching", "noDataProvider"},
            priority = 13,
            enabled = true
    )
    public void SC2_Invalid_Search() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            searching(browserWindow, invalidInput);
            TestFunctions.waitForSite(browserWindow,"#searchValue");
            assertEquals(invalidInput, browserWindow.findElement(By.id("searchValue")).getText());
            assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website+"search?q="+ invalidInput);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/mat-card/mat-card-title/span")).getText(),"No results found");
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    @Test(
            groups = {"Sanity", "Product_Searching", "hasDataProvider"},
            priority = 62,
            dataProvider = "SC3_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void SC3_Invalid_Search_Comprehensive(String testing, String input) throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            searching(browserWindow, input);
            if(input == "")
            {
                TestFunctions.waitForSite(browserWindow,"#search-results-heading");
                assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"search");
            }
            else
            {
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

    @Test(
            groups = {"Regression","Product_Searching", "noDataProvider"},
            priority = 78,
            enabled = true
    )
    public void SC_Regression() throws InterruptedException, IOException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        try
        {
            for(int isValid = 1; isValid <= 2; isValid++)
            {
                String input = validInput;
                if(isValid == 2)
                    input = invalidInput;

                searching(browserWindow, input);
                TestFunctions.waitForSite(browserWindow, "#searchValue");

                int[] productPerPage = new int[]{12, 24, 36};
                int select;

                TestFunctions.waitForSiteXpath(browserWindow, productAmountPath + "div[1]/mat-form-field/div/div[1]/div/mat-select/div", true);
                TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"mat-option-" + 5 + "\"]", true);

                int resultAmount = Integer.parseInt(browserWindow.findElement(By.className("mat-paginator-range-label")).getText().substring(5, 6));

                for (int perPage : productPerPage)
                {
                    select = perPage == 12 ? 3 : perPage == 24 ? 4 : perPage == 36 ? 5 : null;

                    TestFunctions.waitForSiteXpath(browserWindow, productAmountPath + "div[1]/mat-form-field/div/div[1]/div/mat-select/div", true);
                    TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"mat-option-" + select + "\"]", true);

                    int temp = resultAmount / perPage + (resultAmount % perPage > 0 ? 1 : 0);
                    int pages = 2 * temp - 1;

                    for (int i = 1; i <= pages; i++)
                    {
                        TestFunctions.commonRegression(browserWindow, TestFunctions.website + "search?p=" + input, false);
                        int limit = i == temp ? resultAmount % perPage : perPage;
                        for (int j = 1; j <= limit; j++)
                            assertTrue(browserWindow.findElement(By.xpath(listElement + j + "]")).isDisplayed());
                        if (i < temp) //go to the next page if we have not reached the end
                            TestFunctions.waitForSiteXpath(browserWindow, pageNav + "2]", true);
                        else if (i >= temp && i != pages) //go to the previous page if we have reached the end
                            TestFunctions.waitForSiteXpath(browserWindow, pageNav + "1]", true);
                    }
                }
            }
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    public void searching(WebDriver browserWindow, String toSearchFor) throws InterruptedException
    {
        browserWindow.findElement(By.cssSelector("mat-icon.mat-icon:nth-child(2)")).click();
        TestFunctions.waitForSite(browserWindow,"#mat-input-0");
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
