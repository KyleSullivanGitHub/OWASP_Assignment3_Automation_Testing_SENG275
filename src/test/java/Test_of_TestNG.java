import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;


public class Test_of_TestNG
{
    String website = "https://juice-shop.herokuapp.com";
    //File Path to Chosen Browser Driver
    String path = "C:\\Users\\Owner\\Documents\\geckodriver-v0.29.1-win64\\geckodriver.exe"; //Set to Driver file path
    String driverType;
    WebDriver browser;
    //Chosen Browser. Change name to correct browser.
    String chosenBrowser = "Firefox";//'Firefox' or 'Chrome'
    boolean browserOpen = false;
    boolean onLogin = false;
    boolean onSignUp = false;

    @BeforeSuite
    @BeforeMethod(onlyForGroups = "start")
    public void createBrowser() throws IOException
    {
        checkWorking("I have made a browser","Error: I have not made a browser");
    }


    @AfterMethod(onlyForGroups = "End")
    public void closeBrowser()
    {
        checkWorking("I have closed the browser","Error: I have not closed the browser");
    }



    @BeforeMethod(onlyForGroups = "Page: Sign Up", groups = "start")
    @Test
    public void goToLogin()
    {

        checkWorking("I have gone to login","Error: I have not gone to login");

    }

    @BeforeMethod(onlyForGroups = "Page: Sign Up",dependsOnMethods = "goToLogin")
    @Test(
            dependsOnMethods = "goToLogin"
    )
    public void goToSignUp()
    {
        checkWorking("I have gone to signup", "Error: I have not gone to signup");
    }

    @Test(
            //groups = {"End"},
            dependsOnMethods = "goToSignUp",
            dataProvider = "signUpDetails",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 1
    )
    public void signUpValid(String email, String password, boolean expectedResults)
    {
        System.out.println(email);
        System.out.println(password);
        System.out.println(expectedResults);
        checkWorking("I have created an account","Error: I could not make an account");
    }

    /*
    @Test(
            groups = {"Page: Sign Up","End"},
            dependsOnMethods = "goToSignUp",
            priority = 2
    )
    public void signUpInvalid()
    {
        checkWorking("I have not created an account","Error: I could not invalidly sign up");
    }
    */
    public void checkWorking(String validMessage, String invalidMessage)
    {
        boolean working = false;
        if(!browserOpen && validMessage.equals("I have made a browser"))
        {
            System.out.println(validMessage);
            browserOpen = true;
            working = true;
        }
        if(browserOpen)
        {
            if (validMessage.equals("I have closed the browser"))
            {
                System.out.println(validMessage);
                browserOpen = false;
                onLogin = false;
                onSignUp = false;
                working = true;
            }
            if (!onLogin && validMessage.equals("I have gone to login"))
            {
                System.out.println(validMessage);
                onLogin = true;
                working = true;
            }
            if (!onSignUp && validMessage.equals("I have gone to signup"))
            {
                System.out.println(validMessage);
                onLogin = false;
                onSignUp = true;
                working = true;
            }
            if(onSignUp)
            {
                if (validMessage.equals("I have created an account"))
                {
                    System.out.println(validMessage);
                    working = true;
                }
                if (validMessage.equals("I have not created an account"))
                {
                    System.out.println(validMessage);
                    working = true;
                }
            }
        }
        if(!working)
            System.out.println(invalidMessage);
        Assert.assertTrue(working);
    }
}
