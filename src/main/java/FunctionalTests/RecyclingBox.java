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
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

public class RecyclingBox implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;
    WebDriver browserWindow;
    boolean inTest = false; //Whether we are currently in a test
    int counter = 0; //current point in a data provider

    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @BeforeClass
    public void SetUp() throws IOException, InterruptedException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

    /**
     * Smoke test to test valid usage of recycling feature
     * Programmer: Kyle Sullivan
     * @param chosenBrowser browser to use for this test
     * @throws IOException Thrown if no browser selected
     * @throws InterruptedException Thrown if test is interrupted during a thread waiting period
     */
    @Test(
            groups = {"Smoke", "Recycling Box Smoke", "Recycling Box", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RB1_Valid_Usage(String chosenBrowser) throws IOException, InterruptedException
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
            //navigate to recycling
            navToRecycle(browserWindow);
            //fill it out with a correct set of inputs
            fillRecycling(browserWindow,new Object[]{10,true,false,false,""});
            //validate that the recycling feature worked
            validateRecycling(browserWindow);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Smoke test of an invalid usage of the recycling feature
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if the test is interrupted during a thread waiting period
     * @throws IOException Thrown if no browser is selected for test
     */
    @Test(
            groups = {"Smoke", "Recycling Box Smoke", "Recycling Box","noDataProvider"},
            priority = 0,
            enabled = true
    )
    public void RB2_Invalid_Usage() throws InterruptedException, IOException
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
            //navigate to recycling
            navToRecycle(browserWindow);
            //fill out recycling with invalid inputs
            fillRecycling(browserWindow,new Object[]{0,true,false,false,""});
            //confirm the submit button is disabled
            assertEquals(browserWindow.findElement(By.cssSelector("#recycleButton")).getAttribute("disabled"), "true");

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    /**
     * Sanity test for invalid usage of recycling feature
     * Programmer: Kyle Sullivan
     * @param testing What this test is testing. used in the test name
     * @param dataSet Set of inputs for this test
     * @throws InterruptedException thrown if test was interrupted during a thread waiting period
     * @throws IOException Thrown if no browser was set for this test
     */
    @Test(
            groups = {"Sanity", "Recycling Box Sanity", "Recycling Box", "hasDataProvider"},
            priority = 0,
            dataProvider = "RB3_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RB3_Invalid_Usage_Comprehensive(String testing, Object[] dataSet) throws InterruptedException, IOException
    {
        //for the first test:
        if(!inTest)
        {
            //Create Test environment and browser
            browserWindow = environment.makeDriver();
            browserWindow.manage().window().maximize();
            //Go to Website
            browserWindow.get(TestFunctions.website);
            //Ensure the site is ready for testing
            TestFunctions.waitForSite(browserWindow);
            inTest = true;
            counter = 0;
        }

        try
        {
            //navigate to recycling
            navToRecycle(browserWindow);
            //fill it out
            fillRecycling(browserWindow,dataSet);
            //confirm that that the recycling button is disabled
            assertEquals((browserWindow.findElement(By.cssSelector("#recycleButton")).getAttribute("disabled") == null ? "false" : "true"), "true");
            counter++;
        }
        catch (Exception badTest)
        {
            inTest = false;
        }
        finally
        {
            if(!inTest || counter == 7)
            {
                Thread.sleep(TestFunctions.endTestWait);
                browserWindow.quit();
                inTest = false;
            }
        }
    }

    /**
     * Test extra functionality for bulk orders, specifically the pickup date setting
     * Programmer: Kyle Sullivan
     * @param testing Test purpose, used in test name.
     * @param dataSet Data set for test
     * @throws InterruptedException thrown if test was interrupted during a thread waiting period
     * @throws IOException Thrown if no browser was set for this test
     */
    @Test(
            groups = {"Sanity", "Recycling Box Sanity", "Recycling Box", "hasDataProvider"},
            priority = 0,
            dataProvider = "RB4_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RB4_Valid_Bulk_Usage(String testing, Object[] dataSet) throws InterruptedException, IOException
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
            //navigate ot recycling
            navToRecycle(browserWindow);
            //fill out recycling feature
            fillRecycling(browserWindow,dataSet);
            //validate correct results
            validateRecycling(browserWindow);

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Regression test for Recycling Feature
     * Programmer: Kyle Sullivan
     * @throws InterruptedException thrown if test was interrupted during a thread waiting period
     * @throws IOException Thrown if no browser was set for this test
     */
    @Test(
            groups = {"Regression", "Recycling Box Regression", "Recycling Box","noDataProvider"},
            priority = 0,
            enabled = false
    )
    public void RB_Regression() throws InterruptedException, IOException
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
            //navigate to recycling page
            navToRecycle(browserWindow);

            //test common regression features
            TestFunctions.commonRegression(browserWindow, TestFunctions.website+"recycle", true);

            //get quantity input placeholder
            assertEquals(browserWindow.findElement(By.cssSelector("mat-label.ng-tns-c126-10")).getText(),"Quantity");

            WebElement inputBox;

            //find input box, be it alternate or primary
            try
            {
                inputBox = browserWindow.findElement(By.cssSelector(TestFunctions.mInput + "2"));
            } catch (NoSuchElementException tryOther)
            {
                inputBox = browserWindow.findElement(By.cssSelector(TestFunctions.mInput + "3"));
            }
            inputBox.click();
            //check quanityt box placeholder when selected
            assertEquals(inputBox.getAttribute("data-placeholder"),"...in liters");
            try
            {
                //click out of the box
                browserWindow.findElement(By.id("recycle-form")).click();
            }
            catch (Exception ignore){}
            assertEquals(inputBox.getAttribute("aria-invalid"),"false");

            //test invalid input
            fillRecycling(browserWindow,new Object[]{0,true,false,false,""});
            TestFunctions.commonRegression(browserWindow,TestFunctions.website+"recycle", true);

            //test valid input
            fillRecycling(browserWindow,new Object[]{101,true,true,true,"tr.ng-star-inserted:nth-child(5) > td:nth-child(6)"});
            TestFunctions.commonRegression(browserWindow, TestFunctions.website+"recycle", true);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            //browserWindow.quit();
        }
    }

    /**
     * Method to navigate to recycling page
     * Programmer: Kyle Sullivan
     * @param browserWindow Browser window for test
     * @throws InterruptedException Thrown if test was interrupted during thread waiting period
     */
    private void navToRecycle(WebDriver browserWindow) throws InterruptedException
    {
        //If we are currently not on the recyling page
        if(!browserWindow.getCurrentUrl().equals(TestFunctions.website+"recycle"))
        {
            String xPathPart1 = "/html/body/div[3]/div[";
            String xPathPart2 = "]/div/div/div/button[2]";
            //login
            TestFunctions.login(browserWindow);
            TestFunctions.waitForSite(browserWindow, TestFunctions.navPath, true);
            Thread.sleep(500);

            //repeat until element is found
            boolean notFound = true;
            while (notFound)
            {
                try
                {
                    //default account menu path to recycling page
                    browserWindow.findElement(By.xpath(xPathPart1 + 2 + xPathPart2)).click();
                    Thread.sleep(200);
                    browserWindow.findElement(By.xpath(xPathPart1 + 3 + xPathPart2)).click();
                    notFound = false;
                } catch (NoSuchElementException tryOther)
                {
                    //alternate account menu path
                    browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
                    Thread.sleep(500);
                    browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[2]")).click();
                    notFound = false;
                }
            }
        }
        else //if already on recycling page, refresh the page
        {
            browserWindow.navigate().refresh();
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);
        }
    }

    /**
     * Method to fill out input fields on recycling box
     * Programmer: Kyle Sullivan
     * @param browserWindow browser window to perform actions in
     * @param dataSet data set containing inputs
     * @throws InterruptedException thrown if test was interrupted during a thread waiting period
     * @throws IOException Thrown if no browser was set for this test
     */
    private void fillRecycling(WebDriver browserWindow, Object[] dataSet) throws InterruptedException, IOException
    {
        Thread.sleep(3000);
        boolean notClicked = true;
        WebElement inputBox;

        //select the quantity filed, by the default or by the alternative if the default is not found.
        while(notClicked)
        {
            try
            {
                inputBox = browserWindow.findElement(By.cssSelector(TestFunctions.mInput + "2"));
                notClicked = false;
            } catch (NoSuchElementException tryOther)
            {
                inputBox = browserWindow.findElement(By.cssSelector(TestFunctions.mInput + "3"));
                notClicked = false;
            }
            inputBox.click();
            //Send the input
            inputBox.sendKeys(""+dataSet[0]);
        }
        //if we are testing address portion of the page
        if((boolean) dataSet[1])
        {
            //check if there are any addresses already present
            try
            {
                //if so, select one
                browserWindow.findElement(By.cssSelector(TestFunctions.mRadio+"38")).click();
            }
            catch (Exception NoSuchElementException)
            {
                //if not, create a new set of addresses in a separate window, and refresh page
                TestFunctions.createAddress();
                browserWindow.navigate().refresh();
                TestFunctions.waitForSite(browserWindow,TestFunctions.mRadio+"38",true);
            }
        }
        //if the quantity input is over 100
        if((int)dataSet[0] > 100)
        {
            //if we are testing bulk entities
            if((boolean) dataSet[2])
            {
                //try to find the bulk order checkbox. Randomly changes, so several checks may be neccessary
                for(int i = 0; i < 10; i++)
                {
                    try {TestFunctions.waitForSite(browserWindow,"#mat-checkbox-"+i,true);}
                    catch (NoSuchElementException ignored ) {}
                }
                //if we are inputing a date...
                if((boolean) dataSet[3])
                {
                    //select the date menu
                    TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-recycle/mat-card/div[1]/div/mat-form-field[3]/div/div[1]/div[4]/mat-datepicker-toggle",true);
                    //select a date
                    TestFunctions.waitForSite(browserWindow,(String)dataSet[4],true);
                }
            }
        }
    }

    /**
     * Method to validate whether the order was placed
     * Programmer: Kyle Sullivan
     * @param browserWindow browser window to perform operations in
     * @throws InterruptedException Thrown if the test was interrupted during a thread waiting period
     */
    private void validateRecycling(WebDriver browserWindow) throws InterruptedException
    {
        //press the submitt button
        TestFunctions.waitForSite(browserWindow,"#recycleButton",true);
        Thread.sleep(1000);
        boolean foundPopup = false;
        //check the popup is present. May need several checks, due to site setup
        for(int i = 0; i < 20; i++)
        {
            try
            {
                if(browserWindow.findElement(By.cssSelector("#cdk-overlay-"+i)).isDisplayed())
                {
                    foundPopup = true;
                    break;
                }
            }
            catch (NoSuchElementException ignored){}
        }
        assertTrue(foundPopup);
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
