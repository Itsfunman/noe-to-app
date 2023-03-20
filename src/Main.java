package src;

import javax.swing.*;

public class Main {

    public static void main(String [] args){

        LoginFrame loginFrame = new LoginFrame("NOE-TO Login");

        //For some reason this only works when it is set up like this
        loginFrame.setSize(500,500);
        loginFrame.setResizable(true);
        loginFrame.setVisible(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        User t = new User("Tom", "123");
        User s = new User("Susi", "test");
    }
}
