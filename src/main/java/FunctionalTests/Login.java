package FunctionalTests;

import Setup.*;
import org.checkerframework.checker.units.qual.A;
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
import org.openqa.selenium.interactions.Actions;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.testng.Assert.*;

public class Login implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();




    private final String loginErrorMessageCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-login > div > mat-card > div.error.ng-star-inserted";
    private final String loginErrorMessageText = "Invalid email or password.";
    private final String GoogleErrMessageEmailCSS = "#view_container > div > div > div.pwWryf.bxPAYd > div > div.WEQkZc > div > form > span > section > div > div > div.d2CFce.cDSmF.cxMOTc > div > div.LXRPh > div.dEOOab.RxsGPe > div";
    private final String GoogleErrMessageEmailText = "Couldn't find your Google Account";
    private final String mainLogoCSS="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(2) > span.mat-button-wrapper > img";
    private final String searchBarCSS="#searchQuery > span > mat-icon.mat-icon.notranslate.mat-ripple.mat-search_icon-search.ng-tns-c242-1.material-icons.mat-icon-no-color";
    private final String searchBarInputFieldCSS="#searchQuery";
    private final String chooseLanguageBtnCSS ="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button.mat-focus-indicator.mat-tooltip-trigger.mat-menu-trigger.buttons.mat-button.mat-button-base";
    private  final String sideMenuCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)";
    private final String ComplaintCSS ="body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(7) > div > span";
    private final String cusFeedbackCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(6)";
    private final String AboutUsCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(9)";
    private final String scoreBoardCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(10)";
    private final String PhotoWallCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(12) > div > span";
    private final String gitHubCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(13)";
    private final String DeluxeMembershipCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(13) > div > span";
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
            // Navigate to registration page and register for a new account
            fillOutReg(browserWindow, dataSet[0].toString (), dataSet[1].toString (), dataSet[1].toString (),true, dataSet[2].toString ());
            sleep (1);

            // Login using email and password that was just registered with
            loginWithRecentlyRegisteredAccount (dataSet, browserWindow, false);

            //Assertion to check we are at the right URL if logged in properly
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
            groups = {"Smoke","Login Smoke","Login", "hasNoDataProvider"},
            priority = 1,
            enabled = true
    )
    public void LG2_Invalid_Input() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        try {
            // Method to call to fill out the login process
            fillOutLog (browserWindow, "email", "pswrd");


            // Get the error message
            sleep (1);
            WebElement message = browserWindow.findElement (By.cssSelector (loginErrorMessageCSS));


            // Assert and check the error message to be appropriate
            assertEquals (message.getText (), loginErrorMessageText);

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
            // method call to fillout logging in using Google and the valid credential provided by the data provider
            fillOutLogGoogle (browserWindow, dataSet[0].toString (), dataSet[1].toString ());
            sleep (2);

            // Assert proper logged in and directed back to the original website

            assertEquals (browserWindow.getCurrentUrl (), "https://juice-shop.herokuapp.com/#/access_token");

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
            groups = {"Smoke","Login Smoke","Google_Login", "hasNoDataProvider"},
            priority = 1,
            enabled = true
    )
    public void LG4_Invalid_Input() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        try {
            // A method to fill out with invalid credential for Google login. A different method is created because of Google website behaviour
            // In this case, Google stops when the email is wrong

            fillOutLogGoogleInvalid (browserWindow, "email", "pswrd");
            sleep (2);

            // Find the error message
            WebElement message = browserWindow.findElement (By.cssSelector (GoogleErrMessageEmailCSS));


            // Make sure you get the right error for invalid credentialsentered
            assertEquals (message.getText (), GoogleErrMessageEmailText);

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

        try{

        // Method call to fill out registration
        fillOutReg(browserWindow, dataSet[0].toString (), dataSet[1].toString (), dataSet[1].toString (),true, dataSet[2].toString ());
        sleep (1);



        // Test case TC_LF_004: Invalid email and Valid password
        loginWithRecentlyRegisteredAccount(dataSet,browserWindow, true);



        // Get the error message
        WebElement message = browserWindow.findElement (By.cssSelector (loginErrorMessageCSS));


        // Assert to check if we get the right message
        assertEquals (message.getText (), loginErrorMessageText);



        // Test case TC_LF_005: Valid email and Invalid password
         validEmailandInvalidPasswordCase (dataSet, browserWindow);



       // Get the error message
        sleep (1);
        WebElement message1 = browserWindow.findElement (By.cssSelector (loginErrorMessageCSS));



        // check the error message and see if it is right
        assertEquals (message1.getText (), loginErrorMessageText);

        // Test case TC_LF_006: No credentials
        loginNoCredentials (browserWindow);


        // Assert that the login
        browserWindow.get ("https://juice-shop.herokuapp.com/#/login");

        assertFalse (browserWindow.findElement (By.id ("loginButton")).isEnabled ());



        //Todo how to test inactive credentials?
        // Test case TC_LF_010: No credentials:



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



            WebElement message = browserWindow.findElement (By.cssSelector (GoogleErrMessageEmailCSS));
            sleep (1);
            assertEquals (message.getText (), GoogleErrMessageEmailText);


            // Test case TC_LF_023 : Invalid password and valid email
            fillOutLogGoogleInvalid (browserWindow, dataSet[0].toString (), "inv" + dataSet[1].toString ());


            sleep (1);
            WebElement message1 = inavlidPassPlusMessage (dataSet[1], browserWindow);

            //Assert to see if we get an error with the right message
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

    //todo closing and opening does not work
    public void LG7_Login_Memory(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        try{

            fillOutLogGoogle(browserWindow, dataSet[0].toString (), dataSet[1].toString ());
            sleep (2);
            assertEquals(browserWindow.getCurrentUrl(),"https://juice-shop.herokuapp.com/#/access_token");

            browserWindow.navigate ().back ();
            sleep (1);
            assertEquals(browserWindow.getCurrentUrl(),"https://juice-shop.herokuapp.com/#/");

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

    /**
     * Regression test for Login feature within several different browsers.
     * Considers test cases TC_LF_007, TC_LF_008, TC_LF_011, TC_LF_016, TC_LF_017
     * Programmer: Seyedmehrad Adimi
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Regression","Login","Login_Regression","hasDataProvider"},
            priority = 0,
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void LG_Regression(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try{

            /* Test cases TC_LF_016, TC_LF_017: Verify the Page Heading, Page Title and Page URL of Login page, Verify the UI of the Login page*/
            TestFunctions.navToLogin (browserWindow);
            sleep (1);
            testRegressionForMe (browserWindow,false);

            /* Test case TC_LF_008: Verify E-Mail Address and Password text fields in the Login page have the placeholder text*/

            WebElement emailInputField = browserWindow.findElement (By.id ("email"));
            emailInputField.click ();
            assertEquals (emailInputField.getAttribute ("aria-label"),"Text field for the login email");
            assertElement (emailInputField);


            WebElement passwordInputField = browserWindow.findElement (By.id ("password"));
            passwordInputField.click ();
            assertEquals (passwordInputField.getAttribute ("aria-label"),"Text field for the login password");
            assertElement (passwordInputField);


            /* Test case TC_LF_007: Verify logging into the Application using Keyboard keys (Tab and Enter)*/
            // Register first since the accounts get deleted regularly.
            fillOutReg(browserWindow, dataSet[0].toString (), dataSet[1].toString (), dataSet[1].toString (),true, dataSet[2].toString ());
            sleep (2);


            // Go to the Login Page
            browserWindow.get ("https://juice-shop.herokuapp.com/#/login");


            // Use actions to perform TAB and ENTER press
            LoginUsingActions (dataSet, browserWindow);
            //sleep (2);

            // Verify we are logged in and in the home page

            assertEquals (browserWindow.getCurrentUrl (),"https://juice-shop.herokuapp.com/#/search");


            /* Test case TC_LF_011: Verify the number of unsuccessful login attempts  */


            WebElement accountMenu = browserWindow.findElement (By.cssSelector (TestFunctions.navPath));
            accountMenu.click ();

            //sleep (2);
            WebElement logoutBtn = browserWindow.findElement (By.id ("navbarLogoutButton"));
            logoutBtn.click ();

            loginForMeThreeTimesInvalid (browserWindow,dataSet[0].toString ()+"inv",dataSet[1].toString ()+"inv");





        }
        finally {
            //Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    private void LoginUsingActions(Object[] dataSet, WebDriver browserWindow) throws InterruptedException {
        Actions actionToTake = new Actions (browserWindow);
        actionToTake.sendKeys (Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB,Keys.TAB).perform ();
        sleep (1);


        actionToTake.sendKeys (dataSet[0].toString ());
        actionToTake.sendKeys (Keys.TAB).perform ();

        actionToTake.sendKeys (dataSet[1].toString ());
        actionToTake.sendKeys (Keys.TAB,Keys.TAB,Keys.TAB,Keys.ENTER).perform ();
    }

    private void loginForMeThreeTimesInvalid(WebDriver chosenBrowser, String email, String password) throws InterruptedException {
        TestFunctions.navToLogin (chosenBrowser);
        sleep (2);
        for (int i = 0; i < 4; i++) {
            WebElement emailInputField = chosenBrowser.findElement (By.id ("email"));
            WebElement passwordInputField = chosenBrowser.findElement (By.id ("password"));
            WebElement loginBtn = chosenBrowser.findElement (By.id ("loginButton"));
            emailInputField.click ();
            emailInputField.sendKeys (email);
            passwordInputField.click ();
            passwordInputField.sendKeys (password);
            loginBtn.click ();
            sleep (2);
            WebElement message = chosenBrowser.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-login > div > mat-card > div.error.ng-star-inserted"));
            sleep (2);
            assertEquals (message.getText (), "Invalid email or password.");
            emailInputField.clear ();
            passwordInputField.clear ();
        }

    }


    private void testRegressionForMe(WebDriver browserWindow, boolean lgStatus) throws InterruptedException {

        //check Main logo
        WebElement mainPage = browserWindow.findElement(By.cssSelector(mainLogoCSS));
        assertEquals( mainPage.getAttribute ("class"),"logo");
        assertElement (mainPage);


        // Check Search bar
        WebElement searchBar = browserWindow.findElement (By.cssSelector (searchBarCSS));
        assertElement (searchBar);
        searchBar.click ();
        sleep (2);
        WebElement searchBarInputField = browserWindow.findElement (By.cssSelector (searchBarInputFieldCSS));
        assertElement (searchBarInputField);

        // Check account button
        WebElement accountMenu = browserWindow.findElement(By.cssSelector(TestFunctions.navPath));
        assertElement (accountMenu);

        // Check Choose Language
        WebElement chooselanguage = browserWindow.findElement (By.cssSelector (chooseLanguageBtnCSS));
        assertElement (chooselanguage);


        // Check Side menu
        WebElement sideMenu = browserWindow.findElement (By.cssSelector (sideMenuCSS));
        assertElement (sideMenu);


        if (lgStatus){
            sideMenu.click ();
            sleep (1);

            presentInBothLoginAndLogoutRegression (browserWindow);

            // Check Complaint
            WebElement Complaint = browserWindow.findElement (By.cssSelector (ComplaintCSS));
            assertElement (Complaint);
            sleep (1);

            // Check Deluxe Membership

            WebElement DeluxeMembership = browserWindow.findElement (By.cssSelector (DeluxeMembershipCSS));
            assertElement (DeluxeMembership);
            sleep (1);


        }else{
            sideMenu.click ();
            sleep (1);
            presentInBothLoginAndLogoutRegression (browserWindow);
        }
        WebElement bodyClick = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > div.mat-drawer-backdrop.ng-star-inserted.mat-drawer-shown"));
        bodyClick.click ();


        sleep (1);
    }

    private void presentInBothLoginAndLogoutRegression(WebDriver browserWindow) throws InterruptedException {
        // Check Customer Feedback
        WebElement CusFeedback = browserWindow.findElement (By.cssSelector (cusFeedbackCSS));
        assertElement (CusFeedback);
        sleep (1);

        // Check About Us
        WebElement AboutUs = browserWindow.findElement (By.cssSelector (AboutUsCSS));
        assertElement (AboutUs);
        sleep (1);

        // Check Photo Wall
        WebElement PhotoWall = browserWindow.findElement (By.cssSelector (PhotoWallCSS));
        assertElement (PhotoWall);
        sleep (1);

        // Check Score Board
        WebElement scoreBoard = browserWindow.findElement (By.cssSelector (scoreBoardCSS));
        assertElement (scoreBoard);
        sleep (1);

        // Check Github
        WebElement gitHub = browserWindow.findElement (By.cssSelector (gitHubCSS));
        assertElement (gitHub);
        sleep (1);
    }

    static void assertElement(WebElement element){
        assertTrue (element.isDisplayed ());
        assertTrue (element.isEnabled ());
    }



    static void fillOutReg(WebDriver browserWindow, String email, String password, String repeatPassword, Boolean doQuestion, String answer) throws InterruptedException
    {
        boolean notFound = true;
        int optionTry = 0;
        int optionTryLimit = 50;

        browserWindow.get(TestFunctions.website);

        Actions pressESC = new Actions (browserWindow);
        pressESC.sendKeys (Keys.ESCAPE).perform ();

        TestFunctions.navToReg (browserWindow);

        browserWindow.findElement(By.cssSelector("#emailControl")).sendKeys(email); //enter email
        browserWindow.findElement(By.cssSelector("#passwordControl")).sendKeys(password); //enter password
        browserWindow.findElement(By.cssSelector("#repeatPasswordControl")).sendKeys(repeatPassword); //reenter password
        //select security question
        browserWindow.findElement(By.cssSelector(".mat-select-trigger")).click();

        if(doQuestion)
        {
            sleep(1);
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
        sleep (1);
        browserWindow.findElement(By.cssSelector("#registerButton")).click();
    }


    private void fillOutLog(WebDriver browserWindow, String email, String password) throws InterruptedException
    {

        browserWindow.get(TestFunctions.website);
        sleep (2);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        sleep (1);



        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector(TestFunctions.navbarLogin));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        sleep (1);


        browserWindow.findElement(By.id("email")).sendKeys(email); //enter email
        browserWindow.findElement(By.id ("password")).sendKeys(password); //enter password
        browserWindow.findElement(By.id (TestFunctions.logButton)).click (); //click on login
    }


    static void fillOutLogGoogle(WebDriver browserWindow, String email, String password) throws InterruptedException
    {


        browserWindow.get(TestFunctions.website);

        sleep (2);

        Actions pressESC = new Actions (browserWindow);
        pressESC.sendKeys (Keys.ESCAPE).perform ();
        //browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        sleep (1);
        browserWindow.findElement(By.id ("navbarAccount")).click ();
        sleep (1);



        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector(TestFunctions.navbarLogin));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        sleep (1);



        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        sleep (1);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        sleep (1);
        emailPassEnter (browserWindow, email, password, emailUsr);

    }

    static void emailPassEnter(WebDriver browserWindow, String email, String password, WebElement emailUsr) throws InterruptedException {
        emailUsr.click ();

        emailUsr.sendKeys (email);
        sleep(1);
        emailUsr.sendKeys (Keys.ENTER);
        sleep(2);

        WebElement passwordInput = browserWindow.findElement(By.cssSelector ("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input"));
        sleep(1);
        passwordInput.click ();
        passwordInput.sendKeys (password);
        sleep (1);
        passwordInput.sendKeys (Keys.ENTER);
    }


    private void fillOutLogGoogleInvalid(WebDriver browserWindow, String email, String password) throws InterruptedException
    {


        browserWindow.get(TestFunctions.website);
        sleep (1);

        Actions pressESC = new Actions (browserWindow);
        pressESC.sendKeys (Keys.ESCAPE).perform ();


        browserWindow.findElement(By.cssSelector (TestFunctions.navPath)).click ();




        //verify that we can access the login page
        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector(TestFunctions.navbarLogin));
        accountMenuLogin.click();

        sleep (1);



        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login

       // browserWindow.findElement(By.cssSelector ("#view_container > div > div > div.pwWryf.bxPAYd > div > div.WEQkZc > div > form > span > section > div > div > div > div > ul > li.JDAKTe.eARute.W7Aapd.zpCp3.SmR8 > div")).click (); //click on login
        //Thread.sleep(500);

        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        emailUsr.click ();
        emailUsr.click ();
        emailUsr.clear ();
        emailUsr.sendKeys (email);
        emailUsr.sendKeys (Keys.ENTER);
        sleep (1);
    }

    private void loginWithRecentlyRegisteredAccount(Object[] dataSet, WebDriver browserWindow, boolean invalid) throws InterruptedException {
        if (invalid){
            browserWindow.findElement (By.id ("email")).sendKeys (dataSet[0].toString ()+"inv");
        }else{
            browserWindow.findElement (By.id ("email")).sendKeys (dataSet[0].toString ());
        }
        Thread.sleep (1000);

        browserWindow.findElement (By.id ("password")).sendKeys (dataSet[1].toString ());
        Thread.sleep (1000);

        browserWindow.findElement (By.id ("loginButton")).click ();
        Thread.sleep (1000);
    }

    private void loginNoCredentials(WebDriver browserWindow) throws InterruptedException {
        browserWindow.findElement (By.id ("email")).clear ();
        browserWindow.findElement (By.id ("email")).sendKeys ("");
        browserWindow.findElement (By.id ("password")).clear ();
        browserWindow.findElement (By.id ("password")).sendKeys ("");
        Thread.sleep(500);
    }

    private void validEmailandInvalidPasswordCase(Object[] dataSet, WebDriver browserWindow) throws InterruptedException {
        browserWindow.findElement (By.id ("email")).clear ();
        Thread.sleep (1);
        browserWindow.findElement (By.id ("email")).sendKeys (dataSet[0].toString ());
        Thread.sleep (1);
        browserWindow.findElement (By.id ("password")).clear ();
        Thread.sleep (1);
        browserWindow.findElement (By.id ("password")).sendKeys ("inv"+ dataSet[1].toString ());
        Thread.sleep (1);
        browserWindow.findElement (By.id ("loginButton")).click ();
    }

    private WebElement inavlidPassPlusMessage(Object o, WebDriver browserWindow) throws InterruptedException {
        WebElement passwordInput = browserWindow.findElement (By.cssSelector ("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input"));
        passwordInput.click ();
        passwordInput.sendKeys ("inv" + o.toString ());
        sleep (1);
        passwordInput.sendKeys (Keys.ENTER);

        sleep (1);
        WebElement message1 = browserWindow.findElement (By.cssSelector ("#view_container > div > div > div.pwWryf.bxPAYd > div > div.WEQkZc > div > form > span > section > div > div > div.SdBahf.VxoKGd.Jj6Lae > div.OyEIQ.uSvLId > div:nth-child(2)"));
        sleep (1);
        return message1;
    }

    private static void sleep(int a) throws InterruptedException {

        switch (a) {
            case 1:
                Thread.sleep (1000);
                break;
            case 2:
                Thread.sleep (2000);
                break;
            case 3:
                Thread.sleep (3000);
                break;
            case 4:
                Thread.sleep (4000);
                break;
            case 5:
                Thread.sleep (5000);
                break;

        }
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
