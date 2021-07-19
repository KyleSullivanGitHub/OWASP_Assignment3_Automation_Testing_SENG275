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
    public static final String photoWallCSS ="body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(12) > div > span";
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
     * @param chosenBrowser browser used for that test
     * @param dataSet provides email and password to Login
     */
    @Test(
            groups = {"Smoke","Photo_Wall Smoke","Valid_Photo_Wall"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void PW1_Valid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException{
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        // Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {

            // Login
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());

            // PW_001 test case: Verify 'Photo Wall' side menu link
            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")));
            WebElement sideBarMenu = browserWindow.findElement(By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)"));
            sideBarMenu.click ();


            // PW_001 test case: Verify Photo Wall link
            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(12) > div > span")));
            WebElement photoWall = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav > div > sidenav > mat-nav-list > a:nth-child(12) > div > span"));
            assertEquals (photoWall.getText (),"Photo Wall");

            // PW_004 test case: Verify 'Pick image' button works
            photoWall.click ();
            WebElement pickImageBtn = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div.ng-star-inserted > div > form > div > button"));
            assertTrue (pickImageBtn.isEnabled ());




            // PW_005 : Verify can't post picture without caption

//        pickImageBtn.click ();
//        uploadFile("/Users/seyedmehradadimi/IdeaProjects/OWASP_Assignment3_Automation_Testing_SENG275/src/main/java/FunctionalTests/uvic_logo-horizontal.jpeg");
//        Thread.sleep(2000);
//
//
//        Thread.sleep (1300);
//        pickImageBtn.sendKeys (Keys.ESCAPE);

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("mat-input-1")));
            wait.until (ExpectedConditions.elementToBeClickable (By.id ("mat-input-1")));
            WebElement captionInput = browserWindow.findElement (By.id ("mat-input-1"));
            sleep (1);
            captionInput.click ();

          wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div.ng-star-inserted > div")));
          wait.until (ExpectedConditions.elementToBeClickable (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div.ng-star-inserted > div")));
          browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div.ng-star-inserted > div")).click ();

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#mat-error-0")));
            WebElement captionErr = browserWindow.findElement (By.cssSelector ("#mat-error-0"));
            assertEquals (captionErr.getText (), "Please enter a caption");


            //PW_008 test case: Verify picture is posted with caption when 'submit' button is clicked

//            captionInput.click ();
//            captionInput.sendKeys ("UVic Logo");
//
//            WebElement submittBtn =  browserWindow.findElement (By.id ("submitButton"));
//            submittBtn.click ();
//
//
//            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(6) > div > div")));
//            WebElement postedPicture = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(6) > div > div"));
//            assertEquals (postedPicture.getText (), "UVic Logo");



        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }


    }





    /**
     *Smoke tests for Invalid use of Photo_Wall feature and posting picture
     * Includes test cases PW_006, PW_007
     *Programmer: Seyedmehrad Adimi
     * @param chosenBrowser browser used for that test
     * @param dataSet provides email and password to Login
     */
    @Test(
            groups = {"Sanity","Photo_Wall Sanity","Invalid_Photo_Wall"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void PW2_Valid_Use(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException{
        //Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        // Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {

            // Login
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());


            //PW_007 test case: Verify caption warning message is displayed when no caption is entered
            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (sideMenuCSS)));
            WebElement sideBarMenu = browserWindow.findElement(By.cssSelector (sideMenuCSS));
            sideBarMenu.click ();


            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (photoWallCSS)));
            WebElement photoWall = browserWindow.findElement (By.cssSelector (photoWallCSS));

            photoWall.click ();



            wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("mat-input-1")));
            wait.until (ExpectedConditions.elementToBeClickable (By.id ("mat-input-1")));
            WebElement captionInput = browserWindow.findElement (By.id ("mat-input-1"));
            sleep (1);
            captionInput.click ();
            captionInput.click ();

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div.ng-star-inserted > div")));
            wait.until (ExpectedConditions.elementToBeClickable (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div.ng-star-inserted > div")));
            browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div.ng-star-inserted > div")).click ();

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("#mat-error-0")));
            WebElement captionErr = browserWindow.findElement (By.cssSelector ("#mat-error-0"));
            assertEquals (captionErr.getText (), "Please enter a caption");


            // PW_006 test case: Verify can't post caption without picture


            captionInput.click ();
            captionInput.sendKeys ("Hello");


            wait.until (ExpectedConditions.visibilityOfElementLocated (By.id ("submitButton")));
            WebElement submittBtn =  browserWindow.findElement (By.id ("submitButton"));
            assertTrue (submittBtn.isDisplayed ());
            assertFalse (submittBtn.isEnabled ());

            //browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(2) > div > a > button")).click ();

        }finally {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }




//TODO check this later for caption
    @Test(
            groups = {"Sanity","Photo_Wall Sanity","Invalid_Photo_Wall", "Tweeter_Link", "Caption_Check"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void PW3_Valid_Use(String chosenBrowser,Object[] dataSet) throws InterruptedException, IOException{
        ///Create driver and browser for this particular test
        TestBrowser browser = passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();

        // Website setup
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);
        WebDriverWait wait = new WebDriverWait(browserWindow,10);

        try {

            // Login
            loginForMe (browserWindow,dataSet[0].toString (),dataSet[1].toString ());


            //PW_002 test case: Verify captions show up when you hover mouse over photos
            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (sideMenuCSS)));
            WebElement sideBarMenu = browserWindow.findElement(By.cssSelector (sideMenuCSS));
            sideBarMenu.click ();


            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector (photoWallCSS)));
            WebElement photoWall = browserWindow.findElement (By.cssSelector (photoWallCSS));

            photoWall.click ();



            Actions hoverMouse = new Actions (browserWindow);

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(4) > img")));
            WebElement picture = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(4) > img"));
            Thread.sleep(1000);
            hoverMouse.moveToElement (picture).perform ();
            Thread.sleep(2000);

            wait.until (ExpectedConditions.visibilityOfElementLocated (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(4) > div > div")));
            WebElement picCaption = browserWindow.findElement (By.cssSelector ("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-photo-wall > mat-card > div:nth-child(2) > div > span:nth-child(4) > div > div"));
            Thread.sleep (1500);

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
        }finally {
            Thread.sleep (TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }


    @Test(
            groups = {"Regression","Photo_Wall Regression", "Caption_Check"},
            dataProvider = "LG3_Input",
            priority = 1,
            dataProviderClass = Test_Data.class,
            threadPoolSize = 3,
            enabled = true
    )
    public void PW_Regression(String chosenBrowser, String email, String password) throws InterruptedException, IOException{

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
