import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.io.IOException;


public class Setup
{
    //Main Website URL
    String website = "https://juice-shop.herokuapp.com";
    //File Path to Chosen Browser Driver
    String path = ""; //Set to Driver file path
    String driverType;
    WebDriver browser;
    //Chosen Browser. Change name to correct browser.
    String chosenBrowser = "Firefox";//'Firefox' or 'Chrome'


    /**
     * Description
     * @Param
     * @See
     */
    @BeforeSuite
    @BeforeMethod(
            onlyForGroups = {"Start Test"},
            description = "")
    public void setUp() throws IOException
    {
        switch(chosenBrowser)
        {
            case "Firefox":
                browser = new FirefoxDriver();
                driverType = "webdriver.gecko.driver";
                break;
            case "Chrome":
                browser = new ChromeDriver();
                driverType = "webdriver.chrome.driver";
                break;
            default:
                throw new IOException("No Chosen Browser");
        }
        System.setProperty(driverType,path);
        browser.manage().window().maximize();
        //go to site
    }

    @AfterMethod(
            onlyForGroups = {"Close Browser"})
    public void cleanUp(){browser.quit();}
}
