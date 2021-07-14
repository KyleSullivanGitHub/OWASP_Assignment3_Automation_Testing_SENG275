package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PasswordComplexity implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console
    String website = "https://juice-shop.herokuapp.com"; //default website URL

    TestBrowser environment;
    CreateEnvironment passBrowser;

    String validPassword = "aB3!aB3!";
    String pass = "primary";
    String fail = "warn";


    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Kyle Sullivan
     */
    @BeforeSuite
    public void SetUp() throws IOException, InterruptedException
    {
        //Create an environment to set up browser specific test environments
        passBrowser = new CreateEnvironment();
        //Create a test environment of the default browser
        environment = passBrowser.createBrowser();
        //Create a constant account to use in tests
        TestFunctions.createAccount();
    }

    /**
     * Smoke test for valid inputs for Password Complexity within several different browsers
     * Programmer: Kyle Sullivan
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","Password Complexity","Password Complexity Smoke","hasDataProvider"},
            priority = 0,
            dataProvider = "PC1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void PCS1_Password_Complexity_Valid(String chosenBrowser) throws IOException, InterruptedException
    {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        //Navigate to the Registration Page
        TestFunctions.navToReg(browserWindow);

        testPassAdviceReg(browserWindow,validPassword,pass,pass,pass,pass,pass);
        browserWindow.quit();
    }


    /**
     * Smoke test for invalid input for Password Complexity
     * Programmer: Kyle Sullivan
     */
    @Test(
            groups = {"Smoke","Password Complexity","Password Complexity Smoke"},
            priority = 0,
            enabled = true
    )
    public void PCS2_Password_Complexity_Invalid() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);
        //Navigate to the Registration Page
        TestFunctions.navToReg(browserWindow);

        testPassAdviceReg(browserWindow,"1234567!",fail,fail,pass,pass,pass);
        browserWindow.quit();
    }

    /**
     * Sanity test for all types of invalid input for Password Complexity
     * Programmer: Kyle Sullivan
     *  @param type Type of invalid input, used for naming tests
     *  @param password Password string for test
     *  @param lowercase Whether the password should pass the lowercase test
     *  @param uppercase Whether the password should pass the uppercase test
     *  @param number Whether the password should pass the number test
     *  @param special Whether the password should pass the special character test
     *  @param length Whether the password should pass the length test
     * @throws InterruptedException
     */
    @Test(
            groups = {"Sanity","Password Complexity","Password Complexity Sanity","hasDataProvider"},
            priority = 0,
            dataProvider = "PC_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void PCS3_Password_Complexity_Invalid_Reg(String type, String password, String lowercase, String uppercase, String number, String special, String length) throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        testPassAdviceReg(browserWindow,password,lowercase,uppercase,number,special,length);
        browserWindow.quit();
    }

    /**
     * Sanity test for valid inputs in Forgot Password for Password Complexity
     * Programmer: Kyle Sullivan
     */
    @Test(
            groups = {"Sanity","Password Complexity","Password Complexity Sanity"},
            priority = 0,
            enabled = true
    )
    public void PCS4_Password_Complexity_Valid_FP() throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        testPassAdviceFP(browserWindow,validPassword,pass,pass,pass,pass,pass);
        browserWindow.quit();
    }

    /**
     * Sanity test for Invalid Inputs in Forgot Password for Password Complexity
     * @param type type of invalid input for test, used in naming the test.
     * @param password Password string for test
     * @param lowercase Whether the password should pass the lowercase test
     * @param uppercase Whether the password should pass the uppercase test
     * @param number Whether the password should pass the number test
     * @param special Whether the password should pass the special character test
     * @param length Whether the password should pass the length test
     * @throws InterruptedException
     */
    @Test(
            groups = {"Sanity","Password Complexity","Password Complexity Sanity","hasDataProvider"},
            priority = 0,
            dataProvider = "PC_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void PCS5_Password_Complexity_Invalid_FP(String type, String password, String lowercase, String uppercase, String number, String special, String length) throws InterruptedException
    {
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(website);
        //Delay until site is ready
        TestFunctions.waitForSite(browserWindow);

        testPassAdviceFP(browserWindow,password,lowercase,uppercase,number,special,length);
        browserWindow.quit();
    }

    /**
     * Password complexity test for all Password Complexity class test methods
     * Programmer: Kyle Sullivan
     * @param browserWindow browser used for that test
     * @param password Password string for test
     * @param lowercase Whether the password should pass the lowercase test
     * @param uppercase Whether the password should pass the uppercase test
     * @param number Whether the password should pass the number test
     * @param special Whether the password should pass the special character test
     * @param length Whether the password should pass the length test
     */
    public void testPassAdviceReg(WebDriver browserWindow, String password, String lowercase, String uppercase, String number, String special, String length) throws InterruptedException
    {
        //xPath to common part of icon elements on password advice element
        String xPathLoc = "register";
        //Navigate to the Registration Page
        TestFunctions.navToReg(browserWindow);

        //Activate password complexity advice
        browserWindow.findElement(By.className("mat-slide-toggle-thumb")).click();
        browserWindow.findElement(By.cssSelector("#passwordControl")).sendKeys(password); //enter password

        testPassAdvice(browserWindow,lowercase,uppercase,number,special,length,xPathLoc);

    }
    /**
     * Password complexity test for all Password Complexity class test methods
     * Programmer: Kyle Sullivan
     * @param browserWindow browser used for that test
     * @param password Password string for test
     * @param lowercase Whether the password should pass the lowercase test
     * @param uppercase Whether the password should pass the uppercase test
     * @param number Whether the password should pass the number test
     * @param special Whether the password should pass the special character test
     * @param length Whether the password should pass the length test
     */
    public void testPassAdviceFP(WebDriver browserWindow, String password, String lowercase, String uppercase, String number, String special, String length) throws InterruptedException
    {
        //xPath to common part of icon elements on password advice element
        String xPathLoc = "forgot-password";
        //navigate to Login
        TestFunctions.navToLogin(browserWindow);
        //Navigate to Forgot Password
        browserWindow.findElement(By.className("forgot-pw")).click();

        //Fill out form
        browserWindow.findElement(By.cssSelector("#email")).sendKeys(TestFunctions.constEmail);
        browserWindow.findElement(By.cssSelector("#securityAnswer")).sendKeys(TestFunctions.constAnswer);

        //Activate Password Advice
        browserWindow.findElement(By.className("mat-slide-toggle")).click();
        browserWindow.findElement(By.cssSelector("#newPassword")).sendKeys(password); //enter password


        testPassAdvice(browserWindow,lowercase,uppercase,number,special,length,xPathLoc);
    }

    /**
     * Password complexity test for all Password Complexity class test methods
     * Programmer: Kyle Sullivan
     * @param browserWindow browser used for that test
     * @param lowercase Whether the password should pass the lowercase test
     * @param uppercase Whether the password should pass the uppercase test
     * @param number Whether the password should pass the number test
     * @param special Whether the password should pass the special character test
     * @param length Whether the password should pass the length test
     * @param xPathLoc common xpath to the password advice icons.
     */
    public void testPassAdvice(WebDriver browserWindow, String lowercase, String uppercase, String number, String special, String length,String xPathLoc)
    {
        String iconPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-"+xPathLoc+"/div/mat-card/div[2]/mat-password-strength-info/mat-card/mat-card-content/";

        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[1]/mat-icon")).getAttribute("color"), lowercase);//Check lowercase test
        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[2]/mat-icon")).getAttribute("color"), uppercase);//Check uppercase test
        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[3]/mat-icon")).getAttribute("color"), number);//check number test
        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[4]/mat-icon")).getAttribute("color"), special);//check special character test
        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[5]/mat-icon")).getAttribute("color"), length);//check length test
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
