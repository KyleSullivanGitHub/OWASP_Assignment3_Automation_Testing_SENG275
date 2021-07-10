package Setup;

import org.openqa.selenium.WebDriver;

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
        switch (chosenBrowser)
        {
            case "Firefox": //Firefox browser
                return new useFireFox();
            case "Chrome": //Chrome Browser
                return new useChrome();
            case "Edge": //Chrome Browser
                return new useEdge();
            default: //Invalid browser set
                throw new IOException("No Chosen Browser");

        }
    }
}
