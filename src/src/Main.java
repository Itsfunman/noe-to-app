package src;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static boolean dataLoaded = false;

    public static void main(String [] args) throws IOException, UnsupportedLookAndFeelException {

        //UIManager.setLookAndFeel(new FlatLightLaf());

        LoginFrame loginFrame = new LoginFrame("NOE-TO Login");

        //For some reason this only works when it is set up like this
        loginFrame.setSize(500,500);
        loginFrame.setResizable(true);
        loginFrame.setVisible(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        //This code is set up like this because otherwise we would create the same users a million times
        //while using the application

        if (!dataLoaded){
            loadData();
            dataLoaded = true;
        }

    }

    public static void loadData() throws IOException {

        FileReader fileReader = new FileReader("data/loginData.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();

        while (line != null){
            String [] parts = line.split(",");
            User u = new User(parts[0], parts[1]);
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
    }
}
