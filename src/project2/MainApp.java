package project2;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main application class for the Firm Management System.
 * Handles the main application loop, user login, and role-based menu navigation.
 */
public class MainApp {

    /**
     * The entry point of the application.
     * Continuously prompts the user for login credentials and navigates them to the appropriate menu based on their role.
     *
     * @param args command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        while (true) { // Loop for re-login after logout
            try (Connection connection = DatabaseUtil.getConnection()) {
                // Prompt user to log in
                String username = LoginScreen.promptLogin(connection);

                // Determine the role of the logged-in user
                String role = LoginScreen.getUserRole(connection, username);

                // Role-based navigation
                if ("Manager".equalsIgnoreCase(role)) {
                    ManagerOperations.showManagerMenu(username);
                } else if ("Engineer".equalsIgnoreCase(role) || "Technician".equalsIgnoreCase(role) || "Intern".equalsIgnoreCase(role)) {
                    EmployeeOperations.showEmployeeMenu(username);
                } else {
                    System.out.println("Invalid role, exiting application...");
                    break;
                }
            } catch (SQLException e) {
                System.out.println("Database connection error: " + e.getMessage());
                break;
            }
        }
    }
}
