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
    String path = "C:\\Users\\Owner\\Documents\\geckodriver-v0.29.1-win64\\geckodriver.exe"; //Set to Driver file path
    String driverType;
    WebDriver browser;
    //Chosen Browser. Change name to correct browser.
    String chosenBrowser = "Firefox";//'Firefox' or 'Chrome'


    /**
     * Description
     * @Param
     * @See
    **/
    //@BeforeSuite
    @BeforeMethod(
            groups = "Set Up: Browser",
            onlyForGroups = {"Set Up: Login"},
            description = "")
    public void setUp() throws IOException
    {
        System.out.println("opened browser and went to site");
        /*
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
        browser.get(website);
        //go to site

         */
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
