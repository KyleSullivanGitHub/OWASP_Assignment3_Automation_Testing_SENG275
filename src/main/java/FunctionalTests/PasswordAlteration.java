package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.browser.Browser;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;

public class PasswordAlteration implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    String website = "https://juice-shop.herokuapp.com"; //default website URL
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Salam Fazil
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }

    public WebDriver register(WebDriver browserWindow, String email, String password, String answer) throws InterruptedException {

        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        browserWindow.findElement(By.id("navbarLoginButton")).click();
        Thread.sleep(1000);

        browserWindow.findElement(By.id("newCustomerLink")).click();
        Thread.sleep(1000);

        browserWindow = addText(browserWindow, "emailControl", email);
        browserWindow = addText(browserWindow, "passwordControl", password);
        browserWindow = addText(browserWindow, "repeatPasswordControl", password);
        browserWindow = addText(browserWindow, "securityAnswerControl", answer);

        Thread.sleep(1000);

        browserWindow.findElement(By.id("mat-select-2")).click();
        Thread.sleep(1000);

        browserWindow.findElement(By.cssSelector("#mat-option-3 > span")).click();
        Thread.sleep(1000);

        browserWindow.findElement(By.id("registerButton")).click();
        Thread.sleep(1000);

        return browserWindow;
    }

    public WebDriver logIn(WebDriver browserWindow, String email, String password) throws InterruptedException{

        browserWindow.findElement(By.id("navbarAccount")).click();

        Thread.sleep(1000);

        browserWindow.findElement(By.id("navbarLoginButton")).click();

        Thread.sleep(1000);

        browserWindow = addText(browserWindow, "email", email);

        Thread.sleep(1000);

        browserWindow = addText(browserWindow, "password", password);

        Thread.sleep(1000);

        browserWindow.findElement(By.id("loginButton")).click();

        return browserWindow;
    }

//    public WebDriver logInGoogle(String chosenBrowser, String email, String password) throws InterruptedException, IOException {
//        //Create driver and browser for this particular test
//        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
//        WebDriver browserWindow = browser.makeDriver();
//        browserWindow.manage().window().maximize();
//
//        browserWindow.get(website);
//
//        Thread.sleep(1000);
//
//        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted")).click();
//
//        Thread.sleep(1000);
//
//        WebElement accountBtn = browserWindow.findElement(By.id("navbarAccount"));
//        accountBtn.click();
//
//        Thread.sleep(1000);
//
//        browserWindow.findElement(By.id("navbarLoginButton")).click();
//
//        Thread.sleep(1000);
//
//        browserWindow.findElement(By.id("loginButtonGoogle")).click();
//
//        Thread.sleep(2000);
//
//        WebElement username = browserWindow.findElement(By.id("identifierId"));
//        username.click();
//        username.sendKeys(email);
//
//        Thread.sleep(1000);
//
//        browserWindow.findElement(By.cssSelector("#identifierNext > div > button")).click();
//
//        Thread.sleep(1000);
//
//        WebElement pass = browserWindow.findElement(By.cssSelector("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input"));
//        pass.click();
//        pass.sendKeys(password);
//
//        Thread.sleep(1000);
//
//        browserWindow.findElement(By.cssSelector("#passwordNext > div > button")).click();
//
//        return browserWindow;
//    }

    public WebDriver addText(WebDriver browserWindow, String id, String text){
        WebElement elem = browserWindow.findElement(By.id(id));
        elem.click();
        elem.sendKeys(text);

        return browserWindow;
    }

    /* TODO:
    PA1
    PA2
    PA3
    (about password recovery)
     */


    /**
     * Smoke test for valid inputs within several different browsers
     * Programmer: Salam Fazil
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","PasswordAlteration","PA_Smoke","hasDataProvider"},
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3
    )

    public void PA4_validReset(String chosenBrowser, String email, String password, String answer) throws IOException, InterruptedException
    {
        String newPass = "123456";
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(website);

        Thread.sleep(1000);

        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted")).click();

        Thread.sleep(1000);

        browserWindow = register(browserWindow, email, password, answer);
        Thread.sleep(1000);

        browserWindow = logIn(browserWindow, email, password);
        Thread.sleep(1000);

        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        Actions action = new Actions(browserWindow);
        WebElement privacyAndSecurityTab = browserWindow.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div/button[3]"));
        action.moveToElement(privacyAndSecurityTab).perform();
        Thread.sleep(1000);

        browserWindow.findElement(By.cssSelector("#mat-menu-panel-2 > div > button:nth-child(5)")).click();
        Thread.sleep(1000);

        browserWindow = addText(browserWindow, "currentPassword", password);
        //Thread.sleep(1000);

        browserWindow = addText(browserWindow, "newPassword", newPass);
        //Thread.sleep(1000);

        browserWindow = addText(browserWindow, "newPasswordRepeat", newPass);
        //Thread.sleep(1000);

        browserWindow.findElement(By.id("changeButton")).click();
        Thread.sleep(1000);

        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        browserWindow.findElement(By.id("navbarLogoutButton")).click();
        Thread.sleep(1000);

        browserWindow = logIn(browserWindow, email, newPass);
        Thread.sleep(1000);

        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        WebElement emailContainer = browserWindow.findElement(By.cssSelector("#mat-menu-panel-0 > div > button:nth-child(1) > span"));

        assertEquals(emailContainer.getText(), email);
    }

    /*
     *Smoke tests a single invalid registration attempt. //// Change
     *Programmer: Salam Fazil
     */
//    @Test(
//            groups = {"Smoke","Order_History","OH_Smoke","hasDataProvider"},
//            priority = 0,
//            dataProvider = "LG3_Input",
//            dataProviderClass = Test_Data.class,
//            threadPoolSize = 3,
//            enabled = true
//    )
//    public void OH2_Valid(String chosenBrowser, String email, String password) throws IOException, InterruptedException {
//
//        //Create driver and browser for this particular test
//        WebDriver browserWindow = logIn(chosenBrowser, email, password);
//
//        Thread.sleep(10000);
//
//        WebElement accountBtn = browserWindow.findElement(By.id("navbarAccount"));
//        accountBtn.click();
//
//        Thread.sleep(1000);
//
//        Actions action = new Actions(browserWindow);
//        WebElement opTab = browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]"));
//        action.moveToElement(opTab).perform();
//
//        Thread.sleep(1000);
//
//        browserWindow.findElement(By.cssSelector("#mat-menu-panel-3 > div > button:nth-child(1)")).click();
//
//        Thread.sleep(1000);
//
//        // Verify
//        WebElement OHT = browserWindow.findElement(By.className("mat-card-title"));
//        assertEquals(OHT.getText(), "Order History");
//
//        WebElement noResCont = browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > " +
//                "mat-sidenav-content > app-order-history > mat-card > div > mat-card > mat-card-title > span"));
//        assertEquals(noResCont.getText(), "No results found");
//
//        WebElement noOrderCont = browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > " +
//                "mat-sidenav-content > app-order-history > mat-card > div > mat-card > mat-card-content > span"));
//        assertEquals(noOrderCont.getText(), "You have no placed any orders yet.");
//    }


//    /**purpose
//     *Programmer
//     *@param
//     */
//    @Test(
//            groups = {"",""},
//            priority = 2
//    )
//    public void RF3_Validation_Email()
//    {
//
//    }
//
//    /**
//     * Smoke tests several invalid cases, which can be found in the data provider class.
//     *  Programmer:Kyle Sullivan
//     *  @param
//     */
//    @Test(
//            groups = {"Sanity","Registration Sanity","Registration","hasDataProvider"},
//            priority = 3,
//            dataProvider = "RF4_Input",
//            dataProviderClass = Test_Data.class,
//            threadPoolSize = 3
//    )
//    public void RF4_Invalid_Input(String testing, String email, String password, String repeatPassword, Boolean doQuestion, String answer) throws InterruptedException
//    {
//        boolean disabledButton;
//        WebDriver browserWindow = environment.makeDriver();
//        browserWindow.manage().window().maximize();
//
//        fillOutReg(browserWindow, email, password, repeatPassword,doQuestion,answer);
//
//        disabledButton = browserWindow.findElement(By.cssSelector("#registerButton")).isEnabled();
//
//        if(disabledButton)
//        {
//            //if the registration button is enabled, ensure it does not accept the account details.
//            browserWindow.findElement(By.cssSelector("#registerButton")).click();//click register button
//            Thread.sleep(1000);
//            assertEquals(browserWindow.getCurrentUrl(),website+"/#/register");//confirm that we have not left the page.
//        }
//        else
//        {
//            //If the registration button cannot be accessed, confirm the test.
//            assertFalse(disabledButton);
//        }
//
//        browserWindow.quit();
//    }
//
//    /**purpose
//     *Programmer
//     *@param
//     */
//    @Test(
//            groups = {"",""},
//            priority = 4,
//            dataProvider = "",
//            dataProviderClass = Test_Data.class,
//            threadPoolSize =0,
//            enabled = true
//    )
//    public void RF_Regression()
//    {
//
//    }
//
//    /**
//     * Method used to fill out registration form with passed values. Universal for all tests within registration.java
//     * Programmer: Kyle Sullivan
//     * @param browserWindow browser window used for this test
//     * @param email email string used for test
//     * @param password password string used for test
//     * @param repeatPassword repeat password string used for test
//     * @param doQuestion true/false to whether to use a security question
//     * @param answer String for security question.
//     * @throws InterruptedException
//     * @throws IOException triggers if no browser has been set for the test
//     */
//    private void fillOutReg(WebDriver browserWindow, String email, String password, String repeatPassword, Boolean doQuestion, String answer) throws InterruptedException
//    {
//        boolean notFound = true;
//        int optionTry = 0;
//        int optionTryLimit = 50;
//
//        browserWindow.get(website);
//        Thread.sleep(2500);
//        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
//        browserWindow.findElement(By.cssSelector("#navbarAccount")).click();
//
//        //verify that we can access the login page
//        WebElement accountMenuLogin = browserWindow.findElement(By.cssSelector("#navbarLoginButton"));
//        assertTrue(accountMenuLogin.isEnabled());
//        accountMenuLogin.click();
//
//        //Verify that the sign up page is accessible
//        WebElement signUpLink = browserWindow.findElement(By.cssSelector("#newCustomerLink"));
//        assertTrue(signUpLink.isEnabled());
//        signUpLink.click();
//
//        assertEquals(browserWindow.getCurrentUrl(),website+"/#/register");
//
//        browserWindow.findElement(By.cssSelector("#emailControl")).sendKeys(email); //enter email
//        browserWindow.findElement(By.cssSelector("#passwordControl")).sendKeys(password); //enter password
//        browserWindow.findElement(By.cssSelector("#repeatPasswordControl")).sendKeys(repeatPassword); //reenter password
//        browserWindow.findElement(By.cssSelector(".mat-select-trigger")).click(); //select security question
//
//        if(doQuestion)
//        {
//            Thread.sleep(500);
//            /*
//            Due to the nature of the security question answer box, there is potentially dozens of ids for the several answers.
//            The catch is that only a few of these are true for any given instance of a test, but we dont know which ones will be active.
//            As such, we are forced to just check for many different possible answers and use the first valid one we find.
//            This is not an error on the site's part, just a limitation of the automation.
//            */
//            while (notFound && optionTry < optionTryLimit)
//            {
//                try
//                {
//                    browserWindow.findElement(By.cssSelector("#mat-option-" + optionTry)).click();
//                    notFound = false;
//                } catch (Exception NoSuchElementException)
//                {
//                    notFound = true;
//                    optionTry++;
//                }
//            }
//        }
//        //give security question answer
//        browserWindow.findElement(By.cssSelector("#securityAnswerControl")).sendKeys(answer); //enter answer
//    }

    @BeforeMethod(onlyForGroups = {"hasDataProvider"})
    public void BeforeMethod(Method method, Object[] testData)
    {
        testName.set(method.getName()+"_"+testData[0]);
    }

    @Override
    public String getTestName()
    {
        return testName.get();
    }
}
