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
    String checkoutButton_XPath = "//*[@id=\"checkoutButton\"]";
    String savedAddress_XPath = "//*[@id=\"mat-radio-38\"]";
    String shippingMethod_XPath = "//*[@id=\"mat-radio-39\"]";
    String savedPaymentMethod_XPath = "//*[@id=\"mat-radio-47\"]";
    String addSavedAddress_XPath = "//*[@id=\"card\"]/app-address/mat-card/div/button";
    String checkoutProductName_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[1]";
    String checkoutProductPrice_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[2]";
    String checkoutProductQuantity_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[3]";
    String checkoutTotalPrice_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[4]";
    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Nicole Makarowski
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
     *Smoke tests a single invalid login attempt.
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Checkout Smoke","Checkout", "hasDataProvider"},
            priority = 1,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void CO1_Valid_Usage(String chosenBrowser) throws InterruptedException, IOException
    {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        try {
            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps??
            TestFunctions.login(browserWindow);

            Thread.sleep(1000);
            //Add to cart
            browserWindow.findElement(By.xpath(addToCart_XPath)).click();
            Thread.sleep(1500);

            //Navigate to Basket
            browserWindow.findElement(By.xpath(basketIcon_XPath)).click();//click basket icon

            //Verify Navigation to basket page
            Thread.sleep(1500);
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/basket");

            //Verify product Info
            assertEquals(browserWindow.findElement(By.xpath(productName_XPath)).getText(), "Apple Juice (1000ml)"); //Product Name

            //Click checkout button
            browserWindow.findElement(By.xpath(checkoutButton_XPath)).click();
            Thread.sleep(1500);

            //Select saved Address
            if (browserWindow.findElements(By.xpath(savedAddress_XPath)).isEmpty()) {
                addSavedAddress(browserWindow);
                Thread.sleep(500);
            }
            browserWindow.findElement(By.xpath(savedAddress_XPath)).click();
            browserWindow.findElement(By.xpath("//*[@id=\"card\"]/app-address/mat-card/button")).click();
            Thread.sleep(1500);

            //select shipping method
            browserWindow.findElement(By.xpath(shippingMethod_XPath)).click();
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-delivery-method/mat-card/div[4]/button[2]")).click();
            Thread.sleep(1500);

            //Select payment method
            if (browserWindow.findElements(By.xpath(savedPaymentMethod_XPath)).isEmpty()) {
                addSavedPayment(browserWindow);
                Thread.sleep(500);
            }
            browserWindow.findElement(By.xpath(savedPaymentMethod_XPath)).click();
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[2]/button[2]")).click();
            Thread.sleep(1500);

            //Place order
            browserWindow.findElement(By.xpath(checkoutButton_XPath)).click();
            Thread.sleep(1500);

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

    void addSavedAddress(WebDriver browserWindow) {
        String country_XPath = "//*[@id=\"mat-input-1\"]";
        String name_XPath = "//*[@id=\"mat-input-2\"]";
        String mobile_XPath = "//*[@id=\"mat-input-3\"]";
        String zipCode_XPath = "//*[@id=\"mat-input-4\"]";
        String address_XPath = "//*[@id=\"mat-input-5\"]";
        String city_XPath = "//*[@id=\"mat-input-6\"]";
        String state_XPath = "//*[@id=\"mat-input-7\"]";

        browserWindow.findElement(By.xpath(addSavedAddress_XPath)).click();

        browserWindow.findElement(By.xpath(country_XPath)).sendKeys("USA");
        browserWindow.findElement(By.xpath(name_XPath)).sendKeys("Hello World");
        browserWindow.findElement(By.xpath(mobile_XPath)).sendKeys("1234567890");
        browserWindow.findElement(By.xpath(zipCode_XPath)).sendKeys("12346");
        browserWindow.findElement(By.xpath(address_XPath)).sendKeys("275 Seng Rd");
        browserWindow.findElement(By.xpath(city_XPath)).sendKeys("Testing");
        browserWindow.findElement(By.xpath(state_XPath)).sendKeys("CA");

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

    @BeforeMethod(onlyForGroups = {"hasDataProvider"})
    public void BeforeMethod(Method method, Object[] testData)
    {
        testName.set(method.getName()+"_"+testData[0]);
    }

    @Override
    public String getTestName()
    {
        return testName.get();
    }
}