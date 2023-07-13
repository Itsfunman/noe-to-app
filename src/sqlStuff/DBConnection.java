package sqlStuff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DBConnection class provides methods to establish a connection to a Microsoft SQL Server database using JDBC.
 */
public class DBConnection {

    /**
     * Main method for testing the database connection.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        testConnection("nikolic", "nikolic");
    }

    /**
     * Tests the database connection with the provided username and password.
     *
     * @param user The username for the database connection.
     * @param pwd  The password for the database connection.
     */
    public static void testConnection(String user, String pwd) {
        String jdbcUrl = "jdbc:sqlserver://185.119.119.126:1433;databaseName=SWP_2023_MD_GRUPPE_E;encrypt=true;trustServerCertificate=true";

        System.out.println("Connecting to database: " + jdbcUrl);

        try {
            Connection con = DriverManager.getConnection(jdbcUrl, user, pwd);
            if (con != null) {
                // Connection successful, you can perform operations on the database here
                // For example: System.out.println(con.getMetaData());
            } else {
                System.out.println("Keine Verbindung möglich"); // No connection possible
            }
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getErrorCode());
        }
    }

    /**
     * Establishes a database connection with the predefined username and password.
     *
     * @return The Connection object representing the database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:sqlserver://185.119.119.126:1433;databaseName=SWP_2023_MD_GRUPPE_E;encrypt=true;trustServerCertificate=true";

        System.out.println("Connecting to database: " + jdbcUrl);

        Connection con = null;
        try {
            con = DriverManager.getConnection(jdbcUrl, "nikolic", "nikolic");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
}
