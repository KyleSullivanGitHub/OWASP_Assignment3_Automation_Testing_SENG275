package Setup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
/**
 * TestBrowser Class
 * Programmer: Kyle Sullivan
 * Contains basic functions to allow a test to be called from any of three specific browser.
 */
public abstract class TestBrowser
{

    //driver path to folder containing all drivers for web testing. Currently only Windows compatible
    //TODO add other OS Functionality
  /*  protected String driverPath = "src\\main\\java\\Setup\\WebDriver\\";
    //Location of firefox driver within your WebDriver folder.
    protected String fireFoxLoc = "geckodriver-v0.29.1-win64\\geckodriver.exe";
    //Location of chrome driver within your WebDriver folder.
    protected String chromeLoc = "chromedriver_win32\\chromedriver.exe";
    //Location of Edge driver within your webdriver folder.
    protected String edgeLoc = "edgedriver_win64\\msedgedriver.exe";*/

    //driver path to folder contianing all drivers for web testing. MAC
    //TODO add other OS Functionality

    protected String driverPath = "src/main/java/Setup/WebDriverMac/";
    //Location of firefox driver within your WebDriver folder.
    protected String fireFoxLoc = "geckodriver 2";
    //Location of chrome driver within your WebDriver folder.
    protected String chromeLoc = "chromedriver";
    //Location of Edge driver within your webdriver folder.
    protected String edgeLoc = "msedgedriver";

    //TODO Add Safari

    //preferred driver to run all tests on. Change to whatever suits your fancy
    static String primaryBrowser = "Firefox";
//********************************************************************************************************************//
    //String containing driver text
    String driver;
    //String containing driver location
    String driverLoc;


    /**
     * Used by CreateEnvironment to default to a specific browser for the majority of tests.
     * Programmer: Kyle Sullivan
     * @return driver A string containing the default browser for tests
     */
    public static String getPrimaryBrowser() { return primaryBrowser; }

    /**
     * Used by CreateEnvironment to create the test Environment.
     * Programmer: Kyle Sullivan
     * @return driver A string containing the initialization text to create the environment
     */
    public String getDriver() { return driver; }
    /**
     * Used by CreateEnvironment to create the test Environment.
     * Programmer: Kyle Sullivan
     * @return driverLoc A string containing the location text of the driver to create the environment
     */
    public String getDriverLoc() {return driverLoc;}


    /**
     * Abstract class for returning a usable web environment.
     * Programmer: Kyle Sullivan
     */
    public abstract WebDriver makeDriver();
}

class useFireFox extends TestBrowser
{
    /**
     * Sets the driver type and path for a firefox browser
     * Programmer: Kyle Sullivan
     */
    public useFireFox()
    {
        this.driver = "webdriver.gecko.driver";
        this.driverLoc = this.driverPath +fireFoxLoc;
    }

    /**
     * Creates and returns a new  Firefox browser window.
     * Programmer: Kyle Sullivan
     * @return FirefoxDriver() - The initialized browser for the given test
     */
    public WebDriver makeDriver()
    {
        return new FirefoxDriver();
    }
}

class useChrome extends TestBrowser
{
    /**
     * Sets the driver type and path for a chrome browser
     * Programmer: Kyle Sullivan
     */
    public useChrome()
    {
        this.driver = "webdriver.chrome.driver";
        this.driverLoc = this.driverPath +chromeLoc;
    }

    /**
     * Creates and returns a new Google Chrome browser window.
     * Programmer: Kyle Sullivan
     * @return ChromeDriver() - The initialized browser for the given test
     */
    public WebDriver makeDriver()
    {
        return new ChromeDriver();
    }
}

class useEdge extends TestBrowser
{
    /**
     * Sets the driver type and path for an edge browser
     * Programmer: Kyle Sullivan
     */
    public useEdge()
    {
        this.driver = "webdriver.edge.driver";
        this.driverLoc = this.driverPath +edgeLoc;
    }

    /**
     * Creates and returns a new  Edge browser window.
     * Programmer: Kyle Sullivan
     * @return EdgeDriver() - The initialized browser for the given test
     */
    public WebDriver makeDriver()
    {
        return new EdgeDriver();
    }
}

/**
class useSafari extends TestBrowser
{
    /**
     * Sets the driver type and path for a Safari browser
     * Programmer: Seyedmehrad Adimi
     * /
    public useSafari() throws IOException
    {
        this.driver = "webdriver.safari.driver";
        this.driverLoc = this.driverPath+ safariLoc;
    }

    /**
     * Creates and returns a new Safari browser window.
     * Programmer: Seyedmehrad Adimi
     * @return SafariDriver() - The initialized browser for the given test
     * /
    public WebDriver makeDriver()
    {
        return new SafariDriver ();
    }
}
*/

