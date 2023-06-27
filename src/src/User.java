package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static src.DBConnection.getConnection;

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

        LoginHandler.addUser(this.name, this.password);

        addToFile(this.name, this.password);
        addUserToDB(this.name, this.password);

    }

    /**
     * Adds the user to the loginData file
     * @param name
     * @param password
     */
    private void addToFile(String name, String password) {
        String filePath = "data/loginData.txt";
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             FileWriter writer = new FileWriter(filePath, true)) {

            String line = bufferedReader.readLine();
            while (line != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(name)) {
                    // user already exists in file, so return without adding
                    return;
                }
                line = bufferedReader.readLine();
            }

            // user does not exist in file, so add to file
            writer.write(name + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addUserToDB(String name, String password) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO userdata (name, password) VALUES (?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
