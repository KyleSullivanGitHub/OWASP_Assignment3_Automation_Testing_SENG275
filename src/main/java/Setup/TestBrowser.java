package Setup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;

public abstract class TestBrowser
{
    protected String driverPath = "C:\\Users\\Owner\\Documents\\WebDriver\\"; //Overall path of the driver. Change to meet your code.
    protected String fireFoxLoc = "geckodriver-v0.29.1-win64\\geckodriver.exe";
    protected String chromeLoc = "chromedriver_win32\\chromedriver.exe";
    protected String edgeLoc = "";
    static String primaryBrowser = "Firefox";

    WebDriver browser;
    String driver;
    String driverLoc;

    public TestBrowser() throws IOException {}

    public static String getPrimaryBrowser() { return primaryBrowser; }
    public String getDriver() { return driver; }
    public String getDriverLoc() {return driverLoc;}

    public abstract WebDriver makeDriver();
}

class useFireFox extends TestBrowser
{
    /**
     * Sets the driver type and path
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
     * Sets the driver type and path
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
     * Sets the driver type and path
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

