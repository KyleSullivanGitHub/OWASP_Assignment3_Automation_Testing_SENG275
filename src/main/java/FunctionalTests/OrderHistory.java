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
     *Valid order history before placing order
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Order History Smoke","Order Historr", "noDataProvider"},
            priority = 1,
            enabled = true
    )
    public void OH1_Before_Order(String chosenBrowser) throws InterruptedException, IOException
    {}

    /**
     *Valid order history after placing order
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Order History Smoke","Order Historr", "noDataProvider"},
            priority = 1,
            enabled = true
    )
    public void OH2_After_Order(String chosenBrowser) throws InterruptedException, IOException
    {}


    /**
     *Regression Test
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Regression","Order History Regression","Order History", "noDataProvider"},
            priority = 1,
            enabled = true
    )
    public void OH_Regression(String chosenBrowser) throws InterruptedException, IOException
    {}

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
