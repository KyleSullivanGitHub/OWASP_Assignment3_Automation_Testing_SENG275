import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TS_002_Login_Functionality
{
    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
    @BeforeMethod(
            onlyForGroups = {"Set Up: Sign Up"},
            groups = {"Set Up: Login", "Sign Up"},
            alwaysRun = true)
    @Test(
            description = "Test Cases: TC_LF_001",
            groups = {"Login","Set Up: Login"},
            dependsOnGroups = {"Set Up: Browser"},
            priority = 0,
            enabled = true
    )
    public void Login_Page_Access()
    {
        System.out.println("accessed login Page");
    }


    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
    /*
    @Test(
            description = "Test Cases: TC_LF_002",
            groups = {"Sign Up"},
            dependsOnMethods = {"Login_Page_Access"},
            enabled = true
    )
    public void Login_Functionality_Valid()
    {

    }
    */
}
