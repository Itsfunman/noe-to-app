package main;

import sqlStuff.LoginHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Represents the Login Interface
 */
public class LoginFrame extends JFrame {

    private JButton loginButton = new JButton("LOGIN");
    private JLabel nameTag = new JLabel("USER");
    private JTextField nameField = new JTextField("User");
    private JLabel passwordTag = new JLabel("PASSWORD");
    private JPasswordField passwordField = new JPasswordField("Password");
    private JButton registerButton = new JButton("REGISTRATION");
    private LoginHandler loginHandler = new LoginHandler();

    public static String currentUserName;

    /**
     * Method to create a LoginFrame
     * @param title
     */
    public LoginFrame(String title) {
        super(title);

        setLayout(null);

        //Set custom Icon
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        setIconImage(icon.getImage());

        nameFieldInit();
        passwordFieldInit();
        loginButtonInit();
        registerButtonInit();
        addLogo();

        InitLoginHandler();

    }

    private void InitLoginHandler(){
        loginHandler = new LoginHandler();

    }
    /**
     * Adds the logo on the screen
     */
    private void addLogo(){
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        Image img = icon.getImage();

        Image newImg = img.getScaledInstance(440, 200, Image.SCALE_SMOOTH );

        ImageIcon newIcon = new ImageIcon(newImg);
        JLabel label = new JLabel(newIcon);
        label.setBounds(0,0, newIcon.getIconWidth(), newIcon.getIconHeight());
        getContentPane().add(label);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = ((getWidth() - label.getWidth())/2);
                int y = 10;
                label.setLocation(x,y);
            }
        });
    };

    /**
     * initializes the nameField
     */
    private void nameFieldInit(){
        nameTag.setBounds((getWidth() - nameTag.getWidth())/2 - 75, (getHeight() - nameTag.getHeight()) / 2 - 30, 100, 20 );
        nameField.setSize(150,20);

        add(nameField);
        add(nameTag);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = ((getWidth() - nameField.getWidth()) / 2);
                int y = ((getHeight() - nameField.getHeight()) / 2) - 30;
                nameField.setLocation(x, y);
                nameTag.setLocation(x - 45,y);
            }
        });
    }

    /**
     * Initializes the password field
     */
    private void passwordFieldInit(){
        passwordTag.setBounds((getWidth() - passwordTag.getWidth())/2 - 80, (getHeight() - passwordTag.getHeight()) / 2, 100, 20 );
        passwordField.setSize(150,20);
        passwordField.setVisible(true);

        add(passwordField);
        add(passwordTag);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = ((getWidth() - passwordField.getWidth()) / 2);
                int y = ((getHeight() - passwordField.getHeight()) / 2);
                passwordField.setLocation(x, y);
                passwordTag.setLocation(x - 80,y);
            }
        });
    }

    /**
     * Initializes the LoginButton that calls calls the method used to confirm login data
     */
    private void loginButtonInit(){
        loginButton.setSize(150, 20);
        loginButton.setVisible(true);

        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (loginHandler.checkUserData(nameField.getText(), String.valueOf(passwordField.getPassword()))){

                    confirmInput();

                } else {
                    System.out.println("Invalid input!");
                }
            }
        });


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = (getWidth() - loginButton.getWidth()) / 2;
                int y = ((getHeight() - loginButton.getHeight()) / 2) + 30;
                loginButton.setLocation(x, y);
            }
        });
    }

    /**
     * Initializes the register button which leads to the RegisterFrame
     */
    private void registerButtonInit(){
        registerButton.setSize(150, 20);
        registerButton.setVisible(true);

        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                RegisterFrame registerFrame = new RegisterFrame("Registration");

            }
        });


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = (getWidth() - registerButton.getWidth()) / 2;
                int y = ((getHeight() - registerButton.getHeight()) / 2) + 60;
                registerButton.setLocation(x, y);
            }
        });
    }


    /**
     * initializes the main program
     */
    private void confirmInput(){
        System.out.println("Valid input!");
        setVisible(false);

        //------------------------------------
        CapacityFrame frame = new CapacityFrame("NOE-TO");

        //....................................


    }

}
