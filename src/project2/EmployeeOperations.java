package project2;

import java.sql.*;
import java.util.Scanner;

/**
 * This class provides operations for employees, such as viewing and updating personal information.
 */
public class EmployeeOperations {

    /**
     * Displays the employee menu and processes user selections.
     *
     * @param username the username of the logged-in employee.
     */
    public static void showEmployeeMenu(String username) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.printf("Welcome, %s (Employee)!%n", username);
            System.out.println("1- View Personal Information");
            System.out.println("2- Update Personal Information");
            System.out.println("3- Logout");
            System.out.print("Select an option: ");

            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (option) {
                    case 1:
                        viewPersonalInformation(username);
                        break;
                    case 2:
                        updatePersonalInformation(username);
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        return; // Exit menu
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
                System.out.println("\n\n"); // Add spacing for clarity and better usage
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clearing invalid input.
            }
        }
    }

    /**
     * Retrieves and displays the personal information of the employee.
     *
     * @param username the username of the employee.
     */
    private static void viewPersonalInformation(String username) {
        String query = "SELECT first_name, last_name, phone_no, email, date_of_birth, date_of_start " +
                       "FROM employees WHERE username = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Personal Information:");
                System.out.println("First Name: " + resultSet.getString("first_name"));
                System.out.println("Last Name: " + resultSet.getString("last_name"));
                System.out.println("Phone No: " + resultSet.getString("phone_no"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Date of Birth: " + resultSet.getDate("date_of_birth"));
                System.out.println("Date of Start: " + resultSet.getDate("date_of_start"));
            } else {
                System.out.println("Error retrieving personal information.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving personal information: " + e.getMessage());
        }
        System.out.println("\n\n"); // Add spacing for clarity
    }

    /**
     * Updates the personal information of the employee based on user input.
     *
     * @param username The username of the employee.
     */
    private static void updatePersonalInformation(String username) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select the information to update:");
            System.out.println("1. Phone Number");
            System.out.println("2. Email");
            System.out.println("3. Password");
            System.out.println("4. Back to Menu");

            String choiceStr = validateInput(scanner, "Enter your choice: ", "[1-4]", "Invalid choice! Only numbers 1-4 are allowed.", 3);
            if (choiceStr == null) return;

            int choice = Integer.parseInt(choiceStr);
            String updateQuery = null;
            String newValue = null;

            switch (choice) {
                case 1:
                    newValue = validateInput(scanner, "Enter new phone number: ", "\\d{10}", "Invalid phone number! Must be 10 digits.", 3);
                    updateQuery = "UPDATE employees SET phone_no = ? WHERE username = ?";
                    break;
                case 2:
                    newValue = validateInput(scanner, "Enter new email: ", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "Invalid email format!", 3);
                    updateQuery = "UPDATE employees SET email = ? WHERE username = ?";
                    break;
                case 3:
                    newValue = validateInput(scanner, "Enter new password: ", "^.{6,}$", "Invalid password! Must be at least 6 characters.", 3);
                    updateQuery = "UPDATE employees SET password = ? WHERE username = ?";
                    break;
                case 4:
                    System.out.println("Returning to menu...");
                    System.out.println("\n\n"); // Add spacing for clarity
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }

            if (newValue != null) {
                try (Connection connection = DatabaseUtil.getConnection();
                     PreparedStatement statement = connection.prepareStatement(updateQuery)) {

                    statement.setString(1, newValue);
                    statement.setString(2, username);
                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Information updated successfully!");
                    } else {
                        System.out.println("Error updating information.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error updating information: " + e.getMessage());
                }
            }
            System.out.println("\n\n"); // Adding spacing for better user interface
        }
    }

    /**
     * Validates user input based on a regular expression.
     *
     * @param scanner       the scanner for reading user input.
     * @param prompt        the message to display to the user.
     * @param regex         the regular expression for validating input.
     * @param errorMessage  the error message to display for invalid input.
     * @param maxAttempts   the maximum number of invalid attempts allowed.
     * @return the validated input as a string, or null if the user decides to exit.
     */
    private static String validateInput(Scanner scanner, String prompt, String regex, String errorMessage, int maxAttempts) {
        int attempts = 0;

        while (attempts < maxAttempts) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input.matches(regex)) {
                return input;
            } else {
                System.out.println(errorMessage);
                attempts++;
            }
        }

        System.out.println("You have made 3 invalid attempts.");
        System.out.println("1 - Yes");
        System.out.println("2 - No");
        String choice = validateInput(scanner, "Do you want to return to the main menu? (1/2): ", "[1-2]", "Invalid choice! Please select 1 or 2.", 3);
        if ("1".equals(choice)) {
            return null;
        }

        return validateInput(scanner, prompt, regex, errorMessage, maxAttempts);
    }
}
