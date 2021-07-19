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
    @BeforeSuite
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


    public void DW1_Valid_DigitalWallet(String chosenBrowser) throws IOException, InterruptedException {
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
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/saved-payment-methods");

            //Add 100$ to Digital Wallet
            WebElement digit = browserWindow.findElement(By.xpath("//*[@id=\"mat-input-5\"]"));
            digit.sendKeys("100");

            //press continue
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();

            //Select Continue on card page
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div/button[2]")).click();

            assertEquals(browserWindow.findElement(By.xpath("/html/body/simple-snack-bar/span")).getText(), "Wallet successfully charged.");

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    /**
     *Sanity tests for add a new card during payment.
     * Verifies can add money to digital wallet using add new card
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","Digital_Wallet Sanity","Digital_Wallet"},
            priority = 2,
            enabled = true
    )
    public void DW4_Valid_DigitalWallet(String chosenBrowser) throws IOException, InterruptedException {
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

            //Add 100$ to Digital Wallet
            WebElement digit = browserWindow.findElement(By.xpath("//*[@id=\"mat-input-5\"]"));
            digit.sendKeys("100");

            //press continue
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();

            //Add -100$ to Digital Wallet
            WebElement neg_digit = browserWindow.findElement(By.xpath("//*[@id=\"mat-input-5\"]"));
            neg_digit.sendKeys("-100");

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
     *Sanity tests a invalid digital wallet usage.
     * Verifies cannot add negative funds to digital wallet
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","Digital_Wallet sanity","Digital_Wallet"},
            priority = 2,
            enabled = true
    )
    public void DW3_Invalid_DigitalWallet(String chosenBrowser) throws IOException, InterruptedException {
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
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
            Thread.sleep(300);

            //Navigate to Digital Wallet
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[5]")).click();

            //Add 100$ to Digital Wallet
            WebElement digit = browserWindow.findElement(By.xpath("//*[@id=\"mat-input-5\"]"));
            digit.sendKeys("100");

            //press continue
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();

            //select add new card drop down menu
            browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-2\"]")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/mat-form-field[1]/div/div[1]/div[3]")).sendKeys("helloworld");
            browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/mat-form-field[2]/div/div[1]/div[3]")).sendKeys("1111111111111111");

            //Verify Expiry Month dropdown is functional
            Select month = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-13\"]")));
            month.selectByValue("3");
            Thread.sleep(500);

            //Verify Expiry Year dropdown is functional
            Select year = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-14\"]")));
            year.selectByValue("2081");
            Thread.sleep(500);

            //Navigate to Submit
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
            assertEquals(browserWindow.findElement(By.xpath("/html/body/span")).getText(), "Your card ending with 1111 has been saved for your convenience.");
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div/button[2]")).click();

            //navigate to new card
            browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-74\"]")).click();
            assertEquals(browserWindow.findElement(By.xpath("/html/body/simple-snack-bar/span")).getText(), "Wallet successfully charged.");

        } finally {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

}
