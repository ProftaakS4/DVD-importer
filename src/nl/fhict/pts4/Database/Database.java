/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fhict.pts4.Database;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vito Corleone
 * @author Bart
 */
public class Database {

    // set the database properties
    private String id = "";
    private String quantity = "";

    // define the connection
    private Connection connection = null;

    // get the databasesingleton instance
    private DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstanceSingleton();

    public Database() {
        connection = databaseSingleton.getDatabaseConnection();
    }

    public int newDvd(Integer photographer) {

        String query = "{call insertDVD(?, ?)}";

        CallableStatement stmt = null;

        try {
            stmt = (CallableStatement) connection.prepareCall(query);
            stmt.setInt(1, photographer);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            Integer dvdid = stmt.getInt(2);
            System.out.println("DVD ID = " + dvdid);

            return dvdid;
        } catch (SQLException ex) {
            System.err.println("Database error:" + ex.getMessage());
            return 0;
        }
    }

    private String generateRandomLoginCode(int length){
        Random rng = new Random();
        rng.nextInt();

        StringBuilder randStr = new StringBuilder();

        for(int i=0; i<length; i++) {

            char ch;
            if (rng.nextBoolean()) {
                // Generate a number
                ch = (char) (0x30 +rng.nextInt() % 10);
            }
            else
            {
                // Generate a letter
                ch = (char) (0x40 +rng.nextInt() % 26);
            }

            randStr.append(ch);

        }
        return randStr.toString();
    }

    public int newDirectory() {

        int length = 6;



        return 0;
    }

    public int newPhoto() {

        return 0;
    }

    private void executeQuery() {
        // the name of the stored procedure 
        String query = "{call getPhotoPath(?,?)}";

        try {
            // create the statement
            CallableStatement stmt = (CallableStatement) connection.prepareCall(query);

            // set the first procedure paramater
            stmt.setInt(1, Integer.parseInt(id));

            // set the procedure output parameter
            stmt.registerOutParameter(2, Types.INTEGER);

            // execute the query
            stmt.execute();

            // get the output parameter
            String dvdid = stmt.getString(2);
            System.out.println("DVD ID = " + id);


        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
