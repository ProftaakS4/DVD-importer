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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vito Corleone
 */
public class Database {

    // set the database properties
    private String id = "";
    private String quantity = "";

    // define the connection
    private Connection connection = null;

    // get the databasesingleton instance
    private DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstanceSingleton();

    public Database(String id, String quantity) {
        this.id = id;
        this.quantity = quantity;
        connection = databaseSingleton.getDatabaseConnection();
        executeQuery();
    }

    private int newDvd(Integer photographer) {

        String query = "{call newDvd(?)}";

        CallableStatement stmt = null;
        try {
            stmt = (CallableStatement) connection.prepareCall(query);
            stmt.setInt(1, photographer);

        } catch (SQLException e) {
            System.err.println(ex.getMessage());
        }
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
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);

            // execute the query
            stmt.execute();

            // get the output parameter
            String imagePath = stmt.getString(2);
            System.out.println("Image ID = " + id + " Imagepath = " + imagePath);

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
