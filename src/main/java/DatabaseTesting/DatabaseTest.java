package DatabaseTesting;


import FunctionalTests.Test_Data;
import org.asynchttpclient.util.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.Query;
import java.io.IOException;
import java.sql.*;
import static org.testng.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;



/**
 *Database Test Class for OWASP Juice Shop
 * Database imported from Local Host
 * Programmer: Seyedmehrad Adimi
 */
public class DatabaseTest {
    private static Connection con;

    @Test(
            dataProvider = "extraction",
            dataProviderClass = DatabaseTesting_DataProvider.class
    )
    public static void dataExtractionTest(String Entity) {
        try {
            Class.forName ("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection ("jdbc:mysql://localhost:3306/OWASP","root","Meri.ad900");
            Statement stm = con.createStatement ();
            ResultSet EntityList = stm.executeQuery ("SELECT * FROM "+Entity);
            System.out.println ("**************************************** List of "+Entity);
            while (EntityList.next ()){
                //Retrieve by column name
                int id;
                String name;


                id  = EntityList.getInt(Entity+" ID");
                try{
                    name= EntityList.getString(Entity+" Name");
                }catch (SQLException e) {
                    name = EntityList.getString ("First Name");
                }
                //Display values
                System.out.print("ID: "+ id);
                System.out.println(", NAME: "+ name);
            }

        }catch (Exception e){
            System.out.println (e);
        }
    }

    @Test(
            dataProvider = "updateName",
            dataProviderClass = DatabaseTesting_DataProvider.class
    )
    public static void dataUpdateClient(String newName, int ClientID){
        try {
            Class.forName ("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection ("jdbc:mysql://localhost:3306/OWASP","root","Meri.ad900");
            Statement stm = con.createStatement ();

            // Get the name to be changed to we can reset it after updating, we dont want to mess with the tables
            ResultSet previousNameResult = stm.executeQuery ("SELECT `First Name` FROM Client WHERE `Client ID` = "+ClientID+";");
            previousNameResult.next ();
            String previousName = previousNameResult.getString ("First Name");
            previousNameResult.next ();

            // Update the name with the client ID specified
            updateAndResetClientNameWithID (newName, ClientID, stm);


            // Get the new name with the ID passed
            ResultSet updatedNameResult = stm.executeQuery ("SELECT `First Name` FROM CLIENT WHERE `Client ID` ="+ClientID+";");
            updatedNameResult.next ();
            String updatedName = updatedNameResult.getString ("First Name");
            updatedNameResult.next ();
            // Assert if the name is updated
            assertEquals (updatedName,newName);

            // Print new Client list
            System.out.println ("************************************** After updated to " + newName + " with ID: "+ ClientID);
            printClient (stm);

            // reset the name to what it was before. We dont want to mess with the tables
            updateAndResetClientNameWithID (previousName, ClientID, stm);


        }catch (Exception e){
            System.out.println (e);
        }
    }

    private static void updateAndResetClientNameWithID(String newName, int ClientID, Statement stm) throws SQLException {
        String updateClientName = "UPDATE Client SET `First Name` = '" + newName + "' WHERE `Client ID` =" + ClientID + ";";
        stm.executeUpdate (updateClientName);
    }


    private static void printClient(Statement stm) throws SQLException {
        ResultSet ClientList = stm.executeQuery ("SELECT * FROM Client");
        while (ClientList.next ()){
            //Retrieve by column name
            int id  = ClientList.getInt("Client ID");
            String fName = ClientList.getString("First Name");
            String lName = ClientList.getString("Last Name");
            //Display values

            System.out.print("ID: "+ id);
            System.out.println(", NAME: "+ fName+" "+lName);

        }
    }


}
