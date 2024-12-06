package AudioController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class provides a method to establish a connection
 * to the MySQL database for the AudioLibrary application.
 *
 * It uses the JDBC API to connect to a database specified by the URL, user, and password.
 * If the connection fails, a {@link RuntimeException} is thrown with the underlying cause.
 *
 */
public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/AudioLibrary";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    /**
     * Establishes a connection to the database.
     *
     * This method attempts to connect to the database using the provided URL, username,
     * and password. If the connection is successful, it returns a {@link Connection} object.
     * Otherwise, it throws a {@link RuntimeException}.
     *
     * @return a {@link Connection} object representing the connection to the database.
     * @throws RuntimeException if the connection fails due to a {@link SQLException}.
     */
    public Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connection successful!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
