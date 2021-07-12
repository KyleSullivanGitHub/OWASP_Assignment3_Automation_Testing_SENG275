import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Set;


/** Test class for test scenario TS_001_Register Functionality.
 * Class will test all elements of the registration process for OWASP Juice Shop.
 * This class will be run multiple times, once for each browser set up.
 * This class is launched by the TS_001_Factory in Test_Factories.
 */
@Test
public class TS_001_Register_Functionality
{
    WebDriver browser; //Browser for the test
    String driverType; //Driver for the test
    String driverPath = "/Users/seyedmehradadimi/Desktop/Web Develpment/Web Drivers/"; //Overall path of the driver. Change to meet your code.
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    String chosenBrowser; //string stating the browser
    Setup functions = new Setup(); //basic setup class, used to do common functions.

    /** Creates the foundational elements for a group of sign up tests
     * Programmer: Kyle Sullivan
     * @param chosenBrowser sets the class' browser to be used
     * @param driverType Sets the Driver type to be used
     * @param driverLoc Sets the Driver Extension ot be used.
     */
    public TS_001_Register_Functionality(String chosenBrowser, String driverType, String driverLoc)
    {
        this.chosenBrowser = chosenBrowser;
        this.driverType = driverType;
        this.driverPath += driverLoc;
    }

    private void environmentSetup() throws IOException
    {
        this.browser = this.functions.driver(this.chosenBrowser);
        System.setProperty(this.driverType,this.driverPath);
        this.browser.manage().window().maximize();
        this.browser.get(this.website);
    }

    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
   @Test(
            description = "Test Cases: TC_RF_001",
            groups = {"Smoke Testing","Sanity Testing"},
            priority = 0,
            enabled = true)
    public void Sign_Up_Page_Access() throws IOException, InterruptedException
   {

       //start up browser
       environmentSetup();

       //navigate to sign up page from homepage

       //Close Browser
       Thread.sleep(5000);
       this.browser.close();

       //set all other tests to start at the registration page.
       this.website += "/#/register";
    }


    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
    @Test(
            description = "Test Cases: TC_RF_011, TC_RF_012, TC_RF_014, TC_RF_017, TC_RF_020",
            groups = {"Regression Testing", "Sanity Testing"},
            priority = 1,
            enabled = true)
    public void Sign_Up_Check_UI() throws IOException, InterruptedException
    {
        environmentSetup();

        //check UI

        Thread.sleep(5000);
        this.browser.close();
    }

    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
    @Test(
            description = "Test Cases: TC_RF_002, TC_RF_016",
            groups = {"Smoke Testing", "Regression Testing"},
            priority = 2,
            enabled = true)
    public void Sign_Up_Functionality_Valid() throws IOException
    {
        environmentSetup();
        // create account
        //do not close browser
    }

    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
    @Test(
            description = "Test Cases: TC_RF_003",
            groups = {"Regression Testing"},
            dependsOnMethods = "Sign_Up_Functionality_Valid",
            priority = 2,
            enabled = true)
    public void Confirm_Email_Validation() throws InterruptedException
    {
        //check for email validation
        Thread.sleep(5000);
        this.browser.close();
    }

    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
    @Test(
            description = "Test Cases: TC_RF_004, TC_RF_005, TC_RF_006, TC_RF_007, TC_RF_008, TC_RF_009, TC_RF_010, TC_RF_013, TC_RF_018, TC_RF_019",
            groups = {"Regression Testing"},
            priority = 3,
            dataProvider = "Invalid_Sign_Up",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 10,
            enabled = true)
    public void Functionality_Test_Invalid(int test) throws IOException, InterruptedException
    {
        WebDriver multiBrowser = this.functions.driver(this.chosenBrowser);
        System.setProperty(this.driverType,this.driverPath);
        multiBrowser.manage().window().maximize();
        multiBrowser.get(this.website);



        Thread.sleep(5000);
        this.browser.close();
    }

    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
    @Test(
            description = "Test Cases: TC_RF_015",
            groups = {"Regression Testing"},
            priority = 4,
            dataProvider = "Sign_Up_Passwords",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 5,
            enabled = true)
    public void Password_Standards_Test(int test) throws IOException, InterruptedException
    {
        WebDriver multiBrowser = this.functions.driver(this.chosenBrowser);
        System.setProperty(this.driverType,this.driverPath);
        multiBrowser.manage().window().maximize();
        multiBrowser.get(this.website);

        //check password Standards_Test

        Thread.sleep(5000);
        this.browser.close();

    }
}
