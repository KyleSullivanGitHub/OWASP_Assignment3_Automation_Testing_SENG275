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
 * Tests for verifying the full functionality of the Deluxe Membership Feature
 */

public class Deluxe_Membership {
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
     * Smoke Test to confirm the Deluxe Membership is usable
     * Programmer: Ewan Morgan
     * @param chosenBrowser Browser type for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Deluxe_Membership Smoke", "Deluxe_Membership", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )


    public void DL1_Valid_Deluxe(String chosenBrowser) throws IOException, InterruptedException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);


            //Verify deluxe membership does not exist
            WebElement deluxe =  browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div"));
            assertFalse(deluxe.isDisplayed());

            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);


            //Navigate to deluxe membership
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div")).click();

            //Verify navigation to deluxe membership
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(),website+"/#/deluxe-membership");

            //Navigate to become a member
            Thread.sleep(500);
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/mat-card/div[2]/div[3]/div[2]/button")).click();

            //Verify become a member button is functional
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(),website+"/#/payment/deluxe");

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }

    }



    /**
     * Smoke Test to confirm the Deluxe Membership is usable
     * Programmer: Ewan Morgan
     * @param chosenBrowser Browser type for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Deluxe_Membership Smoke", "Deluxe_Membership", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )

    public void DL2_Usability(String chosenBrowser) throws IOException, InterruptedException {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try{

        }
        finally
        {
        //End the Test
        Thread.sleep(TestFunctions.endTestWait);
        browserWindow.quit();

        }


    }

    /**
     *Sanity tests the add a new card deluxe membership.
     * Verifies user can purchase a deluxe memebership by adding a new card
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","deluxe_membership Sanity","deluxe_membership"},
            priority = 0,
            enabled = true
    )
    public void DL3_NewCard(String chosenBrowser) throws InterruptedException, IOException{

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);

            //Verify deluxe membership does not exist
            WebElement deluxe =  browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div"));
            assertFalse(deluxe.isDisplayed());

            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);

            //Navigate to deluxe membership
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div")).click();

            //Verify navigation to deluxe membership
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(),website+"/#/deluxe-membership");

            //Navigate to become a member
            Thread.sleep(500);
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/mat-card/div[2]/div[3]/div[2]/button")).click();

            //Navigate to Add a new card dropdown
            browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-0\"]")).click();
            Thread.sleep(1000);

            //verify Add a New Card dropdown function
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-3\"]/mat-label")).getText(), "Named");
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-5\"]")).getText(), "Card Number");
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-39\"]")).getText(), "Expiry Month");
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-41\"]")).getText(), "Expiry Year");

            Thread.sleep(500);
            WebElement name = browserWindow.findElement(By.className("mat-input-element mat-form-field-autofill-control ng-tns-c126-9 ng-pristine ng-invalid cdk-text-field-autofill-monitored ng-touched"));
            name.click();
            name.sendKeys("Bill");

            Thread.sleep(500);
            WebElement card = browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-0\"]/div/div/mat-form-field[2]/div/div[1]/div[3]"));
            card.click();
            card.sendKeys("1111111111111111");
            Thread.sleep(500);

            //Verify Expiry Month dropdown is functional
            Select month = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-3\"]")));
            month.selectByValue("3");
            Thread.sleep(500);

            //Verify Expiry Year dropdown is functional
            Select year = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-10\"]")));
            year.selectByValue("2081");
            Thread.sleep(500);

            //Navigate to Submit Button

            //Verify Submit Button is functional after adding card
            WebElement button = browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-39\"]/label/span[1]/span[1]"));
            assertTrue(button.isEnabled());
            button.click();
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"cdk-overlay-21\"]/snack-bar-container/div/div/simple-snack-bar/span")).getText(), "Your card ending with 2345 has been saved for your convenience.");


        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }

    /**
     *Sanity tests the digital wallet feature to purchase the deluxe membership.
     * Verifies user can purchase a deluxe memebership by using digital wallet
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","deluxe_membership Sanity","deluxe_membership"},
            priority = 0,
            enabled = true
    )
    public void DL4_DigitalWallet(String chosenBrowser) throws InterruptedException, IOException {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);

            //Verify deluxe membership does not exist
            WebElement deluxe =  browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div"));
            assertFalse(deluxe.isDisplayed());

            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);

            //Navigate to deluxe membership
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div")).click();

            //Verify navigation to deluxe membership
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(),website+"/#/deluxe-membership");

            //Navigate to become a member
            Thread.sleep(500);
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/mat-card/div[2]/div[3]/div[2]/button")).click();

            //Navigate to Pay using Digital Wallet
            String amount = browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[1]/div/div[2]/b/span[2]")).getText();
            int number =0;
            try{
                number = Integer.parseInt(amount);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }
            if(number < 49)
            {
                //Navigate to sidebar
                browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
                Thread.sleep(300);
                //Navigate to Orders & Payments
                browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/mat-list-item[1]/div")).click();
                Thread.sleep((300));
                //Navigate to Digital Wallet
                browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/div/a[5]/div")).click();

            }
            //Click pay
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[1]/div/div[3]/button")).click();

            //Verify digital wallet payment worked
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/div[1]/div/p")).getText(), "You are already a deluxe member!");


        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }


    /**
     *Sanity tests the coupon feature to discount the deluxe membership.
     * Verifies user can get a discount on the a deluxe memebership by using coupon feature
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","deluxe_membership Sanity","deluxe_membership"},
            priority = 0,
            enabled = true
    )
    public void DL5_Coupon(String chosenBrowser) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);

            //Verify deluxe membership does not exist
            WebElement deluxe =  browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div"));
            assertFalse(deluxe.isDisplayed());

            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);

            //Navigate to deluxe membership
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div")).click();

            //Verify navigation to deluxe membership
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(),website+"/#/deluxe-membership");

            //Navigate to become a member
            Thread.sleep(500);
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/mat-card/div[2]/div[3]/div[2]/button")).click();

            //Navigate to Add a Coupon dropdown
            browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-5\"]")).click();

            //Verify Add a Coupon dropdown fucntional
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-11\"]/mat-label")).getText(), "Coupon");

            //Verify redeem button is not clickable before adding coupon
            WebElement redeem = browserWindow.findElement(By.xpath("//*[@id=\"applyCouponButton\"]"));
            assertTrue(redeem.isDisplayed());

            //Verify Redeem button is clickable after adding coupon
            WebElement coupon = browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-4\"]/div/mat-form-field/div"));
            coupon.sendKeys("n(XLuf!Cdm");
            assertTrue(redeem.isEnabled());
            browserWindow.findElement(By.xpath("//*[@id=\"applyCouponButton\"]")).click();
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-4\"]/div/div")).getText(), "Your discount of 20% will be applied during checkout.");

        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }

    /**
     *Sanity tests the Other Payment Options feature to purchase the deluxe membership.
     * Verifies user can get a discount on the a deluxe memebership by using Other payment Options feature
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Sanity","deluxe_membership Sanity","deluxe_membership"},
            priority = 0,
            enabled = true
    )
    public void DL6_Other(String chosenBrowser) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try {

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);

            //Verify deluxe membership does not exist
            WebElement deluxe =  browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div"));
            assertFalse(deluxe.isDisplayed());

            //Login
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);

            //Navigate to deluxe membership
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div")).click();

            //Verify navigation to deluxe membership
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(),website+"/#/deluxe-membership");

            //Navigate to become a member
            Thread.sleep(500);
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/mat-card/div[2]/div[3]/div[2]/button")).click();

            //Verify Other payment option is clickable
            browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-2\"]")).click();
            Thread.sleep(200);
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[1]/div[1]/label")).getText(), "Donations");
            assertEquals(browserWindow.findElement(By.xpath("///*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[1]/label")).getText(), "Merchandise");

            //Verify Other Payment Option links are working
            //stripe credit card
            browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[1]/div[2]/a/button")).click();
            assertEquals(browserWindow.getCurrentUrl(), "https://pwning.owasp-juice.shop/part3/donations.html");

            //Spreadshirt(US)
            browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[2]/a[1]/button")).click();
            assertEquals(browserWindow.getCurrentUrl(), "https://shop.spreadshirt.com/juiceshop");

            //Spreadshirt(DE)
            browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[2]/a[2]/button")).click();
            assertEquals(browserWindow.getCurrentUrl(), "https://shop.spreadshirt.de/juiceshop");

            //StickerYou
            browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[2]/a[3]/button")).click();
            assertEquals(browserWindow.getCurrentUrl(), "https://www.stickeryou.com/products/owasp-juice-shop/794");

            //Leanpub
            browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[2]/a[4]/button")).click();
            assertEquals(browserWindow.getCurrentUrl(), "https://leanpub.com/juice-shop");
        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }
    /**
     *Regression tests for Deluxe_Membership
     *Programmer: Ewan Morgan
     */
    @Test(
            groups = {"Regression","Deluxe_Membership Regression","Deluxe_Membership"},
            priority = 0,
            enabled = true
    )
    public void DL_Regression() {
        //TODO ADD Basket REGRESSION TEST
    }

}
