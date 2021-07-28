package DatabaseTesting;


import FunctionalTests.Test_Data;
import org.asynchttpclient.util.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.Query;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.sql.*;
import static org.testng.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;



/**
 * Database Test Class for OWASP Juice Shop
 * Database imported from Local Host
 * Programmer: Seyedmehrad Adimi
 */
public class DatabaseTest {
    private static Connection con;



    /**
     * Database Test for Valid Extraction from the database of OWASP Juice Shop
     * The test gets the entity as a parameter and prints out the table of the entity.
     * The results for this test are manually compared with the database tables from MySQL to ensure accuracy.
     * Programmer: Seyedmehrad Adimi
     * @param Entity this is the name of the entity that needs to be extracted.
     */
    @Test(
            dataProvider = "extraction",
            dataProviderClass = DatabaseTesting_DataProvider.class
    )
    public static void dataExtractionTest(String Entity) {
        try {
            System.out.println ("\nThis is a Test for Data Extraction from Different Entities. We are Extracting "+ Entity+" Now");

            Statement stm = getStatement ();

            ResultSet EntityList = stm.executeQuery ("SELECT * FROM "+Entity);

            System.out.println ("**************************************** List of "+Entity+":");
            while (EntityList.next ()){

                //Retrieve by column name
                int id;
                String name;

                id  = EntityList.getInt(Entity+" ID");
                name= EntityList.getString(Entity+" Name");

                //Display values
                System.out.print("ID: "+ id);
                System.out.println(", NAME: "+ name);
            }
        }catch (Exception e){
            System.out.println (e);
        }
    }


    /**
     * Database Test for Valid update of the database of different entities' name of OWASP Juice Shop.
     * The test gets the entity, new name, and ID as parameters and updates the name of the particular entity with the particular ID.
     * Programmer: Seyedmehrad Adimi
     * @param Entity this is the name of the entity that needs to be extracted.
     * @param newName new name to be replaced with the old one
     * @param ID this is to be used to find the right row to change
     * NOTE: To keep the table final, all the changes are reset. However, they are printed out, so the changes and the accuracy of the code can be verified
     */
    @Test(
            dataProvider = "updateName",
            dataProviderClass = DatabaseTesting_DataProvider.class
    )
    public static void nameUpdateEntity(String Entity, String newName, int ID){
        try {
            System.out.println ("\nThis is a Test for Updating Name for Different Entities. We are Updating "+ Entity+" with ID: "+ID+" to "+ newName+" Now");

            Statement stm = getStatement ();

            // Get the name to be changed to we can reset it after updating, we dont want to mess with the tables
            ResultSet previousNameResult = stm.executeQuery ("SELECT `"+Entity+" Name` FROM "+Entity+" WHERE `"+Entity+" ID` = "+ID+";");
            previousNameResult.next ();
            String previousName = previousNameResult.getString (Entity+" Name");
            previousNameResult.next ();

            // Update the name with the client ID specified
            updateAndReset (Entity, newName, ID, stm);


            // Get the new name with the ID passed
            ResultSet updatedNameResult = stm.executeQuery ("SELECT `"+Entity+" Name` FROM "+Entity+" WHERE `"+Entity+" ID` ="+ID+";");

            updatedNameResult.next ();
            String updatedName = updatedNameResult.getString (Entity+" Name");
            updatedNameResult.next ();
            // Assert if the name is updated
            assertEquals (updatedName,newName);

            // Print new Client list
            System.out.println ("************************************** After updated to " + newName + " with ID: "+ ID);
            printEntity (Entity,stm);

            // reset the name to what it was before. We dont want to mess with the tables
            updateAndReset (Entity, previousName, ID, stm);


        }catch (Exception e){
            System.out.println (e);
        }
    }

    private static void updateAndReset(String Entity, String newName, int ID, Statement stm) throws SQLException {
        String updateClientName = "UPDATE " + Entity + " SET `" + Entity + " Name` = '" + newName + "' WHERE `" + Entity + " ID` =" + ID + ";";
        stm.executeUpdate (updateClientName);
    }



    /**
     * Database Test for Valid Insertion to the OWASP Juice Shop database of Client entity.
     * The test gets the Client ID, Name, username, number of purchases, and warehouse ID as parameters and adds the new client to the list.
     * Programmer: Seyedmehrad Adimi
     * @param ClientID this is the new client's ID
     * @param Name this is the new client's name
     * @param username this is the new client's username
     * @param numberOfPurchases this is the new client's number of purchases
     * @param warehouseID this is the new client's warehouse ID.
     * NOTE: To keep the table final, all the changes are reset. However, they are printed out, so the changes and the accuracy of the code can be verified
     */
    @Test(
            dataProvider = "addClient",
            dataProviderClass = DatabaseTesting_DataProvider.class
    )
    public static void dataInsertion(int ClientID, String Name, String username, int numberOfPurchases, int warehouseID){
        try {

            System.out.println ("\nThis is a Test for Data Insertion for Client. We are Inserting "+ Name+" with ID: "+ClientID+" Now");


            Statement stm = getStatement ();


            // Insert new Client with the passed parameters
            stm.executeUpdate ("INSERT INTO `Client` VALUES ("+ClientID+",'"+Name+"','"+username+"',"+numberOfPurchases+","+warehouseID+")");



            // Assertion
            ResultSet newClientResult = stm.executeQuery ("SELECT `Client Name` FROM Client WHERE `Client ID` = "+ClientID+";");
            newClientResult.next ();
            String newClientName = newClientResult.getString ("Client Name");
            newClientResult.next ();

            assertEquals (newClientName,Name);

            // Print new Client list
            System.out.println ("************************************** After Adding " +Name+ " with ID: "+ ClientID);
            printEntity ("Client",stm);


            System.out.println ("************************************** After Removing the Recently Added Client with ID: "+ ClientID);
           // reset and delete the added one. we dont want to mess with the tables
            stm.executeUpdate ("DELETE FROM Client WHERE `Client ID`= "+ClientID+";");
            printEntity ("Client",stm);


        }catch (Exception e){
            System.out.println (e);
        }
    }

    /**
     * Database Test for Valid Removal from the OWASP Juice Shop database of Client entity.
     * The test gets the Client ID as the parameter and deletes the corresponding client.
     * Programmer: Seyedmehrad Adimi
     * @param ClientID this is the ID of the client to be deleted from the database.
     * NOTE: To keep the table final, all the changes are reset. However, they are printed out, so the changes and the accuracy of the code can be verified
     */

    @Test(
            dataProvider = "RemoveClient",
            dataProviderClass = DatabaseTesting_DataProvider.class
    )
    public static void dataRemoval(int ClientID){
        try {
            System.out.println ("\nThis is a Test for Data Removal for Client. We are Removing Client with ID: "+ClientID+" Now");


            Statement stm = getStatement ();

            ResultSet toDelete = stm.executeQuery ("SELECT * FROM Client WHERE `Client ID` ="+ClientID+";");
            toDelete.next ();
            String Name = toDelete.getString ("Client Name");
            String username = toDelete.getString ("Username");
            int numberOfPurchases = toDelete.getInt ("Number Of Purchases");
            int warehouseID = toDelete.getInt ("Warehouse ID");
            toDelete.next ();



            stm.executeQuery ("SET foreign_key_checks = 0;");
            try {
                // Delete Client using the passed ID
                stm.executeUpdate ("DELETE FROM Client WHERE `Client ID`= "+ClientID+";");
            }catch (Exception e){
                System.out.println (e);
                System.out.println ("Client does not exist!");
                return;
            }
            stm.executeQuery ("SET foreign_key_checks = 1;");



            // Assertion
            ResultSet newClientResult = stm.executeQuery ("SELECT * FROM Client WHERE `Client ID` = "+ClientID+";");
            assertFalse (newClientResult.next ());

            // Print new Client list
            System.out.println ("************************************** After Removing Client with ID: "+ ClientID);
            printEntity ("Client",stm);


            stm.executeUpdate ("INSERT INTO `Client` VALUES ("+ClientID+",'"+Name+"','"+username+"',"+numberOfPurchases+","+warehouseID+");");

            System.out.println ("************************************** After Putting Client with ID Back: "+ ClientID);
            printEntity ("Client",stm);


        }catch (Exception e){
            System.out.println (e);
        }
    }


    /**
     * Database Test for OWASP Juice Shop database to check that the entities have the proper primary keys
     * The test gets the Entity as the only parameter and checks the primary key to be correct.
     * Programmer: Seyedmehrad Adimi
     * @param Entity the entity to be checked about its primary key
     */
    @Test(
            dataProvider = "primaryKeysData",
            dataProviderClass = DatabaseTesting_DataProvider.class
    )
    public static void primaryKeysTest(String Entity){
        try {

            Statement stm = getStatement ();

            ResultSet primaryKey= stm.executeQuery ("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'OWASP' AND TABLE_NAME = "+ "'"+Entity+"'"+" AND COLUMN_KEY = 'PRI';");
            primaryKey.next ();
            String EntityKey = primaryKey.getString ("COLUMN_NAME");
            System.out.println ("The Primary Key for "+ Entity + " is: "+ EntityKey);
            primaryKey.next ();

            assertEquals (Entity + " ID", EntityKey);


        }catch (Exception e){
            System.out.println (e);
        }
    }

    /**
     * Database Test for OWASP Juice Shop database to check that the entities have the proper foreign keys
     * The test gets the Entity as the only parameter and checks the foreign key to be correct.
     * Programmer: Seyedmehrad Adimi
     * @param Entity the entity to be checked about its foreign key
     */
    @Test(
            dataProvider = "primaryKeysData",
            dataProviderClass = DatabaseTesting_DataProvider.class
    )

    public static void ForeignKeysTest(String Entity){
        try {

            Statement stm = getStatement ();

            ResultSet foreignKey= stm.executeQuery ("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'OWASP' AND TABLE_NAME = "+ "'"+Entity+"'"+" AND COLUMN_KEY = 'MUL';");

            String[] arrayOfFKeys = new String[30];
            int i=0;
            System.out.println ("The Foreign Keys for "+ Entity+" are : ");
            while (foreignKey.next ()){
                String EntityKey = foreignKey.getString ("COLUMN_NAME");
                arrayOfFKeys[i] = String.copyValueOf(EntityKey.toCharArray ());
                System.out.println (arrayOfFKeys[i]);
                i++;
            }
            System.out.println ();

            switch (Entity) {
                case "Employee":
                    assertEquals (arrayOfFKeys[0], "Super ID");
                    assertEquals (arrayOfFKeys[1], "Warehouse ID");
                    break;
                case "Client":
                    assertEquals (arrayOfFKeys[0], "Warehouse ID");
                    break;
                case "Product":
                    assertEquals (arrayOfFKeys[0], "Warehouse ID");
                    break;
                case "Orders":
                    assertEquals (arrayOfFKeys[0], "Client ID");
                    assertEquals (arrayOfFKeys[1], "Product ID");
                    break;
                case "Supplier":
                    assertEquals (arrayOfFKeys[0], "Warehouse ID");
                    break;
                case "Warehouse":
                    assertEquals (arrayOfFKeys[0], "Manager ID");
                    break;
            }

        }catch (Exception e){
            System.out.println (e);
        }
    }

    /**
     * private helper method to help for the setup of the statement to be executed
     * Programmer: Seyedmehrad Adimi
     * @exception ClassNotFoundException thrown if a class is not found
     * @exception SQLException thrown if there is any problems with the SQL queries.
     */
    private static Statement getStatement() throws ClassNotFoundException, SQLException {
        Class.forName ("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection ("jdbc:mysql://localhost:3306/OWASP", "root", "Meri.ad900");
        return con.createStatement ();
    }

    /**
     * private helper method to help printing the entity tables
     * Programmer: Seyedmehrad Adimi
     * @exception SQLException thrown if there is any problems with the SQL queries.
     */
    private static void printEntity(String Entity, Statement stm) throws SQLException {
        ResultSet ClientList = stm.executeQuery ("SELECT * FROM "+Entity);
        while (ClientList.next ()){
            //Retrieve by column name
            int id  = ClientList.getInt(Entity+" ID");
            String fName = ClientList.getString(Entity+" Name");
            //Display values
            System.out.print("ID: "+ id);
            System.out.println(", NAME: "+ fName);

        }
    }


}
