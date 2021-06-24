import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;


public class Setup
{
    //Main Website URL
    String website = "https://juice-shop.herokuapp.com";
    //File Path to Chosen Browser Driver
    String path = "";
    String driverType;
    WebDriver browser;
    //Chosen Browser. Change name to correct browser.
    String chosenBrowser = "Firefox";//'Firefox' or 'Chrome'

    @BeforeMethod(alwaysRun = true)
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
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp(){browser.quit();}


    }
