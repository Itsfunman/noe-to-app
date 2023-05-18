package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {

    //Currently, the file only saves name and password, this can be changed by modifying the FileReader and FileWriter
    private String name;
    private String password;
    private boolean hasAdminRights;

    private final int id;

    private  static int COUTNER = 10;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.hasAdminRights = false;
        this.id = COUTNER;
        COUTNER++;

        LoginHandler.addUser(this.name, this.password);

        addToFile(this.name, this.password);


    }

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
            //AddUserToDB
            addUserToDB();
            // user does not exist in file, so add to file
            writer.write(name + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUserToDB() throws SQLException {
        PreparedStatement pst = null;
        Connection connection = DBConnection.getConnection();

        System.out.println(connection);
        pst = connection.prepareStatement("insert into dbo.example (id,username) values (?,?)");
        pst.setInt(1, id);
        pst.setString(2, name);
//        pst.setInt(3, 3);
//        pst.setInt(4, 4);
        pst.executeUpdate();
        // JOptionPane.showConfirmDialog(null, "Added to DB");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasAdminRights() {
        return hasAdminRights;
    }

    public void setHasAdminRights(boolean hasAdminRights) {
        this.hasAdminRights = hasAdminRights;
    }
}
