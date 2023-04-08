package src;

import org.w3c.dom.events.EventException;

import java.util.HashMap;

public class LoginHandler {

    private static HashMap <String, String> loginData = new HashMap<>();

    public LoginHandler(){

    }

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

    public static HashMap<String, String> getLoginData() {
        return loginData;
    }

    public static void setLoginData(HashMap<String, String> loginData) {
        LoginHandler.loginData = loginData;
    }
}
