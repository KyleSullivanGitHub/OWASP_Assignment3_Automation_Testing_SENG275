package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.testng.Assert.*;

public class OrderHistory implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();
    WebDriver browserWindow;

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Nicole Makarowski
     */
    @BeforeClass
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }

    /**
     *Valid order history before placing order
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Order History Smoke","Order History","hasDataProvider"},
            priority = 1,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void OH1_Before_Order(String chosenBrowser) throws InterruptedException, IOException
    {
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        try {
            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps??
            TestFunctions.navToLogin(browserWindow);
            Thread.sleep(500);
            TestFunctions.manualLogin(browserWindow, "Seng265!");

            TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"navbarAccount\"]");

            //Navigate to orders and payments
            browserWindow.findElement(By.id("navbarAccount")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[1]")).click();

            //Verify Navigation to Order History
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/order-history");

            //Verify product Info
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/mat-card/mat-card-title/span")).getText(), "No results found"); //Product Name

        }finally {
            browserWindow.quit();
        }
    }

    /**
     *Valid order history after placing order
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Order History Smoke","Order History", "noDataProvider"},
            priority = 1,
            enabled = true
    )
    public void OH2_After_Order() throws InterruptedException, IOException
    {
        //Create driver and browser for this particular test
        browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        try {
            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps??
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Navigate to orders and payments
            browserWindow.findElement(By.id("navbarAccount")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[1]")).click();

            //Verify Navigation to Order History
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(), website + "/#/order-history");

            //Verify product Info
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div[1]/div/mat-table/mat-row/mat-cell[1]")).getText(), "Apple Juice (1000ml)"); //Product Name
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div[1]/div/mat-table/mat-row/mat-cell[2]")).getText(), "1.99¤"); //Price
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div[1]/div/mat-table/mat-row/mat-cell[3]/span")).getText(), "1"); //Quantity
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div[1]/div/mat-table/mat-row/mat-cell[4]")).getText(), "1.99¤"); //Total Price
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div[1]/div/div/div[1]/div[2]/div[2]")).getText(), "2.98¤"); //Total Price

        }finally {
            browserWindow.quit();
        }
    }


    /**
     *Regression Test
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Regression","Order History Regression","Order History", "noDataProvider"},
            priority = 1,
            enabled = true
    )
    public void OH_Regression() throws InterruptedException, IOException
    {
        browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        try {
            //Wait for Website to load
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);

            //Login/Initial steps??
            TestFunctions.login(browserWindow);
            TestFunctions.constEmail = "helloworld.owasp@gmail.com";
            Thread.sleep(1000);

            //Navigate to orders and payments
            browserWindow.findElement(By.id("navbarAccount")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
            browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[1]")).click();

            //Test Common regression
            TestFunctions.commonRegression(browserWindow, website + "/#/order-history", true);
        } finally {
            browserWindow.quit();
        }
    }

    @BeforeMethod(onlyForGroups = {"hasDataProvider"})
    public void BeforeMethod(Method method, Object[] testData)
    {
        testName.set(method.getName()+"_"+testData[0]);
    }

    @BeforeMethod(onlyForGroups = {"noDataProvider"})
    public void BeforeMethod(Method method)
    {
        //Set name to (method name)
        testName.set(method.getName());
    }

    @Override
    public String getTestName()
    {
        return testName.get();
    }
}
