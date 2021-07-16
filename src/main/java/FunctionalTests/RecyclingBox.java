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
    String website = "https://juice-shop.herokuapp.com"; //default website URL

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
            threadPoolSize = 3,
            enabled = true
    )
    public void RB1_Valid_Usage(String chosenBrowser) throws IOException, InterruptedException
    {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {

            fillRecycling(browserWindow,new Object[]{10,true,false,false,""},"false");
            //hoverr over ordrs and payment
            //add a saved address
            //click recycle

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

        //navigate to recycling box
        //valid usage of recycling box
    }

    public void RB2_Invalid_Usage() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        fillRecycling(browserWindow,new Object[]{0,true,false,false,""},"true");



    }

    public void RB3_Invalid_Usage_Comprehensive(String testing, Object[] dataSet) throws InterruptedException
    {
    //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        fillRecycling(browserWindow,dataSet,"true");
    }

    public void RB4_Valid_Bulk_Usage(String testing, Object[] dataSet) throws InterruptedException
    {
    //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        fillRecycling(browserWindow,dataSet,"false");
    }

    public void RB_Regression()
    {
        //TODO Recycling Regression
        //UI
        //standard regression
        //quantity mandatory
    }

    private void navToRecycle(WebDriver browserWindow) throws InterruptedException
    {
        String xPathPart1 = "/html/body/div[3]/div[";
        String xPathPart2 = "]/div/div/div/button[2]";

        TestFunctions.login(browserWindow);
        browserWindow.findElement(By.cssSelector(TestFunctions.navPath)).click();
        Thread.sleep(100);
        browserWindow.findElement(By.xpath(xPathPart1+2+xPathPart2)).click();
        Thread.sleep(100);
        browserWindow.findElement(By.xpath(xPathPart1+3+xPathPart2)).click();
    }

    private void fillRecycling(WebDriver browserWindow, Object[] dataSet, String submitDisabled) throws InterruptedException
    {
        navToRecycle(browserWindow);
        TestFunctions.waitForSite(browserWindow,"#mat-input-11");
        browserWindow.findElement(By.cssSelector("#mat-input-11")).sendKeys((String)dataSet[0]);
        if((boolean) dataSet[1])
        {
            browserWindow.findElement(By.cssSelector("#mat-radio-38")).click();
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
        //validate button
        assertEquals(browserWindow.findElement(By.cssSelector("#recycleButton")).getAttribute("disabled"), submitDisabled);
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
