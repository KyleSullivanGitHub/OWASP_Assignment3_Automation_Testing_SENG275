package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import org.openqa.selenium.*;
import org.testng.ITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.reflect.Method;

public class Integration_Tests implements ITest
{
    private final ThreadLocal<String> testName = new ThreadLocal<>(); //Thread for renaming tests in console

    TestBrowser environment;
    CreateEnvironment passBrowser;
    String catchError;
    int ordersPayIndex = 0;


    /**
     * Create an environment for all tests using the same browser app.
     * Programmer: Kyle Sullivan
     * @exception IOException Thrown if no browser is chosen for a test
     */
    @BeforeClass
    public void SetUp() throws IOException
    {
        passBrowser = new CreateEnvironment();
        environment = passBrowser.createBrowser();
    }


    /**
     * Integration test to confirm interconnected functionality of registration, login, and logout units
     * Programmer: Kyle Sullivan, With elements taken from Test Classes Registration, Login, and Logout
     * @throws InterruptedException
     */
    @Test(
            groups = {"Integration", "Registration","Login", "Logout", "noDataProvider"}
    )
    void I1_Registration_Login_Logout_Modules() throws InterruptedException
    {
        //Create Environment
        WebDriver browserWindow = environment.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Register a constant account
            catchError = "Registration";
            TestFunctions.constRandomAccount(); //fill out constant account values
            Registration signUp = new Registration();
            signUp.SetUp();
            TestFunctions.navToReg(browserWindow);
            signUp.fillOutReg(browserWindow, new Object[]{TestFunctions.constEmail, TestFunctions.constPassword, TestFunctions.constPassword, true, TestFunctions.constAnswer});
            TestFunctions.waitForSite(browserWindow,TestFunctions.regButton, true);

            Thread.sleep(1000);

            //Logs in
            catchError = "Login";
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"login");
            TestFunctions.completedRegistration();
            TestFunctions.manualLogin(browserWindow);

            Thread.sleep(1000);

            //looks at the current UI
            catchError = "Site whilst Logged In";
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"search");
            TestFunctions.waitForSite(browserWindow, TestFunctions.navPath,true); //Open account menu
            //Confirms the profile displays the correct email address
            catchError = "Profile Display";
            WebElement account = browserWindow.findElement(By.cssSelector("button.mat-menu-item:nth-child(1) > span:nth-child(2)"));
            TestFunctions.assertWebElement(account);
            assertEquals(account.getText(),TestFunctions.constEmail);

            //Logs out
            catchError = "Logout";
            TestFunctions.waitForSite(browserWindow,"#navbarLogoutButton",true);//Logout

            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath,true);
            Thread.sleep(100);
            //confirms logout
            WebElement element;
            try
            {
                //Find the logout button
                element = browserWindow.findElement(By.cssSelector("#navbarLogoutButton"));
                //If the element is present...
                assertFalse(element.isDisplayed());
            }
            catch (NoSuchElementException ignored) { assertTrue(true); }

        }
        catch (IOException | NoSuchElementException | ElementClickInterceptedException exception)
        {
            assertEquals(catchError + " had an error.", catchError + "Should have run without issues");
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }
    }


    /**
     * Integration test of main UI changes up logging in.
     * Programmer: Kyle Sullivan, with elements taken from Test Classes NavigationMenu, Product Reviews and Basket
     * @throws InterruptedException
     */
    @Test(groups = {"Integration","Login", "Navigation_Menu","Review", "noDataProvider"}
    )
    void I2_Login_and_UI_Changes_Modules() throws InterruptedException
    {
        //Create Environment
        String authorNameXPath = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-contact/div/mat-card/div/mat-form-field[1]/div/div[1]/div[3]/input";
        WebDriver browserWindow = environment.makeDriver();
        JavascriptExecutor htmlEdit = (JavascriptExecutor) browserWindow;
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Login
            catchError = "Login";
            TestFunctions.login(browserWindow);

            Thread.sleep(1000);

            catchError = "Complaint";
            //Check Complaint
            TestFunctions.waitForSiteXpath(browserWindow,NavigationMenu.xPathNavMenu,true);
            TestFunctions.waitForSiteXpath(browserWindow, NavigationMenu.xPathNavMenuCommon + 2 + NavigationMenu.xPathNavMenuEnd,true);

            catchError = "Support Chat";
            //Check Support Chat
            TestFunctions.waitForSiteXpath(browserWindow,NavigationMenu.xPathNavMenu,true);
            TestFunctions.waitForSiteXpath(browserWindow, NavigationMenu.xPathNavMenuCommon + 3 + NavigationMenu.xPathNavMenuEnd,true);

            catchError = "Deluxe Membership";
            //Check Deluxe Membership
            TestFunctions.waitForSiteXpath(browserWindow,NavigationMenu.xPathNavMenu,true);
            TestFunctions.waitForSiteXpath(browserWindow, NavigationMenu.xPathNavMenuCommon + 6 + NavigationMenu.xPathNavMenuEnd,true);


            //check Account menu
            catchError = "Account Menu";
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath,true);

            catchError = "Personal Profile";
            //check the account element of the account menu
            WebElement account = browserWindow.findElement(By.cssSelector("button.mat-menu-item:nth-child(1) > span:nth-child(2)"));
            assertEquals(account.getText(),TestFunctions.menuOption[0]);
            account.click();
            Thread.sleep(500);
            assertEquals(browserWindow.getCurrentUrl(),"https://juice-shop.herokuapp.com/profile");
            browserWindow.get(TestFunctions.website);

            String[][] URLarray = new String[][]
                    {
                            {"button.ng-tns-c245-5:nth-child(1) > span:nth-child(2)","order-history"},
                            {"button.ng-tns-c245-5:nth-child(2) > span:nth-child(2)","recycle"},
                            {"button.ng-tns-c245-5:nth-child(4) > span:nth-child(2)","address/saved"},
                            {"button.mat-menu-item:nth-child(5) > span:nth-child(2)","saved-payment-methods"},
                            {"button.ng-tns-c245-5:nth-child(6) > span:nth-child(2)","wallet"},
                            {"button.ng-tns-c245-4:nth-child(1) > span:nth-child(2)","privacy-security/privacy-policy"},
                            {"button.ng-tns-c245-4:nth-child(2) > span:nth-child(2)","privacy-security/data-export"},
                            {"button.ng-tns-c245-4:nth-child(3) > span:nth-child(2)","dataerasure"},
                            {"button.mat-menu-item:nth-child(5) > span:nth-child(2)","privacy-security/change-password"},
                            {"button.mat-focus-indicator:nth-child(6) > span:nth-child(2)","privacy-security/two-factor-authentication"},
                            {"button.mat-menu-item:nth-child(7) > span:nth-child(2)","privacy-security/last-login-ip"}
                    };

            SoftAssert softAssert = new SoftAssert();

            //check the sub menu options
            catchError = "Sub Menu Options";
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath,true);
            for(int i = 0; i <= 10; i++)
            {
                //if its the first half of the list, open orders and payment. if its the second half, open privacy and security
                catchError = URLarray[i][1];
                if(i <=4)
                    browserWindow.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(2)")).click();
                else if(i > 4)
                    browserWindow.findElement(By.cssSelector("button.mat-menu-trigger:nth-child(3)")).click();
                TestFunctions.waitForSite(browserWindow,URLarray[i][0],true);
                Thread.sleep(500);
                if(i != 7)
                    softAssert.assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+URLarray[i][1]);
                else
                    softAssert.assertEquals(browserWindow.getCurrentUrl(), "https://juice-shop.herokuapp.com/dataerasure");//data errasure has some weird URL stuff

                //check to see if you can return to the homepage, if not, fail an assertion and forcibaly return
                for(int timer = 0; timer < 20; timer++)
                {
                    try
                    {
                        browserWindow.findElement(By.cssSelector(TestFunctions.navPath)).click();
                        break;
                    }
                    catch (NoSuchElementException exception)
                    {
                        if (timer == 9)
                        {
                            assertEquals("No Nav Bar present. UI failure on " + URLarray[i][1], "Nav bar found");
                            browserWindow.get(TestFunctions.website);
                            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath,true);
                        }
                        Thread.sleep(500);
                    }
                }
            }

            catchError = "Logout Button";
            //check the Logout element of the account Menu
            WebElement Logout = browserWindow.findElement(By.cssSelector("#navbarLogoutButton > span:nth-child(2)"));
            TestFunctions.assertWebElement(Logout);
            assertEquals(Logout.getText(),TestFunctions.menuOption[3]);

            browserWindow.get(TestFunctions.website);


            catchError = "Basket";
            //Assert basket UI is present
            WebElement basket = browserWindow.findElement(By.xpath(TestFunctions.basketXpath));
            TestFunctions.assertWebElement(basket);
            WebElement basketIcon = browserWindow.findElement(By.xpath(Basket.basketIcon_XPath));
            TestFunctions.assertWebElement(basketIcon);
            WebElement basketCounter = browserWindow.findElement(By.xpath(Basket.basketIconQuantity_XPath));
            TestFunctions.assertWebElement(basketCounter);

            catchError = "Add to Basket";
            //add to basket
            browserWindow.get(TestFunctions.website);
            WebElement addToCart = browserWindow.findElement(By.xpath(Basket.addToCart_XPath));
            TestFunctions.assertWebElement(addToCart);

            catchError = "Product Review";
            //review
            TestFunctions.waitForSiteXpath(browserWindow, ProductReviews.listElement,true);
            //confirm the expanded view is on display
            TestFunctions.waitForSiteXpath(browserWindow, "//*[@id=\"mat-dialog-0\"]");
            WebElement reviewBox = browserWindow.findElement(By.xpath("//*[@id=\"mat-dialog-0\"]"));
            TestFunctions.assertWebElement(reviewBox);
        }
        catch (NoSuchElementException | ElementClickInterceptedException | InterruptedException exception)
        {
            assertEquals(catchError + " had an error.", catchError + " Should have run without issues");
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }



    /**
     * Full Integration test of shopping units, particularly the cart, checkout and order history units.
     * Programmers: Kyle Sullivan, Nicole Makarowski. Elements taken from Basket, Checkout, and OrderHistory.
     * @throws InterruptedException Thrown if the Test is interrupted during a thread waiting period.
     */
    @Test(groups = {"Integration", "noDataProvider"})
    void I3_Shopping_Modules() throws InterruptedException
    {
        //Create Environment
        WebDriver browserWindow = environment.makeDriver();
        JavascriptExecutor htmlEdit = (JavascriptExecutor) browserWindow;
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Login
            catchError = "Login";
            TestFunctions.login(browserWindow);
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);

            //fillout orders until the end of the address section
            orderUpToAddress(browserWindow);
            //confrim the address section
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"card\"]/app-address/mat-card/button",true);
            Thread.sleep(500);
            //complete the orders up to the payment section
            completeOrderUpToPayment(browserWindow);

            //select the first payment option. if none are avaliable, make a saved payment
            catchError = "Purchase methods";
            try
            {
                TestFunctions.findRadioButton(browserWindow,"mat-radio-",30,60).click();
            }
            catch (NullPointerException addPayment)
            {
                //Add payment info
                TestFunctions.fillOutPayment(browserWindow);
                Thread.sleep(500);
                TestFunctions.findRadioButton(browserWindow,"mat-radio-",30,60).click();
            }

            //fill out the rest of the order
            completeOrderFromPayment(browserWindow);


            catchError = "Order Details";
            //Place order
            Thread.sleep(1000);
            //Validate order
            assertEquals(browserWindow.findElement(By.xpath(Checkout.checkoutProductName_XPath)).getText(), "Apple Juice (1000ml)"); //Product Name
            assertEquals(browserWindow.findElement(By.xpath(Checkout.checkoutProductPrice_XPath)).getText(), "1.99¤"); //Price
            assertEquals(browserWindow.findElement(By.xpath(Checkout.checkoutProductQuantity_XPath)).getText(), "1"); //Quantity
            assertEquals(browserWindow.findElement(By.xpath(Checkout.checkoutTotalPrice_XPath)).getText(), "1.99¤"); //Total Price

            browserWindow.get(TestFunctions.website);
            Thread.sleep(5000);
            catchError = "Order History";
            //click on account menu
            browserWindow.findElement(By.cssSelector(TestFunctions.navPath)).click();
            //click on orders and payments
            TestFunctions.waitForSite(browserWindow, "button.mat-menu-trigger:nth-child(2)", true);
            //click on order history
            TestFunctions.waitForSite(browserWindow, "button.ng-tns-c245-5:nth-child(1) > span:nth-child(2)", true);
            Thread.sleep(1500);
            //check all options in order history are correct
            int temp = 1;
            while(true)
            {
                try
                {
                    browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div["+temp+"]/div/mat-table/mat-row/mat-cell[1]"));
                    temp++;
                }
                catch (NoSuchElementException breakLoop)
                {
                    break;
                }
            }
            temp--;
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div["+temp+"]/div/mat-table/mat-row/mat-cell[1]")).getText(), "Apple Juice (1000ml)"); //Product Name
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div["+temp+"]/div/mat-table/mat-row/mat-cell[2]")).getText(), "1.99¤"); //Price
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div["+temp+"]/div/mat-table/mat-row/mat-cell[3]/span")).getText(), "1"); //Quantity
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div["+temp+"]/div/mat-table/mat-row/mat-cell[4]")).getText(), "1.99¤"); //Total Price
            assertEquals(browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-order-history/mat-card/div/div/div["+temp+"]/div/div/div[1]/div[2]/div[2]")).getText(), "2.98¤"); //Total Price
        }
        catch (NoSuchElementException | ElementClickInterceptedException | InterruptedException | IOException exception)
        {
            assertEquals(catchError + " had an error.", catchError + " Should have run without issues");
        }
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    /**
     * Perfrom a full integration test of the saved values units of the site, including saved addresses, saved payments and the digital wallet, and test them in the payment unit of the site
     * Programmers: Kyle Sullivan, with elements taken from Basket, Checkout, AddressAlteration, Digital Wallet,
     * @throws InterruptedException Thrown if the test is interrupted during a thread waiting period
     * @throws IOException Thrown if the test is not given a browser to test with.
     */
    @Test(groups = {"Integration", "noDataProvider"})
    void I4_Saved_Account_Details_Modules() throws InterruptedException, IOException
    {
        //Create Environment
        WebDriver browserWindow = environment.makeDriver();
        JavascriptExecutor htmlEdit = (JavascriptExecutor) browserWindow;
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Login
            catchError = "Login";
            TestFunctions.login(browserWindow);
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);

            //navigate to saved address
            catchError = "Saved Addresses";
            TestFunctions.navToSavedAddresses(browserWindow);
            while(true)
            {
                //clear all saved addresses currently present
                Thread.sleep(1000);
                try{ browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-saved-address/div/app-address/mat-card/mat-table/mat-row[1]/mat-cell[5]/button")).click();}
                catch(NoSuchElementException breakloop) { break;}
            }
            //add a new saved address
            try {
                browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/" +
                        "app-saved-address/div/app-address/mat-card/div/button")).click();
            } catch (NoSuchElementException ignored){}
            TestFunctions.fillOutAddress(browserWindow);

            //navigate to saved payments
            catchError = "Saved payments";
            TestFunctions.navToSavedPayment(browserWindow);
            while(true)
            {
                //clear all currently saved payment options
                Thread.sleep(1000);
                try{ browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-saved-payment-methods/mat-card/app-payment-method/div/div[1]/mat-table/mat-row/mat-cell[5]/button")).click();}
                catch(NoSuchElementException breakloop) { break;}
                Thread.sleep(500);
            }
            TestFunctions.fillOutPayment(browserWindow);


            //Navigate to Account
            browserWindow.findElement(By.cssSelector(TestFunctions.navPath)).click();
            TestFunctions.waitForSite(browserWindow, "button.mat-menu-trigger:nth-child(2)", true);
            TestFunctions.waitForSite(browserWindow, "button.ng-tns-c245-5:nth-child(6) > span:nth-child(2)", true);


            catchError = "Wallet";

            //Add 100$ to Digital Wallet
            TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-wallet/mat-card/mat-form-field/div/div[1]/div[3]",true);
            Thread.sleep(3000);
            TestFunctions.findRadioButton(browserWindow,"mat-input-",1,60).sendKeys("100");

            //press continue
            browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
            Thread.sleep(1000);

            TestFunctions.findRadioButton(browserWindow,"mat-radio-",30,60).click();
            Thread.sleep(500);
            //Select Continue on card page
            TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div/button[2]",true);
            Thread.sleep(500);
            assertEquals(browserWindow.findElement(By.xpath("/html/body/div[3]")).getText(), "Wallet successfully charged.\nX");

            browserWindow.get(TestFunctions.website);
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);


            catchError = "Saved Addresses Checkout";
            orderUpToAddress(browserWindow);
            //Test Saved Address
            String addressValues = "/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-address-select/div/app-address/mat-card/mat-table/mat-row[1]/mat-cell[";
            //confirm that the correct saved address details are carried over.
            assertEquals(browserWindow.findElement(By.xpath(addressValues+"2]")).getText(),TestFunctions.addressSet[2]);
            assertEquals(browserWindow.findElement(By.xpath(addressValues+"3]")).getText(),TestFunctions.addressSet[5]+", "+TestFunctions.addressSet[6]+", "+TestFunctions.addressSet[7]+", "+TestFunctions.addressSet[4]);
            assertEquals(browserWindow.findElement(By.xpath(addressValues+"4]")).getText(),TestFunctions.addressSet[1]);
            //complete the address portion.
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"card\"]/app-address/mat-card/button",true);
            completeOrderUpToPayment(browserWindow);

            //pay using the card we saved
            catchError = "Purchase methods card";
            TestFunctions.findRadioButton(browserWindow,"mat-radio-",30,60).click();
            Thread.sleep(500);
            //complete the order
            completeOrderFromPayment(browserWindow);

            //restart the order process, and pay using the digital wallet
            Thread.sleep(1000);
            browserWindow.get(TestFunctions.website);
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);

            catchError = "Saved Addresses Checkout";
            orderUpToAddress(browserWindow);
            //Test Saved Address
            assertEquals(browserWindow.findElement(By.xpath(addressValues+"2]")).getText(),TestFunctions.addressSet[2]);
            assertEquals(browserWindow.findElement(By.xpath(addressValues+"3]")).getText(),TestFunctions.addressSet[5]+", "+TestFunctions.addressSet[6]+", "+TestFunctions.addressSet[7]+", "+TestFunctions.addressSet[4]);
            assertEquals(browserWindow.findElement(By.xpath(addressValues+"4]")).getText(),TestFunctions.addressSet[1]);
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"card\"]/app-address/mat-card/button",true);
            completeOrderUpToPayment(browserWindow);

            //pay using the wallet
            catchError = "Purchase methods Wallet";
            TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[1]/div/div[3]/button",true);
            Thread.sleep(500);

            //checkout
            TestFunctions.waitForSite(browserWindow,"#checkoutButton",true);

        }
        catch (NoSuchElementException | ElementClickInterceptedException | InterruptedException | IOException exception)
        {
            assertEquals(catchError + " had an error.", catchError + " Should have run without issues");
        }

        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    /**
     * Perform an integration test on the digital wallet and the product limit units of the site, testing that we can order more than 1 of a normally limited product.
     * Programmers: Kyle Sullivan.
     * @throws InterruptedException Thrown if the test is interrupted during a thread waiting period
     */
    @Test(groups = {"Integration","noDataProvider"})
    void I5_Deluxe_MemberShip_Basket_Checkout_Modules() throws InterruptedException, NoSuchElementException, ElementClickInterceptedException, IOException
    {
        //Create the Environment
        WebDriver browserWindow = environment.makeDriver();
        JavascriptExecutor htmlEdit = (JavascriptExecutor) browserWindow;
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try
        {
            //Login
            catchError = "Login";
            TestFunctions.login(browserWindow);
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);

            //if not a deluxe member, sign up
            catchError = "Deluxe Membership";
            TestFunctions.waitForSiteXpath(browserWindow,NavigationMenu.xPathNavMenu,true);
            TestFunctions.waitForSiteXpath(browserWindow, NavigationMenu.xPathNavMenuCommon + 6 + NavigationMenu.xPathNavMenuEnd,true);
            Thread.sleep(3000);
            //if the header states we are a deluxe member, move on, otherwise sign up
            try
            {
                browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-deluxe-user > div > div.heading.mat-elevation-z6.ng-star-inserted > div > p"));
            }catch (NoSuchElementException ignore){TestFunctions.waitForSite(browserWindow, ".btn-member", true);
                TestFunctions.waitForSite(browserWindow, "button.mat-raised-button:nth-child(1)", true);}

            //return to the main page
            browserWindow.get(TestFunctions.website);
            TestFunctions.waitForSite(browserWindow,TestFunctions.navPath);

            //Check if basket is empty. If not, clear it.
            catchError = "Clear basket";
            try
            {
                if (!browserWindow.findElement(By.className("fa-layers-counter")).getText().equals("0"))
                {
                    TestFunctions.waitForSiteXpath(browserWindow, Basket.basketIcon_XPath, true);
                    TestFunctions.waitForSiteXpath(browserWindow, Basket.removeProduct_XPath, true);
                    browserWindow.get(TestFunctions.website);
                }
            }catch (NoSuchElementException | InterruptedException ignore){}

            //Add product to basket and move into checkout
            catchError = "Add to Cart";
            //Add to cart
            for(int i = 0; i <=10; i++)
                TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-search-result/div/div/div[2]/mat-grid-list/div/mat-grid-tile[1]/figure/mat-card/div[2]/button",true);
            Thread.sleep(1000);

            //Navigate to Basket
            TestFunctions.waitForSiteXpath(browserWindow,Basket.basketIcon_XPath,true);
            assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"basket");
            Thread.sleep(2000);

            //navigate to the saved addresses portion of checkout
            catchError = "Saved Addresses";
            TestFunctions.waitForSiteXpath(browserWindow,Checkout.checkoutButton_XPath,true);
            TestFunctions.waitForSiteXpath(browserWindow, Checkout.addSavedAddress_XPath);

            //Select saved Address
            try
            {
                Thread.sleep(500);
                TestFunctions.findRadioButton(browserWindow, "mat-radio-", 1, 60).click();
            }
            catch (NullPointerException | IOException createAddress)
            {
                TestFunctions.waitForSiteXpath(browserWindow, Checkout.addSavedAddress_XPath, true);
                TestFunctions.fillOutAddress(browserWindow);
                TestFunctions.waitForSiteXpath(browserWindow, Checkout.addSavedAddress_XPath);
                TestFunctions.findRadioButton(browserWindow, "mat-radio-", 1, 60).click();
            }

            //confirm the address choice
            catchError = "Saved Addresses Checkout";
            TestFunctions.waitForSiteXpath(browserWindow,"//*[@id=\"card\"]/app-address/mat-card/button",true);
            completeOrderUpToPayment(browserWindow);

            catchError = "Purchase methods card";
            TestFunctions.findRadioButton(browserWindow,"mat-radio-",1,60).click();
            Thread.sleep(500);

            completeOrderFromPayment(browserWindow);
        }
/*
        catch (NoSuchElementException | ElementClickInterceptedException | InterruptedException | IOException exception)
        {
            assertEquals(catchError + " had an error.", catchError + " Should have run without issues");
        }

 */
        finally
        {
            Thread.sleep(TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    /**
     * completes the checkout from emptying the basket, adding a product up to filling out the saved address fields
     * Programmer: Kyle Sullivan, Nicole
     * @param browserWindow test environment to work in
     * @throws InterruptedException
     * @throws IOException
     */
    private void orderUpToAddress(WebDriver browserWindow) throws InterruptedException, IOException
    {
        //Check if basket is empty. If not, clear it.
        catchError = "Clear basket";
        try
        {
            if (!browserWindow.findElement(By.className("fa-layers-counter")).getText().equals("0"))
            {
                //remove all basket entries
                TestFunctions.waitForSiteXpath(browserWindow, Basket.basketIcon_XPath, true);
                TestFunctions.waitForSiteXpath(browserWindow, Basket.removeProduct_XPath, true);
                browserWindow.get(TestFunctions.website);
            }
        }catch (NoSuchElementException | InterruptedException ignore){}

        //Add product to basket and move into checkout

        catchError = "Add to Cart";
        //Add to cart
        TestFunctions.waitForSiteXpath(browserWindow,Basket.addToCart_XPath,true);
        Thread.sleep(1000);
        //Navigate to Basket
        TestFunctions.waitForSiteXpath(browserWindow,Basket.basketIcon_XPath,true);
        assertEquals(browserWindow.getCurrentUrl(),TestFunctions.website+"basket");
        Thread.sleep(500);

        catchError = "Saved Addresses";
        TestFunctions.waitForSiteXpath(browserWindow,Checkout.checkoutButton_XPath,true);
        TestFunctions.waitForSiteXpath(browserWindow, Checkout.addSavedAddress_XPath);

        //Select saved Address
        try
        {
            Thread.sleep(500);
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 1, 60).click();
        }
        catch (NullPointerException createAddress)
        {
            TestFunctions.waitForSiteXpath(browserWindow, Checkout.addSavedAddress_XPath, true);
            TestFunctions.fillOutAddress(browserWindow);
            TestFunctions.waitForSiteXpath(browserWindow, Checkout.addSavedAddress_XPath);
            TestFunctions.findRadioButton(browserWindow, "mat-radio-", 1, 60).click();
        }


    }

    private void completeOrderUpToPayment(WebDriver browserWindow) throws IOException, InterruptedException
    {
        catchError = "Shipping Methods";
        Thread.sleep(3000);
        //Add Shipping Method
        TestFunctions.findRadioButton(browserWindow, "mat-radio-", 1, 60).click();
        TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-delivery-method/mat-card/div[4]/button[2]",true);
    }

    private void completeOrderFromPayment(WebDriver browserWindow) throws InterruptedException
    {
        TestFunctions.waitForSiteXpath(browserWindow,"/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[2]/button[2]",true);
        //Submit
        TestFunctions.waitForSite(browserWindow,"#checkoutButton",true);
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
