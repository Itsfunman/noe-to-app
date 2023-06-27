package src;

import org.w3c.dom.events.EventException;

import java.util.HashMap;

/**
 * Is used to check inpuit in the LoginFrame
 */
public class LoginHandler {

    private static HashMap <String, String> loginData = new HashMap<>();

    /**
     * Initializes the LoginHandler
     */
    public LoginHandler(){

    }

    /**
     * Adds Users to the loginData
     * @param name
     * @param password
     */
    public static void addUser(String name, String password){
        try {
            for (String s : loginData.keySet()){
                if (s.equals(name)){
                    throw new IllegalArgumentException("User already exists!");
                }
            }

            loginData.put(name, password);

        } catch(Exception e){
            e.printStackTrace();
        }


    }

    /**
     * Getter for Login Data
     * @return HashMap<String, String>
     */
    public static HashMap<String, String> getLoginData() {
        return loginData;
    }

}
