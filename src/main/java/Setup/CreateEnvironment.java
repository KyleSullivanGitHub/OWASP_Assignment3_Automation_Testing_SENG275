package Setup;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.IOException;

public class CreateEnvironment
{
    private String primaryBrowser = TestBrowser.getPrimaryBrowser();

    public TestBrowser createBrowser() throws IOException
    {
        return getTestBrowser(primaryBrowser);
    }

    public TestBrowser createBrowser(String chosenBrowser) throws IOException
    {
        return getTestBrowser(chosenBrowser);
    }

    private Setup.TestBrowser getTestBrowser(String chosenBrowser) throws IOException
    {
        TestBrowser passOn;
        switch (chosenBrowser)
        {
            case "Firefox": //Firefox browser
                passOn = new useFireFox();
                break;
            case "Chrome": //Chrome Browser
                passOn = new useChrome();
                break;
            case "Edge": //Chrome Browser
                passOn = new useEdge();
                break;
            default: //Invalid browser set
                throw new IOException("No Chosen Browser");

        }
        System.setProperty(passOn.getDriver(),passOn.getDriverLoc());
        return passOn;
    }
}
