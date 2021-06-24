import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;

@Test(
        groups = "Sign Up",
        description = ""
        )
public class Sign_Up extends Login
{
    private int currentTest;
    public Sign_Up(int number)
    {
        currentTest = number;
    }
    /**
     *
     */
    @Test(
            groups = {"Integration", "Regression"},
            dataProviderClass = Test_Data.class,
            dataProvider = "signUpDetails",
            description = "",
            priority = 0
    )
    public void testSignUp()
    {
        login
    }
    @Test(
            groups = {"Close Browser"},
            dataProviderClass = Test_Data.class,
            dataProvider = "signUpDetails",
            description = "",
            priority = 0
    )
    private void endTest(){}

}
