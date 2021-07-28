package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;

public class Checkout implements ITest {
    private ThreadLocal<String> testName = new ThreadLocal<>();
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();
    WebDriver browserWindow;


    String addToCart_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[2]/figure/mat-card/div[2]/button";
    String basketIcon_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]/span[1]/mat-icon";
    String productName_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-basket/mat-card/app-purchase-basket/mat-table/mat-row/mat-cell[2]";
    static String checkoutButton_XPath = "//*[@id=\"checkoutButton\"]";
    static String addSavedAddress_XPath = "//*[@id=\"card\"]/app-address/mat-card/div/button";
    static String checkoutProductName_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[1]";
    static String checkoutProductPrice_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[2]";
    static String checkoutProductQuantity_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[3]";
    static String checkoutTotalPrice_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[4]";
    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Nicole Makarowski
     */
    @BeforeClass
    public void SetUp() throws IOException {
        environment = passBrowser.createBrowser();
    }

    @AfterTest
    public void  tearDown(){
        //browserWindow.quit();
    }

    /**
     *Tests purchasing an order using a card
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Checkout", "hasDataProvider"},
            priority = 22,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void CK1_Card_Usage(String chosenBrowser) throws InterruptedException, IOException
    {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        try {
            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Add to cart
            browserWindow.findElement(By.xpath(addToCart_XPath)).click();
            Thread.sleep(500);

            //Navigate to Basket
            browserWindow.findElement(By.xpath(basketIcon_XPath)).click();//click basket icon

            //Verify Navigation to basket page
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/basket");

            //Verify product Info
            assertEquals(browserWindow.findElement(By.xpath(productName_XPath)).getText(), "Apple Juice (1000ml)"); //Product Name

            //Click checkout button
            browserWindow.findElement(By.xpath(checkoutButton_XPath)).click();
            Thread.sleep(500);

            //Select saved Address
            if (TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60) == null) {
                addSavedAddress(browserWindow);
                Thread.sleep(500);
            }
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60).click();
            browserWindow.findElement(By.xpath("//*[@id=\"card\"]/app-address/mat-card/button")).click();
            Thread.sleep(500);

            //select shipping method
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60).click();
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-delivery-method/mat-card/div[4]/button[2]")).click();
            Thread.sleep(500);

            //Select payment method
            if (TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60) == null) {
                addSavedPayment(browserWindow);
                Thread.sleep(500);
            }
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60).click();
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[2]/button[2]")).click();
            Thread.sleep(500);

            //Place order
            browserWindow.findElement(By.xpath(checkoutButton_XPath)).click();
            Thread.sleep(500);

            //Validate order
            assertEquals(browserWindow.findElement(By.xpath(checkoutProductName_XPath)).getText(), "Apple Juice (1000ml)"); //Product Name
            assertEquals(browserWindow.findElement(By.xpath(checkoutProductPrice_XPath)).getText(), "1.99¤"); //Price
            assertEquals(browserWindow.findElement(By.xpath(checkoutProductQuantity_XPath)).getText(), "1"); //Quantity
            assertEquals(browserWindow.findElement(By.xpath(checkoutTotalPrice_XPath)).getText(), "1.99¤"); //Total Price
        }
        finally {
            browserWindow.quit();
        }

    }
    /**
     *Tests purchasing an order using digital wallet
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Checkout", "noDataProvider"},
            priority = 23,
            enabled = true
    )
    public void CK2_Digital_Wallet_Usage() throws InterruptedException, IOException
    {
        //Create driver and browser for this particular test
        browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        try {
            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Add to cart
            browserWindow.findElement(By.xpath(addToCart_XPath)).click();
            Thread.sleep(500);

            //Navigate to digital wallet
            browserWindow.findElement(By.id("navbarAccount")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[5]")).click();

            //Verify Navigation to wallet page
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/wallet");

            TestFunctions.findRadioButton(browserWindow, "mat-input-", 1, 20).sendKeys("50");
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
            Thread.sleep(500);

            if (TestFunctions.findRadioButton(browserWindow, "mat-radio-", 0, 60) == null) {
                //Add new Payment
                browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-0\"]")).click();

                //Populate info
                browserWindow.findElement(By.xpath("//*[@id=\"mat-input-2\"]")).sendKeys("Hello World");//Name
                browserWindow.findElement(By.xpath("//*[@id=\"mat-input-3\"]")).sendKeys("1111222233334444");//Card No
                Select monthSelector = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-4\"]")));
                monthSelector.selectByVisibleText("1");
                Select yearSelector = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-5\"]")));
                yearSelector.selectByVisibleText("2080");

                //Submit
                browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
                Thread.sleep(500);
            }
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 0, 60).click();
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div/button[2]")).click();
            Thread.sleep(1000);


            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]")).click();
            //Verify Navigation to basket page
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/basket");

            //Verify product Info
            assertEquals(browserWindow.findElement(By.xpath(productName_XPath)).getText(), "Apple Juice (1000ml)"); //Product Name

            //Click checkout button
            browserWindow.findElement(By.xpath(checkoutButton_XPath)).click();
            Thread.sleep(500);

            //Select saved Address
            if (TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60) == null) {
                addSavedAddress(browserWindow);
                Thread.sleep(500);
            }
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60).click();
            browserWindow.findElement(By.xpath("//*[@id=\"card\"]/app-address/mat-card/button")).click();
            Thread.sleep(500);

            //select shipping method
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60).click();
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-delivery-method/mat-card/div[4]/button[2]")).click();
            Thread.sleep(500);

            //Select digital wallet payment method
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[1]/div/div[3]/button")).click();
            Thread.sleep(500);

            //Place order
            browserWindow.findElement(By.xpath(checkoutButton_XPath)).click();
            Thread.sleep(500);

            //Validate order
            assertEquals(browserWindow.findElement(By.xpath(checkoutProductName_XPath)).getText(), "Apple Juice (1000ml)"); //Product Name
            assertEquals(browserWindow.findElement(By.xpath(checkoutProductPrice_XPath)).getText(), "1.99¤"); //Price
            assertEquals(browserWindow.findElement(By.xpath(checkoutProductQuantity_XPath)).getText(), "1"); //Quantity
            assertEquals(browserWindow.findElement(By.xpath(checkoutTotalPrice_XPath)).getText(), "1.99¤"); //Total Price


        }
        finally {
            browserWindow.quit();
        }

    }

    /**
     *Tests invalid checkout usage
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Checkout", "noDataProvider"},
            priority = 24,
            enabled = true
    )
    public void CK3_Invalid_Usage() throws InterruptedException, IOException
    {

        browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        try {
            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to Basket
            browserWindow.findElement(By.xpath(basketIcon_XPath)).click();//click basket icon

            //Verify Navigation to basket page
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/basket");

            //Verify Checkout Button Disabled
            assertFalse(browserWindow.findElement(By.xpath(checkoutButton_XPath)).isEnabled());

            //Add product to cart and click checkout button
            browserWindow.get(website);
            Thread.sleep(500);

            browserWindow.findElement(By.xpath(addToCart_XPath)).click();
            browserWindow.findElement(By.xpath(basketIcon_XPath)).click();
            Thread.sleep(750);
            browserWindow.findElement(By.xpath(checkoutButton_XPath)).click();
            Thread.sleep(500);

            //Verify continue button disabled before setting address
            TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"card\"]/app-address/mat-card/button");
            WebElement continueButton = browserWindow.findElement(By.xpath("//*[@id=\"card\"]/app-address/mat-card/button"));
            assertFalse(continueButton.isEnabled());

            //Add address and continue
            //Select saved Address
            if (TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60) == null) {
                addSavedAddress(browserWindow);
                Thread.sleep(500);
            }
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60).click();
            browserWindow.findElement(By.xpath("//*[@id=\"card\"]/app-address/mat-card/button")).click();
            Thread.sleep(500);

            //Verify continue button disabled before setting shipping address
            continueButton = browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-delivery-method/mat-card/div[4]/button[2]"));
            assertFalse(continueButton.isEnabled());

            //select shipping method
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60).click();
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-delivery-method/mat-card/div[4]/button[2]")).click();
            Thread.sleep(500);

            //Verify continue button disabled before setting payment method
            continueButton = browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[2]/button[2]"));
            assertFalse(continueButton.isEnabled());

            //Select payment method
            if (TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60) == null) {
                addSavedPayment(browserWindow);
                Thread.sleep(500);
            }
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 30, 60).click();
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[2]/button[2]")).click();
            Thread.sleep(500);

            //Place order
            browserWindow.findElement(By.xpath(checkoutButton_XPath)).click();
            Thread.sleep(500);


        }
        finally {
            browserWindow.quit();
        }
    }


    /**
     *Regression Test
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Regression","Checkout", "noDataProvider"},
            priority = 82,
            enabled = true
    )
    public void CK_Regression() throws InterruptedException, IOException {
        browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        try {
            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps
            TestFunctions.login(browserWindow);
            TestFunctions.constEmail = "helloworld.owasp@gmail.com";
            Thread.sleep(1000);

            //Navigate to Basket
            browserWindow.findElement(By.xpath(basketIcon_XPath)).click();//click basket icon

            //Test Common regression
            TestFunctions.commonRegression(browserWindow, website + "/#/basket", true);
        } finally {
            browserWindow.quit();
        }
    }

    void addSavedAddress(WebDriver browserWindow) {
        //add new address
        browserWindow.findElement(By.xpath(addSavedAddress_XPath)).click();

        //Populate Info
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-1\"]")).sendKeys("USA");//Country
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-2\"]")).sendKeys("Hello World");//Name
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-3\"]")).sendKeys("1234567890");//Phone
        browserWindow.findElement(By.xpath( "//*[@id=\"mat-input-4\"]")).sendKeys("12346");//Zip
        browserWindow.findElement(By.xpath("//*[@id=\"address\"]")).sendKeys("275 Seng Rd");//Address
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-6\"]")).sendKeys("Testing");//City
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-7\"]")).sendKeys("CA");//State

        //Submit
        browserWindow.findElement(By.id("submitButton")).click();

    }

    void addSavedPayment (WebDriver browserWindow){
        //Add new Payment
        browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-0\"]")).click();

        //Populate info
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-8\"]")).sendKeys("Hello World");//Name
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-9\"]")).sendKeys("1111222233334444");//Card No
        Select monthSelector = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-10\"]")));
        monthSelector.selectByVisibleText("1");
        Select yearSelector = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-11\"]")));
        yearSelector.selectByVisibleText("2080");

        //Submit
        browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
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