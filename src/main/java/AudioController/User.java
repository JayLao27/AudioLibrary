package AudioController;

public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private double balance;

    public User(String userName, String firstName, String lastName, String email, double balance) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }
}
