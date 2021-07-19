package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.testng.Assert.*;

public class TestFunctions
{
    //TODO Replace as many path strings as possible with variables

    private static boolean registerOnce = false;//boolean to see if the constant account has been created for this test session
    private static boolean addressMade = false;//Booelan to see if a shipping address has already been made for the google account.
    public static String website = "https://juice-shop.herokuapp.com/#/";
    public static String OS = System.getProperty("os.name").toLowerCase();

    private static String siteTitle = "OWASP Juice Shop";

    private static final String cookieElement = "#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper";
    public static String navPath = "#navbarAccount";
    public static String navbarLogin = "#navbarLoginButton";
    public static String regButton = "#registerButton";
    public static String loginButtonG = "#loginButtonGoogle";
    public static String identifierID = "#identifierId";
    public static String googlePasswordTarget = "#password > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > input:nth-child(1)";
    public static String logButton = "loginButton";
    public static String mInput = "#mat-input-";
    public static String mRadio = "#mat-radio-";
    public static String basketXpath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[4]";

    public static int endTestWait = 2500;

    //Declarations for constant account values
    private static boolean registerSetup = false;
    public static String constEmail;//String containing the email for the constant session
    public static String constPassword;//password for the constant session
    public static String constAnswer; //answer for security question

    //Declarations for constant google account values
    private static boolean googleSetup = false;
    public static String googleEmail;
    public static String googlePassword;

    //Declarations for constant address values
    private static boolean addressSetup = false;
    public static Object[] addressSet;

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
            constEmail = "HelloWorld9" + emailNumRandomizer + "@gmail.com";
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
        //verify that we can access the login page
        waitForSite(test,navbarLogin);
        WebElement accountMenuLogin = test.findElement(By.cssSelector(navbarLogin));
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

        //check dropdown nav menu
        WebElement navMenu = test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(1)"));
        assertWebElement(navMenu);
        if(loggedIn)
            NavigationMenu.menuOptions = new Object[]{"","Customer Feedback","Complaint","Support Chat","About Us","Photo Wall", "Deluxe Membership"};
        else
            NavigationMenu.menuOptions = new Object[]{"","Customer Feedback","About Us","Photo Wall"};
        NavigationMenu.checkNav(test);
        test.findElement(By.className("mat-drawer-shown")).click();

        //check Main logo
        WebElement mainPageLink = test.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-navbar/mat-toolbar/mat-toolbar-row/button[2]"));
        assertWebElement(mainPageLink);
        assertEquals(mainPageLink.getAttribute("aria-label"),"Back to homepage");
        WebElement mainPageLogo = test.findElement(By.className("logo"));
        assertWebElement(mainPageLogo);
        assertEquals(mainPageLogo.getAttribute("src"),"https://juice-shop.herokuapp.com/assets/public/images/JuiceShop_Logo.png");

        //check Search tools

        //check account button
        waitForSite(test,navPath);
        WebElement accountMenu = test.findElement(By.cssSelector(navPath));
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
        }
        else
        {
            WebElement accountMenuLogin = test.findElement(By.cssSelector(TestFunctions.navbarLogin));
            assertWebElement(accountMenuLogin);
            assertEquals(accountMenuLogin.getAttribute("routerlink"),"/login");
        }

        if(loggedIn)
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
        WebElement changeLanguageMenu = test.findElement(By.cssSelector("button.mat-tooltip-trigger:nth-child(7)"));
        assertWebElement(changeLanguageMenu);
        changeLanguageMenu.click();
        if(!languageListCreated)
            createLanguageList();
        for(int i = 1; i <= 37; i++)
        {
            assertEquals(test.findElement(By.cssSelector(mRadio+i)).getText(),languageList[i]);
        }
        test.findElement(By.className("cdk-overlay-backdrop-showing")).click();

    }

    private static boolean languageListCreated = false;
    private static Object[] languageList;
    private static void createLanguageList()
    {
        languageList = new Object[]{
                "",
                "Azərbaycanca",
                "Bahasa Indonesia",
                "Catalan",
                "Česky",
                "Dansk",
                "Deutsch",
                "Eesti",
                "English",
                "Español",
                "Français",
                "Italiano",
                "Język Polski",
                "Latvijas",
                "Magyar",
                "Nederlands",
                "Norsk",
                "Português",
                "Português (Brasil)",
                "Pусский",
                "Românesc",
                "Schwizerdütsch",
                "Suomalainen",
                "Svenska",
                "Türkçe",
                "Ελληνικά",
                "български (език)",
                "ქართული",
                "עברית",
                "عربي",
                "हिंदी",
                "ไทย",
                "ျမန္မာ",
                "한국어",
                "中文",
                "日本の",
                "繁體中文",
                "繁體中文",
        };
        languageListCreated = true;
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
