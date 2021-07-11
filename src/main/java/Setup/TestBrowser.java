package Setup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
/**
 * TestBrowser Class
 * Programmer: Kyle Sullivan
 * Contains basic functions to allow a test to be called from any of three specific browser.
 */
public abstract class TestBrowser
{
    //driver path to your folder contianing all drivers for web testing. Change to match your setup
    protected String driverPath = "C:\\Users\\Owner\\Documents\\WebDriver\\";
    //Location of firefox driver within your WebDriver folder.
    protected String fireFoxLoc = "geckodriver-v0.29.1-win64\\geckodriver.exe";
    //Location of chrome driver within your WebDriver folder.
    protected String chromeLoc = "chromedriver_win32\\chromedriver.exe";
    //Location of Edge driver within your webdriver folder.
    protected String edgeLoc = "edgedriver_win64\\msedgedriver.exe";

    //preferred driver to run all tests on. Change to whatever suits your fancy
    static String primaryBrowser = "Chrome";

    //String containing driver text
    String driver;
    //String containg driver location
    String driverLoc;

    public TestBrowser() throws IOException {}

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
    public useFireFox() throws IOException
    {
        this.driver = "webdriver.gecko.driver";
        this.driverLoc = this.driverPath + fireFoxLoc;
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
    public useChrome() throws IOException
    {
        this.driver = "webdriver.chrome.driver";
        this.driverLoc = this.driverPath + chromeLoc;
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
    public useEdge() throws IOException
    {
        this.driver = "webdriver.edge.driver";
        this.driverLoc = this.driverPath + edgeLoc;
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

