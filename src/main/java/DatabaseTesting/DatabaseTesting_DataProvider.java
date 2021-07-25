package DatabaseTesting;

import Setup.CreateEnvironment;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Random;

public class DatabaseTesting_DataProvider {

    @DataProvider(
            name = "updateName"
    )
    public static Object[][] updateName()
    {

        return new Object[][]{
                {"David",5},
                {"Simon",4},
                {"Ahmed",1}
        };
    }
    @DataProvider(
            name = "extraction"
    )
    public static Object[][] extraction()
    {

        return new Object[][]{
                {"Employee"},
                {"Client"},
                {"Product"}
        };
    }
}
