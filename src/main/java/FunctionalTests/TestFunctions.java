package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Random;

import static org.testng.Assert.*;

public class TestFunctions
{
    //TODO Replace as many path strings as possible with variables

    private static boolean registerOnce = false;//boolean to see if the constant account has been created for this test session
    private static boolean addressMade = false;//Booelan to see if a shipping address has already been made for the google account.
    static String website = "https://juice-shop.herokuapp.com/#/";
    static String constEmail;//String containing the email for the constant session
    static String constPassword = "Seng265!";//password for the constant session
    static String constAnswer = "Seng"; //answer for security question
    static String googleEmail = "helloworld.owasp@gmail.com";
    static String googlePassword = "seng275@";
    static String OS = System.getProperty("os.name").toLowerCase();
    static String cookieElement = "#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper";
    static int endTestWait = 2500;
    static String navPath = "#navbarAccount";
    
    
    /**
     * Pauses the test until the cookie popup on site visitation is present. Necessary due to slow loading times encountered, causing incorrectly failed tests
     * Programmer: Kyle Sullivan
     * @param test Webdriver to pause
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSite(WebDriver test) throws InterruptedException
    {
        waitForSitePrimary(test,cookieElement);
    }

    /**
     * Pauses the test until a specific element on the site is present. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     * @param test Webdriver to pause
     * @param cssSelector element to look for
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSite(WebDriver test, String cssSelector) throws InterruptedException
    {
        waitForSitePrimary(test,cssSelector);
    }

    /**
     * Functionality for waitForSite
     * @param test Webdriver to pause
     * @param cssElement element to look for
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    private static void waitForSitePrimary(WebDriver test, String cssElement) throws InterruptedException
    {
        boolean ready = false;
        while(!ready)
        {
            try
            {
                //find the element
                WebElement element = test.findElement(By.cssSelector(cssElement));
                //if the element is presented...
                if(element.isDisplayed())
                {
                    //stop the loop
                    ready = true;
                    if(cssElement.equals(cookieElement))
                        element.click();
                }
            }
            //if not, catch the exception, wait a moment then try again.
            catch(Exception NoSuchElementException)
            {
                Thread.sleep(100);
            }
        }
    }

    public void openAccountMenu(WebDriver test)
    {
        //check if account is on header


        //If it is
    }



    //TODO will probably need to make a method for navigating to account menu when screen is not full sized

    /**
     * Quick navigation to the login page from any other page.
     * Programmer: Kyle Sullivan
     * @param test web browser for test
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void navToLogin(WebDriver test) throws InterruptedException
    {
        //Find the account button
        test.findElement(By.cssSelector(navPath)).click();
        Thread.sleep(500);
        //verify that we can access the login page
        WebElement accountMenuLogin = test.findElement(By.cssSelector("#navbarLoginButton"));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click(); //Go to login page
    }

    /**
     * Quick navigation to the registration page from any other page.
     * Programmer: Kyle Sullivan
     * @param test web browser for test
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void navToReg(WebDriver test) throws InterruptedException
    {
        //navigate to login page
        navToLogin(test);
        //ensure that we can navigate to the sign up page
        WebElement signUpLink = test.findElement(By.cssSelector("#newCustomerLink"));
        assertTrue(signUpLink.isEnabled());
        signUpLink.click();//enter sign up page
    }


    /**
     * Creates an account that can be used for any test, using a separate window.
     * Programmer: Kyle Sullivan
     * @exception  IOException Thrown if no browser was selected for test
     * @exception  InterruptedException Thrown if test was interrupted during a wait period

     */
    public static void createAccount() throws IOException, InterruptedException
    {
        if(!registerOnce)
        {
            //Create a random number to add to the end of the email name
            int emailNumRandomizer = 0;
            Random emailRandomizer = new Random();
            for (int i = 0; i < 20; i++)
            {
                emailNumRandomizer += emailRandomizer.nextInt(9);
            }
            //set up the constant email for this session
            constEmail = "HelloWorld9" + emailNumRandomizer + "@gmail.com";

            //create the environment to perform a registration
            CreateEnvironment temp = new CreateEnvironment();
            Registration signUp = new Registration();
            signUp.SetUp();

            //make the separate browser window
            WebDriver tempBrowser = signUp.environment.makeDriver();
            tempBrowser.manage().window().maximize();
            tempBrowser.get(signUp.website);
            //Ensure the site is ready for testing
            waitForSite(tempBrowser);
            //navigate to registration.
            navToReg(tempBrowser);

            //Register the Account
            signUp.fillOutReg(tempBrowser, new Object[]{constEmail, constPassword, constPassword, true, constAnswer});
            tempBrowser.findElement(By.cssSelector("#registerButton")).click();//click register button
            //close the browser
            tempBrowser.quit();

            //mark the account as created for this session
            registerOnce = true;
        }
    }

    /**
     * Logs into the site via google and a pre-created google account.
     * Programmer: Seyedmehrad Adimi, Nicole Makarowski, Kyle Sullivan
     * @param test Test to log in to.
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void login(WebDriver test) throws InterruptedException
    {
        navToLogin(test);

        //register via google
        waitForSite(test,"#loginButtonGoogle");
        test.findElement(By.id("loginButtonGoogle")).click();
        Thread.sleep(500);

        if(!test.getCurrentUrl().startsWith(website))
        {
            waitForSite(test, "#identifierId");
            WebElement emailUser = test.findElement(By.cssSelector("#identifierId"));
            emailUser.click();
            emailUser.sendKeys(googleEmail + Keys.ENTER);//enter email

            waitForSite(test, "#password > div.aCsJod.oJeWuf > div > div,Xb9hP > input");
            WebElement passwordInput = test.findElement(By.cssSelector("#password > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > input:nth-child(1)"));
            Thread.sleep(500);
            passwordInput.click();
            passwordInput.sendKeys(googlePassword + Keys.ENTER);//enter password
        }
        while(!test.getCurrentUrl().equals(website))
            Thread.sleep(100);

        TestFunctions.waitForSite(test,navPath);//wait until the site has loaded
    }

    /**
     * Logs into the constant account for any test.
     * Programmer: Kyle Sullivan
     * @param test browser window to log into.
     * @exception  IOException Thrown if no Browser was selected for test
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void manualLogin(WebDriver test) throws IOException, InterruptedException
    {
        quickLogFill(test, constPassword);
        test.findElement(By.id ("loginButton")).click (); //click on login
    }

    /**
     * Logs into the constant account for any test, using a different password.
     * Programmer: Kyle Sullivan
     * @param test browser window to log into.
     * @param password alternate password to use
     * @exception  IOException Thrown if no Browser was selected for test
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void manualLogin(WebDriver test, String password) throws IOException, InterruptedException
    {
        quickLogFill(test, password);
        test.findElement(By.id ("loginButton")).click (); //click on login
    }

    /**
     * Fills out the login page with the constant account.
     * Programmer: Seyedmehrad Adimi, Kyle Sullivan
     * @param test browser window to log into.
     * @param password alternate password to use
     * @exception  IOException Thrown if no Browser was selected for test
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void quickLogFill(WebDriver test, String password) throws IOException, InterruptedException
    {
        //check if the constant account has been created for this test session
        if(!registerOnce)
            createAccount();
        //Check if we are already on the login page
        if(test.getCurrentUrl().equals("https://juice-shop.herokuapp.com/#/login"))
            navToLogin(test);

        Thread.sleep(500);

        test.findElement(By.id("email")).sendKeys(constEmail); //enter email
        test.findElement(By.id ("password")).sendKeys(password); //enter password
    }

    public static void createAddress(WebDriver browserWindow)
    {
        /*
        if(//not logged in)
        //orders
        //my saved address
        if(//address already present)
                {
                        return
                }
        //add new addresss


         */
    }

    /**
     * Common regression tests performed for most regression test sets
     * Programmer: Kyle Sullivan
     * @param test the browser for the regression test
     * @param loggedIn whether the test is logged in or not
     */
    public static void commonRegression(WebDriver test, Boolean loggedIn)
    {

        //check dropdown menu
            //check correct URLS
        WebElement navMenu = test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(1)"));
        assertWebElement(navMenu);
        navMenu.click();

        //check Main logo
        WebElement mainPage = test.findElement(By.cssSelector("button.buttons:nth-child(2)"));
        assertEquals(mainPage.getAttribute("routerlink"),"/search");

        //check Search tools
        WebElement searchBar = test.findElement(By.cssSelector("#searchQuery"));
        assertWebElement(searchBar);
        searchBar.click();

        //check account button
        WebElement accountMenu = test.findElement(By.id ("navbarAccount"));
        assertWebElement(accountMenu);
        accountMenu.click();

        if(loggedIn)
        {

            WebElement profile = accountMenu.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(1)"));
            assertWebElement(profile);
            assertEquals(profile.getText(),constEmail);

            WebElement ordPayMenu = accountMenu.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(2)"));
            assertWebElement(ordPayMenu);
            assertEquals(ordPayMenu.getText(),"Orders & Payment");

            WebElement privMenu = accountMenu.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(3)"));
            assertWebElement(privMenu);
            assertEquals(privMenu.getText(),"Privacy & Security");

            WebElement logoutMenu = accountMenu.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(4)"));
            assertWebElement(logoutMenu);
            assertEquals(logoutMenu.getText(),"Logout");

            WebElement basketMenu = test.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]"));
            assertWebElement(basketMenu);
            basketMenu.click();

        }
        else
        {
            WebElement accountMenuLogin = test.findElement(By.cssSelector("#navbarLoginButton"));
            assertWebElement(accountMenuLogin);
            assertEquals(accountMenuLogin.getAttribute("routerlink"),"/login");
        }

        WebElement changeLanguageMenu = test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(7)"));
        assertWebElement(changeLanguageMenu);
        changeLanguageMenu.click();
    }

    private static void assertWebElement(WebElement testing)
    {
        assertTrue(testing.isEnabled());
        assertTrue(testing.isDisplayed());
    }


}
