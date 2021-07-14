package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

        testPassAdvice(browserWindow,validPassword,pass,pass,pass,pass,pass);
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
    public void testPassAdvice(WebDriver browserWindow, String password, String lowercase, String uppercase, String number, String special, String length)
    {
        //xPath to common part of icon elements on password advice element
        String iconPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-register/div/mat-card/div[2]/mat-password-strength-info/mat-card/mat-card-content/";

        //Activate password complexity advice
        browserWindow.findElement(By.className ("mat-slide-toggle-thumb")).click();
        browserWindow.findElement(By.cssSelector("#passwordControl")).sendKeys(password); //enter password

        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[1]/mat-icon")).getAttribute("color"), lowercase);//Check lowercase test
        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[2]/mat-icon")).getAttribute("color"), uppercase);//Check uppercase test
        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[3]/mat-icon")).getAttribute("color"), number);//check number test
        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[4]/mat-icon")).getAttribute("color"), special);//check special character test
        assertEquals(browserWindow.findElement(By.xpath(iconPath+"div[5]/mat-icon")).getAttribute("color"), length);//check length test
    }

    /**
     * Smoke test for Login Security within several different browsers
     * Programmer: Kyle Sullivan
     */
    @Test(
            groups = {"Smoke","Password Complexity","Password Complexity Smoke"},
            priority = 0,
            dataProvider = "AS1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = false
    )
    public void PCS2_Password_Complexity_Invalid()
    {

    }

    /**
     * Smoke test for Login Security within several different browsers
     * Programmer: Kyle Sullivan
     */
    @Test(
            groups = {"Sanity","Password Complexity","Password Complexity Sanity","hasDataProvider"},
            priority = 0,
            dataProvider = "AS1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = false
    )
    public void PCS3_Password_Complexity_Invalid_Reg()
    {

    }

    /**
     * Smoke test for Login Security within several different browsers
     * Programmer: Kyle Sullivan
     */
    @Test(
            groups = {"Sanity","Password Complexity","Password Complexity Sanity","hasDataProvider"},
            priority = 0,
            dataProvider = "AS1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = false
    )
    public void PCS4_Password_Complexity_Valid_FP()
    {

    }
    /**
     * Smoke test for Login Security within several different browsers
     * Programmer: Kyle Sullivan
     */
    @Test(
            groups = {"Sanity","Password Complexity","Password Complexity Sanity","hasDataProvider"},
            priority = 0,
            dataProvider = "AS1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = false
    )
    public void PCS5_Password_Complexity_Invalid_FP()
    {

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
