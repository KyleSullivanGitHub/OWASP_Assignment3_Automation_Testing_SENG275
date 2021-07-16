package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;


import static org.testng.Assert.*;

public class Basket implements ITest{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();
    WebDriver browserWindow;

    String addToCart_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[2]/figure/mat-card/div[2]/button";
    String basketIconQuantity_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]/span[1]/span[2]";
    String basketIcon_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]/span[1]/mat-icon";
    String productName_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-basket/mat-card/app-purchase-basket/mat-table/mat-row/mat-cell[2]";
    String productPrice_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-basket/mat-card/app-purchase-basket/mat-table/mat-row/mat-cell[4]";
    String productQuantity_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-basket/mat-card/app-purchase-basket/mat-table/mat-row/mat-cell[3]/span";
    String totalPrice_XPath = "//*[@id=\"price\"]";
    String increaseQuantity_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-basket/mat-card/app-purchase-basket/mat-table/mat-row/mat-cell[3]/button[2]";
    String decreaseQuantity_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-basket/mat-card/app-purchase-basket/mat-table/mat-row/mat-cell[3]/button[1]";
    String removeProduct_XPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-basket/mat-card/app-purchase-basket/mat-table/mat-row/mat-cell[5]/button";

    /**
     *Adds a single apple juice to basket and navigates to your Basket page
     *Programmer: Nicole Makarowski
     */
    public void addToBasket(WebDriver browserWindow) throws IOException, InterruptedException {
        browserWindow.get(website);
        TestFunctions.waitForSite(browserWindow);

        //Add to cart
        browserWindow.findElement(By.xpath(addToCart_XPath)).click();
        //Navigate to Basket
        browserWindow.findElement(By.xpath(basketIcon_XPath)).click();//click basket icon

        //Verify Navigation to basket page
        Thread.sleep(500);
        assertEquals(browserWindow.getCurrentUrl(),website+"/#/basket");
    }

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Nicole Makarowski
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }
    @AfterTest
    public void  tearDown(){
        browserWindow.quit();
    }

    /**
     *Smoke tests a valid basket usage.
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Basket Smoke","Basket", "hasDataProvider"},
            priority = 1,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void BA1_Valid_Usage(String chosenBrowser) throws IOException, InterruptedException {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        try {
            browserWindow.manage().window().maximize();

            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps??
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Add to cart
            browserWindow.findElement(By.xpath(addToCart_XPath)).click();
            Thread.sleep(1000);

            //Quantity icon on cart updated
            assertEquals(browserWindow.findElement(By.xpath(basketIconQuantity_XPath)).getText(), "1"); //Quantity updated

            //Navigate to Basket
            browserWindow.findElement(By.xpath(basketIcon_XPath)).click();//click basket icon
            Thread.sleep(1000);

            //Verify Navigation to basket page
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/basket");

            //Verify product Info
            assertEquals(browserWindow.findElement(By.xpath(productName_XPath)).getText(), "Apple Juice (1000ml)"); //Product Name
            assertEquals(browserWindow.findElement(By.xpath(productPrice_XPath)).getText(), "1.99¤"); //Price
            assertEquals(browserWindow.findElement(By.xpath(productQuantity_XPath)).getText(), "1"); //Quantity
            assertEquals(browserWindow.findElement(By.xpath(totalPrice_XPath)).getText(), "Total Price: 1.99¤"); //Total Price

            //Remove Product
            browserWindow.findElement(By.xpath(removeProduct_XPath)).click();//click trash icon
            Thread.sleep(500);
            assertEquals(browserWindow.findElement(By.xpath(totalPrice_XPath)).getText(), "Total Price: 0¤");
        }
        finally {
            browserWindow.quit();
        }
    }

    /**
     *Smoke tests a invalid basket usage.
     * Verifies cannot add a product to cart when logged out
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Basket Smoke","Basket", "noDataProvider"},
            priority = 2,
            enabled = true
    )
    public void BA2_Invalid_Usage() throws InterruptedException{
        browserWindow = environment.makeDriver();
        try {
            browserWindow.manage().window().maximize();
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);
            Thread.sleep(500);
            //Verify add to basket element does not exist
            assertTrue(browserWindow.findElements(By.xpath(addToCart_XPath)).isEmpty());
        }
        finally {
            browserWindow.quit();
        }

    }

    /**
     *Sanity tests for Alternative Add to Cart use cases
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Sanity","Basket Sanity","Basket", "noDataProvider"},
            priority = 3,
            enabled = true
    )
    public void BA3_Alternate_Usages() throws IOException, InterruptedException {
        //Create  browser for this particular test
        browserWindow = environment.makeDriver();
        try {
            browserWindow.get(website);
            browserWindow.manage().window().maximize();
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps??
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Add to cart
            browserWindow.findElement(By.xpath(addToCart_XPath)).click();
            Thread.sleep(1000);

            //Navigate to Basket
            browserWindow.findElement(By.xpath(basketIcon_XPath)).click();//click basket icon
            Thread.sleep(1000);

            //Verify Navigation to basket page
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/basket");

            //Increase Quantity by one
            browserWindow.findElement(By.xpath(increaseQuantity_XPath)).click();
            Thread.sleep(500);
            assertEquals(browserWindow.findElement(By.xpath(basketIconQuantity_XPath)).getText(), "2"); //Quantity updated

            //Decrease Quantity by one
            browserWindow.findElement(By.xpath(decreaseQuantity_XPath)).click();
            Thread.sleep(500);
            assertEquals(browserWindow.findElement(By.xpath(basketIconQuantity_XPath)).getText(), "1"); //Quantity updated

            //Remove Product
            browserWindow.findElement(By.xpath(removeProduct_XPath)).click();//click trash icon
            Thread.sleep(500);
            assertEquals(browserWindow.findElement(By.xpath(totalPrice_XPath)).getText(), "Total Price: 0¤");
        }
        finally {
            browserWindow.quit();
        }
    }

    /**
     *Regression tests for Basket Functionality
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Regression","Basket Regression","Basket"},
            priority = 4,
            enabled = true
    )
    public void BA_Regression() {
        //TODO ADD Basket REGRESSION TEST
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



