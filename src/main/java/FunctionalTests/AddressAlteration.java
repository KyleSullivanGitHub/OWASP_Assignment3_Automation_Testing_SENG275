package FunctionalTests;

import Setup.*;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.v85.browser.Browser;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
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
        constAddressValues();
    }

    /*
    TODO:
    AA1 - Smoke - Valid Addition - STATUS: COMPLETED (need review)
    AA2 - Smoke - Invalid Addition - STATUS: COMPLETED (need review)
    AA3 - Smoke - Address Removal - STATUS: COMPLETED (need review)
    AA4 - Sanity - Invalid Addition - STATUS: COMPLETED (need review)
    AA - regression - STATUS: Not Done, complete at the end
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

        // Login and navigate to saved addresses
        login(browserWindow);
        navigateToSavedAddresses(browserWindow);
        Thread.sleep(1000);

        // Clear all existing addresses
        clearExistingAddresses(browserWindow);

        // Create an address
        boolean result = createAddress(browserWindow, addressSet[1].toString(), addressSet[2].toString(), addressSet[3].toString(),
                addressSet[4].toString(), addressSet[5].toString(), addressSet[6].toString(), addressSet[7].toString());
        Thread.sleep(1000);

        // Validate created address exists
        assertTrue(result);
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

        // Navigate to create and address page and then fill in data with incorrect phone
        navigateToSavedAddresses(browserWindow);
        boolean result = createAddress(browserWindow, addressSet[1].toString(), addressSet[2].toString(), "123456",
                addressSet[4].toString(), addressSet[5].toString(), addressSet[6].toString(), addressSet[7].toString());

        // Validate that submit button is greyed out and un-clickable
        assertFalse(result);
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

        // Login and navigate to saved addresses
        login(browserWindow);
        navigateToSavedAddresses(browserWindow);

        // Initialize address set then clear all existing addresses
        clearExistingAddresses(browserWindow);

        // Create an address
        createAddress(browserWindow, addressSet[1].toString(), addressSet[2].toString(), addressSet[3].toString(),
                addressSet[4].toString(), addressSet[5].toString(), addressSet[6].toString(), addressSet[7].toString());
        Thread.sleep(1000);

        // Remove added address
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content" +
                "/app-saved-address/div/app-address/mat-card/mat-table/mat-row/mat-cell[5]/button")).click();
        Thread.sleep(1000);

        // Validate address was removed
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
//        Thread.sleep(1000);

        // Set invalid values one by one, and validate that submit
        // button cannot be clicked when any of the data is invalid
        cycleInvalidity(browserWindow);
    }

    private static void cycleInvalidity(WebDriver browserWindow) throws InterruptedException {
        for (int i = 1; i <= 6 ; i++) {
            Object temp = invalidate(i);

            // Fill in new address information with the i'th data being invalid
            boolean result = createAddress(browserWindow, addressSet[1].toString(), addressSet[2].toString(),
                    addressSet[3].toString(), addressSet[4].toString(), addressSet[5].toString(),
                    addressSet[6].toString(), addressSet[7].toString());

            //Thread.sleep(10000);
            revalidate(i, temp);

            // Validate that submit button is greyed out and un-clickable
            assertFalse(result);
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

    private static void clearExistingAddresses(WebDriver browserWindow){

        while (browserWindow.getPageSource().contains(addressSet[1].toString()))

            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/" +
                    "app-saved-address/div/app-address/mat-card/mat-table/mat-row[1]/mat-cell[5]/button")).click();

    }

    private static boolean createAddress(WebDriver browserWindow, String country, String name, String phone, String zip,
                                         String address, String city, String state) throws InterruptedException {

        // Click 'Add New Address' if available
        try {
            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/" +
                    "app-saved-address/div/app-address/mat-card/div/button")).click();
            Thread.sleep(1000);
        } catch (NoSuchElementException ignored){}

        // Fill in fields with given data
        browserWindow.findElement(By.id("mat-input-1")).sendKeys(country);
        browserWindow.findElement(By.id("mat-input-2")).sendKeys(name);
        browserWindow.findElement(By.id("mat-input-3")).sendKeys(phone);
        browserWindow.findElement(By.id("mat-input-4")).sendKeys(zip);
        browserWindow.findElement(By.id("address")).sendKeys(address);
        browserWindow.findElement(By.id("mat-input-6")).sendKeys(city);
        browserWindow.findElement(By.id("mat-input-7")).sendKeys(state);

        WebElement submitBtn = browserWindow.findElement(By.id("submitButton"));
        try {
            browserWindow.findElement(By.cssSelector(".mat-simple-snackbar-action > " +
                    "button:nth-child(1) > span:nth-child(1)")).click();

        } catch (NoSuchElementException | ElementNotInteractableException ignored){}

        Thread.sleep(1000);

        // If submit button is enabled, click, then return true, otherwise refresh page and return false
        if (submitBtn.isEnabled()) {
            submitBtn.click();
            return true;
        }else{
            browserWindow.navigate().refresh();
            return false;
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
