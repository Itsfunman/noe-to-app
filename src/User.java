package src;

public class User {

    private String name;
    private String password;
    private boolean hasAdminRights;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.hasAdminRights = false;

        LoginHandler.addUser(this.name, this.password);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasAdminRights() {
        return hasAdminRights;
    }

    public void setHasAdminRights(boolean hasAdminRights) {
        this.hasAdminRights = hasAdminRights;
    }
}
