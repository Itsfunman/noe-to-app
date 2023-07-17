package sqlStuff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static sqlStuff.DBConnection.getConnection;

/**
 * Is used to check inpuit in the LoginFrame
 */
public class LoginHandler {

    /**
     * Initializes the LoginHandler
     */
    public LoginHandler(){


    }

    /**
     * Checks whether the user exists and the password is correct
     * @param name
     * @param password
     * @return
     */
    public boolean checkUserData(String name, String password) {
        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT password FROM userdata WHERE name = ?")) {

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
