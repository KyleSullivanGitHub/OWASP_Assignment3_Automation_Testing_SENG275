package FunctionalTests;
import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.browser.Browser;
import org.openqa.selenium.devtools.v85.log.Log;
import org.testng.ITest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.openqa.selenium.support.ui.Select;




import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Tests for verifying the full functionality of the Data Management Feature
 */

public class Data_Management {
    private final ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();
    WebDriver browserWindow;


    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Ewan Morgan
     * @exception IOException Thrown if no browser is chosen for a test
     */
    @BeforeSuite
    public void SetUp() throws IOException {
        environment = passBrowser.createBrowser();
    }

    @AfterTest
    public void  tearDown(){
        browserWindow.quit();
    }
    /**
     * Smoke Test to confirm the privacy and policy page is accessible
     * Programmer: Ewan Morgan
     * @param chosenBrowser Browser type for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Data Management Smoke", "Data Management", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )


    public void DM1_Policy(String chosenBrowser) throws IOException, InterruptedException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {
            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to account Menu

            browserWindow.findElement(By.xpath ("/html/body/div[3]/div[1]")).click();
            Thread.sleep(300);


            //Navigate to Privacy and Security
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[3]/span")).click();

            //Navigate to Privacy Policy
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-2\"]/div/button[1]")).click();

            //Verify Privacy Policy is functional
            assertEquals(browserWindow.getCurrentUrl(), website+ "/#/privacy-security/privacy-policy");

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }

    }

    /**
     *Smoke tests functioning data export.
     * Verifies cannot add a product to cart when logged out
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","Data Management Smoke","Data Management"},
            priority = 2,
            enabled = true
    )
    public void DM5_JSON(String chosenBrowser) throws IOException, InterruptedException{

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {
            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to account Menu

            browserWindow.findElement(By.xpath ("/html/body/div[3]/div[1]")).click();
            Thread.sleep(300);


            //Navigate to Privacy and Security
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[3]/span")).click();

            //Navigate to Request Data Export
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/div/a[2]/div/span")).click();

            //Verify Request Data Export is functional
            assertEquals(browserWindow.getCurrentUrl(), website+ "/#/privacy-security/data-export");

            //navigate to JSON
            browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-51\"]/label/span[1]/span[1]")).click();

            //Navigate to submit
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }
    /**
     *Smoke tests functioning data export.
     * Verifies cannot add a product to cart when logged out
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","Data Management Smoke","Data Management"},
            priority = 2,
            enabled = true
    )
    public void DM2_Erasure(String chosenBrowser) throws IOException, InterruptedException{

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {
            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to account Menu

            browserWindow.findElement(By.xpath ("/html/body/div[3]/div[1]")).click();
            Thread.sleep(300);


            //Navigate to Privacy and Security
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[3]/span")).click();

            //Navigate to Request Data Erasure
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/div/a[3]/div")).click();

            //Verify Request Data Export is functional
            assertEquals(browserWindow.getCurrentUrl(), website+ "/dataerasure");



        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }
    /**
     *Smoke tests functioning data export.
     * Verifies cannot add a product to cart when logged out
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","Data Management Sanity","Data Management"},
            priority = 2,
            enabled = true
    )
    public void DM6_PDF(String chosenBrowser) throws IOException, InterruptedException{

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {
            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to account Menu

            browserWindow.findElement(By.xpath ("/html/body/div[3]/div[1]")).click();
            Thread.sleep(300);


            //Navigate to Privacy and Security
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[3]/span")).click();

            //Navigate to Request Data Export
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/div/a[2]/div/span")).click();

            //Verify Request Data Export is functional
            assertEquals(browserWindow.getCurrentUrl(), website+ "/#/privacy-security/data-export");

            //navigate to PDF
            browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-52\"]")).click();

            //Navigate to submit
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }

    /**
     *Smoke tests functioning data export.
     * Verifies cannot add a product to cart when logged out
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","Data Management Sanity","Data Management"},
            priority = 2,
            enabled = true
    )
    public void DM7_Excel(String chosenBrowser) throws IOException, InterruptedException{

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {
            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to account Menu

            browserWindow.findElement(By.xpath ("/html/body/div[3]/div[1]")).click();
            Thread.sleep(300);


            //Navigate to Privacy and Security
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[3]/span")).click();

            //Navigate to Request Data Export
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/div/a[2]/div/span")).click();

            //Verify Request Data Export is functional
            assertEquals(browserWindow.getCurrentUrl(), website+ "/#/privacy-security/data-export");

            //navigate to Excel
            browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-53\"]")).click();

            //Navigate to submit
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }


}

