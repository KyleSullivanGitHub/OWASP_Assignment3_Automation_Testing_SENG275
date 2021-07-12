package FunctionalTests;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.Random;

public class Test_Data
{

    private static int randomNum1 = emailRandomizer();
    private static int randomNum2 = emailRandomizer();
    private static int randomNum3 = emailRandomizer();
    private static int randomNum4 = emailRandomizer();


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
                {"Firefox", email+randomNum1+"@gmail.com",password,question},
                {"Chrome", email+randomNum2+"@gmail.com",password,question},
                {"Edge", email+randomNum3+"@gmail.com",password,question}, //Edge is causing issues, always needs to be in focus for the test to actually pass. need to fix
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
                {"Invalid Password (Too Short)", goodEmail,"ad1!","ad1!",true,goodAnswer},
                {"Invalid Repeat Password (incorrect repeat password)", goodEmail,goodPassword,"BadPassword",true,goodAnswer},
                {"Invalid Email (Existing Email)", "helloworld"+randomNum1+"@gmail.com",goodPassword,goodPassword,true,goodAnswer},
                {"Invalid Email (bad email)", "eda@e",goodPassword,goodPassword,true,goodAnswer},
                {"Invalid Repeat Password (no repeat password)", goodEmail,goodPassword,"",true,goodAnswer},
        };
    }


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
                //{"Firefox", email+randomNum1+"@gmail.com",password,question},
                {"Chrome", email+randomNum2+"@gmail.com",password,question},
               // {"Edge", email+randomNum3+"@gmail.com",password,question}, //Edge is causing issues, always needs to be in focus for the test to actually pass. need to fix
               // {"Safari", email+randomNum4+"@gmail.com",password,question}
        };
    }

    /**
     * This dataProvider passes valid email and password strings for the LG3 valid input test (Google Account)
     * Programmer: Seyedmehrad Adimi
     * @return object with email and password for that test.
     */
    @DataProvider(
            name = "LG3_Input",
            parallel = true
    )
    public static Object[][] LG3_Input()
    {

        String email = "helloworld.owasp" ;
        String password = "seng275@";

        return new Object[][]{
                //{"Firefox", email+"@gmail.com",password},
                {"Chrome", email+"@gmail.com",password},
                // {"Edge", email+"@gmail.com",password}, //Edge is causing issues, always needs to be in focus for the test to actually pass. need to fix
                // {"Safari", email+"@gmail.com",password}
        };
    }

    /**
     * This dataProvider passes valid email and valid password strings for the LG5 Invalid input test.
     * The data will be modified in the test class since we have 4 combinations (valid/invalid passwords and emails)
     * for this test. For now, this data provider only provides the valid ones
     * Programmer: Seyedmehrad Adimi
     * @return object with emails and passwords for that test.
     */
    @DataProvider(
            name = "LG5_Input",
            parallel = true
    )
    public static Object[][] LG5_Input()
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
                //{"Firefox", email+randomNum1+"@gmail.com",password,question},
                {"Chrome", email+randomNum2+"@gmail.com",password,question},
                // {"Edge", email+randomNum3+"@gmail.com",password,question}, //Edge is causing issues, always needs to be in focus for the test to actually pass. need to fix
                // {"Safari", email+randomNum4+"@gmail.com",password,question}
        };
    }






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
