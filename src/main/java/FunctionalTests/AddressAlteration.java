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

import static FunctionalTests.TestFunctions.*;
import static org.testng.Assert.*;

public class AddressAlteration implements ITest
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

    /*
    TODO:
    AA1
    AA regression
    */


    /**
     * Sanity test for valid password reset
     * Programmer: Salam Fazil
     */
    @Test(
            groups = {"Smoke","AddressAlteration","AA_Smoke","hasDataProvider"},
            dataProvider = "AA1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3
    )

    public void AA1_validAddressAdditionAndRemoval(String email, String password, String answer,
                                                   String chosenBrowser, String country, String name,
                                                   String phoneNumber, String postalCode, String address,
                                                   String city, String state) throws IOException, InterruptedException
    {
        // Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(website);
        Thread.sleep(1000);

        // Dismiss the initial pop up
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > " +
                "app-welcome-banner > div > div:nth-child(3) > " +
                "button.mat-focus-indicator.close-dialog." +
                "mat-raised-button.mat-button-base.mat-primary.ng-star-inserted")).click();
        Thread.sleep(1000);

        // Create an account with random email and password
        register(browserWindow, email, password, answer);
        Thread.sleep(1000);

        // Log in to created account
        logIn(browserWindow, email, password);
        Thread.sleep(1000);

        // Navigate to saved addresses page
        navigateToSavedAddresses(browserWindow);
        Thread.sleep(1000);

        // Initiate valid address addition
        browserWindow.findElement(By.xpath(
                "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-saved-address/div/" +
                        "app-address/mat-card/div/button")).click();
        Thread.sleep(1000);

        addText(browserWindow, "mat-input-8", country);
        addText(browserWindow, "mat-input-9", name);
        addText(browserWindow, "mat-input-10", phoneNumber);
        addText(browserWindow, "mat-input-11", postalCode);
        addText(browserWindow, "address", address);
        addText(browserWindow, "mat-input-13", city);
        addText(browserWindow, "mat-input-14", state);
        Thread.sleep(1000);

        browserWindow.findElement(By.id("submitButton")).click();
        Thread.sleep(1000);

        // Validate if address was added
        navigateToSavedAddresses(browserWindow);
        Thread.sleep(1000);



        // Initiate Valid address removal

        // Validate if address was removed
    }

    /**
     * Sanity test for valid password reset
     * Programmer: Salam Fazil
     * @param email email text for test
     * @param password password text for test
     * @param chosenBrowser browser used for that test
     */
    @Test(
            groups = {"Smoke","PasswordAlteration","PA_Sanity","hasDataProvider"},
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3
    )

    public void PA5_invalidReset(String chosenBrowser, String email, String password, String answer) throws IOException, InterruptedException
    {
        // Reset password to this
        String newPass = "1234";

        // Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(website);
        Thread.sleep(1000);

        // Dismiss the initial pop up
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > " +
                "app-welcome-banner > div > div:nth-child(3) > " +
                "button.mat-focus-indicator.close-dialog." +
                "mat-raised-button.mat-button-base.mat-primary.ng-star-inserted")).click();
        Thread.sleep(1000);

        // Create an account with random email and password
        register(browserWindow, email, password, answer);
        Thread.sleep(1000);

        // Log in to created account
        logIn(browserWindow, email, password);
        Thread.sleep(1000);

        // Initiate password reset
        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        Actions action = new Actions(browserWindow);
        WebElement privacyAndSecurityTab = browserWindow.findElement(By.xpath(
                "/html/body/div[3]/div[2]/div/div/div/button[3]"));
        action.moveToElement(privacyAndSecurityTab).perform();
        Thread.sleep(1000);

        browserWindow.findElement(By.cssSelector("#mat-menu-panel-2 > div > button:nth-child(5)")).click();
        Thread.sleep(1000);

        addText(browserWindow, "currentPassword", password);

        addText(browserWindow, "newPassword", newPass);

        browserWindow.findElement(By.id("newPasswordRepeat")).click();
        Thread.sleep(1000);

        WebElement invalidPassErrorMsgContainer = browserWindow.findElement(By.id("mat-error-16"));
        assertEquals(invalidPassErrorMsgContainer.getText(), "Password must be 5-20 characters long.");
    }


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
