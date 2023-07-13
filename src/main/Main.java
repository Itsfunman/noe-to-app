package main;

import utilityClasses.User;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static sqlStuff.DBConnection.getConnection;

/**
 * Main method of the program
 */
public class Main {

    /**
     * initializes loginFrame
     * @param args
     * @throws IOException
     * @throws UnsupportedLookAndFeelException
     */
    public static void main(String [] args) throws IOException, UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel(new MetalLookAndFeel());

        LoginFrame loginFrame = new LoginFrame("NOE-TO Login");

        //For some reason this only works when it is set up like this
        loginFrame.setSize(500,500);
        loginFrame.setResizable(true);
        loginFrame.setVisible(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);


    }

}
