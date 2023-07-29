package objectClasses;

import jakarta.persistence.*;
import sqlStuff.UserDAO;
import javax.swing.*;
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
        try {
            if (!Namegültig(name)) {
                throw new IllegalArgumentException("Ungültiger Benutzername! Der Benutzername darf keine Zahlen oder Sonderzeichen enthalten und darf nicht leer sein.");
            }
            if (!Passwortgültig(password)) {
                throw new IllegalArgumentException("Ungültiges Passwort! Das Passwort muss mindestens 8 Zeichen lang sein.");
            }

            this.name = name;
            this.password = password;

            UserDAO.addUserToDB(this.name, this.password);
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private boolean Namegültig(String name) {
        // Überprüfung ob der Name keine Zahlen oder Sonderzeichen enthält und nicht leer ist
        return name != null && !name.isEmpty() && name.matches("^[a-zA-Z]+$");
    }

    private boolean Passwortgültig(String passwort) {
        // Überprüfung ob Passwort mindestens 8 Zeichen lang
        return passwort != null && passwort.length() >= 8;
    }

    private void showErrorMessage(String message) {
        // Fehlermeldungsfenster
        JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}

