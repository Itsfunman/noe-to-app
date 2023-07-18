package objectClasses;

import java.io.Serializable;

//Class used because hibernate needs an id
public class UserId implements Serializable {

    private String name;
    private String password;

}