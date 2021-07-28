package DatabaseTesting;

import Setup.CreateEnvironment;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Random;



/**
 * Database data provider class for OWASP Juice Shop Database testing
 * Programmer: Seyedmehrad Adimi
 */

public class DatabaseTesting_DataProvider {

    @DataProvider(
            name = "updateName"
    )
    public static Object[][] updateName()
    {

        return new Object[][]{
                {"Client","David Collins",5},
                {"Client","Simon Travis",4},
                {"Client","Ahmed Salman",1},
                {"Employee","Darwin Darkoff",101},
                {"Employee", "Mike Oxlong", 102}
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
                {"Product"},
                {"Supplier"}
        };
    }

    @DataProvider(
            name = "addClient"
    )
    public static Object[][] addClient()
    {
        return new Object[][]{
                {44,"James Frizzors","JamieFriz", 0, 3},
                {42,"Ben Drover","Bendvr", 0, 4},
                {45,"Salam Hello","salamH", 0, 1},
                {51,"No Name","NoNameMe",0, 3}
        };
    }

    @DataProvider(
            name = "RemoveClient"
    )
    public static Object[][] RemoveClient()
    {
        return new Object[][]{
                {1},{2},{3},{4},{5}
        };
    }

    @DataProvider(
            name = "primaryKeysData"
    )
    public static Object[][] primaryKeysData()
    {
        return new Object[][]{
                {"Employee"},
                {"Client"},
                {"Product"},
                {"Orders"},
                {"Supplier"},
                {"Warehouse"}
        };
    }
}
