package FunctionalTests;
import org.testng.annotations.*;
import java.io.IOException;

public class Test_Data
{



    /** This dataProvider passes valid email, password, and question strings for the RF1 valid input test
     *  Programmer: Kyle Sullivan
     * @return object with emails, passwords, security question answers, and the browser for that test.
     */
    @DataProvider(
            name = "RF1_Input",
            parallel = true
    )
    public static Object[][] RF1_Input()
    {
        String email = "";
        String password = "";
        String question = "";

        return new Object[][]{
                {email,password,question,"Firefox"},
                {email,password,question,"Chrome"},
                {email,password,question,"Edge"},
        };
    }
}
