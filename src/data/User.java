
package data;

/**
 *
 * @author Blake Thompson
 */
public class User extends DbEntry{
    private int userId;
    private String userName;
    private String password;
    private int active;
    public static String tableFields = "userName, password, active, createDate,"
            + "createdBy, lastUpdateBy";
    public User(int userId, String userName, String password, int active, 
            String createDate, String createdBy, String lastUpdate, String lastUpdateBy) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
}
