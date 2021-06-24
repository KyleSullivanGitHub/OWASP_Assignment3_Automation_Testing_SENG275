import org.testng.annotations.*;

/**
 * A class providing data values for all tests in the automated system.
 */
public class Test_Data
{
    /**
     *
     * @return
     */
    @DataProvider(name = "signUpDetails")
    public static Object[][] signUpDetails()
    {
        return new Object[][]
        {
                {"valid data"},
                {}
        };
    }
}
