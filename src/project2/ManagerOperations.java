package project2;

import java.sql.*;
import java.util.Scanner;

public class ManagerOperations {
    public static void showManagerMenu(String username) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.printf("Welcome, %s (Manager)!%n", username);
            System.out.println("1-Displaying all employees");
            System.out.println("2-Displaying employees by role");
            System.out.println("3-Hiring employee");
            System.out.println("4-Firing employee");
            System.out.println("5-Updating employee info");
            System.out.println("6-Logging out");
            System.out.print("Select an option: ");

            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Buffer temizleme

                switch (option) {
                    case 1:
                        displayAllEmployees();
                        break;
                    case 2:
                        displayEmployeesByRole();
                        break;
                    case 3:
                        hireEmployee();
                        break;
                    case 4:
                        fireEmployee();
                        break;
                    case 5:
                        updateEmployeeInfo();
                        break;
                    case 6:
                        System.out.println("Logging out...");
                        return; // Logout işlemi
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please enter a number.");
                scanner.nextLine(); // Geçersiz girişleri temizle
            }
        }
    }

    private static void displayAllEmployees() {
        String query = "SELECT e.employee_id, e.first_name, e.last_name, r.role_name " +
                       "FROM employees e " +
                       "JOIN employee_roles er ON e.employee_id = er.employee_id " +
                       "JOIN roles r ON er.role_id = r.role_id";

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.printf("%-10s %-15s %-15s %-15s%n", "ID", "First Name", "Last Name", "Role");
            while (resultSet.next()) {
                System.out.printf("%-10d %-15s %-15s %-15s%n",
                        resultSet.getInt("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("role_name"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving employees: " + e.getMessage());
        }
    }

    private static void displayEmployeesByRole() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter role name (Manager, Engineer, Technician, Intern): ");
        String roleName = scanner.nextLine();

        String query = "SELECT e.employee_id, e.first_name, e.last_name " +
                       "FROM employees e " +
                       "JOIN employee_roles er ON e.employee_id = er.employee_id " +
                       "JOIN roles r ON er.role_id = r.role_id " +
                       "WHERE r.role_name = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, roleName);
            ResultSet resultSet = statement.executeQuery();

            System.out.printf("%-10s %-15s %-15s%n", "ID", "First Name", "Last Name");
            while (resultSet.next()) {
                System.out.printf("%-10d %-15s %-15s%n",
                        resultSet.getInt("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving employees by role: " + e.getMessage());
        }
    }

    private static void hireEmployee() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter default password: ");
        String password = scanner.nextLine();

        System.out.print("Enter role (Manager, Engineer, Technician, Intern): ");
        String roleName = scanner.nextLine();

        String insertEmployeeQuery = "INSERT INTO employees (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
        String assignRoleQuery = "INSERT INTO employee_roles (employee_id, role_id) VALUES (LAST_INSERT_ID(), (SELECT role_id FROM roles WHERE role_name = ?))";

        try (Connection connection = DatabaseUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertEmployeeStmt = connection.prepareStatement(insertEmployeeQuery);
                 PreparedStatement assignRoleStmt = connection.prepareStatement(assignRoleQuery)) {

                insertEmployeeStmt.setString(1, username);
                insertEmployeeStmt.setString(2, password);
                insertEmployeeStmt.setString(3, firstName);
                insertEmployeeStmt.setString(4, lastName);
                insertEmployeeStmt.executeUpdate();

                assignRoleStmt.setString(1, roleName);
                assignRoleStmt.executeUpdate();

                connection.commit();
                System.out.println("Employee hired successfully!");
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Error hiring employee: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void fireEmployee() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter employee ID to fire: ");
        int employeeId = scanner.nextInt();

        String deleteQuery = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setInt(1, employeeId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Employee fired successfully!");
            } else {
                System.out.println("Employee not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error firing employee: " + e.getMessage());
        }
    }

    private static void updateEmployeeInfo() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter employee ID to update: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine(); // Buffer temizleme

        while (true) {
            System.out.println("Select the information to update:");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Phone Number");
            System.out.println("4. Email");
            System.out.println("5. Password");
            System.out.println("6. Back to Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Buffer temizleme

                String updateQuery = null;
                String newValue = null;

                switch (choice) {
                    case 1:
                        System.out.print("Enter new first name: ");
                        newValue = scanner.nextLine();
                        updateQuery = "UPDATE employees SET first_name = ? WHERE employee_id = ?";
                        break;

                    case 2:
                        System.out.print("Enter new last name: ");
                        newValue = scanner.nextLine();
                        updateQuery = "UPDATE employees SET last_name = ? WHERE employee_id = ?";
                        break;

                    case 3:
                        System.out.print("Enter new phone number: ");
                        newValue = scanner.nextLine();
                        updateQuery = "UPDATE employees SET phone_no = ? WHERE employee_id = ?";
                        break;

                    case 4:
                        System.out.print("Enter new email: ");
                        newValue = scanner.nextLine();
                        updateQuery = "UPDATE employees SET email = ? WHERE employee_id = ?";
                        break;

                    case 5:
                        System.out.print("Enter new password: ");
                        newValue = scanner.nextLine();
                        updateQuery = "UPDATE employees SET password = ? WHERE employee_id = ?";
                        break;

                    case 6:
                        System.out.println("Returning to menu...");
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

                try (Connection connection = DatabaseUtil.getConnection();
                     PreparedStatement statement = connection.prepareStatement(updateQuery)) {

                    statement.setString(1, newValue);
                    statement.setInt(2, employeeId);

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Information updated successfully!");
                    } else {
                        System.out.println("Employee not found.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error updating employee info: " + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.nextLine(); // Geçersiz girişleri temizle
            }
        }
    }
}

