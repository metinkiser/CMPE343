package project2;

import java.sql.*;
import java.util.Scanner;

/**
 * Handles the login functionality for the Firm Management System.
 * Provides methods for authenticating users and retrieving their roles.
 */
public class LoginScreen {

    /**
     * Clears the console screen for a cleaner user interface.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Displays the login screen, handles user input for login or termination, 
     * and returns the authenticated username if the login is successful.
     *
     * @param connection the database connection for authentication.
     * @return the authenticated username if login is successful, or exits the program on termination.
     */
    public static String promptLogin(Connection connection) {
        clearScreen();
        Scanner scanner = new Scanner(System.in);

        // Welcome message
        System.out.println("\u001B[35m" + "       /\\     /\\        /\\     /\\        /\\     /\\        /\\     /\\        /\\     /\\" + "\u001B[0m");
        System.out.println("\u001B[36m" + "      {  `---'  }      {  `---'  }      {  `---'  }      {  `---'  }      {  `---'  }" + "\u001B[0m");
        System.out.println("\u001B[33m" + "      {  O   O  }      {  O   O  }      {  O   O  }      {  O   O  }      {  O   O  }" + "\u001B[0m");
        System.out.println("\u001B[32m" + "~~~-oOOo-----oOOo~~~~oOOo-----oOOo~~~~oOOo-----oOOo~~~~oOOo-----oOOo~~~~oOOo-----oOOo~~~" + "\u001B[0m");
        System.out.println("\u001B[34m" + "     /        \\        /        \\        /        \\        /        \\        /        \\" + "\u001B[0m");
        System.out.println("\u001B[31m" + "    /          \\      /          \\      /          \\      /          \\      /          \\" + "\u001B[0m");
        System.out.println("\u001B[36m" + "   |            |    |            |    |            |    |            |    |            |" + "\u001B[0m");
        System.out.println("\u001B[33m" + "    \\  ()  ()  /      \\  ()  ()  /      \\  ()  ()  /      \\  ()  ()  /      \\  ()  ()  /" + "\u001B[0m");
        System.out.println("\u001B[35m" + "     \\        /        \\        /        \\        /        \\        /        \\        /" + "\u001B[0m");
        System.out.println("\u001B[32m" + "      `------'          `------'          `------'          `------'          `------'" + "\u001B[0m");
        System.out.println("\u001B[33m" + "	  	██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗" + "\u001B[0m");
        System.out.println("\u001B[36m" + "		██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝" + "\u001B[0m");
        System.out.println("\u001B[35m" + "		██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗  " + "\u001B[0m");
        System.out.println("\u001B[32m" + "		██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝  " + "\u001B[0m");
        System.out.println("\u001B[31m" + "		╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗" + "\u001B[0m");
        System.out.println("\u001B[34m" + "		 ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝" + "\u001B[0m");
        System.out.println("\u001B[36m" + "			  WELCOME TO FIRM MANAGEMENT SYSTEM " + "\u001B[0m");

        while (true) {
            System.out.println("=====Firm Management System=====");
            System.out.println("1- Login");
            System.out.println("2- Terminate");
            System.out.print("Select an option: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }

            switch (option) {
                case 1:
                    // Prompt user for login credentials
                    clearScreen();
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    clearScreen();

                    if (authenticateUser(connection, username, password)) {
                        System.out.println("Login successful!");
                        clearScreen();
                        return username;
                    } else {
                        System.out.println("Invalid username or password, please try again.");
                    }
                    break;
                case 2:
                    // Exit the program
                    System.out.println("Terminating the system. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option, please select 1 or 2.");
                    clearScreen();
            }
        }
    }

    /**
     * Authenticates a user by verifying their username and password against the database.
     *
     * @param connection the database connection.
     * @param username   the username provided by the user.
     * @param password   the password provided by the user.
     * @return true if the username and password match a record in the database, false otherwise.
     */
    public static boolean authenticateUser(Connection connection, String username, String password) {
        String query = "SELECT COUNT(*) FROM employees WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Authentication error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves the role of a user from the database based on their username.
     *
     * @param connection the database connection.
     * @param username   the username of the user.
     * @return the role name of the user if found, or null otherwise.
     */
    public static String getUserRole(Connection connection, String username) {
        String query = "SELECT r.role_name " +
                       "FROM employees e " +
                       "JOIN employee_roles er ON e.employee_id = er.employee_id " +
                       "JOIN roles r ON er.role_id = r.role_id " +
                       "WHERE e.username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("role_name");
            }
        } catch (SQLException e) {
            System.out.println("Retrieve user role error: " + e.getMessage());
        }
        return null;
    }
}
