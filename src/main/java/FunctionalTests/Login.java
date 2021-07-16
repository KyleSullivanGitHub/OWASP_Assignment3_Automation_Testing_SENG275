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

public class Login implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    String website = "https://juice-shop.herokuapp.com"; //default website URL
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
     * Smoke test for valid inputs within several different browsers
     * Programmer: Seyedmehrad Adimi
     * @param dataSet has email, password, and answer used to register
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Login","Login Smoke","hasDataProvider"},
            priority = 0,
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void LG1_Valid_Input(String chosenBrowser, Object[] dataSet) throws IOException, InterruptedException
    {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();



        try
        {
            fillOutReg(browserWindow, dataSet[0].toString (), dataSet[1].toString (), dataSet[1].toString (),true, dataSet[2].toString ());

            browserWindow.findElement(By.cssSelector("#registerButton")).click();//click register button

            Thread.sleep(1000);

            browserWindow.findElement (By.id ("email")).sendKeys (dataSet[0].toString ());
            Thread.sleep(5000);
            browserWindow.findElement (By.id ("password")).sendKeys (dataSet[1].toString ());
            Thread.sleep(500);

            browserWindow.findElement (By.id ("loginButton")).click ();
            Thread.sleep(1000);
            assertEquals(browserWindow.getCurrentUrl(),"https://juice-shop.herokuapp.com/#/search");
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     *Smoke tests a single invalid login attempt.
     *Programmer: Seyedmehrad Adimi
     */
    @Test(
            groups = {"Smoke","Login Smoke","Login"},
            priority = 1,
            enabled = true
    )
    public void LG2_Invalid_Input() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        try {
            fillOutLog (browserWindow, "email", "pswrd");
            Thread.sleep (2000);
            // Check that error message appears. Keep for tmr since website is not working
            WebElement message = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-login > div > mat-card > div.error.ng-star-inserted"));
            Thread.sleep (1500);
            assertEquals (message.getText (), "Invalid email or password.");
        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }


    /**
     * Smoke test for valid Google Login within several different browsers
     * Programmer: Seyedmehrad Adimi
     * @param dataSet has the email and password required
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Google_Login","Login Smoke","hasDataProvider"},
            priority = 0,
            dataProvider = "LG3_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void LG3_Valid_Input(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        try {
            fillOutLogGoogle (browserWindow, dataSet[0].toString (), dataSet[1].toString ());
            Thread.sleep (4000);
            assertEquals (browserWindow.getCurrentUrl (), "https://juice-shop.herokuapp.com/#/");
        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }


    /**
     *Smoke tests a single invalid Google login attempt.
     *Programmer: Seyedmehrad Adimi
     */
    @Test(
            groups = {"Smoke","Login Smoke","Google_Login"},
            priority = 1,
            enabled = true
    )
    public void LG4_Invalid_Input() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        try {
            fillOutLogGoogleInvalid (browserWindow, "email", "pswrd");
            Thread.sleep (2000);
            // Check that error message appears. Keep for tmr since website is not working

            WebElement message = browserWindow.findElement (By.cssSelector ("#view_container > div > div > div.pwWryf.bxPAYd > div > div.WEQkZc > div > form > span > section > div > div > div.d2CFce.cDSmF.cxMOTc > div > div.LXRPh > div.dEOOab.RxsGPe > div"));
            Thread.sleep (1000);
            assertEquals (message.getText (), "Couldn't find your Google Account");
        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }



    @Test(
            groups = {"Sanity","Login","Login Sanity","hasDataProvider"},
            priority = 0,
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void LG5_Invalid_Input(String chosenBrowser, Object[] dataSet) throws IOException, InterruptedException
    {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        try{fillOutReg(browserWindow, dataSet[0].toString (), dataSet[1].toString (), dataSet[1].toString (),true, dataSet[2].toString ());

        browserWindow.findElement(By.cssSelector("#registerButton")).click();//click register button

        Thread.sleep(1000);
        // Test case TC_LF_004: Invalid email and Valid password
        browserWindow.findElement (By.id ("email")).sendKeys ("inv"+dataSet[0].toString ());
        browserWindow.findElement (By.id ("password")).sendKeys (dataSet[1].toString ());
        Thread.sleep(500);

        browserWindow.findElement (By.id ("loginButton")).click ();

        Thread.sleep(1500);
        // Message Assertion?
        WebElement message = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-login > div > mat-card > div.error.ng-star-inserted"));
        Thread.sleep(500);
        assertEquals (message.getText (), "Invalid email or password.");



        // Test case TC_LF_005: Valid email and Inalid password
        browserWindow.findElement (By.id ("email")).clear ();
        Thread.sleep(500);
        browserWindow.findElement (By.id ("email")).sendKeys (dataSet[0].toString ());
        Thread.sleep(500);
        browserWindow.findElement (By.id ("password")).clear ();
        Thread.sleep(500);
        browserWindow.findElement (By.id ("password")).sendKeys ("inv"+dataSet[1].toString ());
        Thread.sleep(500);

        browserWindow.findElement (By.id ("loginButton")).click ();
        // Message Assertion?
        Thread.sleep(500);
        WebElement message1 = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-login > div > mat-card > div.error.ng-star-inserted"));
        Thread.sleep(500);
        assertEquals (message1.getText (), "Invalid email or password.");

        // Test case TC_LF_006: No credentials

        browserWindow.findElement (By.id ("email")).clear ();
        browserWindow.findElement (By.id ("email")).sendKeys ("");
        browserWindow.findElement (By.id ("password")).clear ();
        browserWindow.findElement (By.id ("password")).sendKeys ("");
        Thread.sleep(500);


        // Message Assertion?
        assertTrue (browserWindow.findElement (By.id ("loginButton")).isDisplayed ());


        // Test case TC_LF_010: No credentials: HOW?


        Thread.sleep(1000);
        assertEquals(browserWindow.getCurrentUrl(),"https://juice-shop.herokuapp.com/#/login");}
        finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }




    /**
     *Smoke tests invalid Google login attempt with valid password + Invalid email and Invalid password + Valid email.
     * @param dataSet has the email and password required
     * @param chosenBrowser browser used for that test
     *Programmer: Seyedmehrad Adimi
     */
    @Test(
            groups = {"Sanity","Login Sanity","Google_Login"},
            priority = 0,
            dataProvider = "LG3_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void LG6_Invalid_Input(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();


        try {
            // Test case TC_LF_022 : Valid password and Invalid email

            fillOutLogGoogleInvalid (browserWindow, "email", dataSet[1].toString ());
            Thread.sleep (2000);


            WebElement message = browserWindow.findElement (By.cssSelector ("#view_container > div > div > div.pwWryf.bxPAYd > div > div.WEQkZc > div > form > span > section > div > div > div.d2CFce.cDSmF.cxMOTc > div > div.LXRPh > div.dEOOab.RxsGPe > div"));
            Thread.sleep (1000);
            assertEquals (message.getText (), "Couldn't find your Google Account");
            Thread.sleep (500);

            // Test case TC_LF_023 : Invalid password and valid email
            fillOutLogGoogleInvalid (browserWindow, dataSet[0].toString (), "inv" + dataSet[1].toString ());

            Thread.sleep (1000);

            WebElement passwordInput = browserWindow.findElement (By.cssSelector ("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input"));
            passwordInput.click ();
            passwordInput.sendKeys ("inv" + dataSet[1].toString ());
            Thread.sleep (500);
            passwordInput.sendKeys (Keys.ENTER);

            Thread.sleep (500);
            WebElement message1 = browserWindow.findElement (By.cssSelector ("#view_container > div > div > div.pwWryf.bxPAYd > div > div.WEQkZc > div > form > span > section > div > div > div.SdBahf.VxoKGd.Jj6Lae > div.OyEIQ.uSvLId > div:nth-child(2)"));
            Thread.sleep (500);
            // Message Assertion????
            assertEquals (message1.getText (), "Wrong password. Try again or click ‘Forgot password’ to reset it.");
        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }





    /**
     * Smoke test for valid Login memory within several different browsers
     * Programmer: Seyedmehrad Adimi
     * @param dataSet has the email and password required
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Google_Login","Login_Memory","Login Sanity","hasDataProvider"},
            priority = 0,
            dataProvider = "LG3_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void LG7_Login_Memory(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        try{
        fillOutLogGoogle(browserWindow, dataSet[0].toString (), dataSet[1].toString ());
        Thread.sleep (2000);
        assertEquals(browserWindow.getCurrentUrl(),"https://juice-shop.herokuapp.com/#/access_token");

        Thread.sleep (2000);
        browserWindow.navigate ().back ();
        assertEquals(browserWindow.getCurrentUrl(),"https://juice-shop.herokuapp.com/#/access_token");

      /*  Thread.sleep (2000);

        WebDriver browserWindow1 = environment.makeDriver();
        browserWindow1.manage().window().maximize();
        browserWindow1.get(website);

        Thread.sleep (2000);
        browserWindow1.findElement (By.cssSelector ("body")).sendKeys (Keys.ESCAPE);
        Thread.sleep (1000);
        assertTrue (browserWindow1.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button.mat-focus-indicator.buttons.mat-button.mat-button-base.ng-star-inserted")).isDisplayed ());

        Thread.sleep (2000);
        browserWindow.quit();*/
        }
        finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }




    private void fillOutReg(WebDriver browserWindow, String email, String password, String repeatPassword, Boolean doQuestion, String answer) throws InterruptedException
    {
        boolean notFound = true;
        int optionTry = 0;
        int optionTryLimit = 50;

        browserWindow.get(website);

        TestFunctions.waitForSite(browserWindow);

        Thread.sleep(2500);
       // browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        browserWindow.findElement(By.cssSelector("#navbarAccount")).click();

        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector("#navbarLoginButton"));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        //Verify that the sign up page is accessible
        WebElement signUpLink = browserWindow.findElement(By.cssSelector("#newCustomerLink"));
        assertTrue(signUpLink.isEnabled());
        signUpLink.click();

        assertEquals(browserWindow.getCurrentUrl(),website+"/#/register");

        browserWindow.findElement(By.cssSelector("#emailControl")).sendKeys(email); //enter email
        browserWindow.findElement(By.cssSelector("#passwordControl")).sendKeys(password); //enter password
        browserWindow.findElement(By.cssSelector("#repeatPasswordControl")).sendKeys(repeatPassword); //reenter password
        //select security question
        browserWindow.findElement(By.cssSelector(".mat-select-trigger")).click();

        if(doQuestion)
        {
            Thread.sleep(500);
            /*
            Due to the nature of the security question answer box, there is potentially dozens of ids for the several answers.
            The catch is that only a few of these are true for any given instance of a test, but we dont know which ones will be active.
            As such, we are forced to just check for many different possible answers and use the first valid one we find.
            This is not an error on the site's part, just a limitation of the automation.
            */
            while (notFound && optionTry < optionTryLimit)
            {
                try
                {
                    browserWindow.findElement(By.cssSelector("#mat-option-" + optionTry)).click();
                    notFound = false;
                } catch (Exception NoSuchElementException)
                {
                    notFound = true;
                    optionTry++;
                }
            }
        }

        //give security question answer
        browserWindow.findElement(By.cssSelector("#securityAnswerControl")).sendKeys(answer); //enter answer
    }


    private void fillOutLog(WebDriver browserWindow, String email, String password) throws InterruptedException
    {

        browserWindow.get(website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep(500);



        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector("#navbarLoginButton"));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        Thread.sleep(500);


        browserWindow.findElement(By.id("email")).sendKeys(email); //enter email
        browserWindow.findElement(By.id ("password")).sendKeys(password); //enter password
        browserWindow.findElement(By.id ("loginButton")).click (); //click on login
    }


    private void fillOutLogGoogle(WebDriver browserWindow, String email, String password) throws InterruptedException
    {


        browserWindow.get(website);

        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep(500);



        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector("#navbarLoginButton"));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        Thread.sleep(500);



        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        Thread.sleep(1000);
//        browserWindow.findElement(By.cssSelector ("#view_container > div > div > div.pwWryf.bxPAYd > div > div.WEQkZc > div > form > span > section > div > div > div > div > ul > li.JDAKTe.eARute.W7Aapd.zpCp3.SmR8 > div")).click (); //click on login
//        Thread.sleep(500);

        WebElement emailUsr = browserWindow.findElement(By.cssSelector ("#identifierId"));
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

    }






    private void fillOutLogGoogleInvalid(WebDriver browserWindow, String email, String password) throws InterruptedException
    {


        browserWindow.get(website);

        Thread.sleep(2500);
        browserWindow.findElement (By.cssSelector ("body")).sendKeys (Keys.ESCAPE);
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep(500);



        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector("#navbarLoginButton"));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        Thread.sleep(500);



        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        Thread.sleep(1000);
       // browserWindow.findElement(By.cssSelector ("#view_container > div > div > div.pwWryf.bxPAYd > div > div.WEQkZc > div > form > span > section > div > div > div > div > ul > li.JDAKTe.eARute.W7Aapd.zpCp3.SmR8 > div")).click (); //click on login
        //Thread.sleep(500);

        WebElement emailUsr = browserWindow.findElement(By.cssSelector ("#identifierId"));
        Thread.sleep(1000);
        emailUsr.click ();
        emailUsr.click ();
        Thread.sleep(1000);
        emailUsr.clear ();
        emailUsr.sendKeys (email);
        Thread.sleep(500);
        emailUsr.sendKeys (Keys.ENTER);
        Thread.sleep(500);
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
