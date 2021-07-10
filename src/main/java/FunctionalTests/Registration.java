package FunctionalTests;

import Setup.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.io.IOException;

public class Registration
{
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Kyle Sullivan
     */
    @BeforeSuite
    public void regSetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }

    /**
     * purpose
     * Programmer
     * @param email
     * @param password
     * @param question
     * @param chosenBrowser
     */
    @Test(
            groups = {"Smoke","Registration","Registration Smoke"},
            priority = 0,
            dataProvider = "RF1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void RF1_Valid_Input(String email, String password, String question, String chosenBrowser) throws IOException
    {
        TestBrowser driver = passBrowser.createBrowser(chosenBrowser); //basic setup class, used to do common functions.
        System.setProperty(driver.getDriver(),driver.getDriverLoc());
        WebDriver browserWindow = driver.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(website);

    }

    /**purpose
     *Programmer
     *@Param
     */
    @Test(
            groups = {"",""},
            priority = 1,
            dataProvider = "",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 0,
            enabled = true
    )
    public void RF2_Invalid_Input()
    {

    }

    /**purpose
     *Programmer
     *@Param
     */
    @Test(
            groups = {"",""},
            priority = 2,
            dataProvider = "",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 0,
            enabled = true
    )
    public void RF3_Validation_Email()
    {

    }

    /**purpose
     *Programmer
     *@Param
     */
    @Test(
            groups = {"",""},
            priority = 3,
            dataProvider = "",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 0,
            enabled = true
    )
    public void RF4_Invalid_Input()
    {

    }

    /**purpose
     *Programmer
     *@Param
     */
    @Test(
            groups = {"",""},
            priority = 4,
            dataProvider = "",
            dataProviderClass = Test_Data.class,
            threadPoolSize =0,
            enabled = true
    )
    public void RF_Regression()
    {

    }
}
