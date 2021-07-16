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

public class Choose_Language implements ITest
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
     *Smoke tests for Valid use of Choose Language
     * Includes test cases CL_001,CL_002,CL_003
     *Programmer: Seyedmehrad Adimi
     */
    @Test(
            groups = {"Smoke","Choose_Language Smoke","Valid_Choose_Language"},
            priority = 1,
            enabled = true
    )
    public void CL1_Valid_Use() throws InterruptedException {
        //Browser setup
        WebDriver browserWindow = environment.makeDriver();

        BrowserPlusWebsiteSetup(browserWindow);

        // CL_001 test case: Verify the complete functionality of the Application by selecting 'Dansk' language
        String CL1_Language = "Dansk";
        TestThisLanguage(browserWindow,CL1_Language);
        Thread.sleep(1000);

        String CL2_Language = "Italiano";
        TestThisLanguage(browserWindow,CL2_Language);

        String CL3_Language = "Magyar";
        TestThisLanguage(browserWindow,CL3_Language);

        Thread.sleep(2000);
        browserWindow.quit();
    }

    private void TestThisLanguage(WebDriver browserWindow, String Language) throws InterruptedException {
        browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button.mat-focus-indicator.mat-tooltip-trigger.mat-menu-trigger.buttons.mat-button.mat-button-base")).click ();
        Thread.sleep(1000);

        if (Language.equals ("Dansk")){
            browserWindow.findElement (By.cssSelector ("#mat-radio-5")).click ();
            Thread.sleep(2000);

            WebElement navBar = browserWindow.findElement(By.cssSelector("#navbarAccount"));
            Thread.sleep(1000);
            assertEquals (navBar.getText (), "account_circle Konto");
            return;
        }else if (Language.equals ("Italiano")){
            browserWindow.findElement (By.cssSelector ("#mat-radio-11")).click ();
            Thread.sleep(2000);

            WebElement navBar = browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-search-result > div > div > div.heading.mat-elevation-z6 > div.ng-star-inserted"));
            Thread.sleep(1000);
            assertEquals (navBar.getText (), "Tutti i prodotti");
            return;
        }else if (Language.equals ("Magyar")){
            browserWindow.findElement (By.cssSelector ("#mat-radio-14")).click ();
            Thread.sleep(2000);

            WebElement navBar = browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-search-result > div > div > div.heading.mat-elevation-z6 > div.ng-star-inserted"));
            Thread.sleep(1000);
            assertEquals (navBar.getText (), "Összes termék");
            return;
        }
    }

    private void BrowserPlusWebsiteSetup(WebDriver browserWindow) throws InterruptedException {
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);
    }





    /**
     *Smoke tests for Invalid use of support chat
     * Includes test case SC_006
     *Programmer: Seyedmehrad Adimi
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Support_Chat Smoke","Invalid_Support_Chat"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void SC2_Invalid_Use(String chosenBrowser, String email, String password) throws InterruptedException, IOException {
        //Browser setup
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);



        browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click ();
        Thread.sleep(200);



        loginForMe (browserWindow,email,password);
        Thread.sleep (1000);
        browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click ();
        Thread.sleep (1000);
        WebElement SupportChat = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(8)"));
        assertTrue (SupportChat.isDisplayed ());
        Thread.sleep (1000);


        SupportChat.click ();

        WebElement inputText = browserWindow.findElement (By.id ("message-input"));
        inputText.sendKeys ("");
        inputText.sendKeys (Keys.ENTER);
        Thread.sleep (1000);

        try {
            WebElement SupportChat_answer = browserWindow.findElement (By.cssSelector ("#chat-window > div:nth-child(3) > div"));
            assertTrue (SupportChat_answer.isDisplayed ());
        }catch (Exception e){
            assertFalse (false);
        }


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
