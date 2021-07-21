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
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class Deluxe_Membership {
    private final ThreadLocal<String> testName = new ThreadLocal();
    String website = "https://juice-shop.herokuapp.com";
    TestBrowser environment;
    CreateEnvironment passBrowser = new CreateEnvironment();
    WebDriver browserWindow;

    public Deluxe_Membership() {
    }

    @BeforeSuite
    public void SetUp() throws IOException {
        this.environment = this.passBrowser.createBrowser();
    }

    @AfterTest
    public void tearDown() {
        this.browserWindow.quit();
    }

    @Test(
            groups = {"Smoke", "Logout Smoke", "Logout", "hasDataProvider"},
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
            this.browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-0\"]")).click();
            Thread.sleep(1000L);
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-3\"]/mat-label")).getText(), "Named");
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-5\"]")).getText(), "Card Number");
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-39\"]")).getText(), "Expiry Month");
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-41\"]")).getText(), "Expiry Year");
            Select month = new Select(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-input-3\"]")));
            month.selectByValue("3");
            Thread.sleep(500L);
            Select year = new Select(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-input-10\"]")));
            year.selectByValue("2081");
            Thread.sleep(500L);
            WebElement button = this.browserWindow.findElement(By.xpath("//*[@id=\"mat-radio-39\"]/label/span[1]/span[1]"));
            Assert.assertTrue(button.isEnabled());
            button.click();
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-overlay-21\"]/snack-bar-container/div/div/simple-snack-bar/span")).getText(), "Your card ending with 2345 has been saved for your convenience.");
            String amount = this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[1]/div/div[2]/b/span[2]")).getText();
            int number = 0;

            try {
                number = Integer.parseInt(amount);
            } catch (NumberFormatException var13) {
                var13.printStackTrace();
            }

            if (number < 49) {
                this.browserWindow.findElement(By.cssSelector("body > app-root > div > mat-sidenav-container > mat-sidenav-content > app-navbar > mat-toolbar > mat-toolbar-row > button:nth-child(1)")).click();
                Thread.sleep(300L);
                this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/mat-list-item[1]/div")).click();
                Thread.sleep(300L);
                this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav/div/sidenav/mat-nav-list/div/div/a[5]/div")).click();
            }

            this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-payment/mat-card/div/div[1]/div/div[3]/button")).click();
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("/html/body/app-root/div/mat-sidenav-container/mat-sidenav-content/app-deluxe-user/div/div[1]/div/p")).getText(), "You are already a deluxe member!");
            this.browserWindow.findElement(By.xpath("//*[@id=\"mat-expansion-panel-header-5\"]")).click();
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"mat-form-field-label-11\"]/mat-label")).getText(), "Coupon");
            WebElement redeem = this.browserWindow.findElement(By.xpath("//*[@id=\"applyCouponButton\"]"));
            Assert.assertTrue(redeem.isDisplayed());
            WebElement coupon = this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-4\"]/div/mat-form-field/div"));
            coupon.sendKeys(new CharSequence[]{"n(XLuf!Cdm"});
            Assert.assertTrue(redeem.isEnabled());
            this.browserWindow.findElement(By.xpath("//*[@id=\"applyCouponButton\"]")).click();
            Assert.assertEquals(this.browserWindow.findElement(By.xpath("//*[@id=\"cdk-accordion-child-4\"]/div/div")).getText(), "Your discount of 20% will be applied during checkout.");
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
            groups = {"Smoke", "Basket Smoke", "Basket"},
            priority = 2,
            enabled = true
    )
    public void BA2_Invalid_Usage() throws InterruptedException {
        this.browserWindow = this.environment.makeDriver();
        this.browserWindow.get(this.website);
        TestFunctions.waitForSite(this.browserWindow);
        this.browserWindow.quit();
    }
}
