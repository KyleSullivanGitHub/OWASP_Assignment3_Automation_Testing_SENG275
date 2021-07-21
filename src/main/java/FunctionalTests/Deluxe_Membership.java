//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package FunctionalTests;

import Setup.CreateEnvironment;
import Setup.TestBrowser;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Deluxe_Membership {
    private final ThreadLocal<String> testName = new ThreadLocal();
    String website = "https://juice-shop.herokuapp.com";
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();
    WebDriver browserWindow;

    public Deluxe_Membership() {
    }

    @BeforeClass
    public void SetUp() throws IOException {
        this.environment = this.passBrowser.createBrowser();
    }

    @AfterTest
    public void tearDown() {
        this.browserWindow.quit();
    }

    @Test(
            groups = {"Smoke", "Deluxe_Membership Smoke", "Deluxe_Membership", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void DL1_Valid_Deluxe(String chosenBrowser) throws IOException, InterruptedException {
        TestBrowser browser = this.passBrowser.createBrowser(chosenBrowser);
        this.browserWindow = browser.makeDriver();
        this.browserWindow.manage().window().maximize();
        this.browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(this.browserWindow);

        try {
            TestFunctions.login(this.browserWindow);
            Thread.sleep(1000L);
            this.browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300L);
            this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div")).click();
            Thread.sleep(500L);
            Assert.assertEquals(this.browserWindow.getCurrentUrl(), this.website + "/#/deluxe-membership");
            Thread.sleep(500L);
            this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/mat-card/div[2]/div[3]/div[2]/button")).click();
            Thread.sleep(500L);
            Assert.assertEquals(this.browserWindow.getCurrentUrl(), this.website + "/#/payment/deluxe");
        } finally {
            Thread.sleep((long)TestFunctions.endTestWait);
            this.browserWindow.quit();
        }

    }

    @Test(
            groups = {"Smoke", "Deluxe_Membership Smoke", "Deluxe_Membership", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void DL2_Invalid(String chosenBrowser) throws IOException, InterruptedException {
        TestBrowser browser = this.passBrowser.createBrowser(chosenBrowser);
        this.browserWindow = browser.makeDriver();
        this.browserWindow.manage().window().maximize();
        this.browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(this.browserWindow);

        try {
            this.browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
            Thread.sleep(300L);
            Assert.assertTrue(this.browserWindow.findElements(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div")).isEmpty());
        } finally {
            Thread.sleep((long)TestFunctions.endTestWait);
            this.browserWindow.quit();
        }

    }

    @Test(
            groups = {"Smoke", "Deluxe_Membership Smoke", "Deluxe_Membership", "hasDataProvider"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void DL3_Usability(String chosenBrowser) throws IOException, InterruptedException {
        TestBrowser browser = this.passBrowser.createBrowser(chosenBrowser);
        this.browserWindow = browser.makeDriver();
        this.browserWindow.manage().window().maximize();
        this.browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(this.browserWindow);
        Thread.sleep((long)TestFunctions.endTestWait);
        this.browserWindow.quit();
    }

    @Test(
            groups = {"Sanity", "deluxe_membership Sanity", "deluxe_membership"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void DL4_NewCard(String chosenBrowser) throws InterruptedException, IOException {
        TestBrowser browser = this.passBrowser.createBrowser(chosenBrowser);
        this.browserWindow = browser.makeDriver();
        this.browserWindow.manage().window().maximize();
        this.browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(this.browserWindow);

        try {
            this.navToDeluxe(this.browserWindow);
            this.browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-0\"]")).click();
            Thread.sleep(1000L);
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-0\"]/div/div/mat-form-field[1]/div/div[1]/div[3]")).getText(), "Name");
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-0\"]/div/div/mat-form-field[2]/div/div[1]")).getText(), "Card Number");
            this.addSavedPayment(this.browserWindow);
            WebElement button = this.browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]"));
            Assert.assertTrue(button.isEnabled());
            button.click();
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("/html/body/div[3]")).getText(), "Your card ending with 4444 has been saved for your convenience.\nX");
        } finally {
            Thread.sleep((long)TestFunctions.endTestWait);
            this.browserWindow.quit();
        }

    }

    @Test(
            groups = {"Sanity", "deluxe_membership Sanity", "deluxe_membership"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void DL5_DigitalWallet(String chosenBrowser) throws InterruptedException, IOException {
        TestBrowser browser = this.passBrowser.createBrowser(chosenBrowser);
        this.browserWindow = browser.makeDriver();
        this.browserWindow.manage().window().maximize();
        this.browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(this.browserWindow);

        try {
            this.navToDeluxe(this.browserWindow);
            String amount = this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[1]/div/div[2]/b/span[2]")).getText();
            double number = 0.0D;

            try {
                number = Double.parseDouble(amount);
            } catch (NumberFormatException var11) {
                var11.printStackTrace();
            }

            if (number < 49.0D) {
                this.browserWindow.findElement(By.xpath("//*[@id=\"navbarAccount\"]")).click();
                Thread.sleep(300L);
                this.browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-0\"]/div/button[2]")).click();
                Thread.sleep(300L);
                this.browserWindow.findElement(By.xpath("//*[@id=\"mat-menu-panel-3\"]/div/button[5]")).click();
                Thread.sleep(500L);
                this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-wallet/mat-card/mat-form-field/div/div[1]/div[3]")).click();
                this.browserWindow.findElement(By.xpath("//*[@id=\"mat-input-6\"]")).sendKeys(new CharSequence[]{"49"});
                this.browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
                if (this.browserWindow.findElements(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/app-payment-method/div/div[1]")).isEmpty()) {
                    this.browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-3\"]")).click();
                    this.browserWindow.findElement(By.xpath("//*[@id=\"mat-input-7\"]")).sendKeys(new CharSequence[]{"Hello World"});
                    this.browserWindow.findElement(By.xpath("//*[@id=\"mat-input-8\"]")).sendKeys(new CharSequence[]{"1111222233334444"});
                    Select monthSelector = new Select(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-input-9\"]")));
                    monthSelector.selectByVisibleText("1");
                    Select yearSelector = new Select(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-input-10\"]")));
                    yearSelector.selectByVisibleText("2080");
                    this.browserWindow.findElement(By.xpath("//*[@id=\"submitButton\"]")).click();
                }

                this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div/button[2]")).click();
                Thread.sleep(500L);
                this.browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-39\"]")).click();
                this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div/button[2]")).click();
                this.navToDeluxe(this.browserWindow);
            }

            this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[1]/div/div[3]/button")).click();
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/div[1]/div/p")).getText(), "You are already a deluxe member!");
        } finally {
            Thread.sleep((long)TestFunctions.endTestWait);
            this.browserWindow.quit();
        }

    }

    @Test(
            groups = {"Sanity", "deluxe_membership Sanity", "deluxe_membership"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void DL6_Coupon(String chosenBrowser) throws InterruptedException, IOException {
        TestBrowser browser = this.passBrowser.createBrowser(chosenBrowser);
        this.browserWindow = browser.makeDriver();
        this.browserWindow.manage().window().maximize();
        this.browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(this.browserWindow);

        try {
            this.navToDeluxe(this.browserWindow);
            this.browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-5\"]")).click();
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-11\"]/mat-label")).getText(), "Coupon");
            WebElement redeem = this.browserWindow.findElement(By.xpath("//*[@id=\"applyCouponButton\"]"));
            Assert.assertTrue(redeem.isDisplayed());
            WebElement coupon = this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-4\"]/div/mat-form-field/div"));
            coupon.sendKeys(new CharSequence[]{"n(XLuf!Cdm"});
            Assert.assertTrue(redeem.isEnabled());
            this.browserWindow.findElement(By.xpath("//*[@id=\"applyCouponButton\"]")).click();
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-4\"]/div/div")).getText(), "Your discount of 20% will be applied during checkout.");
        } finally {
            Thread.sleep((long)TestFunctions.endTestWait);
            this.browserWindow.quit();
        }

    }

    @Test(
            groups = {"Sanity", "deluxe_membership Sanity", "deluxe_membership"},
            priority = 0,
            dataProvider = "browserSwitch",
            dataProviderClass = Test_Data.class,
            enabled = true
    )
    public void DL7_Other(String chosenBrowser) throws InterruptedException, IOException {
        TestBrowser browser = this.passBrowser.createBrowser(chosenBrowser);
        this.browserWindow = browser.makeDriver();
        this.browserWindow.manage().window().maximize();
        this.browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(this.browserWindow);

        try {
            this.navToDeluxe(this.browserWindow);
            this.browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-2\"]")).click();
            Thread.sleep(200L);
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[1]/div[1]/label")).getText(), "Donations");
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("///*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[1]/label")).getText(), "Merchandise");
            this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[1]/div[2]/a/button")).click();
            Assert.assertEquals(this.browserWindow.getCurrentUrl(), "https://pwning.owasp-juice.shop/part3/donations.html");
            this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[2]/a[1]/button")).click();
            Assert.assertEquals(this.browserWindow.getCurrentUrl(), "https://shop.spreadshirt.com/juiceshop");
            this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[2]/a[2]/button")).click();
            Assert.assertEquals(this.browserWindow.getCurrentUrl(), "https://shop.spreadshirt.de/juiceshop");
            this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[2]/a[3]/button")).click();
            Assert.assertEquals(this.browserWindow.getCurrentUrl(), "https://www.stickeryou.com/products/owasp-juice-shop/794");
            this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-2\"]/div/div/div[2]/div[2]/a[4]/button")).click();
            Assert.assertEquals(this.browserWindow.getCurrentUrl(), "https://leanpub.com/juice-shop");
        } finally {
            Thread.sleep((long)TestFunctions.endTestWait);
            this.browserWindow.quit();
        }

    }

    @Test(
            groups = {"Regression", "Deluxe_Membership Regression", "Deluxe_Membership", "noDataProvider"},
            priority = 0,
            enabled = true
    )
    public void DL_Regression(String chosenBrowser, Object[] dataSet) throws InterruptedException, IOException {
        TestBrowser browser = this.passBrowser.createBrowser(chosenBrowser);
        WebDriver browserWindow = browser.makeDriver();
        browserWindow.manage().window().maximize();
        browserWindow.get(TestFunctions.website);
        TestFunctions.waitForSite(browserWindow);

        try {
            Login.fillOutLogGoogle(browserWindow, dataSet[0].toString(), dataSet[1].toString());
            Thread.sleep(300L);
        } finally {
            Thread.sleep((long)TestFunctions.endTestWait);
            browserWindow.quit();
        }

    }

    private void navToDeluxe(WebDriver browserWindow) throws InterruptedException {
        TestFunctions.login(browserWindow);
        Thread.sleep(1000L);
        browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
        Thread.sleep(300L);
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/a[6]/div")).click();
        Thread.sleep(500L);
        Assert.assertEquals(browserWindow.getCurrentUrl(), this.website + "/#/deluxe-membership");
        Thread.sleep(500L);
        browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/mat-card/div[2]/div[3]/div[2]/button")).click();
    }

    void addSavedPayment(WebDriver browserWindow) {
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-1\"]")).sendKeys(new CharSequence[]{"Hello World"});
        browserWindow.findElement(By.xpath("//*[@id=\"mat-input-2\"]")).sendKeys(new CharSequence[]{"1111222233334444"});
        Select monthSelector = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-3\"]")));
        monthSelector.selectByVisibleText("1");
        Select yearSelector = new Select(browserWindow.findElement(By.xpath("//*[@id=\"mat-input-4\"]")));
        yearSelector.selectByVisibleText("2080");
    }
}

