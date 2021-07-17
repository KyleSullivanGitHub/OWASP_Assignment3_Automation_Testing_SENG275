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
    AA1 - DONE
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

    public void AA1_validAddressAdditionAndRemoval(String chosenBrowser, String email, String password, String answer,
                                                   String country, String name, String phoneNumber, String postalCode,
                                                   String address, String city, String state) throws IOException, InterruptedException
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

        // Log in. If no account, register
        manualLogin(browserWindow);
        Thread.sleep(1000);

        // Navigate to saved addresses page
        navigateToSavedAddresses(browserWindow);
        Thread.sleep(1000);

        // Initiate valid address addition
        browserWindow.findElement(By.xpath(
                "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-saved-address/div/" +
                        "app-address/mat-card/div/button")).click();
        Thread.sleep(1000);

        browserWindow.findElement(By.id("mat-input-9")).sendKeys(country);
        browserWindow.findElement(By.id("mat-input-10")).sendKeys(name);
        browserWindow.findElement(By.id("mat-input-11")).sendKeys(phoneNumber);
        browserWindow.findElement(By.id("mat-input-12")).sendKeys(postalCode);
        browserWindow.findElement(By.id("address")).sendKeys(address);
        browserWindow.findElement(By.id("mat-input-14")).sendKeys(city);
        browserWindow.findElement(By.id("mat-input-15")).sendKeys(state);

        Thread.sleep(1000);

        browserWindow.findElement(By.id("submitButton")).click();
        Thread.sleep(1000);

        // Validate if address was added
        WebElement nameContainer = browserWindow.findElement(By.xpath("/html/body/app-root/div/" +
                "mat-sidenav-container/mat-sidenav-content/app-saved-address/div/app-address/" +
                "mat-card/mat-table/mat-row/mat-cell[1]"));

        WebElement fullAddressContainer = browserWindow.findElement(By.xpath("/html/body/app-root/div/" +
                "mat-sidenav-container/mat-sidenav-content/app-saved-address/div/app-address/mat-card/mat-table" +
                "/mat-row/mat-cell[2]"));

        WebElement countryContainer = browserWindow.findElement(By.xpath("/html/body/app-root/div/" +
                "mat-sidenav-container/mat-sidenav-content/app-saved-address/div/app-address/mat-card/mat-table/" +
                "mat-row/mat-cell[3]"));

        assertEquals(nameContainer.getText(), name);
        assertEquals(fullAddressContainer.getText(), address + ", " + city + ", " + state + ", " + postalCode);
        assertEquals(countryContainer.getText(), country);

        Thread.sleep(1000);

        // Initiate Valid address removal
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content" +
                "/app-saved-address/div/app-address/mat-card/mat-table/mat-row/mat-cell[5]/button")).click();
        Thread.sleep(1000);

        // Validate if address was removed
        assertFalse(browserWindow.getPageSource().contains(country));
        assertFalse(browserWindow.getPageSource().contains(name));
        assertFalse(browserWindow.getPageSource().contains(phoneNumber));
        assertFalse(browserWindow.getPageSource().contains(postalCode));
        assertFalse(browserWindow.getPageSource().contains(address));
        assertFalse(browserWindow.getPageSource().contains(city));
        assertFalse(browserWindow.getPageSource().contains(state));
    }
/* Original
    private void navigateToSavedAddresses(WebDriver browserWindow) throws InterruptedException {

        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        Actions action = new Actions(browserWindow);
        WebElement oAndPTab = browserWindow.findElement(By.cssSelector(
                "#mat-menu-panel-0 > div > button:nth-child(2) > span"));
        action.moveToElement(oAndPTab).perform();
        Thread.sleep(1000);

        browserWindow.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/button[3]")).click();

    }

 */
    //Optimized
    private void navigateToSavedAddresses(WebDriver browserWindow) throws InterruptedException {

        String xPathPart1 = "/html/body/div[3]/div[";
        String xPathPart2 = "]/div/div/div/button[";
        String xPathPart3 = "]";

        browserWindow.findElement(By.cssSelector(navPath)).click();
        Thread.sleep(100);
        browserWindow.findElement(By.xpath(xPathPart1 + 2 + xPathPart2 + 2 + xPathPart3)).click();//Click on Orders and payments
        Thread.sleep(100);
        browserWindow.findElement(By.xpath(xPathPart1 + 3 + xPathPart2 + 3 + xPathPart3)).click();//Click on My Saved Addresses
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
