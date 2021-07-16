package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;

public class NavigationMenu
{
    private ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

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
}
