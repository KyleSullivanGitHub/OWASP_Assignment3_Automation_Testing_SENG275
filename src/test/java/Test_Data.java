import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.io.IOException;

/**
 * A class providing data values for all tests in the automated system.
 */
public class Test_Data
{
    static String FireFoxDriver = "webdriver.gecko.driver";
    static String FireFoxLoc = "geckodriver-v0.29.1-win64\\geckodriver.exe";

    /**
     *
     * @return
     */
    @DataProvider(
            name = "Environment_Setup_TS_001"
    )
    public static Object[][] Environment_Setup_TS_001() throws IOException
    {
        return new Object[][]{
                //{new ChromeDriver(), "webdriver.gecko.driver", driverPath+"geckodriver-v0.29.1-win64\\geckodriver.exe"},
                new Object[] {
                        new TS_001_Register_Functionality("Firefox",FireFoxDriver,FireFoxLoc)},
        };
    }


    /**
     *
     * @return
     */
    @DataProvider(
            name = "Invalid_Sign_Up",
            parallel = true
    )
    public static Object[][] Invalid_Sign_Up()
    {
        return new Object[][]{
                {1},
                {2},
                {3},
                {4},
                {5},
                {6},
                {7},
                {8},
                {9},
                {10},
        };
    }

    @DataProvider(
            name = "Sign_Up_Passwords",
            parallel = true
    )
    public static Object[][] Sign_Up_Passwords()
    {
        return new Object[][]{
                {1},
                {2},
                {3},
                {4},
                {5},
        };
    }

    /**
     *
     * @return
     */
    @DataProvider(
            name = "signUpDetails",
            parallel = true
    )
    public static Object[][] signUpDetails()
    {
        return new Object[][]{
                {"email 1"," password 1",true},
                {"email 2"," password 2",true},
        };
    }
}
