package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
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
    WebDriver browserWindow;

    //Path to product list elements
    static String listElement = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[1]/figure/mat-card/div[1]";
    //Path to the container of a product
    static String productContainer = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details";
    //Alternate path due to random swithcehs
    static String productContainerAlt = "/html/body/div[4]/div[2]/div/mat-dialog-container/app-product-details";
    //Path to review text box
    static String reviewPathMain = "/html/body/div[3]/div[2]/div/mat-dialog-container/app-product-details/mat-dialog-content/div/mat-expansion-panel/div/div/div/div[";
    //Alternate path to review textbox
    static String reviewPathAlt = "/html/body/div[4]/div[2]/div/mat-dialog-container/app-product-details/mat-dialog-content/div/mat-expansion-panel/div/div/div/div[";
    //path to a review's text contents
    static String reviewContents = "]/div/div[1]/p";
    //primary path to the review count headers number of reviews
    static String reviewNumPath = "//*[@id=\"mat-expansion-panel-header-0\"]/span[1]/mat-panel-title/span[2]";
    //Alternate path
    static String reviewNumPathAlt = "//*[@id=\"mat-expansion-panel-header-1\"]/span[1]/mat-panel-title/span[2]";
    //Set inital reivew text box path
    static String reviewPath = reviewPathMain;
    //Valid review input
    static String validInput = "Testing Review";
    //counters for number of reviews. 1 for before submitting a review. 2 for after submitting
    static int numOfReviews, numOfReviews2;
    //detection for whether in test or not
    boolean inTest = false;
    //counter for tests
    int counter = 0;

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Kyle Sullivan
     */
    @BeforeClass
    public void SetUp() throws IOException, InterruptedException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

    /**
     * Smoke test of Review functionality, including valid submission, and checking if the review actual appears
     * Programmer: Kyle Sullivan
     * @param chosenBrowser Browser string for test browser
     * @throws IOException Thrown if no browser selected
     * @throws InterruptedException Thrown if the test is interrupted during a string waiting period
     */
    @Test(
            groups = {"Smoke", "Review", "hasDataProvider"},
            priority = 31,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RE1_Valid_Review(String chosenBrowser) throws IOException, InterruptedException
    {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        JavascriptExecutor scroll = (JavascriptExecutor) browserWindow;
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        reviewPath = reviewPathMain;
        try
        {
            //Fill out the review form
            fillReview(browserWindow,validInput);
            //test that the review counter is working
            testReview(browserWindow);
            //submit the review and check that the counter has increased
            submitReview(browserWindow);
            WebElement product;
            //find the product container
            while(true)
            {
                try { try
                {
                    product = browserWindow.findElement(By.xpath(productContainer));
                    break;
                } catch (NoSuchElementException tryAlt)
                {
                    product = browserWindow.findElement(By.xpath(productContainerAlt));
                    break;
                }
                } catch (Exception ignore){}
            }
            //close the product window
            WebElement closeButton = product.findElement(By.className("close-dialog"));
            scroll.executeScript("arguments[0].scrollIntoView();",closeButton);
            closeButton.click();

            //reopen the product window
            TestFunctions.waitForSiteXpath(browserWindow, listElement,true);
            Thread.sleep(1000);

            //open up the preview window
            browserWindow.findElement(By.cssSelector("#mat-expansion-panel-header-1")).click();

            //find the review number path
            while (true)
            {
                try
                {
                    browserWindow.findElement(By.xpath(reviewNumPath));
                    break;
                } catch (NoSuchElementException tryAlt)
                {
                    browserWindow.findElement(By.xpath(reviewNumPathAlt));
                    break;
                }
            }
            //check that the new review is present, and the counter has increased
            checkReview(browserWindow,getNumOfReviews(browserWindow),numOfReviews2, validInput);
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Smoke test to check invalid review case
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Review", "noDataProvider"},
            priority = 32,
            enabled = true
    )
    public void RE2_Invalid_Review() throws InterruptedException
    {
        //Create Test environment and browser
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Go to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        String input = "";
        reviewPath = reviewPathMain;
        try
        {
            //fill out review window
            fillReview(browserWindow,input);
            //confirm that the empty review cannot be sent
            assertEquals(browserWindow.findElement(By.cssSelector("#submitButton")).getAttribute("disabled"),"true");
        }
        finally
        {
            //End Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Sanity test to test all types of invalid inputs into the review feature
     * @param testing Test being performed. Used in the test name
     * @param input input string to be placed in review box.
     * @throws InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Sanity", "Review", "hasDataProvider"},
            priority = 67,
            dataProvider = "RE2_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RE3_Invalid_Review_Comprehensive(String testing, String input) throws InterruptedException
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
        reviewPath = reviewPathMain;

        try
        {
            //fill out reivew
            fillReview(browserWindow,input);
            //if it is the blank test
            if(input.equals(""))
            {
                //check that the submit button is disabled
                assertEquals(browserWindow.findElement(By.cssSelector("#submitButton")).getAttribute("disabled"),"true");
            }
            else
            {
                //else, target the review box
                WebElement reviewBox = browserWindow.findElement(By.xpath("//*[@id=\"mat-input-1\"]"));

                //Spam an input into the reivew box, and keep track of what has "supposedly" been put in  there
                String supposedInput = input;
                for(int i = 0; i <= 50; i++)
                {
                    reviewBox.sendKeys(input);
                    supposedInput.concat(input);
                }

                //copy the actual contents of the review box
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                Keys OSspecific = TestFunctions.OS.contains("win") ? Keys.CONTROL : Keys.COMMAND;
                reviewBox.sendKeys(OSspecific + "a");
                reviewBox.sendKeys(OSspecific + "c");

                //Confirm that the review box limited the input to 160 characters.
                String shortendInput = (String) cb.getData(DataFlavor.stringFlavor);
                assertNotEquals(supposedInput, shortendInput);
                assertEquals(shortendInput.length(), 160);
            }
            counter++;
        }
        catch (Exception badTest)
        {
            inTest = false;
        }
        finally
        {
            if(!inTest || counter == 2)
            {
                Thread.sleep(TestFunctions.endTestWait);
                browserWindow.quit();
                inTest = false;
            }
        }
    }

    /**
     * method to reach and fill out review page
     * Programmer: Kyle Sullivan
     * @param browserWindow test window to perform method in
     * @param input string to input into registration box
     * @throws InterruptedException Thrown if test is interruped during a thread waiting period
     */
    public static void fillReview(WebDriver browserWindow, String input) throws InterruptedException
    {
        //login
        TestFunctions.login(browserWindow);
        //check product
        TestFunctions.waitForSiteXpath(browserWindow, listElement,true);
        //confirm the expanded view is on display
        TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"mat-dialog-0\"]");
        System.out.println("clicked found display");
        //fill Review
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-1\"]")).sendKeys(input);
        System.out.println("sent keyes");
    }

    public static void submitReview(WebDriver browserWindow) throws InterruptedException
    {
        Thread.sleep(1000);
        browserWindow.findElement(By.id("submitButton")).click();
        System.out.println("clicked Submitt. looking for "+reviewPath+numOfReviews);
        while(true)
        {
            try
            {
                try
                {
                    browserWindow.findElement(By.xpath(reviewPath + numOfReviews + "]"));
                    break;
                } catch (NoSuchElementException tryAlt)
                {
                    browserWindow.findElement(By.xpath(reviewPathAlt + numOfReviews + "]"));
                    reviewPath = reviewPathAlt;
                    break;
                }
            }
            catch (Exception ignore){}
        }
        System.out.println("checked review num");
        Thread.sleep(2000);
        numOfReviews2 = getNumOfReviews(browserWindow);
        checkReview(browserWindow, numOfReviews2, numOfReviews+1, validInput);

    }

    public static void testReview(WebDriver browserWindow) throws InterruptedException
    {
        System.out.println("Testing Reviews");
        browserWindow.findElement(By.className("mat-expansion-panel-header")).click();
        //identify how many reviews are supposed to be present.
        numOfReviews = getNumOfReviews(browserWindow);
        //Check that there are actually that number of reviews
        for (int i = 1; i <= numOfReviews; i++)
        {
            try
            {
                System.out.println("checking review: " + i);
                assertTrue(browserWindow.findElement(By.xpath(reviewPath + i + "]")).isEnabled());
            } catch (NoSuchElementException ignore) {}
        }
    }


    public static int getNumOfReviews(WebDriver browserWindow) throws InterruptedException
    {
        String reviewAmount;
        while (true)
        {
            try
            {
                Thread.sleep(2000);
                try
                {
                    System.out.println("0");
                    browserWindow.findElement(By.xpath(reviewNumPath));
                    System.out.println("1");
                    reviewAmount = browserWindow.findElement(By.xpath(reviewNumPath)).getText();
                    System.out.println("2");
                    System.out.println(Integer.parseInt(reviewAmount.substring(1, reviewAmount.length() - 1)));
                    return Integer.parseInt(reviewAmount.substring(1, reviewAmount.length() - 1));
                } catch (NoSuchElementException tryAlt)
                {
                    System.out.println("3");
                    browserWindow.findElement(By.cssSelector(".mat-expansion-panel-header-title > span:nth-child(2)"));
                    System.out.println("4");
                    reviewAmount = browserWindow.findElement(By.cssSelector(".mat-expansion-panel-header-title > span:nth-child(2)")).getText();
                    System.out.println("5");
                    System.out.println(Integer.parseInt(reviewAmount.substring(1, reviewAmount.length() - 1)));
                    return Integer.parseInt(reviewAmount.substring(1, reviewAmount.length() - 1));
                }
            }
            catch (Exception ignore){}
        }
    }

    public static void checkReview(WebDriver browserWindow, int actual, int previous, String expectedString) throws InterruptedException
    {
        Thread.sleep(1000);
        assertEquals(actual, previous);
        TestFunctions.waitForSiteXpath(browserWindow,reviewPath+actual+reviewContents);
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
