package FunctionalTests;

import Setup.*;
import org.openqa.selenium.*;
import org.testng.ITest;
import org.testng.annotations.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.testng.Assert.*;

/**
 * Tests for verifying the full functionality of the registration feature
 */
public class Registration implements ITest
{
    private final ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;
    WebDriver browserWindow;
    boolean inTest = false;
    int counter = 0;


    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
     * @exception IOException Thrown if no browser is chosen for a test
     */
    @BeforeClass
    public void SetUp() throws IOException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }

    /**
     * Smoke test for valid inputs to Registration within several different browsers
     * Programmer: Kyle Sullivan
     * @param chosenBrowser Browser used for that test
     * @param dataSet Object containing a data set for this test
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Registration", "hasDataProvider"},
            priority = 1,
            dataProvider = "RF1_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RF1_Valid_Input(String chosenBrowser, Object[] dataSet) throws IOException, InterruptedException
    {

        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        //Navigate to Site
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);
        try
        {
            //Navigate to registration.
            TestFunctions.navToReg(browserWindow);

            //Fill out registration with a valid data set
            fillOutReg(browserWindow, dataSet);

            TestFunctions.waitForSite(browserWindow,TestFunctions.regButton,true);//click register button

            //Check that the user is taken back to the login page
            Thread.sleep(1000);
            assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website + "login");
            Thread.sleep(500);
        }
        finally
        {
            //End the Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Smoke tests a single invalid registration attempt.
     * Programmer: Kyle Sullivan
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Smoke", "Registration","noDataProvider"},
            priority = 2,
            enabled = true
    )
    public void RF2_Invalid_Input() throws InterruptedException
    {
        //Create the Test Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        //Navigate to Website
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Navigate to registration.
            TestFunctions.navToReg(browserWindow);
            //Fill out registration form with an invalid data set
            fillOutReg(browserWindow, new Object[] {"email", "pswrd", "password", false, ""});

            //Check that the registration button cannot be clicked.
            assertFalse(browserWindow.findElement(By.cssSelector(TestFunctions.regButton)).isEnabled());
        }
        finally
        {
            //End Test
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }


    /**
     * Smoke tests several invalid cases for Registration, which can be found in the data provider class.
     * Programmer: Kyle Sullivan
     * @param testing Invalid case being tested
     * @param dataSet An Object containing a data set for this test
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    @Test(
            groups = {"Sanity", "Registration", "hasDataProvider"},
            priority = 51,
            dataProvider = "RF4_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void RF4_Invalid_Input(String testing, Object[] dataSet) throws InterruptedException
    {

        boolean disabledButton; //Whether the Registration button is disabled

        if(!inTest)
        {
            //Create Test Environment
            browserWindow = environment.makeDriver();
            browserWindow.manage().window().maximize();
            //Navigate to Website
            browserWindow.get(TestFunctions.website);
            //Ensure the site is ready for testing
            TestFunctions.waitForSite(browserWindow);
            inTest = true;
            counter = 0;
        }

        try
        {
            //Navigate to registration.
            TestFunctions.navToReg(browserWindow);
            //Fill out Registration form
            fillOutReg(browserWindow, dataSet);
            //Check if the registration button is disabled.
            disabledButton = browserWindow.findElement(By.cssSelector(TestFunctions.regButton)).isEnabled();

            //If the button is enabled...
            if (disabledButton)
            {
                //...Ensure it does not accept the account details.
                TestFunctions.waitForSite(browserWindow,TestFunctions.regButton,true);//click register button
                Thread.sleep(1000);
                assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website + "login");//confirm that we have not left the page.
            }
            else
            {
                //If the registration button cannot be accessed, confirm the test.
                assertFalse(disabledButton);
            }
            counter++;
        }
        catch (Exception badTest)
        {
            inTest = false;
        }
        finally
        {
            if(!inTest || counter == 6)
            {
                //End the Test
                Thread.sleep(TestFunctions.endTestWait);
                browserWindow.quit();
                inTest = false;
            }
        }
    }

    /**
     * Regression test for registration feature
     * Programmer: Kyle Sullivan
     * @throws InterruptedException Thrown if the test is interrupted during a thread waiting period
     */
    @Test(
            groups = {"Regression", "Registration", "noDataProvider"},
            priority = 75,
            enabled = true
    )
    public void RF_Regression() throws InterruptedException
    {
        //make environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        try
        {
            String xPathLoc = "register";
            String pass = "primary";
            String fail = "warn";
            //object of test results when enteirng
            Object[] passwordComplexityExpected = new Object[]{"",fail,fail,fail,fail,fail};
            //Object of field placeholders
            String[] placeholders = new String[]{"Email","Password","Repeat Password","Security Question","Answer"};

            //navigate to registration
            TestFunctions.navToReg(browserWindow);

            //test common regression features
            TestFunctions.commonRegression(browserWindow, TestFunctions.website+"register", false);

            //check feature title
            assertEquals(browserWindow.findElement(By.cssSelector(".mat-card > h1:nth-child(1)")).getText(),"User Registration");

            //Set input fields
            WebElement email = browserWindow.findElement(By.id("emailControl"));
            WebElement password = browserWindow.findElement(By.id("passwordControl"));
            WebElement repPassword = browserWindow.findElement(By.id("repeatPasswordControl"));
            WebElement secQuest = browserWindow.findElement(By.name("securityQuestion"));
            WebElement secAnswer = browserWindow.findElement(By.id("securityAnswerControl"));
            //gather into objects
            WebElement[] fields = new WebElement[]{email,password,repPassword,secQuest,secAnswer};


            for(int preFilled = 0; preFilled <= 4; preFilled++)
            {
                assertEquals(fields[preFilled].getAttribute("aria-invalid"),"false");
            }
            //confirm the placeholder fields
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-register/div/mat-card/div[2]/mat-form-field[1]/div/div[1]/div[3]/span/label/mat-label")).getText(),placeholders[0]);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-register/div/mat-card/div[2]/mat-form-field[2]/div/div[1]/div[3]/span/label/mat-label")).getText(),placeholders[1]);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-register/div/mat-card/div[2]/mat-form-field[3]/div/div[1]/div[3]/span/label/mat-label")).getText(),placeholders[2]);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-register/div/mat-card/div[2]/div[1]/mat-form-field[1]/div/div[1]/div[3]/span/label/mat-label")).getText(),placeholders[3]);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-register/div/mat-card/div[2]/div[1]/mat-form-field[2]/div/div[1]/div[3]/span/label/mat-label")).getText(),placeholders[4]);

            //Activate password complexity feature
            browserWindow.findElement(By.className("mat-slide-toggle-thumb")).click();
            PasswordComplexity.testPassAdvice(browserWindow,passwordComplexityExpected,xPathLoc);

            //Fill out the registration page with blank fields
            fillOutReg(browserWindow, new Object[]{"","","",false,""});
            Thread.sleep(1000);
            secAnswer.click();

            //test common regression features
            TestFunctions.commonRegression(browserWindow, TestFunctions.website+"register", false);
            //assert all fields must be filled out
            for(int blank = 0; blank <= 4; blank++)
                assertEquals(fields[blank].getAttribute("aria-invalid"),"false");
            //test password advice
            PasswordComplexity.testPassAdvice(browserWindow,passwordComplexityExpected,xPathLoc);

            //fill out registration with bad inputs
            fillOutReg(browserWindow, new Object[]{"testUser" + new Random().nextInt(100),"aB!", "aB3!a!", true, "answer"});

            //test common regresssion
            TestFunctions.commonRegression(browserWindow, TestFunctions.website+"register", false);

            //test password complexity
            passwordComplexityExpected = new Object[]{"",pass,pass,fail,pass,fail};
            PasswordComplexity.testPassAdvice(browserWindow,passwordComplexityExpected,xPathLoc);


            //Remove all values from input fields
            Keys OSspecific = TestFunctions.OS.contains("win") ? Keys.CONTROL : Keys.COMMAND;
            for(int remove = 0; remove <= 4; remove++)
            {
                if(remove != 3)
                {
                    fields[remove].sendKeys(OSspecific + "a");
                    fields[remove].sendKeys(OSspecific + "x");
                }
            }

            //fill out registration with valid inputs
            fillOutReg(browserWindow, new Object[]{"testUser" + new Random().nextInt(100) +"@gmail.com","aB3!aB3!", "aB3!aB3!", true, "answer"});
            TestFunctions.commonRegression(browserWindow, TestFunctions.website+"register", false);

            //test password complexity
            passwordComplexityExpected = new Object[]{"",pass,pass,pass,pass,pass};
            PasswordComplexity.testPassAdvice(browserWindow,passwordComplexityExpected,xPathLoc);//check out password advice

            //check password is hidden
            WebElement passwordField = browserWindow.findElement(By.cssSelector("#passwordControl"));
            assertEquals(passwordField.getAttribute("type"), "password");
            WebElement repPasswordField = browserWindow.findElement(By.cssSelector("#repeatPasswordControl"));
            assertEquals(repPasswordField.getAttribute("type"), "password");

        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }

    /**
     * Method used to fill out registration form with passed values. Universal for all tests within registration.java
     * Programmer: Kyle Sullivan
     * @param browserWindow  Browser window used for this test
     * @param dataSet an object containing:
     *  Email string used for test
     *  Password string used for test
     *  Repeat password string used for test
     *  True/false to whether to use a security question
     *  String for security question.
     * @exception InterruptedException Thrown if the test is interrupted during a wait period
     */
    public void fillOutReg(WebDriver browserWindow, Object[] dataSet) throws InterruptedException
    {
        boolean notFound = true;//Whether the correct value for a security question has been found
        int optionTry = 0;//Current list element of the security question options being tried for the security question
        int optionTryLimit = 50;//Maximum amount of attempts

        //Ensure the test is on the registration page
        assertEquals(browserWindow.getCurrentUrl(), TestFunctions.website + "register");

        browserWindow.findElement(By.cssSelector("#emailControl")).sendKeys((String)dataSet[0]); //Enter email
        browserWindow.findElement(By.cssSelector("#passwordControl")).sendKeys((String)dataSet[1]); //Enter password
        browserWindow.findElement(By.cssSelector("#repeatPasswordControl")).sendKeys((String)dataSet[2]); //Re-enter password

        //If a security question is being selected for this test
        if ((boolean) dataSet[3])
        {
            browserWindow.findElement(By.cssSelector(".mat-select-trigger")).click(); //Select security question
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
                    //Try an potential option
                    browserWindow.findElement(By.cssSelector("#mat-option-" + optionTry)).click();
                    notFound = false;
                }
                catch (NoSuchElementException exception)
                {
                    //If the option was invalid, prepare to try the next one
                    notFound = true;
                    optionTry++;
                }
            }
        }
        //give security question answer
        browserWindow.findElement(By.cssSelector("#securityAnswerControl")).sendKeys((String)dataSet[4]); //Enter answer
    }


    /**
     * Method for changing the name of tests performed multiple times by adding the first value in their data provider to the end of their names
     * Taken from: https://www.swtestacademy.com/change-test-name-testng-dataprovider/
     * Programmer: Canberk Akduygu
     * @param method Test method whose name is to be changed
     * @param testData The data parameters for the method
     */
    @BeforeMethod(onlyForGroups = {"hasDataProvider"})
    public void BeforeMethod(Method method, Object[] testData)
    {
        //Set name to (method name)_(first value in data provider)
        testName.set(method.getName() + "_" + testData[0]);
    }
    @BeforeMethod(onlyForGroups = {"noDataProvider"})
    public void BeforeMethod(Method method)
    {
        //Set name to (method name)
        testName.set(method.getName());
    }
    /**
     * Returns the name of the test. Used to alter the name of tests performed multiple times
     * Taken from: https://www.swtestacademy.com/change-test-name-testng-dataprovider/
     * Programmer: Canberk Akduygu
     * @return Name of test
     */
    @Override
    public String getTestName()
    {
        return testName.get();
    }
}
