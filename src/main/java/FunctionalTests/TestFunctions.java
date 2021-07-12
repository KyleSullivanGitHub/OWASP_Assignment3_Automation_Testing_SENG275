package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestFunctions
{

    private static boolean registerOnce = false;//boolean to see if the constant account has been created for this test session
    static String constEmail;//String containing the email for the constant session
    static String constPassword = "Seng265!";//password for the constant session

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
            //Register teh Account
            signUp.fillOutReg(tempBrowser, constEmail, constPassword, constPassword, true, "seng");
            tempBrowser.findElement(By.cssSelector("#registerButton")).click();//click register button
            //close the browser
            tempBrowser.close();

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

        test.findElement(By.id ("navbarAccount")).click ();
        Thread.sleep(500);

        //verify that we can access the login page
        WebElement accountMenuLogin = test.findElement(By.cssSelector("#navbarLoginButton"));
        assertTrue(accountMenuLogin.isEnabled());
        accountMenuLogin.click();

        Thread.sleep(500);

        test.findElement(By.id("email")).sendKeys(constEmail); //enter email
        test.findElement(By.id ("password")).sendKeys(password); //enter password
    }

}
