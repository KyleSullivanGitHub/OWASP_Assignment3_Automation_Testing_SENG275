package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Random;

import static org.testng.Assert.*;

public class TestFunctions
{
    private static boolean registerOnce = false;//boolean to see if the constant account has been created for this test session
    private static boolean addressMade = false;//Booelan to see if a shipping address has already been made for the google account.
    public static String website = "https://juice-shop.herokuapp.com/#/";//main website
    public static String OS = System.getProperty("os.name").toLowerCase(); //operation system of user

    //site title
    private static String siteTitle = "OWASP Juice Shop";
    //css path to cookie element
    private static final String cookieElement = "#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper";
    //css path to navigation bar element
    public static String navPath = "#navbarAccount";
    //css path to login button
    public static String navbarLogin = "#navbarLoginButton";
    //css path to register button
    public static String regButton = "#registerButton";
    //css path to the google login button
    public static String loginButtonG = "#loginButtonGoogle";
    //css path to the email field on google login
    public static String identifierID = "#identifierId";
    //css path to the password field on google login
    public static String googlePasswordTarget = "#password > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > input:nth-child(1)";
    //id path to the login button
    public static String logButton = "loginButton";
    //common css path components
    public static String mInput = "#mat-input-";
    public static String mRadio = "#mat-radio-";
    //xpath to the basket
    public static String basketXpath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]";

    //amount of time to wait after test is concluded before ending the test
    public static int endTestWait = 500;

    //Declarations for constant account values
    private static boolean registerSetup = false; //has the constant account constants been set for this test session.
    public static String constEmail;//String containing the email for the constant session
    public static String constPassword;//password for the constant session
    public static String constAnswer; //answer for security question

    //Declarations for constant google account values
    private static boolean googleSetup = false; //has the google login constants been set up for this test session
    public static String googleEmail; //google email login
    public static String googlePassword; //google password login

    //Declarations for constant address values
    private static boolean addressSetup = false;
    public static Object[] addressSet;


    /**
     *
     */
    private static void constRandomAccount()
    {
        if(!registerSetup)
        {
            //Create a random number to add to the end of the email name
            int emailNumRandomizer = 0;
            Random emailRandomizer = new Random();
            for (int i = 0; i < 20; i++)
            {
                emailNumRandomizer += emailRandomizer.nextInt(9);
            }
            //set up the constant email for this session
            constEmail = "HelloWorld934" + emailNumRandomizer + "@gmail.com";
            constPassword = "Seng265!";
            constAnswer = "Seng";

            registerSetup = true;
        }
    }

    private static void constGoogleAccount()
    {
        if(!googleSetup)
        {
            googleEmail = "helloworld.owasp@gmail.com";
            googlePassword = "seng275@";
            googleSetup = true;
        }
    }

    public static void constAddressValues()
    {
        if(!addressSetup)
        {
            addressSet = new Object[]
                    {
                            "",
                            "Canada",//Country
                            "Seng275",//Name
                            "9999999",//PhoneNumber
                            "A0A 0A0",//Zip Code
                            "Internet",//Address
                            "Victoria",//City
                            "BC",//State
                    };
            addressSetup = true;
        }
    }

    /**
     * Pauses the test until the cookie popup on site visitation is present. Necessary due to slow loading times encountered, causing incorrectly failed tests
     * Programmer: Kyle Sullivan
     * @param test Webdriver to pause
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSite(WebDriver test) throws InterruptedException
    {
        waitForSitePrimary(test,cookieElement, true, false);
        waitForSiteXpath(test,"/html/body/div[1]/div/a",true);
    }

    /**
     * Pauses the test until a specific element on the site is present via cssSelector. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     * @param test Webdriver to pause
     * @param cssSelector element to look for
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSite(WebDriver test, String cssSelector) throws InterruptedException
    {
        waitForSitePrimary(test,cssSelector, false, false);
    }
    /**
     * Pauses the test until a specific element on the site is present via cssSelector. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     * @param test Webdriver to pause
     * @param cssSelector element to look for
     * @param interactive boolean to instruct the wait period to try clicking the element as well.
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSite(WebDriver test, String cssSelector, boolean interactive) throws InterruptedException
    {
        waitForSitePrimary(test,cssSelector,interactive, false);
    }

    /**
     * Pauses the test until a specific element on the site is present. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     * @param test Webdriver to pause
     * @param xPath element to look for
     * @param interactive boolean to instruct the wait period to try clicking the element as well.
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSiteXpath(WebDriver test, String xPath, boolean interactive) throws InterruptedException
    {
        waitForSitePrimary(test,xPath,interactive, true);

    }

    /**
     * Pauses the test until a specific element on the site is present. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     * @param test Webdriver to pause
     * @param xPath element to look for
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSiteXpath(WebDriver test, String xPath) throws InterruptedException
    {
        waitForSitePrimary(test,xPath,false, true);
    }

    /**
     * Functionality for waitForSite
     * @param test Webdriver to pause
     * @param path element to look for
     * @param interactive whether to click on the element
     * @param xPath whether to nav via xpath or not
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    private static void waitForSitePrimary(WebDriver test, String path,boolean interactive, boolean xPath) throws InterruptedException
    {
        boolean ready = false;
        WebElement element;
        By find = !xPath ? By.cssSelector(path) : By.xpath(path);
        while(!ready)
        {
            try
            {
                //find the element
                    element = test.findElement(find);

                //if the element is presented...
                if(element.isDisplayed())
                {
                    if (interactive)
                        element.click();
                    //stop the loop
                    ready = true;
                }
            }
            //if not, catch the exception, wait a moment then try again.
            catch(NoSuchElementException | ElementClickInterceptedException exception)
            {
                Thread.sleep(100);
            }
        }
    }

    /**
     * Quick navigation to the login page from any other page.
     * Programmer: Kyle Sullivan
     * @param test web browser for test
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void navToLogin(WebDriver test) throws InterruptedException
    {
        try{
            test.findElement(By.cssSelector(navbarLogin)).click();
        }
        catch(NoSuchElementException | ElementClickInterceptedException qucikLog)
        {
            //Find the account button
            test.findElement(By.cssSelector(navPath)).click();
            //verify that we can access the login page
            waitForSite(test, navbarLogin);
            WebElement accountMenuLogin = test.findElement(By.cssSelector(navbarLogin));
            assertTrue(accountMenuLogin.isEnabled());
            accountMenuLogin.click(); //Go to login page
        }
    }

    /**
     * Quick navigation to the registration page from any other page.
     * Programmer: Kyle Sullivan
     * @param test web browser for test
     * @exception  InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void navToReg(WebDriver test) throws InterruptedException
    {
        if(!test.getCurrentUrl().equals(website+"register"))
        {
            //navigate to login page
            navToLogin(test);
            //ensure that we can navigate to the sign up page
            WebElement signUpLink = test.findElement(By.cssSelector("#newCustomerLink"));
            assertTrue(signUpLink.isEnabled());
            signUpLink.click();//enter sign up page
        }
        else
        {
            test.navigate().refresh();
            waitForSite(test,navPath);
        }
    }
    public static void navToSavedAddresses(WebDriver test) throws InterruptedException
    {
        String xPathPart1 = "/html/body/div[3]/div[";
        String xPathPart2 = "]/div/div/div/button[";
        String xPathPart3 = "]";

        test.findElement(By.cssSelector(navPath)).click();
        waitForSiteXpath(test,xPathPart1 + 2 + xPathPart2 + 2 + xPathPart3,true);
        waitForSiteXpath(test,xPathPart1 + 3 + xPathPart2 + 3 + xPathPart3,true);
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
            constRandomAccount();
            //create the environment to perform a registration
            CreateEnvironment temp = new CreateEnvironment();
            Registration signUp = new Registration();
            signUp.SetUp();
            WebDriver tempBrowser = signUp.environment.makeDriver();
            try
            {
                //make the separate browser window
                tempBrowser.manage().window().maximize();
                tempBrowser.get(website);
                //Ensure the site is ready for testing
                waitForSite(tempBrowser);
                //navigate to registration.
                navToReg(tempBrowser);

                //Register the Account
                signUp.fillOutReg(tempBrowser, new Object[]{constEmail, constPassword, constPassword, true, constAnswer});
                waitForSite(tempBrowser, regButton, true);
            }
            finally
            {
                //close the browser
                Thread.sleep(endTestWait);
                tempBrowser.quit();
            }
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
        waitForSite(test,loginButtonG,true);
        Thread.sleep(500);

        if(!test.getCurrentUrl().startsWith(website))
        {
            constGoogleAccount();
            waitForSite(test, identifierID);
            WebElement emailUser = test.findElement(By.cssSelector(identifierID));
            emailUser.click();
            emailUser.sendKeys(googleEmail + Keys.ENTER);//enter email

            waitForSite(test, googlePasswordTarget);
            WebElement passwordInput = test.findElement(By.cssSelector(googlePasswordTarget));
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
        test.findElement(By.id (logButton)).click (); //click on login
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
        test.findElement(By.id (logButton)).click (); //click on login
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
        if(!test.getCurrentUrl().equals(website+"login"))
            navToLogin(test);

        Thread.sleep(500);

        test.findElement(By.id("email")).sendKeys(constEmail); //enter email
        test.findElement(By.id ("password")).sendKeys(password); //enter password
    }

    /**
     * Creates a new address for any tests requiring one.
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if test is interrupted during a thread waiting period
     */
    public static void createAddress() throws InterruptedException, IOException
    {
        //Check if a constant address is already made
        if(!addressMade)
        {
            //Create a new setup environment to create an address in.
            CreateEnvironment passBrowser = new CreateEnvironment();
            TestBrowser environment = passBrowser.createBrowser();
            WebDriver test = environment.makeDriver();
            test.manage().window().maximize();
            //Go to Website
            test.get(website);
            //Ensure the site is ready for testing
            TestFunctions.waitForSite(test);

            try
            {

                //Login and navigate to saved addresses
                login(test);
                navToSavedAddresses(test);

                //Click on Create New Address
                waitForSiteXpath(test,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-saved-address/div/app-address/mat-card/div/button",true);
                constAddressValues();

                for (int i = 1; i <= 7; i++)
                {
                    if (i != 5)
                        test.findElement(By.cssSelector(mInput + i)).sendKeys((String) addressSet[i]);
                    else
                        test.findElement(By.cssSelector("#address")).sendKeys((String) addressSet[i]);
                }
                waitForSite(test,"#submitButton",true);
                addressMade = true;
            }
            finally
            {
                Thread.sleep(endTestWait);
                test.quit();
            }
        }
    }

    //Regression Constants Orders & Payment
    static String[] menuOption = new String[]{"helloworld.owasp@gmail.com","Orders & Payment","Privacy & Security","Logout"};
    static String[] ordersPay = new String[]{"Order History","Recycle","My saved addresses","My Payment Options", "Digital Wallet"};
    static String[] privSec = new String[]{"Privacy Policy","Request Data Export","Request Data Erasure","Change Password", "2FA Configuration", "Last Login IP"};
    static Object[] navbarLoggedIn = new Object[]{"","Customer Feedback","Complaint","Support Chat","About Us","Photo Wall", "Deluxe Membership"};
    static Object[] navbarLoggedOut = new Object[]{"","Customer Feedback","About Us","Photo Wall"};


    /**
     * Common regression tests performed for most regression test sets
     * Programmer: Kyle Sullivan
     * @param test the browser for the regression test
     * @param loggedIn whether the test is logged in or not
     */
    public static void commonRegression(WebDriver test, String expectedURL, Boolean loggedIn) throws InterruptedException
    {
        assertEquals(test.getCurrentUrl(), expectedURL);
        assertEquals(test.getTitle(),siteTitle);
        try
        {
            //check dropdown nav menu
            WebElement navMenu = test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(1)"));
            assertWebElement(navMenu);
            if (loggedIn)
                NavigationMenu.menuOptions = navbarLoggedIn;
            else
                NavigationMenu.menuOptions = navbarLoggedOut;
            NavigationMenu.checkNav(test);
            test.findElement(By.className("mat-drawer-shown")).click();

            //check Main logo
            WebElement mainPageLink = test.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[2]"));
            assertWebElement(mainPageLink);
            assertEquals(mainPageLink.getAttribute("aria-label"), "Back to homepage");
            WebElement mainPageLogo = test.findElement(By.className("logo"));
            assertWebElement(mainPageLogo);
            assertEquals(mainPageLogo.getAttribute("src"), "https://juice-shop.herokuapp.com/assets/public/images/JuiceShop_Logo.png");

            //check Search tools
            if(!expectedURL.contains(website+"search?"))
            {
                WebElement searchElement = test.findElement(By.className("mat-search_icon-search"));
                Thread.sleep(1000);
                searchElement.click();
                TestFunctions.waitForSite(test, "#mat-input-0");
                searchElement = test.findElement(By.cssSelector("#mat-input-0"));
                assertWebElement(searchElement);
                searchElement.sendKeys("testing");


                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                //Try to copy the contents of the password field
                Keys OSspecific = TestFunctions.OS.contains("win") ? Keys.CONTROL : Keys.COMMAND;
                searchElement.sendKeys(OSspecific + "a");
                searchElement.sendKeys(OSspecific + "c");
                //Confirm that the clipboard does not contain the password
                assertEquals(cb.getData(DataFlavor.stringFlavor), "testing");
                test.findElement(By.className("mat-search_icon-close")).click();

            }
            //check account button
            waitForSite(test, navPath);
            WebElement accountMenu = test.findElement(By.cssSelector(navPath));
            assertWebElement(accountMenu);
            accountMenu.click();
            if (loggedIn)
            {
                WebElement account = test.findElement(By.cssSelector("button.mat-menu-item:nth-child(1) > span:nth-child(2)"));
                assertWebElement(account);
                assertEquals(account.getText(),menuOption[0]);

                WebElement OandP = test.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(2) > span:nth-child(2)"));
                assertWebElement(OandP);
                assertEquals(OandP.getText(),menuOption[1]);
                test.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(2)")).click();
                Thread.sleep(1000);


                WebElement OrderHistory = test.findElement(By.cssSelector("button.ng-tns-c245-5:nth-child(1) > span:nth-child(2)"));
                assertWebElement(OrderHistory);
                assertEquals(OrderHistory.getText(),ordersPay[0]);

                WebElement recycle = test.findElement(By.cssSelector("button.ng-tns-c245-5:nth-child(2) > span:nth-child(2)"));
                assertWebElement(recycle);
                assertEquals(recycle.getText(),ordersPay[1]);

                WebElement addresses = test.findElement(By.cssSelector("button.ng-tns-c245-5:nth-child(4) > span:nth-child(2)"));
                assertWebElement(addresses);
                assertEquals(addresses.getText(),ordersPay[2]);

                WebElement payment = test.findElement(By.cssSelector("button.mat-menu-item:nth-child(5) > span:nth-child(2)"));
                assertWebElement(payment);
                assertEquals(payment.getText(),ordersPay[3]);

                WebElement wallet = test.findElement(By.cssSelector("button.ng-tns-c245-5:nth-child(6) > span:nth-child(2)"));
                assertWebElement(wallet);
                assertEquals(wallet.getText(),ordersPay[4]);

                WebElement PandS = test.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(3) > span:nth-child(2)"));
                assertWebElement(PandS);
                assertEquals(PandS.getText(),menuOption[2]);

                test.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(3)")).click();
                Thread.sleep(2000);

                WebElement policy = test.findElement(By.cssSelector("button.ng-tns-c245-4:nth-child(1) > span:nth-child(2)"));
                assertWebElement(policy);
                assertEquals(policy.getText(),privSec[0]);

                WebElement export = test.findElement(By.cssSelector("button.ng-tns-c245-4:nth-child(2) > span:nth-child(2)"));
                assertWebElement(export);
                assertEquals(export.getText(),privSec[1]);

                WebElement erasure = test.findElement(By.cssSelector("button.ng-tns-c245-4:nth-child(3) > span:nth-child(2)"));
                assertWebElement(erasure);
                assertEquals(erasure.getText(),privSec[2]);

                WebElement changePassword = test.findElement(By.cssSelector("button.mat-menu-item:nth-child(5) > span:nth-child(2)"));
                assertWebElement(changePassword);
                assertEquals(changePassword.getText(),privSec[3]);

                WebElement config = test.findElement(By.cssSelector("button.mat-focus-indicator:nth-child(6) > span:nth-child(2)"));
                assertWebElement(config);
                assertEquals(config.getText(),privSec[4]);

                WebElement loginIP = test.findElement(By.cssSelector("button.mat-menu-item:nth-child(7) > span:nth-child(2)"));
                assertWebElement(loginIP);
                assertEquals(loginIP.getText(),privSec[5]);

                WebElement Logout = test.findElement(By.cssSelector("#navbarLogoutButton > span:nth-child(2)"));
                assertWebElement(Logout);
                assertEquals(Logout.getText(),menuOption[3]);
            }
            else
            {
                WebElement accountMenuLogin = test.findElement(By.cssSelector(TestFunctions.navbarLogin));
                assertWebElement(accountMenuLogin);
            }

            if (loggedIn)
            {
                WebElement basket = test.findElement(By.xpath(basketXpath));
                assertWebElement(basket);
                WebElement basketIcon = test.findElement(By.xpath(Basket.basketIcon_XPath));
                assertWebElement(basketIcon);
                WebElement basketCounter = test.findElement(By.xpath(Basket.basketIconQuantity_XPath));
                assertWebElement(basketCounter);
            }
            test.findElement(By.className("cdk-overlay-backdrop-showing")).click();

            //Change Language
            WebElement changeLanguageMenu;
            try
            { changeLanguageMenu= test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(7)")); }
            catch (NoSuchElementException tryAlt){ changeLanguageMenu= test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(8)")); }

            assertWebElement(changeLanguageMenu);
            changeLanguageMenu.click();
            test.findElement(By.className("cdk-overlay-backdrop-showing")).click();
        }
        catch (/*NoSuchElementException  | */UnsupportedFlavorException | IOException elementNotFound) { assertEquals("Missing","Header Bar Option");}
    }



    private static void assertWebElement(WebElement testing)
    {
        assertTrue(testing.isEnabled());
        assertTrue(testing.isDisplayed());
    }

    /*
    Due to the nature of the security question answer box, there is potentially dozens of ids for the several answers.
   The catch is that only a few of these are true for any given instance of a test, but we dont know which ones will be active.
   As such, we are forced to just check for many different possible answers and use the first valid one we find.
   This is not an error on the site's part, just a limitation of the automation.
   */
    /**
     * Recursive function for finding elements with varying IDs
     * Programmer: Kyle Sullivan, Nicole Makarowski
     * @param browserWindow the browser for the regression test
     * @param idPrefix ID of the element to search for excluding the number e.g "mat-radio-"
     * @param optionTry starting index for element
     * @param optionTryLimit ending index for element
     * @return the WebElement if found, or null if unable to find.
     */
    public static WebElement findRadioButton(WebDriver browserWindow, String idPrefix, int optionTry, int optionTryLimit){
        if (optionTry > optionTryLimit)
            return null;

        try {
            //Try an potential option
            WebElement element = browserWindow.findElement(By.id(idPrefix + optionTry));
            return element;
        }
        catch (Exception NoSuchElementException) {
            return findRadioButton(browserWindow, idPrefix, ++optionTry, optionTryLimit);
        }
    }

}
