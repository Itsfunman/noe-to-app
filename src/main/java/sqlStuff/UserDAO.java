package sqlStuff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static sqlStuff.DBConnection.getConnection;

/**
 * Is used to check inpuit in the LoginFrame
 */
public class UserDAO {

    /**
     * Initializes the LoginHandler
     */
    public UserDAO(){
    }

    /**
     * Adds the user data to the DB and checks whether it already exists
     * @param name
     * @param password
     */
    public static void addUserToDB(String name, String password) {
        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM userdata WHERE userName = ?");
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO userdata (userName, password) VALUES (?, ?)")) {

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

    /**
     * Checks whether the user exists and the password is correct
     * @param name
     * @param password
     * @return
     */
    public static boolean checkUserData(String name, String password) {
        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT password FROM userdata WHERE userName = ?")) {

            selectStatement.setString(1, name);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                // Compare the stored password with the provided password
                if (storedPassword.equals(String.valueOf(password.hashCode()))) {
                    return true; // Username and password match
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Username does not exist or password does not match
    }




}
