package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITest;
import org.testng.annotations.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Random;
import static org.testng.Assert.*;


/*
Tests for verifying the full functionality of the Choose Language feature
*/

public class Choose_Language implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();


    public static final String titleCSS="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-search-result > div > div > div.heading.mat-elevation-z6 > div.ng-star-inserted";
    public static final String chooseLanguageCSS="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button.mat-focus-indicator.mat-tooltip-trigger.mat-menu-trigger.buttons.mat-button.mat-button-base";
    public static final String DanskCSS="#mat-radio-5";
    public static final String ItalianoCSS="#mat-radio-11";
    public static final String MagyarCSS="#mat-radio-14";
    public static final String DanskTitleCSS="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-search-result > div > div > div.heading.mat-elevation-z6 > div.ng-star-inserted";
    public static final String ItalianoTitleCSS="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-search-result > div > div > div.heading.mat-elevation-z6 > div.ng-star-inserted";
    public static final String MagyarTitleCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-search-result > div > div > div.heading.mat-elevation-z6 > div.ng-star-inserted";


    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Seyedmehrad Adimi
     */
    @BeforeClass
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }


    /**
     *Smoke tests for Valid use of Choose Language
     * Includes test cases CL_001,CL_002,CL_003
     *Programmer: Seyedmehrad Adimi
     * @param chosenBrowser browser used for that test
     * @param dataSet object provides email and password
     * @exception IOException Thrown if no browser is chosen for a test
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    @Test(
            groups = {"Smoke","Choose_Language", "hasDataProvider"},
            priority = 44,
            dataProvider = "LG1_Input",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void CL1_Valid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try {
            // CL_001 test case: Verify the complete functionality of the Application by selecting 'Dansk' language
            String CL1_Language = "Dansk";
            TestThisLanguage (browserWindow, CL1_Language);


            String CL2_Language = "Italiano";
            TestThisLanguage (browserWindow, CL2_Language);

            String CL3_Language = "Magyar";
            TestThisLanguage (browserWindow, CL3_Language);

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }




    /**
     * This is a helper method to help verifiying the languages chosen
     * Programmer: Seyedmehrad Adimi
     * @exception InterruptedException is thrown if a test is interrupted during a wait time
     */
    private void TestThisLanguage(WebDriver browserWindow, String Language) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(browserWindow,10);
        browserWindow.findElement(By.cssSelector (chooseLanguageCSS)).click ();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector (DanskCSS)));
        if (Language.equals ("Dansk")){
            browserWindow.findElement (By.cssSelector (DanskCSS)).click ();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector (DanskTitleCSS)));
            WebElement Title = browserWindow.findElement(By.cssSelector(DanskTitleCSS));
            wait.until (ExpectedConditions.textToBe (By.cssSelector(DanskTitleCSS),"Alle produkter"));
            assertEquals (Title.getText (), "Alle produkter");
            return;
        }else if (Language.equals ("Italiano")){
            wait.until (ExpectedConditions.elementToBeClickable (By.cssSelector (ItalianoCSS)));
            browserWindow.findElement (By.cssSelector (ItalianoCSS)).click ();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector (ItalianoTitleCSS)));
            WebElement Title = browserWindow.findElement(By.cssSelector(ItalianoTitleCSS));
            wait.until (ExpectedConditions.textToBe (By.cssSelector(ItalianoTitleCSS),"Tutti i prodotti"));
            assertEquals (Title.getText (), "Tutti i prodotti");
            return;
        }else if (Language.equals ("Magyar")){
            wait.until (ExpectedConditions.elementToBeClickable (By.cssSelector (MagyarCSS)));
            browserWindow.findElement (By.cssSelector (MagyarCSS)).click ();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector (MagyarTitleCSS)));
            WebElement Title = browserWindow.findElement(By.cssSelector(MagyarTitleCSS));
            wait.until (ExpectedConditions.textToBe (By.cssSelector(MagyarTitleCSS),"??sszes term??k"));
            assertEquals (Title.getText (), "??sszes term??k");
            return;
        }
    }


    /**
     * Method for changing the name of tests performed multiple times by adding the first value in their data provider to the end of their names
     * Taken from: https://www.swtestacademy.com/change-test-name-testng-dataprovider/
     * Programmer: Canberk Akduygu
     * @param method Test method whose name is to be changed
     * @param testData The data parameters for the method
     */
    @BeforeMethod(onlyForGroups = {"hasDataProvider"})
    public void BeforeMethod(Method method, Object[] testData)
    {
        //Set name to (method name)_(first value in data provider)
        testName.set(method.getName() + "_" + testData[0]);
    }
    @BeforeMethod(onlyForGroups = {"noDataProvider"})
    public void BeforeMethod(Method method)
    {
        //Set name to (method name)
        testName.set(method.getName());
    }
    /**
     * Returns the name of the test. Used to alter the name of tests performed multiple times
     * Taken from: https://www.swtestacademy.com/change-test-name-testng-dataprovider/
     * Programmer: Canberk Akduygu
     * @return Name of test
     */
    @Override
    public String getTestName()
    {
        return testName.get();
    }
}
