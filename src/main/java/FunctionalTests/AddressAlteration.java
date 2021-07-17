package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

    TestBrowser environment;
    CreateEnvironment passBrowser;

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Salam Fazil
     */
    @BeforeSuite
    public void SetUp() throws IOException, InterruptedException {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();

       // TestFunctions.createAccount();
       // TestFunctions.createAddress();
    }

    /*
    TODO:
    AA1 - Smoke - Valid Addition - STATUS: COMPLETED (need review)
    AA2 - Smoke - Invalid Addition - STATUS: COMPLETED (need review)
    AA3 - Smoke - Address Removal - STATUS: Mostly done, need to ask a few questions
    AA4 - Sanity - Invalid Addition - STATUS: COMPLETED (need review)
    AA regression - STATUS: Not Done, complete at the end
    */

    /**
     * Smoke test for valid address addition
     * Programmer: Salam Fazil
     * @param chosenBrowser
     */
    @Test(
            groups = {"Smoke","AddressAlteration","AA_Smoke","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void AA1_validAddressAddition(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Create an address
        TestFunctions.createAddress();
        Thread.sleep(1000);

        // Validate created address exists
        login(browserWindow);
        Thread.sleep(500);
        navToSavedAddresses(browserWindow);
        Thread.sleep(1000);

        for (int i = 1; i <= 7; i++) {
            if(i == 3)
                continue;
            assertTrue(browserWindow.getPageSource().contains(addressSet[i].toString()));
        }
    }

    /**
     * Smoke test for invalid address addition
     * Programmer: Salam Fazil
     * TODO: params
     */
    @Test(
            groups = {"Smoke","AddressAlteration","AA_Smoke","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void AA2_invalidAddressAddition(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Login
        TestFunctions.login(browserWindow);

        // Initialize the constant address object
        constAddressValues();

        // Change phone number to invalid phone number
        invalidate(3);

        // Navigate to create and address page and then fill in data
        navigateToSavedAddresses(browserWindow);
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/" +
                "app-saved-address/div/app-address/mat-card/div/button")).click();
        fillAddressInformation(browserWindow);

        // Validate that necessary warning shows up and submit button is greyed out and un-clickable
        assertTrue(browserWindow.getPageSource().contains("Mobile number must match 1000000-9999999999 format."));
        assertFalse(browserWindow.findElement(By.id("submitButton")).isEnabled());
    }

    /**
     * Smoke test for address removal
     * Programmer: Salam Fazil
     * TODO: params
     */
    @Test(
            groups = {"Smoke","AddressAlteration","AA_Smoke","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void AA3_addressRemoval(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Register
        TestFunctions.createAccount();

        // Login and create an address
        TestFunctions.createAddressManualLogin();

        // Navigate to saved addresses page
        manualLogin(browserWindow);
        navigateToSavedAddresses(browserWindow);

        // Remove added address
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content" +
                "/app-saved-address/div/app-address/mat-card/mat-table/mat-row/mat-cell[5]/button")).click();
        Thread.sleep(1000);

        // Validate if address was removed
        for (int i = 1; i <= 7; i++) {
            if(i == 3)
                continue;
            assertFalse(browserWindow.getPageSource().contains(addressSet[i].toString()));
        }
    }

    /**
     * Sanity test for invalid address addition
     * Programmer: Salam Fazil
     * TODO: params
     */
    @Test(
            groups = {"Sanity","AddressAlteration","AA_Sanity","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void AA4_invalidAddressAddition(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Login
        TestFunctions.login(browserWindow);

        // Initialize the constant address object
        constAddressValues();

        // Navigate to the 'create an address' page and then fill in data
        navigateToSavedAddresses(browserWindow);
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/" +
                "app-saved-address/div/app-address/mat-card/div/button")).click();

        // Set invalid values one by one, and validate that submit
        // button cannot be clicked when any of the data is invalid
        cycleInvalidity(browserWindow);
    }

    private static void cycleInvalidity(WebDriver browserWindow) throws InterruptedException {
        for (int i = 1; i <= 7 ; i++) {
            Object temp = invalidate(i);

            // Fill in new address information with the i'th data being invalid
            fillAddressInformation(browserWindow);

            // Validate that submit button is greyed out and un-clickable
            assertFalse(browserWindow.findElement(By.id("submitButton")).isEnabled());

            revalidate(i, temp);
        }
    }

    /**
     * Invalidates given index in addressSet
     * @param invalidDataIndex - The index in which we want to invalidate
     * Return old value
     */
    private static Object invalidate(int invalidDataIndex){
        Object temp = addressSet[invalidDataIndex];

        boolean mobile = invalidDataIndex == 3;
        boolean zip = invalidDataIndex == 4;

        if (mobile)
            addressSet[invalidDataIndex] = 123456; // Invalid phone number
        else if (zip)
            addressSet[invalidDataIndex] = 123456789; // Invalid zip code
        else
            addressSet[invalidDataIndex] = ""; // Input for rest

        return temp;
    }

    private static void revalidate(int invalidDataIndex, Object prev){
        addressSet[invalidDataIndex] = prev;
    }

    private static void navigateToSavedAddresses(WebDriver browserWindow) throws InterruptedException {

        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        Actions action = new Actions(browserWindow);
        WebElement oAndPTab = browserWindow.findElement(By.cssSelector(
                "#mat-menu-panel-0 > div > button:nth-child(2) > span"));
        action.moveToElement(oAndPTab).perform();
        Thread.sleep(1000);

        try {
            browserWindow.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/button[3]")).click();
        } catch (NoSuchElementException e){
            browserWindow.findElement(By.xpath("/html/body/div[4]/div[3]/div/div/div/button[3]")).click();
        }

    }

    private static void fillAddressInformation(WebDriver browserWindow){
        int addressIndex = 1;
        for (int i = 1; i <= 7 ; i++) {
            if (addressIndex == 5)
                browserWindow.findElement(By.id(("address"))).sendKeys(addressSet[addressIndex++].toString());
            else
                browserWindow.findElement(By.id(("mat-input-" + i))).sendKeys(addressSet[addressIndex++].toString());
        }
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
