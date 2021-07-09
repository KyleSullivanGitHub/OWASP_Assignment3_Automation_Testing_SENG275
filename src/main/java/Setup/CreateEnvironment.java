package Setup;

import java.io.IOException;

public class CreateEnvironment
{
    public TestBrowser createBrowser(String chosenBrowser) throws IOException
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
