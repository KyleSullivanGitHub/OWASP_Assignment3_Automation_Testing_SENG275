import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class Test_Factories
{
    /**
     * This factory will
     *
     * @param testCase a set of tests from the registration test cases.
     * @return an object containing the result of the tests
     */
    @Test(groups = "Sign Up")
    @Factory(
            dataProviderClass = Test_Data.class,
            dataProvider = "Environment_Setup_TS_001"
    )
    public Object[] TS_001_Factory(TS_001_Register_Functionality testCase)
    {
        Object[] results =  new Object[1];
        results[0] = testCase;
        return results;
    }
}
