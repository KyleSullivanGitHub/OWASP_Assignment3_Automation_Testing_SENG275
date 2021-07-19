package FunctionalTests;
import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.browser.Browser;
import org.openqa.selenium.devtools.v85.log.Log;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static org.testng.Assert.*;




import java.io.IOException;
import java.lang.reflect.Method;

public class Customer_Feedback {
    private final ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();
    WebDriver browserWindow;


    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Ewan Morgan
     */
    @BeforeSuite
    public void SetUp() throws IOException, InterruptedException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }
    @AfterTest
    public void  tearDown(){
        browserWindow.quit();
    }
    /**
     * Smoke Test to confirm the Customer Feedback is usable when logged out
     * @param chosenBrowser Browser type for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */

    @Test(
            groups = {"Smoke", "Customer_Feedback Smoke", "Customer_Feedback", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void CF1_Feedback(String chosenBrowser) throws IOException, InterruptedException
    {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try{
            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[1]/div/span")).click();

            //Verify customer feedback page
            assertEquals(browserWindow.getCurrentUrl(), website+ "/#/contact");

            //verify submitting feedback
            browserWindow.findElement(By.xpath("//*[@id=\"comment\"]")).sendKeys("Anything");
            WebElement slider = browserWindow.findElement(By.xpath("//*[@id=\"rating\"]/div/div[3]/div[3]/span"));

            Dimension sliderSize = slider.getSize();
            int sliderWidth = sliderSize.getWidth();
            int xCoord = slider.getLocation().getX();
            Actions builder = new Actions(browserWindow);
            builder.moveToElement(slider).click().dragAndDropBy(slider, xCoord + sliderWidth, 0).build().perform();

            //Enter captcha
            String captcha = browserWindow.findElement(By.id("captcha")).getText().trim();
            Object result = captcha_Calc(captcha);
            browserWindow.findElement(By.xpath("//*[@id=\"captchaControl\"]")).sendKeys(result.toString());

            //submit button functionality
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"cdk-overlay-2\"]/snack-bar-container/div/div/simple-snack-bar")).getText(), "Thank you so much for your amazing 5-star feedback!");

        }


        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }

    /**
     * Smoke Test to confirm the Customer Feedback is usable when logged in
     * @param chosenBrowser Browser type for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Customer_Feedback Smoke", "Customer_Feedback", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void CF2_Feedback_Loggedin(String chosenBrowser) throws IOException, InterruptedException, ScriptException
    {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try{
            //login
            TestFunctions.login(browserWindow);

            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[1]/div/span")).click();

            //Verify customer feedback page
            assertEquals(browserWindow.getCurrentUrl(), website+ "/#/contact");

            //verify submitting feedback
            browserWindow.findElement(By.xpath("//*[@id=\"comment\"]")).sendKeys("Anything");
            WebElement slider = browserWindow.findElement(By.xpath("//*[@id=\"rating\"]/div/div[3]/div[3]/span"));

            Dimension sliderSize = slider.getSize();
            int sliderWidth = sliderSize.getWidth();
            int xCoord = slider.getLocation().getX();
            Actions builder = new Actions(browserWindow);
            builder.moveToElement(slider).click().dragAndDropBy(slider, xCoord + sliderWidth, 0).build().perform();

            //Enter captcha
            String captcha = browserWindow.findElement(By.id("captcha")).getText().trim();
            Object result = captcha_Calc(captcha);
            browserWindow.findElement(By.xpath("//*[@id=\"captchaControl\"]")).sendKeys(result.toString());

            //submit button functionality
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
            assertEquals(browserWindow.findElement(By.xpath("//*[@id=\"cdk-overlay-2\"]/snack-bar-container/div/div/simple-snack-bar/span")).getText(), "Thank you for your feedback.");

        }


        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }
    /**
     * Smoke Test to confirm the Customer Feedback is not usable when no details are provided
     * @param chosenBrowser Browser type for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Sanity", "Customer_Feedback Sanity", "Customer_Feedback", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )

    public void CF3_NoDetails(String chosenBrowser)throws IOException, InterruptedException
    {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Navigate both to the website
        browserWindow.get(TestFunctions.website);

        //Wait for site to fully load
        TestFunctions.waitForSite(browserWindow);

        try{


            //Navigate to side bar Menu
            browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300);
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[1]/div/span")).click();

            //Verify customer feedback page
            assertEquals(browserWindow.getCurrentUrl(), website+ "/#/contact");

            //submit button functionality
            assertFalse(browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).isEnabled());

        }

        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();

        }
    }



    public Object captcha_Calc(String captcha){
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        try {

            return engine.eval(captcha.toString());
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
