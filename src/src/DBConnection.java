package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        testConnection("nikolic", "nikolic");

    }

    public static void testConnection(String user, String pwd) {
        String jdbcUrl = "jdbc:sqlserver://185.119.119.126:1433;databaseName=SWP_2023_MD_GRUPPE_E;encrypt=true;trustServerCertificate=true";

        System.out.println("Connecting to database: " + jdbcUrl);

        try {
            Connection con = DriverManager.getConnection(jdbcUrl, user, pwd);
            // something = ok, null
            if (con != null) {
                //System.out.println(con.getMetaData());

            } else {
                System.out.println("Keine Verbindnug m�glich");
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getErrorCode());

        }
    }

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:sqlserver://185.119.119.126:1433;databaseName=SWP_2023_MD_GRUPPE_E;encrypt=true;trustServerCertificate=true";

        System.out.println("Connecting to database: " + jdbcUrl);

        Connection con = null;
        try {
            con = DriverManager.getConnection(jdbcUrl, "nikolic", "nikolic");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // something = ok, null
        return con;

    }
}