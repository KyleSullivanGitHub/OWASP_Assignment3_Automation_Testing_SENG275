package FunctionalTests;

import Setup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITest;
import org.testng.annotations.*;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.testng.Assert.*;

public class Photo_Wall implements ITest
{
    private ThreadLocal<String> testName = new ThreadLocal<>();
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();

    public static final String sideMenuCSS = "body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)";
    public static final String titleCSS="body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-search-result > div > div > div.heading.mat-elevation-z6 > div.ng-star-inserted";

    /**
     *Create an environment for all tests using the same browser app.
     *Programmer: Seyedmehrad Adimi
     */
    @BeforeSuite
    public void SetUp() throws IOException
    {
        environment = passBrowser.createBrowser();
    }



    /**
     *Smoke tests for Valid use of Photo_Wall feature and posting picture
     * Includes test cases PW_001, PW_004,PW_008
     *Programmer: Seyedmehrad Adimi
     */
    @Test(
            groups = {"Smoke","Photo_Wall","hasDataProvider"},
            dataProvider = "LG3_Input",
            priority = 45,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PW1_Valid_Use(String chosenBrowser, String email, String password) throws InterruptedException, IOException{
        //TODO remove all paramaters other then chosen browser. Smoke tests only use one set of inputs
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        // Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);


        // Login
        loginForMe (browserWindow,email,password);

        // PW_001 test case: Verify 'Photo Wall' side menu link
        Thread.sleep(500);
        WebElement sideBarMenu = browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)"));
        sideBarMenu.click ();
        Thread.sleep(300);

        // Verify Photo Wall link

        WebElement photoWall = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(12) > div > span"));
        assertEquals (photoWall.getText (),"Photo Wall");

        // PW_004 test case: Verify 'Pick image' button works
        photoWall.click ();
        WebElement pickImageBtn = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div.ng-star-inserted > div > form > div > button"));
        assertTrue (pickImageBtn.isEnabled ());

        Thread.sleep (1000);


        // PW_005 : Verify can't post picture without caption

//        pickImageBtn.click ();
//        uploadFile("/Users/seyedmehradadimi/IdeaProjects/OWASP_Assignment3_Automation_Testing_SENG275/src/main/java/FunctionalTests/uvic_logo-horizontal.jpeg");
//        Thread.sleep(2000);
//
//
//        Thread.sleep (1300);
//        pickImageBtn.sendKeys (Keys.ESCAPE);

        WebElement captionInput = browserWindow.findElement (By.id ("mat-input-1"));
        captionInput.click ();
        browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall")).click ();

        Thread.sleep (1111);
        WebElement captionErr = browserWindow.findElement (By.cssSelector ("#mat-error-0"));
        assertEquals (captionErr.getText (), "Please enter a caption");


         //PW_008 test case: Verify picture is posted with caption when 'submit' button is clicked

        captionInput.click ();
        captionInput.sendKeys ("UVic Logo");
//
//        WebElement submittBtn =  browserWindow.findElement (By.id ("submitButton"));
//        submittBtn.click ();

        Thread.sleep (1500);

        WebElement postedPicture = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(6) > div > div"));
        assertEquals (postedPicture.getText (), "UVic Logo");




        Thread.sleep(2000);
        browserWindow.quit();
    }





    /**
     *Smoke tests for Invalid use of Photo_Wall feature and posting picture
     * Includes test cases PW_006, PW_007
     *Programmer: Seyedmehrad Adimi
     */
    @Test(
            groups = {"Smoke","Photo_Wall","noDataProvider"},
            dataProvider = "LG3_Input",
            priority = 46,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PW2_Invalid_Use(String email, String password) throws InterruptedException, IOException{
        //TODO Change to smoke test. remove all inputs
        //Browser setup
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);

        // Login
        loginForMe (browserWindow,email,password);
        Thread.sleep(1500);

        //PW_007 test case: Verify caption warning message is displayed when no caption is entered
        WebElement sideBarMenu = browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)"));
        sideBarMenu.click ();
        Thread.sleep(300);


        WebElement photoWall = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(12) > div > span"));

        photoWall.click ();


        Thread.sleep (1000);

        WebElement captionInput = browserWindow.findElement (By.id ("mat-input-1"));

        captionInput.clear ();
        Thread.sleep (500);

        captionInput.click ();
        Thread.sleep (500);
        browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall")).click ();

        Thread.sleep (1111);
        WebElement captionErr = browserWindow.findElement (By.cssSelector ("#mat-error-0"));
        assertEquals (captionErr.getText (), "Please enter a caption");


        // PW_006 test case: Verify can't post caption without picture


        captionInput.click ();
        captionInput.sendKeys ("Hello");
        Thread.sleep (1000);


        WebElement submittBtn =  browserWindow.findElement (By.id ("submitButton"));
        assertTrue (submittBtn.isDisplayed ());
        Thread.sleep (1000);

        browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(2) > div > a > button")).click ();

        Thread.sleep(2000);
        browserWindow.quit();
    }




//TODO check this later for caption
    @Test(
            groups = {"Sanity","Photo_Wall", "noDataProvider"},
            dataProvider = "LG3_Input",
            priority = 72,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PW3_Valid_Use(String email, String password) throws InterruptedException, IOException{
        //Remove inputs, you dont need them
        //Browser setup
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();

        //Website
        browserWindow.get(TestFunctions.website);
        Thread.sleep(2500);
        browserWindow.findElement(By.cssSelector("#mat-dialog-0 > app-welcome-banner > div > div:nth-child(3) > button.mat-focus-indicator.close-dialog.mat-raised-button.mat-button-base.mat-primary.ng-star-inserted > span.mat-button-wrapper")).click();
        Thread.sleep(300);

        // Login
        loginForMe (browserWindow,email,password);
        Thread.sleep(1500);

        //PW_002 test case: Verify captions show up when you hover mouse over photos
        WebElement sideBarMenu = browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)"));
        sideBarMenu.click ();
        Thread.sleep(300);


        WebElement photoWall = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(12) > div > span"));

        photoWall.click ();


        Thread.sleep(1000);
        WebElement picCaption = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(4) > div > div"));
        Thread.sleep (1500);

        Actions hoverMouse = new Actions (browserWindow);


        WebElement picture = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(4) > img"));
        Thread.sleep(1000);
        hoverMouse.moveToElement (picture).perform ();
        Thread.sleep(2000);

        assertEquals (picCaption.getText (),"I love going hiking here... (© j0hNny)");


        // PW_003 test case: Verify twitter link works on photo captions


        browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(2) > div > a > button")).click ();




        ArrayList<String> newTb = new ArrayList<String>(browserWindow.getWindowHandles());
        browserWindow.switchTo().window(newTb.get(1));


        Thread.sleep(1000);
        assertEquals (browserWindow.getCurrentUrl (),"https://twitter.com/intent/tweet?text=Magn(et)ificent!%20(%C2%A9%20bkimminich)%20@owasp_juiceshop&hashtags=appsec");

        // go back to the previous window
        browserWindow.switchTo().window(newTb.get(0));
        Thread.sleep(2000);
        browserWindow.quit();
    }


    @Test(
            groups = {"Regression","Photo_Wall", "noDataProvider"},
            dataProvider = "LG3_Input",
            priority = 90,
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void PW_Regression() throws InterruptedException, IOException{

    }



    private void loginForMe(WebDriver browserWindow,  String email, String password) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(browserWindow,10);
        browserWindow.get (TestFunctions.website);
        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (titleCSS)));
        TestFunctions.navToLogin (browserWindow);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("loginButtonGoogle")));
        browserWindow.findElement(By.id ("loginButtonGoogle")).click (); //click on login
        sleep (1);


        WebElement emailUsr = browserWindow.findElement(By.cssSelector (TestFunctions.identifierID));
        sleep (1);
        Login.emailPassEnter (browserWindow, email, password, emailUsr);

        wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (titleCSS)));
    }

    private static void sleep(int a) throws InterruptedException {

        switch (a) {
            case 1:
                Thread.sleep (1000);
                break;
            case 2:
                Thread.sleep (2000);
                break;
            case 3:
                Thread.sleep (3000);
                break;
            case 4:
                Thread.sleep (4000);
                break;
            case 5:
                Thread.sleep (5000);
                break;

        }
    }


    public static void setClipboardData(String string) {
        //StringSelection is a class that can be used for copy and paste operations.
        StringSelection stringSelection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    public static void uploadFile(String fileLocation) {
        try {
            //Setting clipboard with file location
            setClipboardData(fileLocation);
            //native key strokes for CTRL, V and ENTER keys
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    // TODO PW3 and PW1

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
