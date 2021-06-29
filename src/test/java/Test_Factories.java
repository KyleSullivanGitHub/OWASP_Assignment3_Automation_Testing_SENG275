import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class Test_Factories
{
    @Test
    @Factory(
            dataProviderClass = Test_Data.class,
            dataProvider = "Environment_Setup"
    )
    public Object[] Test_Factories(TS_001_Register_Functionality n)
    {
        System.out.println("running tests");
        Object[] temp =  {n};
        return temp;
    }
}
