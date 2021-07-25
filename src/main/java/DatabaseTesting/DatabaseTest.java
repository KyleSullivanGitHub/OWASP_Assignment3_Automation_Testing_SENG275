package DatabaseTesting;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;


/**
 *Database Test Class for OWASP Juice Shop
 * Database imported from Local Host
 * Programmer: Seyedmehrad Adimi
 */
public class DatabaseTest {

    public static void main(String[] args) {
        try{
            Class.forName ("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection ("jdbc:mysql://localhost:3306/OWASP","root","Meri.ad900");
            Statement stm = con.createStatement ();


            ResultSet r = stm.executeQuery ("SELECT * FROM Employee");

            while (r.next ()){
                //Retrieve by column name
                 int id  = r.getInt("Employee ID");
                 String name= r.getString("Employee Name");
                 //Display values
                 System.out.print("ID: "+ id);
                 System.out.println(", NAME: "+ name);
            }


        } catch (Exception e) {
            System.out.println (e);
        }
    }

}
