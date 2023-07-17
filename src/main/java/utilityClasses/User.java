package utilityClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static sqlStuff.DBConnection.getConnection;

/**
 * Class used for User management
 */

public class User {

    //Currently, the file only saves name and password, this can be changed by modifying the FileReader and FileWriter
    private String name;
    private String password;
    private boolean hasAdminRights;

    /**
     * Initializes the User
     * @param name
     * @param password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.hasAdminRights = false;


        addUserToDB(this.name, this.password);

    }


    /**
     * Adds the user data to the DB and checks whether it already exists
     * @param name
     * @param password
     */
    private void addUserToDB(String name, String password) {
        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM userdata WHERE name = ?");
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO userdata (name, password) VALUES (?, ?)")) {

            // Check if the username already exists
            selectStatement.setString(1, name);
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                System.out.println("Username already exists.");
                return; // Exit the method if username exists
            }

            // Insert the new user
            insertStatement.setString(1, name);
            insertStatement.setString(2, String.valueOf(password.hashCode()));
            insertStatement.executeUpdate();
            System.out.println("User added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
