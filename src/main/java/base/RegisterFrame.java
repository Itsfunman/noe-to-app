package base;



import objectClasses.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Initializes the frame for users to register
 */
public class RegisterFrame extends JFrame {

    private JButton returnButton = new JButton("BACK");
    private JButton createButton = new JButton("CREATE");
    private JTextField nameField = new JTextField();
    private JPasswordField passwordField1 = new JPasswordField();
    private JPasswordField passwordField2 = new JPasswordField();
    private JLabel userLabel;
    private JLabel passwordLabel1;
    private JLabel passwordLabel2;

    /**
     * Creates the frame
     * @param title
     */
    public RegisterFrame(String title) {
        super(title);

        setLayout(null);

        setSize(400,400);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Set custom Icon
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        setIconImage(icon.getImage());

        initNameField();
        initPasswordField1();
        initPasswordField2();
        initCreateButton();
        initReturnButton();

    }

    /**
     * Initializes the field to enter new data
     */
    private void initNameField(){
        nameField.setSize(150,20);
        nameField.setVisible(true);

        userLabel = new JLabel("USER");
        userLabel.setSize(150,20);
        userLabel.setVisible(true);

        add(nameField);
        add(userLabel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = ((getWidth() - nameField.getWidth()) / 2);
                int y = ((getHeight() - nameField.getHeight()) / 2) - 100;
                nameField.setLocation(x, y);
                int x2 = ((getWidth() - userLabel.getWidth()) / 2);
                int y2 = ((getHeight() - userLabel.getHeight()) / 2) - 120;
                userLabel.setLocation(x2, y2);
            }
        });
    }

    /**
     * First password field
     */
    private void initPasswordField1(){
        passwordField1.setSize(150,20);
        passwordField1.setVisible(true);

        passwordLabel1 = new JLabel("PASSWORD");
        passwordLabel1.setSize(150,20);
        passwordLabel1.setVisible(true);

        add(passwordField1);
        add(passwordLabel1);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = ((getWidth() - passwordField1.getWidth()) / 2);
                int y = ((getHeight() - passwordField1.getHeight()) / 2) - 50;
                passwordField1.setLocation(x, y);
                int x2 = ((getWidth() - passwordLabel1.getWidth()) / 2);
                int y2 = ((getHeight() - passwordLabel1.getHeight()) / 2) - 70;
                passwordLabel1.setLocation(x2, y2);
            }
        });
    }

    /**
     * Second password field
     */
    private void initPasswordField2(){
        passwordField2.setSize(150,20);
        passwordField2.setVisible(true);

        passwordLabel2 = new JLabel("REPEAT PASSWORD");
        passwordLabel2.setSize(150,20);
        passwordLabel2.setVisible(true);

        add(passwordField2);
        add(passwordLabel2);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = ((getWidth() - passwordField2.getWidth()) / 2);
                int y = ((getHeight() - passwordField2.getHeight()) / 2);
                passwordField2.setLocation(x, y);
                int x2 = ((getWidth() - passwordLabel2.getWidth()) / 2);
                int y2 = ((getHeight() - passwordLabel2.getHeight()) / 2) - 20;
                passwordLabel2.setLocation(x2, y2);
            }
        });
    }

    private void initCreateButton(){
        createButton.setSize(150, 20);
        createButton.setVisible(true);

        add(createButton);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try{

                    String p1 = new String(passwordField1.getPassword());
                    String p2 = new String(passwordField2.getPassword());

                    if (nameField.getText().trim().equals("") || nameField.getText() == null ||
                        p1.trim().equals("") || p1 == null || p2.trim().equals("") || p2 == null
                        ){

                        throw new IllegalArgumentException("Invalid Input!");

                    }

                    // Is fixed
                    if (!p1.equals(p2)){
                        System.out.println(p1 + " " + p2);
                        throw new IllegalArgumentException("Make sure you use the correct password!");

                    }

                    User u = new User(nameField.getText(), p1);

                    returnToLogin();

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = (getWidth() - createButton.getWidth()) / 2;
                int y = ((getHeight() - createButton.getHeight()) / 2) + 30;
                createButton.setLocation(x, y);
            }
        });
    }

    private void initReturnButton(){
        returnButton.setSize(150, 20);
        returnButton.setVisible(true);

        add(returnButton);

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                returnToLogin();

            }
        });


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = (getWidth() - returnButton.getWidth()) / 2;
                int y = ((getHeight() - returnButton.getHeight()) / 2) + 60;
                returnButton.setLocation(x, y);
            }
        });
    }

    private void returnToLogin(){
        //------------------------------------------------------------------
        setVisible(false);
        LoginFrame loginFrame = new LoginFrame("NOE-TO Login");

        //For some reason this only works when it is set up like this
        loginFrame.setSize(500,500);
        loginFrame.setResizable(true);
        loginFrame.setVisible(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        //------------------------------------------------------------------
    }
}
