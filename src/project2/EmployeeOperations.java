package project2;

import java.sql.*;
import java.util.Scanner;

public class EmployeeOperations {
    public static void showEmployeeMenu(String username) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.printf("Welcome, %s (Employee)!%n", username);
            System.out.println("1. Display profile");
            System.out.println("2. Update profile");
            System.out.println("3. Logout");
            System.out.print("Select an option: ");

            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Buffer temizleme

                switch (option) {
                    case 1:
                        displayProfile(username);
                        break;
                    case 2:
                        updateProfile(username);
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Hatalı giriş temizleme
            }
        }
    }

    private static void displayProfile(String username) {
        String query = "SELECT first_name, last_name, phone_no, email FROM employees WHERE username = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Profile Information:");
                System.out.printf("First Name: %s%n", resultSet.getString("first_name"));
                System.out.printf("Last Name: %s%n", resultSet.getString("last_name"));
                System.out.printf("Phone No: %s%n", resultSet.getString("phone_no"));
                System.out.printf("Email: %s%n", resultSet.getString("email"));
            } else {
                System.out.println("Profile not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving profile: " + e.getMessage());
        }
    }

    private static void updateProfile(String username) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select the information to update:");
            System.out.println("1. Phone Number");
            System.out.println("2. Email");
            System.out.println("3. Password");
            System.out.println("4. Back to Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Buffer temizleme

                String updateQuery = null;
                String newValue = null;

                switch (choice) {
                    case 1:
                        System.out.print("Enter new phone number: ");
                        newValue = scanner.nextLine();
                        updateQuery = "UPDATE employees SET phone_no = ? WHERE username = ?";
                        break;

                    case 2:
                        System.out.print("Enter new email: ");
                        newValue = scanner.nextLine();
                        updateQuery = "UPDATE employees SET email = ? WHERE username = ?";
                        break;

                    case 3:
                        System.out.print("Enter new password: ");
                        newValue = scanner.nextLine();
                        updateQuery = "UPDATE employees SET password = ? WHERE username = ?";
                        break;

                    case 4:
                        System.out.println("Returning to menu...");
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

                // Güncelleme işlemini gerçekleştir
                try (Connection connection = DatabaseUtil.getConnection();
                     PreparedStatement statement = connection.prepareStatement(updateQuery)) {

                    statement.setString(1, newValue);
                    statement.setString(2, username);

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Information updated successfully!");
                    } else {
                        System.out.println("Failed to update information.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error updating profile: " + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Hatalı giriş temizleme
            }
        }
    }
}

