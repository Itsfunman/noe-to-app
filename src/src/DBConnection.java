package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        testConnection("dummy", "dummy");

    }
    public static void testConnection(String user, String pwd) {
        String jdbcUrl = "jdbc:sqlserver://185.119.119.126:1433;databaseName=SP-TEST-MD;encrypt=true;trustServerCertificate=true";

        System.out.println("Connecting to database: " + jdbcUrl);

        try {
            Connection con = DriverManager.getConnection(jdbcUrl, user, pwd);
            // something = ok, null
            if (con != null) {
                //System.out.println(con.getMetaData());
                System.out.println("OK");
            } else {
                System.out.println("Keine Verbindnug m�glich");
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getErrorCode());

        }
    }
}
