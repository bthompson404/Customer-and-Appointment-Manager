
package utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Blake Thompson
 */
public class Query {
    private static String query;
    private static Statement stmt;
    private static ResultSet result;
    
    public static void makeQuery(String q) {
        query = q;
        try {
            //create statement object
            stmt = DBConnection.conn.createStatement();
            
            //determine query execution
            if (query.toLowerCase().startsWith("select"))
                result = stmt.executeQuery(query);
            
            if (query.toLowerCase().startsWith("delete") || query.toLowerCase().startsWith("insert")
                    || query.toLowerCase().startsWith("update"))
                stmt.executeUpdate(query);
        } catch(SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public static ResultSet getResult() {
        return result;
    }
}