/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fhict.pts4.Database;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;
import nl.fhict.pts4.Photo;

import java.io.IOException;
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

    private String generateRandomCode(int length){
        // Only generate numbers
        boolean only_numbers = true;

        Random rng = new Random();
        rng.nextInt();

        StringBuilder code = new StringBuilder();

        for(int i=0; i<length; i++) {

            char ch;
            if (rng.nextBoolean() || only_numbers) {
                // Generate a number
                ch = (char) (0x30 +rng.nextInt() % 10);
            }
            else
            {
                // Generate a letter
                ch = (char) (0x40 +rng.nextInt() % 26);
            }

            code.append(ch);
        }
        return code.toString();
    }

    public String newLoginCode(int directory, int photographer)
    {
        String code = generateRandomCode(5) + directory;
        /*
        INSERT INTO LOGINCODE (ID, MAP_ID, PHOTOGRAPHER_ID)
        VALUES (p_map_id, p_photographer_id, p_used, p_validUntil);
        */

        String query = "{call insertLoginCode(?,?,?,?)}";

        try {
            CallableStatement stmt = (CallableStatement) connection.prepareCall(query);
            stmt.setInt(1, directory);

            stmt.execute();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }


    }

    public int newDirectory(int dvdid, String path, boolean isPrivate) {

        String query = "{call insertMap(?,?,?,?)}";

        try {
            CallableStatement stmt = (CallableStatement) connection.prepareCall(query);

            stmt.setInt(1, dvdid);
            stmt.setString(2, path);

            if (isPrivate) {
                stmt.setString(3, "Prive");
            } else {
                stmt.setString(3, "Openbaar");
            }

            // p_mapid
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.execute();

            return stmt.getInt(4);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return 0;
    }

    public int newPhoto(int directory, int photographer, Photo photo) throws IOException {
        String query = "{call insertMap(?,?,?,?,?,?)}";

        // p_photographer_id
        // map id
        // image
        // resolution
        // description

        String image = photo.getPath();
        String resolution = photo.getResolution();
        String description = photo.getTitle();

        try {
            CallableStatement stmt = (CallableStatement) connection.prepareCall(query);

            stmt.setInt(1, photographer);
            stmt.setInt(2, directory);
            stmt.setString(3, image);
            stmt.setString(4, resolution);
            stmt.setString(5, description);

            // p_mapid
            stmt.registerOutParameter(4, Types.INTEGER);

            stmt.execute();

            return stmt.getInt(4);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return 0;
    }
}
