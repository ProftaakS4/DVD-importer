/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fhict.pts4.Database;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vito Corleone
 */
public class DatabaseSingleton {

    // connectionstring properties
    private final String url = "jdbc:mysql://192.168.27.120:3306/";
    private final String dbName = "TestPhotocasus";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String userName = "root";
    private final String password = "F05XsL";

    // define connection
    private Connection connection = null;

    // create null instance of singleton
    private static DatabaseSingleton instance = null;

    // protected constructor
    protected DatabaseSingleton() {

    }

    // get or create databasesingleton
    public static DatabaseSingleton getInstanceSingleton() {
        if (instance == null) {
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    // get or create databaseconnection
    public Connection getDatabaseConnection() {
        if (connection == null) {
            try {
                connection = (Connection) DriverManager.getConnection(url + dbName, userName, password);
                System.out.println("Databaseconnection OK");
                return connection;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return connection;
    }
}
