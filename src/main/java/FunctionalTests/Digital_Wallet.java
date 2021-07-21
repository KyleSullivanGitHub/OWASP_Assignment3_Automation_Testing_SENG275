package FunctionalTests;
import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.browser.Browser;
import org.openqa.selenium.devtools.v85.log.Log;
import org.testng.ITest;
import org.testng.annotations.*;

import static org.testng.Assert.*;
import org.openqa.selenium.support.ui.Select;




import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Tests for verifying the full functionality of the Digital Wallet
 */

public class Digital_Wallet {
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
    @BeforeClass
    public void SetUp() throws IOException {
        environment = passBrowser.createBrowser();
    }

    @AfterTest
    public void  tearDown(){
        browserWindow.quit();
    }
    /**
     * Smoke Test to confirm the Digital Wallet button is functional
     * Programmer: Ewan Morgan
     * @param chosenBrowser Browser type for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Digital_Wallet Smoke", "Digital_Wallet", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )


    public void DW1_ValidFunds(String chosenBrowser) throws IOException, InterruptedException {
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

            //Navigate to Account
            browserWindow.findElement(By.xpath("//*[@id=\"navbarAccount\"]")).click();
            Thread.sleep(300);

            //Navigate to Orders & Payment
            browserWindow.findElement(By.xpath ("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
            Thread.sleep(300);

            //Navigate to Digital Wallet
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[5]")).click();

            //Verify Digital Wallet Page
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/wallet");

            //Add 100$ to Digital Wallet
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-wallet/mat-card/mat-form-field/div/div[1]/div[3]")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-input-1\"]")).sendKeys("100");

            //press continue
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();

            if(browserWindow.findElements(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/app-payment-method/div/div[1]")).isEmpty()) {
                //select new card dropdown
                browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-0\"]")).click();
                addSavedPayment(browserWindow);

                //Select Submit
                browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
            }
            Thread.sleep(500);
            //select new card
            browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-38\"]")).click();

            Thread.sleep(500);
            //Select Continue on card page
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div/button[2]")).click();
            Thread.sleep(500);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/div[3]")).getText(), "Wallet successfully charged.\nX");

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    /**
     *Sanity tests for adding negative funds.
     * Verifies cannot add funds to digital wallet using negative value
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","Digital_Wallet Sanity","Digital_Wallet"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void DW3_InvalidAmount(String chosenBrowser) throws IOException, InterruptedException {
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

            //Navigate to Account
            browserWindow.findElement(By.xpath("//*[@id=\"navbarAccount\"]")).click();
            Thread.sleep(300);

            //Navigate to Orders & Payment
            browserWindow.findElement(By.xpath ("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
            Thread.sleep(300);

            //Navigate to Digital Wallet
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[5]")).click();

            //Add -100$ to Digital Wallet
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-wallet/mat-card/mat-form-field/div/div[1]/div[3]")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-input-1\"]")).sendKeys("-100");

            //press continue
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();

            WebElement cont = browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]"));
            assertFalse(cont.isEnabled());

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    /**
     *Regression tests for Digital_Wallet
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Regression","Digital_Wallet Regression","Digital_Wallet"},
            priority = 0,
            enabled = true
    )
    public void DW_Regression() {
        //TODO ADD Digital Wallet REGRESSION TEST
    }

    void addSavedPayment (WebDriver browserWindow){

        //Populate info
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-2\"]")).sendKeys("Hello World");//Name
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-3\"]")).sendKeys("1111222233334444");//Card No
        Select monthSelector = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-4\"]")));
        monthSelector.selectByVisibleText("1");
        Select yearSelector = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-5\"]")));
        yearSelector.selectByVisibleText("2080");

    }

}
