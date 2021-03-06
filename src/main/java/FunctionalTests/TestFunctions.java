package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

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
    private static boolean paymentMade = false;//boolean to see if a payment method has already been made for teh account
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

    //Declarations for constant payment values
    private static boolean paymentSetup = false;
    public static String payName;
    public static String cardNo;
    public static String exMonth;
    public static String exYear;
    public static String finalFour;



    /**
     * Method to set up the constant random account values
     * Programmer: Kyle Sullivan
     */
    public static void constRandomAccount()
    {
        if (!registerSetup)
        {
            //Create a random number to add to the end of the email name
            int emailNumRandomizer = 0;
            Random emailRandomizer = new Random();
            for (int i = 0; i < 20; i++)
            {
                emailNumRandomizer += emailRandomizer.nextInt(9);
            }
            //set up the constant email for this session
            constEmail = "HelloWorld9343332423423423411" + emailNumRandomizer + "@gmail.com";
            constPassword = "Seng265!";
            constAnswer = "Seng";

            registerSetup = true;
        }
    }

    /**
     * Establishes string values fro the constant google account
     * Programmer: Kyle Sullivan
     */
    private static void constGoogleAccount()
    {
        if (!googleSetup)
        {
            googleEmail = "helloworld.owasp@gmail.com";
            googlePassword = "seng275@";
            googleSetup = true;
        }
    }

    /**
     * Establishes string values fro the constant address
     * Programmer: Kyle Sullivan
     */
    public static void constAddressValues()
    {
        if (!addressSetup)
        {
            int nameNumRandomizer = 0;
            Random emailRandomizer = new Random();
            for (int i = 0; i < 20; i++)
            {
                nameNumRandomizer += emailRandomizer.nextInt(9);
            }
            addressSet = new Object[]
                    {
                            "",
                            "Canada",//Country
                            "Seng275" + nameNumRandomizer,//Name
                            "9999999",//PhoneNumber
                            "A0A 0A0",//Zip Code
                            "Internet",//Address
                            "Victoria",//City
                            "BC",//State
                    };
            addressSetup = true;
            finalFour = ""+nameNumRandomizer;
        }
    }

    /**
     * Declarations for the constant payment values.
     */
    public static void constPaymentValues()
    {
        String cardNumber = "";
        Random emailRandomizer = new Random();
        for (int i = 1; i <= 4; i++)
        {
            cardNumber += emailRandomizer.nextInt(9);
        }

        if (!paymentSetup)
        {
            payName = "Hellow World";
            cardNo = "111122223333"+cardNumber;
            exMonth = "1";
            exYear = "2080";
            paymentSetup = true;
        }
    }

    /**
     * Pauses the test until the cookie popup on site visitation is present. Necessary due to slow loading times encountered, causing incorrectly failed tests
     * Programmer: Kyle Sullivan
     *
     * @param test Webdriver to pause
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSite(WebDriver test) throws InterruptedException
    {
        waitForSitePrimary(test, cookieElement, true, false);
        waitForSiteXpath(test, "/html/body/div[1]/div/a", true);
    }

    /**
     * Pauses the test until a specific element on the site is present via cssSelector. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     *
     * @param test        Webdriver to pause
     * @param cssSelector element to look for
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSite(WebDriver test, String cssSelector) throws InterruptedException
    {
        waitForSitePrimary(test, cssSelector, false, false);
    }

    /**
     * Pauses the test until a specific element on the site is present via cssSelector. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     *
     * @param test        Webdriver to pause
     * @param cssSelector element to look for
     * @param interactive boolean to instruct the wait period to try clicking the element as well.
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSite(WebDriver test, String cssSelector, boolean interactive) throws InterruptedException
    {
        waitForSitePrimary(test, cssSelector, interactive, false);
    }

    /**
     * Pauses the test until a specific element on the site is present. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     *
     * @param test        Webdriver to pause
     * @param xPath       element to look for
     * @param interactive boolean to instruct the wait period to try clicking the element as well.
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSiteXpath(WebDriver test, String xPath, boolean interactive) throws InterruptedException
    {
        waitForSitePrimary(test, xPath, interactive, true);

    }

    /**
     * Pauses the test until a specific element on the site is present. Neccessary due to slow loading times causing tests to invalidly fail.
     * Programmer: Kyle Sullivan
     *
     * @param test  Webdriver to pause
     * @param xPath element to look for
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void waitForSiteXpath(WebDriver test, String xPath) throws InterruptedException
    {
        waitForSitePrimary(test, xPath, false, true);
    }

    /**
     * Functionality for waitForSite
     *
     * @param test        Webdriver to pause
     * @param path        element to look for
     * @param interactive whether to click on the element
     * @param xPath       whether to nav via xpath or not
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    private static void waitForSitePrimary(WebDriver test, String path, boolean interactive, boolean xPath) throws InterruptedException
    {
        boolean ready = false; //whether the element has been found
        WebElement element;
        By find = !xPath ? By.cssSelector(path) : By.xpath(path); //Whether to check by xpath or cssinput
        while (!ready)
        {
            try
            {
                //find the element
                element = test.findElement(find);

                //if the element is presented...
                if (element.isDisplayed())
                {
                    if (interactive)
                        element.click();
                    //stop the loop
                    ready = true;
                }
            }
            //if not, catch the exception, wait a moment then try again.
            catch (NoSuchElementException | ElementClickInterceptedException exception)
            {
                Thread.sleep(100);
            }
        }
    }

    /**
     * Quick navigation to the login page from any other page.
     * Programmer: Kyle Sullivan
     *
     * @param test web browser for test
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void navToLogin(WebDriver test) throws InterruptedException
    {

        try
        {
            test.findElement(By.cssSelector(navbarLogin)).click();
        } catch (NoSuchElementException | ElementClickInterceptedException qucikLog)
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
     *
     * @param test web browser for test
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void navToReg(WebDriver test) throws InterruptedException
    {
        if (!test.getCurrentUrl().equals(website + "register"))
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
            waitForSite(test, navPath);
        }
    }

    /**
     * Navigate to the saved address page via the account menu
     * Programmer: Kyle Sullivan
     *
     * @param test Test environment to act in
     * @throws InterruptedException Thrown if the test was interruped during a thread waiting period
     */
    public static void navToSavedAddresses(WebDriver test) throws InterruptedException
    {
        String xPathPart1 = "/html/body/div[3]/div[";
        String xPathPart2 = "]/div/div/div/button[";
        String xPathPart3 = "]";

        //click on account menu
        test.findElement(By.cssSelector(navPath)).click();
        //click on orders and payments
        waitForSiteXpath(test, xPathPart1 + 2 + xPathPart2 + 2 + xPathPart3, true);
        //click on my addresses
        waitForSiteXpath(test, xPathPart1 + 3 + xPathPart2 + 3 + xPathPart3, true);
    }

    /**
     * Navigate to the saved payments via the account menu
     * Programmer: Kyle Sullivan
     *
     * @param test test environment ot act in
     * @throws InterruptedException Thrown if test was interrputed during a thread waiting period.
     */
    public static void navToSavedPayment(WebDriver test) throws InterruptedException
    {
        String xPathPart1 = "/html/body/div[3]/div[";
        String xPathPart2 = "]/div/div/div/button[";
        String xPathPart3 = "]";

        //click on account menu
        test.findElement(By.cssSelector(navPath)).click();
        //click on orders and payments
        waitForSiteXpath(test, xPathPart1 + 2 + xPathPart2 + 2 + xPathPart3, true);
        //click on my addresses
        waitForSiteXpath(test, xPathPart1 + 3 + xPathPart2 + 4 + xPathPart3, true);
    }


    /**
     * Creates an account that can be used for any test, using a separate window.
     * Programmer: Kyle Sullivan
     *
     * @throws IOException          Thrown if no browser was selected for test
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void createAccount() throws IOException, InterruptedException
    {
        if (!registerOnce)
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
            } finally
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
     * Quickly confirms a registration of the constant account outside of the create account method
     * Programmer: Kyle Sullivan
     */
    public static void completedRegistration()
    {
        registerOnce = true;
    }

    /**
     * Logs into the site via google and a pre-created google account.
     * Programmer: Seyedmehrad Adimi, Nicole Makarowski, Kyle Sullivan
     *
     * @param test Test to log in to.
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void login(WebDriver test) throws InterruptedException
    {
        navToLogin(test);

        //register via google
        waitForSite(test, loginButtonG, true);
        Thread.sleep(500);

        if (!test.getCurrentUrl().startsWith(website))
        {
            constGoogleAccount();
            //fill out email
            waitForSite(test, identifierID);
            WebElement emailUser = test.findElement(By.cssSelector(identifierID));
            emailUser.click();
            emailUser.sendKeys(googleEmail + Keys.ENTER);//enter email

            //fill out password
            waitForSite(test, googlePasswordTarget);
            WebElement passwordInput = test.findElement(By.cssSelector(googlePasswordTarget));
            Thread.sleep(500);
            passwordInput.click();
            passwordInput.sendKeys(googlePassword + Keys.ENTER);//enter password
        }
        //as long as we are not back on the main page fo the website, wait
        while (!test.getCurrentUrl().equals(website))
            Thread.sleep(100);

        TestFunctions.waitForSite(test, navPath);//wait until the site has loaded
    }

    /**
     * Logs into the constant account for any test.
     * Programmer: Kyle Sullivan
     *
     * @param test browser window to log into.
     * @throws IOException          Thrown if no Browser was selected for test
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void manualLogin(WebDriver test) throws IOException, InterruptedException
    {
        quickLogFill(test, constPassword);
        test.findElement(By.id(logButton)).click(); //click on login
    }

    /**
     * Logs into the constant account for any test, using a different password.
     * Programmer: Kyle Sullivan
     *
     * @param test     browser window to log into.
     * @param password alternate password to use
     * @throws IOException          Thrown if no Browser was selected for test
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void manualLogin(WebDriver test, String password) throws IOException, InterruptedException
    {
        quickLogFill(test, password);
        test.findElement(By.id(logButton)).click(); //click on login
    }

    /**
     * Fills out the login page with the constant account.
     * Programmer: Seyedmehrad Adimi, Kyle Sullivan
     *
     * @param test     browser window to log into.
     * @param password alternate password to use
     * @throws IOException          Thrown if no Browser was selected for test
     * @throws InterruptedException Thrown if test was interrupted during a wait period
     */
    public static void quickLogFill(WebDriver test, String password) throws IOException, InterruptedException
    {
        //check if the constant account has been created for this test session
        if (!registerOnce)
            createAccount();
        //Check if we are already on the login page
        if (!test.getCurrentUrl().equals(website + "login"))
            navToLogin(test);

        Thread.sleep(500);

        test.findElement(By.id("email")).sendKeys(constEmail); //enter email
        test.findElement(By.id("password")).sendKeys(password); //enter password
    }

    /**
     * Creates a new address for any tests requiring one.
     * Programmer: Kyle Sullivan
     *
     * @throws InterruptedException Thrown if test is interrupted during a thread waiting period
     */
    public static void createAddress() throws InterruptedException, IOException
    {
        //Check if a constant address is already made
        if (!addressMade)
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
                waitForSiteXpath(test, "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-saved-address/div/app-address/mat-card/div/button", true);
                fillOutAddress(test);

            } finally
            {
                Thread.sleep(endTestWait);
                test.quit();
            }
        }
    }

    public static void fillOutAddress(WebDriver test) throws InterruptedException
    {
        constAddressValues();
        //Set address values
        for (int i = 1; i <= 7; i++)
        {
            if (i != 5)
                test.findElement(By.cssSelector(mInput + i)).sendKeys((String) addressSet[i]);
            else
                test.findElement(By.cssSelector("#address")).sendKeys((String) addressSet[i]);
        }
        waitForSite(test, "#submitButton", true);
        addressMade = true;
    }

    /**
     * QUick fill out of a payment form with constant values
     * Programmers: Nicole Makarowski, Kyle Sullivan
     * @param test test environment to work int
     * @throws InterruptedException Thrown if the test is interrupted during a thread waiting period
     * @throws IOException Thrown if no browser is set for test
     */
    public static void fillOutPayment(WebDriver test) throws InterruptedException, IOException
    {
        constPaymentValues();
        //Set address values
        TestFunctions.waitForSiteXpath(test, "//*[@id=\"mat-expansion-panel-header-0\"]", true);
        Thread.sleep(3000);
        findRadioButton(test, "mat-input-", 1, 15,true).sendKeys(payName);//Name
        findRadioButton(test, "mat-input-", 1, 15,true).sendKeys(cardNo);//Card No

        new Select(findRadioButton(test, "mat-input-", 1, 15,true)).selectByVisibleText(exMonth);//expiry month
        new Select(findRadioButton(test, "mat-input-", 1, 15,true)).selectByVisibleText(exYear);//expiry year

        waitForSite(test,"#submitButton",true);
        paymentMade = true;

    }


    //Regression Constants Orders & Payment
    //Logged in account menu options
    static String[] menuOption = new String[]{"helloworld.owasp@gmail.com","Orders & Payment","Privacy & Security","Logout"};
    //Logged in account menu -> Orders and payment submenu options
    static String[] ordersPay = new String[]{"Order History","Recycle","My saved addresses","My Payment Options", "Digital Wallet"};
    //Logged in account menu -> privacy and security submenu options
    static String[] privSec = new String[]{"Privacy Policy","Request Data Export","Request Data Erasure","Change Password", "2FA Configuration", "Last Login IP"};
    //navmenu options while logged in
    static Object[] navbarLoggedIn = new Object[]{"","Customer Feedback","Complaint","Support Chat","About Us","Photo Wall", "Deluxe Membership"};
    //navemenu options while logged out
    static Object[] navbarLoggedOut = new Object[]{"","Customer Feedback","About Us","Photo Wall"};


    /**
     * Common regression tests performed for most regression test sets
     * Programmer: Kyle Sullivan
     * @param test the browser for the regression test
     * @param expectedURL expected url for the test to be on
     * @param loggedIn whether the test is logged in or not
     */
    public static void commonRegression(WebDriver test, String expectedURL, Boolean loggedIn) throws InterruptedException
    {
        //confirm header and site title
        assertEquals(test.getCurrentUrl(), expectedURL);
        assertEquals(test.getTitle(),siteTitle);
        try
        {
            //check dropdown nav menu
            WebElement navMenu = test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(1)"));
            assertWebElement(navMenu);
            //check if we should be checkign the logged in or logged out options
            if (loggedIn)
                NavigationMenu.menuOptions = navbarLoggedIn;
            else
                NavigationMenu.menuOptions = navbarLoggedOut;
            //test the values
            NavigationMenu.checkNav(test);
            //close the nav menu
            test.findElement(By.className("mat-drawer-shown")).click();

            //check Main logo
            WebElement mainPageLink = test.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[2]"));
            assertWebElement(mainPageLink);
            //check that the element is correct
            assertEquals(mainPageLink.getAttribute("aria-label"), "Back to homepage");
            //check teh logo
            WebElement mainPageLogo = test.findElement(By.className("logo"));
            assertWebElement(mainPageLogo);
            assertEquals(mainPageLogo.getAttribute("src"), "https://juice-shop.herokuapp.com/assets/public/images/JuiceShop_Logo.png");

            //check Search tools, unless we are doing a search regression
            if(!expectedURL.contains(website+"search?"))
            {
                //find search element
                WebElement searchElement = test.findElement(By.className("mat-search_icon-search"));
                Thread.sleep(1000);
                //open it
                searchElement.click();
                TestFunctions.waitForSite(test, "#mat-input-0");
                searchElement = test.findElement(By.cssSelector("#mat-input-0"));
                assertWebElement(searchElement);
                //enter search value
                searchElement.sendKeys("testing");


                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                //Try to copy the contents of the search field
                Keys OSspecific = TestFunctions.OS.contains("win") ? Keys.CONTROL : Keys.COMMAND;
                searchElement.sendKeys(OSspecific + "a");
                searchElement.sendKeys(OSspecific + "c");
                //Confirm that the clipboard has the search value
                assertEquals(cb.getData(DataFlavor.stringFlavor), "testing");
                //close the search menu
                test.findElement(By.className("mat-search_icon-close")).click();

            }
            //check account button
            waitForSite(test, navPath);
            WebElement accountMenu = test.findElement(By.cssSelector(navPath));
            assertWebElement(accountMenu);
            //open the menu, closing other menus if neccessary
            while(true)
            {
                try
                {
                    accountMenu.click();
                    break;
                } catch (ElementNotInteractableException ignore)
                {
                }
            }
            if (loggedIn)
            {
                //check the account element of the account menu
                WebElement account = test.findElement(By.cssSelector("button.mat-menu-item:nth-child(1) > span:nth-child(2)"));
                assertWebElement(account);
                assertEquals(account.getText(),menuOption[0]);

                //check the Orders and Payment element of the account Menu
                WebElement OandP = test.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(2) > span:nth-child(2)"));
                assertWebElement(OandP);
                assertEquals(OandP.getText(),menuOption[1]);
                //click to open the submenu
                test.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(2)")).click();
                Thread.sleep(1000);

                //check the order history element of the Orders and Payment Menu
                WebElement OrderHistory = test.findElement(By.cssSelector("button.ng-tns-c245-5:nth-child(1) > span:nth-child(2)"));
                assertWebElement(OrderHistory);
                assertEquals(OrderHistory.getText(),ordersPay[0]);

                //check the recycle element of the Orders and Payment Menu
                WebElement recycle = test.findElement(By.cssSelector("button.ng-tns-c245-5:nth-child(2) > span:nth-child(2)"));
                assertWebElement(recycle);
                assertEquals(recycle.getText(),ordersPay[1]);

                //check the My saved addresses element of the Orders and Payment Menu
                WebElement addresses = test.findElement(By.cssSelector("button.ng-tns-c245-5:nth-child(4) > span:nth-child(2)"));
                assertWebElement(addresses);
                assertEquals(addresses.getText(),ordersPay[2]);

                //check the My Saved Payments element of the Orders and Payment Menu
                WebElement payment = test.findElement(By.cssSelector("button.mat-menu-item:nth-child(5) > span:nth-child(2)"));
                assertWebElement(payment);
                assertEquals(payment.getText(),ordersPay[3]);

                //check the wallet element of the Orders and Payment Menu
                WebElement wallet = test.findElement(By.cssSelector("button.ng-tns-c245-5:nth-child(6) > span:nth-child(2)"));
                assertWebElement(wallet);
                assertEquals(wallet.getText(),ordersPay[4]);

                //check the privacy and security element of the Account Menu
                WebElement PandS = test.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(3) > span:nth-child(2)"));
                assertWebElement(PandS);
                assertEquals(PandS.getText(),menuOption[2]);

                //click to open the privacy and security sebmenu
                test.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(3)")).click();
                Thread.sleep(2000);

                //check the privacy policy element of the privacy and security Menu
                WebElement policy = test.findElement(By.cssSelector("button.ng-tns-c245-4:nth-child(1) > span:nth-child(2)"));
                assertWebElement(policy);
                assertEquals(policy.getText(),privSec[0]);

                //check the export data element of the privacy and security Menu
                WebElement export = test.findElement(By.cssSelector("button.ng-tns-c245-4:nth-child(2) > span:nth-child(2)"));
                assertWebElement(export);
                assertEquals(export.getText(),privSec[1]);

                //check the Data Erasure element of the privacy and security Menu
                WebElement erasure = test.findElement(By.cssSelector("button.ng-tns-c245-4:nth-child(3) > span:nth-child(2)"));
                assertWebElement(erasure);
                assertEquals(erasure.getText(),privSec[2]);

                //check the change password element of the privacy and security Menu
                WebElement changePassword = test.findElement(By.cssSelector("button.mat-menu-item:nth-child(5) > span:nth-child(2)"));
                assertWebElement(changePassword);
                assertEquals(changePassword.getText(),privSec[3]);

                //check the configuration element of the privacy and security Menu
                WebElement config = test.findElement(By.cssSelector("button.mat-focus-indicator:nth-child(6) > span:nth-child(2)"));
                assertWebElement(config);
                assertEquals(config.getText(),privSec[4]);

                //check the last login IP element of the privacy and security Menu
                WebElement loginIP = test.findElement(By.cssSelector("button.mat-menu-item:nth-child(7) > span:nth-child(2)"));
                assertWebElement(loginIP);
                assertEquals(loginIP.getText(),privSec[5]);

                //check the Logout element of the account Menu
                WebElement Logout = test.findElement(By.cssSelector("#navbarLogoutButton > span:nth-child(2)"));
                assertWebElement(Logout);
                assertEquals(Logout.getText(),menuOption[3]);
            }
            else //if regression test is doen while logged out
            {
                //Check the Login element of the account menu
                WebElement accountMenuLogin = test.findElement(By.cssSelector(TestFunctions.navbarLogin));
                assertWebElement(accountMenuLogin);
            }

            //Check the basket and basket icon elements
            if (loggedIn)
            {
                WebElement basket = test.findElement(By.xpath(basketXpath));
                assertWebElement(basket);
                WebElement basketIcon = test.findElement(By.xpath(Basket.basketIcon_XPath));
                assertWebElement(basketIcon);
                WebElement basketCounter = test.findElement(By.xpath(Basket.basketIconQuantity_XPath));
                assertWebElement(basketCounter);
            }
            //close any open menu
            test.findElement(By.className("cdk-overlay-backdrop-showing")).click();

            //Check the change Language elements,
            WebElement changeLanguageMenu;
            try
            {changeLanguageMenu= test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(7)")); }
            catch (NoSuchElementException tryAlt)
            { //Fallback path
                changeLanguageMenu= test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(8)"));
            }

            assertWebElement(changeLanguageMenu);
            changeLanguageMenu.click();
            test.findElement(By.className("cdk-overlay-backdrop-showing")).click();
        }
        catch (UnsupportedFlavorException | IOException elementNotFound) { assertEquals("Missing","Header Bar Option");}
    }

    /**
     * Simple assetion of web elements for regression tests
     * Programmer: Kyle Sullivan
     * @param testing Test environment to test in
     */
    public static void assertWebElement(WebElement testing)
    {
        //Check that the element is enabled
        assertTrue(testing.isEnabled());
        //Check that the element is displayed
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
    public static WebElement findRadioButton(WebDriver browserWindow, String idPrefix, int optionTry, int optionTryLimit) throws IOException, InterruptedException
    {
        for (int i = optionTry; i <= optionTryLimit; i++)
        {
            try
            {
                //Try an potential option
                WebElement element = browserWindow.findElement(By.id(idPrefix + i));
                return element;
            } catch (NoSuchElementException ignore) {}
        }
        return null;
    }

    public static WebElement findRadioButton(WebDriver browserWindow, String idPrefix, int optionTry, int optionTryLimit, Boolean filter) throws IOException, InterruptedException
    {
        for (int i = optionTry; i <= optionTryLimit; i++)
        {
            try
            {
                //Try an potential option
                WebElement element = browserWindow.findElement(By.id(idPrefix + i));
                if(!element.getAttribute("class").contains("ng-valid"))
                    return element;
            } catch (NoSuchElementException ignore) {}
        }
        return null;
    }



}
