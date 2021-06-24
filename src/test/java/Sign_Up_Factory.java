import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class Sign_Up_Factory
{
    int numOfTests;

    @BeforeGroups("Regression")
    public void regressionTest(){numOfTests = 0;}
    @BeforeGroups("Integration")
    public void integrationTest(){numOfTests = 0;}
    @BeforeGroups("Sign Up")
    public void signUpTest(){numOfTests = 5;}

    @Test
    @Factory
    public Object[] createSignUpInstances()
    {
        Object[] result = new Object[numOfTests];
        for(int i = 0; i < numOfTests; i++)
        {
            result[i] = new Sign_Up(i);
        }
        return result;
    }
}
