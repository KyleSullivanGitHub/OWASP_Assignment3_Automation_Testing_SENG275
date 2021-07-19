package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.Method;

public class ProductReviews implements ITest
{
    private final ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;

    //Path to product list elements

    static String listElement = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[1]/figure/mat-card/div[1]";
    //Path to the container of a product
    static String productContainer = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details";
    static String reviewPath = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details/mat-dialog-content/div/mat-expansion-panel/div/div/div/div[";
    static String reviewContents = "]/div/div[1]/p";
    static String reviewNumPath = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details/mat-dialog-content/div/mat-expansion-panel/mat-expansion-panel-header/span[1]/mat-panel-title/span[2]";
    //Valid review input
    static String validInput = "Testing Review";
    static int numOfReviews, numOfReviews2;

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

    /**
     * Smoke test of Review functionality, including valid submission, and checking if the reiview actual appears
     * Programmer: Kyle Sullivan
     * @param chosenBrowser
     * @throws IOException
     * @throws InterruptedException
     */
    @Test(
            groups = {"Smoke", "Review Smoke", "Review Box", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RE1_Valid_Review(String chosenBrowser) throws IOException, InterruptedException
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

            fillReview(browserWindow,validInput);
            testReview(browserWindow);
            submitReview(browserWindow);

            WebElement product = browserWindow.findElement(By.xpath(productContainer));
            product.findElement(By.className("close-dialog")).click();

            TestFunctions.waitForSiteXpath(browserWindow, listElement,true);

            browserWindow.findElement(By.cssSelector("#mat-expansion-panel-header-1")).click();

            TestFunctions.waitForSiteXpath(browserWindow,reviewNumPath);
            checkReview(browserWindow,getNumOfReviews(browserWindow),numOfReviews2, validInput);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    @Test(
            groups = {"Smoke", "Review Smoke", "Review Box", "hasDataProvider"},
            priority = 0,
            dataProvider = "RE2_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RE2_Invalid_Review(String testing, String input) throws IOException, InterruptedException, UnsupportedFlavorException
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
            fillReview(browserWindow,input);
            if(input.equals(""))
            {
                assertEquals(browserWindow.findElement(By.cssSelector("#submitButton")).getAttribute("disabled"),"true");
            }
            else
            {
                WebElement reviewBox = browserWindow.findElement(By.xpath("//*[@id=\"mat-input-1\"]"));
                String supposedInput = input;

                for(int i = 0; i <= 50; i++)
                {
                    reviewBox.sendKeys(input);
                    supposedInput.concat(input);
                }
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

                Keys OSspecific = TestFunctions.OS.contains("win") ? Keys.CONTROL : Keys.COMMAND;
                reviewBox.sendKeys(OSspecific + "a");
                reviewBox.sendKeys(OSspecific + "c");

                //Confirm that the clipboard does not contain the password
                String shortendInput = (String) cb.getData(DataFlavor.stringFlavor);
                assertFalse(shortendInput.equals(supposedInput));
                assertTrue(shortendInput.length() == 160);
            }

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    public static void fillReview(WebDriver browserWindow, String input) throws InterruptedException
    {
        TestFunctions.login(browserWindow);
        //check product
        TestFunctions.waitForSiteXpath(browserWindow, listElement,true);
        //confirm the expanded view is on display
        TestFunctions.waitForSiteXpath(browserWindow, productContainer);
        //fill Review
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-1\"]")).sendKeys(input);
    }

    public static void testReview(WebDriver browserWindow) throws InterruptedException
    {
        int numOfReviews;

        numOfReviews = getNumOfReviews(browserWindow);
        browserWindow.findElement(By.cssSelector("#mat-expansion-panel-header-0")).click();
        //identify how many reviews are supposed to be present.
        numOfReviews = getNumOfReviews(browserWindow);
        //Check that there are actually that number of reviews
        for (int i = 1; i <= numOfReviews; i++)
        {
            try
            {
                assertTrue(browserWindow.findElement(By.xpath(reviewPath + i + "]")).isEnabled());
            } catch (NoSuchElementException ignore) {}
        }
    }
    public static void submitReview(WebDriver browserWindow) throws InterruptedException
    {
        browserWindow.findElement(By.cssSelector("#submitButton")).click();

        TestFunctions.waitForSiteXpath(browserWindow, reviewPath+numOfReviews+"]");

        numOfReviews2 = getNumOfReviews(browserWindow);
        checkReview(browserWindow, numOfReviews2, numOfReviews+1, validInput);

    }

    public static int getNumOfReviews(WebDriver browserWindow)
    {
        String reviewAmount = browserWindow.findElement(By.xpath(reviewNumPath)).getText();
        return Integer.parseInt(reviewAmount.substring(1,reviewAmount.length()-1));
    }

    public static void checkReview(WebDriver browserWindow, int actual, int previous, String expectedString)
    {
        assertEquals(actual, previous);
        assertEquals(browserWindow.findElement(By.xpath(reviewPath+actual+reviewContents)).getText(),expectedString);
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
