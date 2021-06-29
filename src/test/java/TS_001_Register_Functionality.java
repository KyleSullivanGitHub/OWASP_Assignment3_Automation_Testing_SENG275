import org.testng.annotations.*;

@Test
public class TS_001_Register_Functionality
{
    String word;
    public TS_001_Register_Functionality(String Message)
    {
        this.word = "other";
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
            groups = {"Sign Up","Set Up: Sign Up"},
            priority = 0,
            enabled = true)
    public void Sign_Up_Page_Access()
    {
        System.out.println("accessed Sign Up Page");
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
            groups = {"Sign Up","Start: Sign Up","Close Browser"},
            priority = 1,
            enabled = true)
    public void Sign_Up_Check_UI()
    {
        System.out.println("Check UI");

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
            groups = {"Sign Up","Start: Sign Up"},
            priority = 2,
            enabled = true)
    public void Sign_Up_Functionality_Valid()
    {
        System.out.println("Created Account");

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
            groups = {"Sign Up","Close Browser"},
            priority = 2,
            enabled = true)
    public void Confirm_Email_Validation()
    {
        System.out.println("checked Email");

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
            groups = {"Sign Up"},
            priority = 3,
            dataProvider = "Invalid_Sign_Up",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 10,
            enabled = true)
    public void Functionality_Test_Invalid(int test)
    {
        System.out.println(test);
        System.out.println("bad signup");

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
            groups = {"Sign Up","Close Browser"},
            priority = 4,
            dataProvider = "Sign_Up_Passwords",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 5,
            enabled = true)
    public void Password_Standards_Test(int test)
    {
        System.out.println(test);

        System.out.println("bad password");

    }

    /**
     * Method Name:
     * Programmer:
     * Purpose:
     * Used in Tests:
     * Depended on by
     */
    @Test(
            description = "Test Cases: TC_RF_021",
            groups = {"Sign Up"},
            priority = 5,
            //dataProvider = "",
            //dataProviderClass = Test_Data.class,
            enabled = true)
    public void Sign_Up_Functionality_All_Browsers()
    {
        System.out.println("checked using other browser");

    }

}
