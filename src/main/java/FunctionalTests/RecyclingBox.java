package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;

public class RecyclingBox
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
    public void SetUp() throws IOException, InterruptedException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
        TestFunctions.createAccount();
    }

    public void RB1_Valid_Usage()
    {
        //navigate to recycling box
        //valid usage of recycling box
    }

    public void RB2_Invalid_Usage()
    {
        //navigate
        //test empty quanityty

    }

    public void RB3_Invalid_Usage_Comprehensive()
    {
        //test empty quantity
        //test negative quantity
        //test no address set
        //test no address saved
    }

    public void RB_Regression()
    {

    }
}
