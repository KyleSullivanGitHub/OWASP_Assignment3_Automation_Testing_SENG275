package FunctionalTests;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.Random;

public class Test_Data
{
//TODO maybe switch from multiple parameters to a builder pattern or an object?

    private static int randomNum1 = emailRandomizer();
    private static int randomNum2 = emailRandomizer();
    private static int randomNum3 = emailRandomizer();
    private static int randomNum4 = emailRandomizer();

    /**
     * This dataProvider passes the desired browsers to any test Method which only needs to know what browsers they have to test with
     * Programmer: Kyle Sullivan
     * @return object with Strings corresponding to valid web browsers.
     */
    @DataProvider(
            name = "browserSwitch",
            parallel = true
    )
    public static Object[][] browserSwitch()
    {

        return new Object[][]{
                {"Firefox"},
                //{"Chrome"},
                //{"Edge"}, //Edge is causing issues, always needs to be in focus for the test to actually pass. need to fix
                //{"Safari", email+randomNum4+"@gmail.com",password,question}
        };
    }

    //*******************************************************************************************************

    /**
     * This dataProvider passes valid random email, password, and question strings for the RF1 valid input test
     * Programmer: Kyle Sullivan
     * @return object with emails, passwords, security question answers, and the browser for that test.
     */
    @DataProvider(
            name = "RF1_Input",
            parallel = true
    )
    public static Object[][] RF1_Input()
    {
        while(randomNum1 == randomNum2 || randomNum1 == randomNum3 || randomNum2 == randomNum3)
        {
            randomNum1 = emailRandomizer();
            randomNum2 = emailRandomizer();
            randomNum3 = emailRandomizer();
            randomNum4 = emailRandomizer();
        }
        String email = "helloworld" ;
        String password = "Seng310@#$";
        String question = "seng";

        return new Object[][]{
                {"Firefox", new Object[]{email+randomNum1+"@gmail.com",password, password, true, question}},
                {"Chrome", new Object[]{email+randomNum2+"@gmail.com",password, password, true, question}},
                {"Edge", new Object[]{email+randomNum3+"@gmail.com",password, password, true, question}}, //Edge is causing issues, always needs to be in focus for the test to actually pass. need to fix
                //{"Safari", email+randomNum4+"@gmail.com",password,question}
        };
    }

    /**
     * This dataProvider passes various invalid registration details to RF4_Invalid_Input
     * Programmer: Kyle Sullivan
     * @return object with emails, passwords, security question answers, and the browser for that test.
     */
    @DataProvider(
            name = "RF4_Input",
            parallel = true
    )
    public static Object[][] RF4_Input()
    {
        String goodEmail = "helloWorld2000@gmail.com";
        String goodPassword = "Seng310@#$";
        String goodAnswer = "seng";

        return new Object[][]{
                {"No inputs", "","","",false,""},
                {"Invalid Password (Too Short)",new Object[]{goodEmail,"ad1!","ad1!",true,goodAnswer}},
                {"Invalid Repeat Password (incorrect repeat password)", new Object[]{goodEmail,goodPassword,"BadPassword",true,goodAnswer}},
                {"Invalid Email (Existing Email)", new Object[]{"helloworld"+randomNum1+"@gmail.com",goodPassword,goodPassword,true,goodAnswer}},
                {"Invalid Email (bad email)", new Object[]{"eda@e",goodPassword,goodPassword,true,goodAnswer}},
                {"Invalid Repeat Password (no repeat password)", new Object[]{goodEmail,goodPassword,"",true,goodAnswer}},
        };
    }

    //*******************************************************************************************************


    /**
     * This dataProvider passes valid random email and password strings for the LG1 valid input test
     * Programmer: Seyedmehrad Adimi
     * @return object with email and password for that test.
     */
    @DataProvider(
            name = "LG1_Input",
            parallel = true
    )
    public static Object[][] LG1_Input()
    {
        while(randomNum1 == randomNum2 || randomNum1 == randomNum3 || randomNum2 == randomNum3)
        {
            randomNum1 = emailRandomizer();
            randomNum2 = emailRandomizer();
            randomNum3 = emailRandomizer();
            randomNum4 = emailRandomizer();
        }
        String email = "helloworld" ;
        String password = "Seng310@#$";
        String question = "seng";

        return new Object[][]{
                {"Firefox", email+randomNum1+"@gmail.com",password,question},
                {"Chrome", email+randomNum2+"@gmail.com",password,question},
                {"Edge", email+randomNum3+"@gmail.com",password,question}, //Edge is causing issues, always needs to be in focus for the test to actually pass. need to fix
               // {"Safari", email+randomNum4+"@gmail.com",password,question}
        };
    }

    //*******************************************************************************************************

    /**
     * This dataProvider passes the desired browsers to the Account Safety methods.
     * Programmer: Kyle Sullivan
     * @return object with Strings corresponding to valid web browsers.
     */
    @DataProvider(
            name = "AS1_Input",
            parallel = true
    )
    public static Object[][] AS1_Input()
    {

        return new Object[][]{
                {"Firefox"},
                {"Chrome"},
                {"Edge"}, //Edge is causing issues, always needs to be in focus for the test to actually pass. need to fix
                //{"Safari", email+randomNum4+"@gmail.com",password,question}
        };
    }

    //*******************************************************************************************************



    /**
     * This dataProvider passes invalid Passwords to the Password Complexity class.
     * Programmer: Kyle Sullivan
     * @return object with invalid passwords, and true/false values for the password complexity.
     */
    @DataProvider(
            name = "PC_Input",
            parallel = true
    )
    public static Object[][] PC_Input()
    {
        String pass = "primary";
        String fail = "warn";
        return new Object[][]{
                {"No_Characters",new Object[]{"1234567!",fail,fail,pass,pass,pass}},
                {"No_Uppercase_or_Num",new Object[]{"abcdefg!",pass,fail,fail,pass,pass}},
                {"No_Lowercase_or_Num",new Object[]{"ABCDEFG!",fail,pass,fail,pass,pass}},
                {"No_Special",new Object[]{"123abcDEF",pass,pass,pass,fail,pass}},
                {"Too_Short",new Object[]{"1aB!",pass,pass,pass,pass,fail}},
        };
    }
    //*******************************************************************************************************
    @DataProvider(
            name = "RB3_Input",
            parallel = true
    )
    public static Object[][] RB3_Input()
    {
        int validQuantity = 10;
        return new Object[][]{
                {"Empty_Quantity",new Object[]{0,true,false,false,""}},
                {"Below_Min_Quantity",new Object[]{1,true,false,false,""}},
                {"Above_Max_Quantity",new Object[]{1001,true,false,false,""}},
                {"Negative_Quantity",new Object[]{-1,true,false,false,""}},
                {"No_Address",new Object[]{validQuantity,false,false,false,""}},
                {"Bulk_No_Date",new Object[]{200,true,true,false,""}},
                {"Bulk_Past_Date",new Object[]{200,true,true,true,"/html/body/div[3]/div[2]/div/mat-datepicker-content/div[2]/mat-calendar/div/mat-month-view/table/tbody/tr[5]/td[7]"}},
        };
    }

    @DataProvider(
            name = "RB4_Input",
            parallel = true
    )
    public static Object[][] RB4_Input()
    {
        int validQuantity = 200;
        return new Object[][]{
                {"Bulk_No_Pickup",new Object[]{validQuantity,true,false,false,""}},
                {"Bulk_Valid_Date",new Object[]{validQuantity,true,true,true,"/html/body/div[3]/div[2]/div/mat-datepicker-content/div[2]/mat-calendar/div/mat-month-view/table/tbody/tr[3]/td[4]"}},
        };
    }
    //int quantity, boolean doAddress, boolean doPickup, boolean doDate, String date

    //*******************************************************************************************************

    /**
     * This method create a random integer to add to the end of an email to ensure unique emails for each.
     * Programmer: Kyle Sullivan
     * @return random integer.
     */
    private static int emailRandomizer()
    {
        int emailNumRandomizer = 0;
        Random emailRandomizer = new Random();
        for(int i = 0; i < 20; i++)
        {
            emailNumRandomizer += emailRandomizer.nextInt(9);
        }
        return emailNumRandomizer;
    }
}
