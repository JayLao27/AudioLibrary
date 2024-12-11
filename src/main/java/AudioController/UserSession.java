package AudioController;

/**
 * Singleton class that manages the user's session information.
 * <p>
 * The {@link UserSession} class provides a global access point to the current user's session.
 * It ensures that only one instance of the session is maintained throughout the application's
 * lifecycle. This is implemented using the Singleton pattern to ensure that the same user session
 * is shared across different parts of the application.
 * </p>
 *
 * <p>
 * The session stores the {@code userID}, which represents the identifier of the current user.
 * The {@link #getUserID()} and {@link #setUserID(int)} methods provide access to the {@code userID},
 * while the {@link #clearSession()} method resets the session by clearing the user ID.
 * </p>
 *
 * <p>
 * This class is thread-safe due to its lazy initialization of the instance.
 * </p>
 *
 * @see #getInstance()
 */
public class UserSession {

    // The single instance of UserSession.
    private static UserSession instance;

    // The user ID for the current session.
    private int userID;

    // Private constructor to prevent instantiation from other classes.
    private UserSession() {}


    /**
     * Returns the singleton instance of the {@link UserSession}.
     * <p>
     * If the instance does not exist, a new one is created. This method ensures that only one
     * instance of the session is used throughout the application.
     * </p>
     *
     * @return the singleton instance of the {@link UserSession}.
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }


    /**
     * Gets the user ID for the current session.
     * <p>
     * This method retrieves the identifier of the current user, which is set through
     * the {@link #setUserID(int)} method.
     * </p>
     *
     * @return the current user ID.
     */
    public int getUserID() {
        return userID;
    }


    /**
     * Sets the user ID for the current session.
     * <p>
     * This method allows setting the {@code userID} for the current session. It can be used
     * when a user logs in or when the user ID needs to be updated.
     * </p>
     *
     * @param userID the new user ID to set for the session.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }


    /**
     * Clears the current session by resetting the user ID.
     * <p>
     * This method is typically called when a user logs out or when the session needs to be reset.
     * It resets the user ID to its default state (0).
     * </p>
     */
    public void clearSession() {
        userID = 0;
    }
}
