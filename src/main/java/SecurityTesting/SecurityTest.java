package SecurityTesting;

import FunctionalTests.Logout;
import FunctionalTests.TestFunctions;
import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SecurityTest {

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
    @AfterTest
    public void  tearDown(){
        browserWindow.quit();
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

    public String getTestName()
    {
        return testName.get();
    }


    /**
     *Tests if User can login to admin account Via SQL Injection
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Security", "noDataProvider"},
            priority = 2,
            enabled = true
    )
    public void SEC1_Login_to_admin() throws InterruptedException{
        browserWindow = environment.makeDriver();
        try {
            browserWindow.manage().window().maximize();
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);
            Thread.sleep(500);

            TestFunctions.navToLogin(browserWindow);
            TestFunctions.waitForSite(browserWindow, "#email");
            WebElement emailInput = browserWindow.findElement(By.cssSelector("#email"));
            emailInput.click();
            emailInput.sendKeys("' OR true--");
            WebElement pwInput = browserWindow.findElement(By.cssSelector("#password"));
            pwInput.click();
            pwInput.sendKeys("X\n");

            if (!browserWindow.getCurrentUrl().equals(website + "/#/login")) {

                Thread.sleep(500);
                assertEquals(browserWindow.getCurrentUrl(), website + "/#/search");
                TestFunctions.waitForSite(browserWindow, "#navbarAccount");
                browserWindow.findElement(By.cssSelector("#navbarAccount")).click();
                assertEquals(browserWindow.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(1)")).getText(), "account_circle admin@juice-sh.op");
                Assert.fail("Admin account should not be accessible");
            }
        }
        finally {
            browserWindow.quit();
        }

    }

    /**
     *Logs out user and verifies hitting the back button does not log the user back in
     *Programmer: Nicole Makarowski
     */
    @Test(
            groups = {"Smoke","Basket Smoke","Basket", "noDataProvider"},
            priority = 2,
            enabled = true
    )
    public void SEC2_Logout_then_hit_back() throws InterruptedException{
        browserWindow = environment.makeDriver();
        try {
            browserWindow.manage().window().maximize();
            browserWindow.get(website);
            TestFunctions.waitForSite(browserWindow);
            Thread.sleep(500);

            //Login/Initial steps
            TestFunctions.login(browserWindow);
            Thread.sleep(1000);

            //Logout

            TestFunctions.waitForSite(browserWindow, "#navbarAccount");
            browserWindow.findElement(By.cssSelector("#navbarAccount")).click();
            browserWindow.findElement(By.cssSelector("#navbarLogoutButton")).click();

            Thread.sleep(1000);
            browserWindow.navigate().back();


            Thread.sleep(1000);
            TestFunctions.waitForSite(browserWindow, "#navbarAccount");
            browserWindow.findElement(By.cssSelector("#navbarAccount")).click();
            assertNotEquals(browserWindow.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(1)")).getText(), "account_circle admin@juice-sh.op");

        }
        finally {
            browserWindow.quit();
        }

    }
}
