
package part1;

/**
 *Student Number:
 * Full Name: 
 * Assignment: 
 * 
 */
public class UserCredentials {
    private static UserCredentials instance;
    private String username;
    private String password;

    private UserCredentials() 
    {
    
    }

    public static UserCredentials getInstance() {
        if (instance == null) {
            instance = new UserCredentials();
        }
        return instance;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean checkCredentials(String username, String password) {
        return this.username != null && this.username.equals(username) && this.password != null && this.password.equals(password);
    }
}
