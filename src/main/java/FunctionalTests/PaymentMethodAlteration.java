package FunctionalTests;

import Setup.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.devtools.v85.browser.Browser;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITest;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;

import static FunctionalTests.TestFunctions.*;
import static org.testng.Assert.*;

public class PaymentMethodAlteration implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();

    TestBrowser environment;
    CreateEnvironment passBrowser;

    // Declarations for constant payment method values
    String name;
    String cardNum;

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Salam Fazil
     */
    @BeforeSuite
    public void SetUp() throws IOException, InterruptedException {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();

        name = "Hello World";
        cardNum = "2564234323542343";

        // TestFunctions.createAccount();
        // TestFunctions.createAddress();
    }

    /*
    TODO:
    PMA1 - Smoke - Valid Addition - STATUS: COMPLETED (need review)
    PMA2 - Smoke - Invalid Addition - STATUS: COMPLETED (need review)
    PMA3 - Smoke - Payment method Removal - STATUS: COMPLETED (need review)
    PMA4 - Sanity - Invalid Addition - STATUS: COMPLETED (need review)
    PMA - regression - STATUS: Not Done, complete at the end
    */

    /**
     * Smoke test for valid payment method addition
     * Programmer: Salam Fazil
     * @param chosenBrowser
     */
    @Test(
            groups = {"Smoke","PaymentMethodAlteration","PMA_Smoke","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void PMA1_validPaymentAddition(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Login and navigate to payment options page
        login(browserWindow);
        navigateToPaymentOptions(browserWindow);

        // Create a valid payment method
        boolean result = createPaymentMethod(browserWindow, name, cardNum);
        Thread.sleep(1000);

        // Validate created payment method exists
        assertTrue(result);
        assertTrue(browserWindow.getPageSource().contains(cardNum.subSequence(cardNum.length() - 4, cardNum.length())));
        assertTrue(browserWindow.getPageSource().contains(name));
    }

    /**
     * Smoke test for invalid payment method addition
     * Programmer: Salam Fazil
     * @param chosenBrowser
     */
    @Test(
            groups = {"Smoke","PaymentMethodAlteration","PMA_Smoke","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void PMA2_invalidPaymentAddition(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Login and navigate to payment options page
        login(browserWindow);
        navigateToPaymentOptions(browserWindow);

        // Fill in payment method information with invalid card num
        boolean result = createPaymentMethod(browserWindow, name, "123456789112345");
        Thread.sleep(1000);

        // Validate that submit button was disabled
        assertFalse(result);
    }

    /**
     * Smoke test for payment method removal
     * Programmer: Salam Fazil
     * @param chosenBrowser
     */
    @Test(
            groups = {"Smoke","PaymentMethodAlteration","PMA_Smoke","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )
    public void PMA3_paymentMethodRemoval(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Login and navigate to payment options page
        login(browserWindow);
        navigateToPaymentOptions(browserWindow);

        // Clear all existing payment methods
        clearPaymentMethods(browserWindow, cardNum, name);

        // Create a valid payment method
        boolean result = createPaymentMethod(browserWindow, name, cardNum);
        Thread.sleep(1000);

        // Make sure payment method was added successfully
        assertTrue(result);
        assertTrue(browserWindow.getPageSource().contains(cardNum.subSequence(cardNum.length() - 4, cardNum.length())));
        assertTrue(browserWindow.getPageSource().contains(name));

        // Remove the added payment method
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/" +
                "app-saved-payment-methods/mat-card/app-payment-method/div/div[1]/mat-table/mat-row/mat-cell[4]/" +
                "button")).click();
        Thread.sleep(5000); // Need to give it time to process removal

        // Validate that added card was removed
        assertFalse(browserWindow.getPageSource().contains(cardNum.subSequence(cardNum.length() - 4, cardNum.length())));
        assertFalse(browserWindow.getPageSource().contains(name));
    }

    /**
     * Sanity test for invalid payment method addition
     * Programmer: Salam Fazil
     * @param chosenBrowser
     */
    @Test(
            groups = {"Sanity","PaymentMethodAlteration","PMA_Sanity","hasDataProvider"},
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class
    )

    public void PMA4_invalidPaymentAddition(String chosenBrowser) throws IOException, InterruptedException {
        //Create Test environment and browser
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        //Go to Website
        browserWindow.get(TestFunctions.website);

        //Ensure the site is ready for testing
        TestFunctions.waitForSite(browserWindow);

        // Login and navigate to payment options page
        login(browserWindow);
        navigateToPaymentOptions(browserWindow);

        boolean result;
        // Fill in payment method information with invalid name and validate that submit button is disabled in that case
        result = createPaymentMethod(browserWindow,"", cardNum);
        assertFalse(result);

        // Fill in payment method information with invalid card numbers and validate that submit button is disabled in those case
        result = createPaymentMethod(browserWindow, name, "123456789112345");
        assertFalse(result);

        result = createPaymentMethod(browserWindow, name, "12345678911234567");
        assertFalse(result);
    }

    private static void clearPaymentMethods(WebDriver browserWindow, String cardNum, String name){

        while (browserWindow.getPageSource().contains(cardNum.subSequence(cardNum.length() - 4, cardNum.length())) &&
                browserWindow.getPageSource().contains(name))

            browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/" +
                    "app-saved-payment-methods/mat-card/app-payment-method/div/div[1]/mat-table/mat-row/mat-cell[4]/" +
                    "button")).click();

    }

    private static void navigateToPaymentOptions(WebDriver browserWindow) throws InterruptedException {
        browserWindow.findElement(By.id("navbarAccount")).click();
        Thread.sleep(1000);

        Actions action = new Actions(browserWindow);
        WebElement oAndPTab = browserWindow.findElement(By.cssSelector(
                "#mat-menu-panel-0 > div > button:nth-child(2) > span"));
        action.moveToElement(oAndPTab).perform();
        Thread.sleep(1000);

        try {
            browserWindow.findElement(By.xpath("/html/body/div[4]/div[3]/div/div/div/button[4]")).click();
        } catch (NoSuchElementException e){
            browserWindow.findElement(By.cssSelector("#mat-menu-panel-3 > div > button:nth-child(5)")).click();
        }
    }

    // Assuming on payment options page
    private static boolean createPaymentMethod(WebDriver browserWindow, String name, String cardNum) throws InterruptedException {

        // Click 'Add new card'
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/" +
                "app-saved-payment-methods/mat-card/app-payment-method/div/div/mat-expansion-panel")).click();
        Thread.sleep(1000);

        // Clear data from name and card number (in case other text exists)
        browserWindow.findElement(By.id("mat-input-1")).clear();
        browserWindow.findElement(By.id("mat-input-2")).clear();

        // Fill in data for name, card number, and expiry date
        browserWindow.findElement(By.id("mat-input-1")).sendKeys(name);
        browserWindow.findElement(By.id("mat-input-2")).sendKeys(cardNum);

        WebElement expiryMonthContainer = browserWindow.findElement(By.id("mat-input-3"));
        new Select(expiryMonthContainer).selectByIndex(1);

        WebElement expiryYearContainer = browserWindow.findElement(By.id("mat-input-4"));
        new Select(expiryYearContainer).selectByIndex(1);

        try {
            browserWindow.findElement(By.xpath("/html/body/div[1]/div/a")).click();
            Thread.sleep(1000);
        } catch (NoSuchElementException | ElementNotInteractableException ignored){}

        WebElement submitBtn = browserWindow.findElement(By.id("submitButton"));

        if (submitBtn.isEnabled()) {
            submitBtn.click();
            return true;
        }else
            return false;
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