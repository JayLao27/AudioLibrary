package AudioController;

/**
 * Represents a user in the system with personal and account information.
 * <p>
 * The {@link User} class encapsulates the details of a user, including their username,
 * first and last names, email address, and account balance. This class serves as a model
 * for user data that can be used throughout the application.
 * </p>
 *
 * <p>
 * Instances of this class are typically created when a user logs in, registers, or when
 * their data needs to be stored or retrieved in the system.
 * </p>
 */
public class User {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private double balance;

    /**
     * Constructs a new {@link User} object with the specified user details.
     * <p>
     * This constructor initializes the user with their username, first name, last name,
     * email address, and account balance. The user object represents a registered user
     * in the system.
     * </p>
     *
     * @param userName the username of the user (unique identifier).
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @param email the email address of the user.
     * @param balance the account balance of the user (e.g., for purchases or transactions).
     */
    public User(String userName, String firstName, String lastName, String email, double balance) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.balance = balance;
    }

    /**
     * Gets the username of the user.
     * <p>
     * The username is typically used as the unique identifier for the user in the system.
     * </p>
     *
     * @return the username of the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the first name of the user.
     *
     * @return the first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return the last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the current account balance of the user.
     * <p>
     * The balance represents the user's financial status in the system, such as available funds.
     * </p>
     *
     * @return the balance of the user.
     */
    public double getBalance() {
        return balance;
    }
}
