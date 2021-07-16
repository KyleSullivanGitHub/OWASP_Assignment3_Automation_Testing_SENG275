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

public class Checkout implements ITest {
    private ThreadLocal<String> testName = new ThreadLocal<>();
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();
    WebDriver browserWindow;

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

        //Wait for Website to load
        browserWindow.get(website);
        TestFunctions.waitForSite(browserWindow);

        //Login/Initial steps??
        TestFunctions.login(browserWindow);

        Thread.sleep(1000);
        //Add to cart
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[2]/figure/mat-card/div[2]/button")).click();
        Thread.sleep(1500);

        //Navigate to Basket
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]/span[1]/mat-icon")).click();//click basket icon

        //Verify Navigation to basket page
        Thread.sleep(1500);
        assertEquals(browserWindow.getCurrentUrl(),website+"/#/basket");

        //Verify product Info
        assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-basket/mat-card/app-purchase-basket/mat-table/mat-row/mat-cell[2]")).getText(),"Apple Juice (1000ml)"); //Product Name

        //Click checkout button
        browserWindow.findElement(By.xpath("//*[@id=\"checkoutButton\"]")).click();
        Thread.sleep(1500);

        //Select saved Address
        browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-38\"]")).click();
        browserWindow.findElement(By.xpath("//*[@id=\"card\"]/app-address/mat-card/button")).click();
        Thread.sleep(1500);

        //select shipping method
        browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-44\"]")).click();
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-delivery-method/mat-card/div[4]/button[2]")).click();
        Thread.sleep(1500);

        //Select payment method
        browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-47\"]")).click();
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[2]/button[2]")).click();
        Thread.sleep(1500);

        //Place order
        browserWindow.findElement(By.xpath("//*[@id=\"checkoutButton\"]")).click();
        Thread.sleep(1500);

        //Validate order
        assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[1]")).getText(),"Apple Juice (1000ml)"); //Product Name
        assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[2]")).getText(),"1.99¤"); //Price
        assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[3]")).getText(), "1"); //Quantity
        assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-completion/mat-card/div[2]/mat-table/mat-row/mat-cell[4]")).getText(),"1.99¤"); //Total Price

        browserWindow.quit();
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