import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.io.IOException;

//Often used functions for other test cases
public class Setup
{
    WebDriver browser; //container to create a browser before passing it on
    public Setup() {}


    /**
     * Creates and returns a new browser of a specific driver, based on the data passed to it.
     * Programmer: Kyle Sullivan
     * @param chosenBrowser a string stating the browser for this test
     * @return browser - The initilized browser for the given test
     */
    public WebDriver driver(String chosenBrowser) throws IOException
    {
        switch(chosenBrowser)
        {
            case "Firefox": //Firefox browser
                this.browser = new FirefoxDriver();
                break;
            case "Chrome": //Chrome Browser
                this.browser = new ChromeDriver();
                break;
            default: //Invalid browser set
                throw new IOException("No Chosen Browser");
        }
        return browser;
    }


    /*
    //@BeforeSuite
    @BeforeMethod(
            groups = "Set Up: Browser",
            onlyForGroups = {"Set Up: Login"},
            description = "")
    public void setUp() throws IOException
    {
        System.out.println("opened browser and went to site");

        //go to site
    }

    @AfterMethod(
            onlyForGroups = {"Close Browser"}
    )
    public void cleanUp(){        System.out.println("closed browser");
    }

    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     **/
    /*
    @Test(
            description = "Test Cases: TC_RF_00",
            groups = {""},
            dependsOnMethods = {""},
            priority = 0,
            enabled = true
    )
    public void Page_Access()
    {

    }
    */
}
