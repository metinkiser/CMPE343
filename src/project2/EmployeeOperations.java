package project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 */
public class DatabaseUtil {
	 /**
     * The URL of the database to connect to, including timezone settings.
     */
	
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/firm_management?useTimezone=true&serverTimezone=UTC";
    /**
     * The username required for database access.
     */
    
    private static final String DATABASE_USER = "root"; // MySQL username

    /**
     * The password required for database access.
     */
    
    private static final String DATABASE_PASSWORD = "Ananas93."; // MySQL password
    /**
     * Establishes a connection to the database using the provided credentials.
     *
     * @return a {@link Connection} object that allows interaction with the database.
     * @throws SQLException if the connection cannot be established or if a database access error occurs.
     */
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }
}
