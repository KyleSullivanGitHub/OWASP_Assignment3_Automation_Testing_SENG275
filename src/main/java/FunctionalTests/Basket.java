package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Nicole Makarowski
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }



    /**
     *Smoke tests a valid basket usage.
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Basket Smoke","Basket", "hasDataProvider"},
            priority = 1,
            dataProvider = "webBrowserData",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void BA1_Valid_Usage(String chosenBrowser) throws IOException {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Login/Initial steps??

        //Add product to cart
        // browserWindow.findElement(By.cssSelector("_____")).click();//click add to basket

        //Add to cart
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[2]/figure/mat-card/div[2]/button")).click();//click product icon

        //Quantity icon on cart updated
        assertEquals(browserWindow.findElement(By.cssSelector("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]/span[1]/span[2]")),"1"); //Quantity updated

        //Navigate to Basket
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]/span[1]/mat-icon")).click();//click basket icon

        //Verify Navigation to basket page
        assertEquals(browserWindow.getCurrentUrl(),website+"/#/basket");
        /*
        //Verify product Info
        assertEquals(browserWindow.findElement(By.cssSelector("___")),"____"); //Product Name
        assertEquals(browserWindow.findElement(By.cssSelector("___")),"____"); //Price
        assertEquals(browserWindow.findElement(By.cssSelector("___")),"____"); //Quantity
        assertEquals(browserWindow.findElement(By.cssSelector("___")),"____"); //Total Price

        //Change quantity

        // Verify Quantity change


        //Remove Product
        browserWindow.findElement(By.cssSelector("_____")).click();//click trash icon

        //Verify Product is removed
        */
        browserWindow.quit();
    }

    /**
     *Smoke tests a invalid basket usage.
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Basket Smoke","Basket"},
            priority = 2,
            enabled = true
    )
    public void BA2_Invalid_Usage() {

        //Create  browser for this particular test
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        //Login/Initial steps??

        //Select Product
        browserWindow.findElement(By.cssSelector("_____")).click();//click product icon

        //Add product to cart
        browserWindow.findElement(By.cssSelector("_____")).click();//click add to basket

        //Navigate to Basket
        browserWindow.findElement(By.cssSelector("_____")).click();//click basket icon

        //Verify Navigation to basket page
        assertEquals(browserWindow.getCurrentUrl(),website+"/#/basket");

        //Verify product is present
        assertEquals(browserWindow.findElement(By.cssSelector("___")),"____"); //Product Name

        //Attempt to lower quantity  below zero
        browserWindow.findElement(By.cssSelector("_____")).click();//click Minus button

        //Verify value above one or button disabled
        assertEquals(browserWindow.findElement(By.cssSelector("___")),"____");//Quantity is above 1

        browserWindow.quit();

    }

    /**
     *Sanity tests for Alternative Add to Cart use cases
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Sanity","Basket Sanity","Basket"},
            priority = 3,
            enabled = true
    )
    public void BA3_Alternate_Usages() {

        //Create  browser for this particular test
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        //Verify Add to cart is visible when clicking on product
        browserWindow.findElement(By.cssSelector("_____")).click();//click product icon
        assertTrue( browserWindow.findElement(By.cssSelector("_____")).isEnabled());//Add to cart visible

        //Verify Add to cart visible when viewing 'compare products'
        browserWindow.findElement(By.cssSelector("_____")).click();//click compare products
        assertTrue( browserWindow.findElement(By.cssSelector("_____")).isEnabled());//Add to cart visible

        browserWindow.quit();
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



