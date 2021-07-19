package Setup;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Used to determine which browser to create for a given test.
 * Programmer: Kyle Sullivan
 */
public class CreateEnvironment
{
    //String containg primary browser.
    private String primaryBrowser = TestBrowser.getPrimaryBrowser();

    /**
     * Creates a test environment based on the default settings
     * Programmer: Kyle Sullivan
     * @return the test environment.
     */
    public TestBrowser createBrowser() throws IOException
    {
        return getTestBrowser(primaryBrowser);
    }

    /**
     * Creates a test environment based on a provided string.
     * Programmer: Kyle Sullivan.
     * @return the test environment.
     */
    public TestBrowser createBrowser(String chosenBrowser) throws IOException
    {
        return getTestBrowser(chosenBrowser);
    }

    /**
     * Determins what type of test environment to make based on a passed value.
     * Programmer: Kyle Sullivan.
     * @param chosenBrowser a string containing the desired browser. Implemented options are:
     *                      Chrome
     *                      Firefox
     *                      Edge
     * @return the an TestBrowser Object tied to the desired browser.
     */
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
                /*
            case "Safari": // Safari Browser
                passOn = new useSafari (); //TODO Add safari web driver
                break;

                 */
            default: //Invalid browser set
                throw new IOException("No Chosen Browser");

        }
        //set up the system for the specific browser in question.
        System.setProperty(passOn.getDriver(),passOn.getDriverLoc());
        return passOn;
    }
}
