package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import java.io.IOException;
import java.lang.reflect.Method;

public class RecyclingBox implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;

    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @BeforeSuite
    public void SetUp() throws IOException, InterruptedException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

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
            navToRecycle(browserWindow);
            fillRecycling(browserWindow,new Object[]{10,true,false,false,""});
            browserWindow.findElement(By.cssSelector("#recycleButton")).click();
            Thread.sleep(500);
            assertTrue(browserWindow.findElement(By.cssSelector("#cdk-overlay-0")).isDisplayed());
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    @Test(
            groups = {"Smoke", "Recycling Box Smoke", "Recycling Box"},
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
            navToRecycle(browserWindow);
            fillRecycling(browserWindow,new Object[]{0,true,false,false,""});
            assertEquals(browserWindow.findElement(By.cssSelector("#recycleButton")).getAttribute("disabled"), "true");

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    @Test(
            groups = {"Sanity", "Recycling Box Sanity", "Recycling Box", "hasDataProvider"},
            priority = 0,
            dataProvider = "RB3_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RB3_Invalid_Usage_Comprehensive(String testing, Object[] dataSet) throws InterruptedException, IOException
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
            navToRecycle(browserWindow);
            fillRecycling(browserWindow,dataSet);
            assertEquals(browserWindow.findElement(By.cssSelector("#recycleButton")).getAttribute("disabled"), "true");
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

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
            navToRecycle(browserWindow);
            fillRecycling(browserWindow,dataSet);
            //validate button
            browserWindow.findElement(By.cssSelector("#recycleButton")).click();
            assertTrue(browserWindow.findElement(By.cssSelector("#cdk-overlay-0")).isDisplayed());
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    @Test(
            groups = {"Regression", "Recycling Box Regression", "Recycling Box"},
            priority = 0,
            enabled = false
    )
    public void RB_Regression() throws InterruptedException, IOException
    {
        //TODO Recycling Regression
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            navToRecycle(browserWindow);
            Object[] UI = new Object[]{"get URL", "Get Header", "Get title"};

            TestFunctions.commonRegression(browserWindow, UI, true);

            //Test Place holder parts
            //test for red
            browserWindow.findElement(By.cssSelector(TestFunctions.mInput+"11")).sendKeys("");
            //check box has gone red

            //test invalid input
            fillRecycling(browserWindow,new Object[]{0,true,false,false,""});
            TestFunctions.commonRegression(browserWindow, UI, true);

            //test valid input
            fillRecycling(browserWindow,new Object[]{1001,true,true,true,"/html/body/div[3]/div[2]/div/mat-datepicker-content/div[2]/mat-calendar/div/mat-month-view/table/tbody/tr[3]/td[4]"});
            TestFunctions.commonRegression(browserWindow, UI, true);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    private void navToRecycle(WebDriver browserWindow) throws InterruptedException
    {
        String xPathPart1 = "/html/body/div[3]/div[";
        String xPathPart2 = "]/div/div/div/button[2]";

        TestFunctions.login(browserWindow);
        browserWindow.findElement(By.cssSelector(TestFunctions.navPath)).click();
        Thread.sleep(500);
        browserWindow.findElement(By.xpath(xPathPart1+2+xPathPart2)).click();
        Thread.sleep(500);
        browserWindow.findElement(By.xpath(xPathPart1+3+xPathPart2)).click();
    }

    private void fillRecycling(WebDriver browserWindow, Object[] dataSet) throws InterruptedException, IOException
    {
        TestFunctions.waitForSite(browserWindow,TestFunctions.mInput+"2");
        browserWindow.findElement(By.cssSelector(TestFunctions.mInput+"2")).sendKeys(""+dataSet[0]);
        if((boolean) dataSet[1])
        {
            try
            {
                browserWindow.findElement(By.cssSelector(TestFunctions.mRadio+"38")).click();
            }
            catch (Exception NoSuchElementException)
            {
                TestFunctions.createAddress();
                browserWindow.navigate().refresh();
                TestFunctions.waitForSite(browserWindow,TestFunctions.mRadio+"38");
                browserWindow.findElement(By.cssSelector(TestFunctions.mRadio+"38")).click();
            }
        }
        if((int)dataSet[0] > 100)
        {
            if((boolean) dataSet[2])
            {
                browserWindow.findElement(By.cssSelector("#mat-checkbox-7")).click();
                if((boolean) dataSet[3])
                {
                 browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-recycle/mat-card/div[1]/div/mat-form-field[3]/div/div[1]/div[4]/mat-datepicker-toggle")).click();
                 browserWindow.findElement(By.xpath((String)dataSet[4])).click();
                }
            }
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
