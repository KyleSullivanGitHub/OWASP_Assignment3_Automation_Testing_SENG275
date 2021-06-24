import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Test_of_TestNG
{
    @BeforeMethod
    public void createBrowser()
    {
        System.out.println("I have made a browser");
    }

    @AfterMethod
    public void closeBrowser()
    {
        System.out.println("I have closed the browser");
    }

    @Test
    public void goToLogin()
    {
        System.out.println("I have gone to login");
    }
    @Test
    public void goToSignUp()
    {
        System.out.println("I have gone to signup");
    }
    @Test
    public void signUpValid()
    {
        System.out.println("I have created an account");
    }
    @Test
    public void signUpInvalid()
    {
        System.out.println("I have not created an account");
    }
}
