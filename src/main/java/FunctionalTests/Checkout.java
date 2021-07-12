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

    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Nicole Makarowski
     */
    @BeforeSuite
    public void SetUp() throws IOException {
        environment = passBrowser.createBrowser();
    }

    /**
     *Smoke tests a single invalid login attempt.
     *Programmer: Seyedmehrad Adimi
     */
    @Test(
            groups = {"Smoke","Checkout Smoke","Checkout"},
            priority = 1,
            enabled = true
    )
    public void CO1_Valid_Usage() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();


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