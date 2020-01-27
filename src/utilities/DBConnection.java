/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Blake Thompson
 */
public class DBConnection {
    public static final String databaseName = "";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + databaseName;
    private static final String username = "";
    private static final String password = "";
    private static final String driver = "com.mysql.jdbc.Driver";
    public static Connection conn;

    public static void makeConnection() throws Exception {
        Class.forName(driver);
        conn = DriverManager.getConnection(DB_URL, username, password);
    }
    
    public static void closeConnection() throws Exception {
        conn.close();
    }
        
    public static Connection checkConnection() {
        return conn;
    }
}