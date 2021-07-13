package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Random;

import static org.testng.Assert.*;

public class TestFunctions
{

    private static boolean registerOnce = false;//boolean to see if the constant account has been created for this test session
    static String constEmail;//String containing the email for the constant session
    static String constPassword = "Seng265!";//password for the constant session
    static String constAnswer = "Seng"; //answer for security question
    static String OS = System.getProperty("os.name").toLowerCase();

    /**
     * Pauses the test until a specific element is present. Necessary due to slow loading times encountered, causing incorrectly failed tests
     * Programmer: Kyle Sullivan
     * @param test Webdriver to pause
     * @throws InterruptedException
     */
    public static void waitForSite(WebDriver test) throws InterruptedException
    {
        boolean ready = false;
        Thread.sleep(500);
        while(!ready)
        {
            try
            {
                WebElement cookies = test.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper"));
                if(cookies.isDisplayed())
                {
                    ready = true;
                    cookies.click();
                }
            }
            catch(Exception NoSuchElementException)
            {
                Thread.sleep(500);
            }
        }
    }

    /**
     * Quick navigation to the login page from any other page.
     * Programmer: Kyle Sullivan
     * @param test web browser for test
     * @throws InterruptedException
     */
    public static void navToLogin(WebDriver test) throws InterruptedException
    {
        //Find the account button
        test.findElement(By.cssSelector("#navbarAccount")).click();
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
     * @throws InterruptedException
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
     * Creates an account that can be used for any test, using a seperate window.
     * Programmer: Kyle Sullivan
     * @throws IOException
     * @throws InterruptedException
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
            signUp.fillOutReg(tempBrowser, constEmail, constPassword, constPassword, true, constAnswer);
            tempBrowser.findElement(By.cssSelector("#registerButton")).click();//click register button
            //close the browser
            tempBrowser.quit();

            //mark the account as created for this session
            registerOnce = true;
        }
    }

    /**
     * Logs into the constant account for any test.
     * Programmer: Kyle Sullivan
     * @param test browser window to log into.
     * @throws IOException
     * @throws InterruptedException
     */
    public static void login(WebDriver test) throws IOException, InterruptedException
    {
        quickLogFill(test, constPassword);
        test.findElement(By.id ("loginButton")).click (); //click on login
    }

    /**
     * Logs into the constant account for any test, using a different password.
     * Programmer: Kyle Sullivan
     * @param test browser window to log into.
     * @param password alternate password to use
     * @throws IOException
     * @throws InterruptedException
     */
    public static void login(WebDriver test, String password) throws IOException, InterruptedException
    {
        quickLogFill(test, password);
        test.findElement(By.id ("loginButton")).click (); //click on login
    }

    /**
     * Fills out the login page with the constant account.
     * Programmer: Seyedmehrad Adimi, Kyle Sullivan
     * @param test browser window to log into.
     * @param password alternate password to use
     * @throws IOException
     * @throws InterruptedException
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

    /**
     * Common regression tests performed for most regression test sets
     * Programmer: Kyle Sullivan
     * @param test the browser for the regression test
     * @param loggedIn whether the test is logged in or not
     * @throws InterruptedException
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
