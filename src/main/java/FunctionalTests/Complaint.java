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
import org.openqa.selenium.support.ui.Select;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.testng.Assert.*;

public class Complaint implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Seyedmehrad Adimi
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }





    /**
     *Smoke tests for Valid use of Complaint feature
     * Includes test cases C_001,C_002,C_003,C_005
     *Programmer: Seyedmehrad Adimi
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Complaint Smoke","Valid_Complaint", "has_Data_Provider"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void CO1_Valid_Use(String chosenBrowser, String email, String password) throws InterruptedException, IOException {
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);



        // C_001 test case: Verify  'Complaints' field is not visible before login
        browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click ();
        Thread.sleep(500);

        try {
            WebElement Complaint = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(7) > div > span"));
            assertFalse (Complaint.isDisplayed ());
        }catch (Exception e){
            assertFalse (false);
        }


        // C_002 test case: Verify navigation to complaint page
        loginForMe (browserWindow,email,password);
        Thread.sleep (1000);
        browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click ();
        Thread.sleep (1000);
        WebElement Complaint = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(7) > div > span"));
        assertTrue (Complaint.isDisplayed ());
        Thread.sleep (1000);


        // C_003 test case: Verify whether the required details and fields are displayed in the 'Complaint' page after login (Customer, Message. INvoice)
        Complaint.click ();
        WebElement customer = browserWindow.findElement (By.cssSelector ("#mat-input-1"));
        assertTrue (customer.isDisplayed ());

        WebElement Message = browserWindow.findElement (By.id ("complaintMessage"));
        assertTrue (Message.isDisplayed ());

        WebElement Invoice = browserWindow.findElement (By.cssSelector ("#complaint-form > div > label"));
        assertEquals (Invoice.getText (), "Invoice:");

        // C_005 test case: Verify whether the required details and fields are displayed in the 'Complaint' page after login (Customer, Message. INvoice)

        Message.click ();
        Message.sendKeys ("I have a complaint");

        WebElement submitButton = browserWindow.findElement (By.id ("submitButton"));
        submitButton.click ();
        Thread.sleep (500);

        WebElement complaintConfirmation = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-complaint > div > mat-card > div.confirmation"));
        assertTrue (complaintConfirmation.getText ().startsWith ("Customer support will get in touch with you soon! Your complaint reference is"));


        Thread.sleep(2000);
        browserWindow.quit();
    }





    /**
     *Smoke (also included in Sanity) test for Invalid use of Complaint feature
     * Includes test cases C_006
     *Programmer: Seyedmehrad Adimi
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Complaint Smoke","Sanity Smoke","Invalid_Complaint", "has_Data_Provider"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void CO2_Invalid_Use(String chosenBrowser, String email, String password) throws InterruptedException, IOException {
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);



        // C_006 test case: Verify submitting the Complaints in 'Complaint' page by not providing any details
        loginForMe (browserWindow,email,password);
        Thread.sleep (1000);
        browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click ();
        Thread.sleep (1000);
        WebElement Complaint = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(7) > div > span"));
        Complaint.click ();
        Thread.sleep (1000);


        WebElement submitButton = browserWindow.findElement (By.id ("submitButton"));
        assertFalse (submitButton.isEnabled ());


        Thread.sleep(2000);
        browserWindow.quit();
    }



    /**
     * Sanity test for Invalid use of Complaint feature
     * Includes test cases C_004
     *Programmer: Seyedmehrad Adimi
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Sanity","Sanity Smoke","Invalid_Complaint", "has_Data_Provider"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void CO3_Invalid_Use(String chosenBrowser, String email, String password) throws InterruptedException, IOException {
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);



        // C_004 test case: Verify all the text fields in the 'Complaint' page are mandatory
        loginForMe (browserWindow,email,password);
        Thread.sleep (1000);
        browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click ();
        Thread.sleep (1000);
        WebElement Complaint = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(7) > div > span"));
        Complaint.click ();
        Thread.sleep (1000);


        WebElement Message = browserWindow.findElement (By.id ("complaintMessage"));
        Message.click ();

        // Click on the screen and leave Message empty, to make sure it is mandatory (gives a message to provide a text)

        browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content")).click ();

        // Error message to provide text
        WebElement errorMessage = browserWindow.findElement (By.id ("mat-error-0"));
        assertEquals (errorMessage.getText (), "Please provide a text.");


        Thread.sleep(1000);
        browserWindow.quit();
    }



    private void loginForMe(WebDriver browserWindow,  String email, String password) throws InterruptedException{
        browserWindow.get (TestFunctions.website);
        Thread.sleep(500);
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep(500);



        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector(TestFunctions.navbarLogin));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        Thread.sleep(500);



        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        Thread.sleep(1000);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        Thread.sleep(1000);
        emailUsr.click ();

        emailUsr.sendKeys (email);
        Thread.sleep(500);
        emailUsr.sendKeys (Keys.ENTER);
        Thread.sleep(1000);

        WebElement passwordInput = browserWindow.findElement(By.cssSelector ("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input"));
        Thread.sleep(500);
        passwordInput.click ();
        passwordInput.sendKeys (password);
        Thread.sleep(500);
        passwordInput.sendKeys (Keys.ENTER);
        Thread.sleep(1000);
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
