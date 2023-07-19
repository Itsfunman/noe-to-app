package objectClasses;

import jakarta.persistence.*;
import sqlStuff.UserDAO;
/**
 * Class used for User management
 */
@Entity
@Table(name="userdata")
@IdClass(UserId.class)
public class User {

    //Currently, the file only saves name and password, this can be changed by modifying the FileReader and FileWriter
    @Id
    @Column(name="userName")
    private String name;
    @Id
    private String password;


    public User(){
    }
    /**
     * Initializes the User
     * @param name
     * @param password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;

        UserDAO.addUserToDB(this.name, this.password);

    }




}
