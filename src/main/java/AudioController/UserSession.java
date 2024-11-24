package AudioController;

public class UserSession {
    private static UserSession instance;

    private int userID;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void clearSession() {
        userID = 0;
    }
}
