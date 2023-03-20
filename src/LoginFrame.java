package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class LoginFrame extends JFrame {

    private JButton loginButton = new JButton("Login");
    private JTextField nameField = new JTextField("User");
    private JPasswordField passwordField = new JPasswordField("Password");
    private JButton registerButton = new JButton("Register");

    public LoginFrame(String title) {
        super(title);

        setLayout(null);

        nameFieldInit();
        passwordFieldInit();
        loginButtonInit();
        registerButtonInit();
        addLogo();

    }

    private void addLogo(){
      ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
      Image img = icon.getImage();

      Dimension size = getSize();
      int width = size.width/4;
      int height = size.height/4;
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

    private void nameFieldInit(){
        nameField.setSize(100,20);
        nameField.setVisible(true);

        add(nameField);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = ((getWidth() - nameField.getWidth()) / 2);
                int y = ((getHeight() - nameField.getHeight()) / 2) - 30;
                nameField.setLocation(x, y);
            }
        });
    }

    private void passwordFieldInit(){
        passwordField.setSize(100,20);
        passwordField.setVisible(true);

        add(passwordField);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = ((getWidth() - passwordField.getWidth()) / 2);
                int y = ((getHeight() - passwordField.getHeight()) / 2);
                passwordField.setLocation(x, y);
            }
        });
    }

    private void loginButtonInit(){
        loginButton.setSize(100, 20);
        loginButton.setVisible(true);

        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (checkInput()){
                    System.out.println("Valid input!");
                    setVisible(false);

                    //------------------------------------
                    MainFrame frame = new MainFrame("NOE-TO");

                    //....................................
                } else {
                    System.out.println("Invalid input!");
                }
            }
        });
        checkInput();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = (getWidth() - loginButton.getWidth()) / 2;
                int y = ((getHeight() - loginButton.getHeight()) / 2) + 30;
                loginButton.setLocation(x, y);
            }
        });
    }

    private void registerButtonInit(){
        registerButton.setSize(100, 20);
        registerButton.setVisible(true);

        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                RegisterFrame registerFrame = new RegisterFrame("Registration");

            }
        });
        checkInput();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = (getWidth() - registerButton.getWidth()) / 2;
                int y = ((getHeight() - registerButton.getHeight()) / 2) + 60;
                registerButton.setLocation(x, y);
            }
        });
    }

    private boolean checkInput(){

        for (String s : LoginHandler.getLoginData().keySet()){
            if (nameField.getText().equals(s)){

                String p = new String(passwordField.getPassword());
                if (p.equals(LoginHandler.getLoginData().get(s))){
                    return true;
                } else if (!p.equals(LoginHandler.getLoginData().get(s))){
                    System.out.println("Wrong Password!");
                    return false;
                }

            }
        }
        return false;
    }

}
