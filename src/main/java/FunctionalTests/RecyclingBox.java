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

import static org.testng.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

public class RecyclingBox implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;
    WebDriver browserWindow;
    boolean inTest = false;
    int counter = 0;

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

    @Test(
            groups = {"Smoke", "Recycling Box", "hasDataProvider"},
            priority = 47,
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
            validateRecycling(browserWindow);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }


    @Test(
            groups = {"Smoke", "Recycling Box","noDataProvider"},
            priority = 48,
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
            groups = {"Sanity", "Recycling Box", "hasDataProvider"},
            priority = 73,
            dataProvider = "RB3_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RB3_Invalid_Usage_Comprehensive(String testing, Object[] dataSet) throws InterruptedException, IOException
    {
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
            navToRecycle(browserWindow);
            fillRecycling(browserWindow,dataSet);
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

    @Test(
            groups = {"Sanity", "Recycling Box Sanity", "Recycling Box", "hasDataProvider"},
            priority = 73,
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
            validateRecycling(browserWindow);

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    @Test(
            groups = {"Regression", "Recycling Box","noDataProvider"},
            priority = 91,
            enabled = true
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
            navToRecycle(browserWindow);

            TestFunctions.commonRegression(browserWindow, TestFunctions.website+"recycle", true);

            assertEquals(browserWindow.findElement(By.cssSelector("mat-label.ng-tns-c126-10")).getText(),"Quantity");

            WebElement inputBox;
            //test for red

            try
            {
                inputBox = browserWindow.findElement(By.cssSelector(TestFunctions.mInput + "2"));
            } catch (NoSuchElementException tryOther)
            {
                inputBox = browserWindow.findElement(By.cssSelector(TestFunctions.mInput + "3"));
            }
            inputBox.click();
            assertEquals(inputBox.getAttribute("data-placeholder"),"...in liters");
            try
            {
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

    private void navToRecycle(WebDriver browserWindow) throws InterruptedException
    {

        if(!browserWindow.getCurrentUrl().equals(TestFunctions.website+"recycle"))
        {
            String xPathPart1 = "/html/body/div[3]/div[";
            String xPathPart2 = "]/div/div/div/button[2]";
            TestFunctions.login(browserWindow);
            TestFunctions.waitForSite(browserWindow, TestFunctions.navPath, true);
            Thread.sleep(500);

            boolean notFound = true;
            while (notFound)
            {
                try
                {
                    browserWindow.findElement(By.xpath(xPathPart1 + 2 + xPathPart2)).click();
                    Thread.sleep(200);
                    browserWindow.findElement(By.xpath(xPathPart1 + 3 + xPathPart2)).click();
                    notFound = false;
                } catch (NoSuchElementException tryOther)
                {
                    browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
                    Thread.sleep(500);
                    browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[2]")).click();
                    notFound = false;
                }
            }
        }
        else
        {
            browserWindow.navigate().refresh();
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);
        }
    }

    private void fillRecycling(WebDriver browserWindow, Object[] dataSet) throws InterruptedException, IOException
    {
        Thread.sleep(3000);
        boolean notClicked = true;
        WebElement inputBox;
        //test for red
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
            inputBox.sendKeys(""+dataSet[0]);
        }
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
                TestFunctions.waitForSite(browserWindow,TestFunctions.mRadio+"38",true);
            }
        }
        if((int)dataSet[0] > 100)
        {
            if((boolean) dataSet[2])
            {
                for(int i = 0; i < 10; i++)
                {
                    try {browserWindow.findElement(By.cssSelector( "#mat-checkbox-"+i)).click(); break;}
                    catch (NoSuchElementException ignored ) {}
                }
                if((boolean) dataSet[3])
                {
                    TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-recycle/mat-card/div[1]/div/mat-form-field[3]/div/div[1]/div[4]/mat-datepicker-toggle",true);
                    TestFunctions.waitForSite(browserWindow,(String)dataSet[4],true);
                }
            }
        }

    }
    private void validateRecycling(WebDriver browserWindow) throws InterruptedException
    {
        TestFunctions.waitForSite(browserWindow,"#recycleButton",true);
        Thread.sleep(1000);
        boolean foundPopup = false;
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
