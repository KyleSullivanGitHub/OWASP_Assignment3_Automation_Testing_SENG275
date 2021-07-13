package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;

public class Registration implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console
    String website = "https://juice-shop.herokuapp.com"; //default website URL

    TestBrowser environment;
    CreateEnvironment passBrowser;


    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Kyle Sullivan
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

    /**
     * Smoke test for valid inputs within several different browsers
     * Programmer: Kyle Sullivan
     * @param email email text for test
     * @param password password text for test
     * @param answer answer to security question text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Registration","Registration Smoke","hasDataProvider"},
            priority = 0,
            dataProvider = "RF1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void RF1_Valid_Input(String chosenBrowser, String email, String password, String answer) throws IOException, InterruptedException
    {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        fillOutReg(browserWindow, email, password, password,true, answer);

        browserWindow.findElement(By.cssSelector("#registerButton")).click();//click register button

        Thread.sleep(1000);
        assertEquals(browserWindow.getCurrentUrl(),website+"/#/login");
        Thread.sleep(500);
        browserWindow.quit();
    }

    /**
     *Smoke tests a single invalid registration attempt.
     *Programmer: Kyle Sullivan
     */
    @Test(
            groups = {"Smoke","Registration Smoke","Registration"},
            priority = 1,
            enabled = true
    )
    public void RF2_Invalid_Input() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        fillOutReg(browserWindow, "email", "pswrd", "password",false, "");

        //check that the registration button cannot be clicked.
        assertFalse(browserWindow.findElement(By.cssSelector("#registerButton")).isEnabled());

        Thread.sleep(5000);
        browserWindow.quit();
    }


    /**purpose
     *Programmer
     *@param
     */
    @Test(
            groups = {"",""},
            priority = 2,
            enabled = true
    )
    public void RF3_Validation_Email() throws IOException, InterruptedException
    {
        TestFunctions.createAccount();
    }

    /**
     * Smoke tests several invalid cases, which can be found in the data provider class.
     *  Programmer:Kyle Sullivan
     *  @param
     */
    @Test(
            groups = {"Sanity","Registration Sanity","Registration","hasDataProvider"},
            priority = 3,
            dataProvider = "RF4_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void RF4_Invalid_Input(String testing, String email, String password, String repeatPassword, Boolean doQuestion, String answer) throws InterruptedException
    {
        boolean disabledButton;
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        fillOutReg(browserWindow, email, password, repeatPassword,doQuestion,answer);

        disabledButton = browserWindow.findElement(By.cssSelector("#registerButton")).isEnabled();

        if(disabledButton)
        {
            //if the registration button is enabled, ensure it does not accept the account details.
            browserWindow.findElement(By.cssSelector("#registerButton")).click();//click register button
            Thread.sleep(1000);
            assertEquals(browserWindow.getCurrentUrl(),website+"/#/register");//confirm that we have not left the page.
        }
        else
        {
            //If the registration button cannot be accessed, confirm the test.
            assertFalse(disabledButton);
        }

        browserWindow.quit();
    }

    /**purpose
     *Programmer
     *@param
     */
    @Test(
            groups = {"",""},
            priority = 4,
            dataProvider = "",
            dataProviderClass = Test_Data.class,
            threadPoolSize =0,
            enabled = true
    )
    public void RF_Regression()
    {
        //Test Header
        //Test URL
        //Test Title
        //Check mandatory fields
        //check password advice
        //check password is hidden
    }

    /**
     * Method used to fill out registration form with passed values. Universal for all tests within registration.java
     * Programmer: Kyle Sullivan
     * @param browserWindow browser window used for this test
     * @param email email string used for test
     * @param password password string used for test
     * @param repeatPassword repeat password string used for test
     * @param doQuestion true/false to whether to use a security question
     * @param answer String for security question.
     * @throws InterruptedException
     * @throws IOException triggers if no browser has been set for the test
     */
    public void fillOutReg(WebDriver browserWindow, String email, String password, String repeatPassword, Boolean doQuestion, String answer) throws InterruptedException
    {
        boolean notFound = true;
        int optionTry = 0;
        int optionTryLimit = 50;

        browserWindow.get(website);
        TestFunctions.waitForSite(browserWindow);

        Thread.sleep(500);
        browserWindow.findElement(By.cssSelector("#navbarAccount")).click();
        Thread.sleep(500);
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
        browserWindow.findElement(By.cssSelector(".mat-select-trigger")).click(); //select security question

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
