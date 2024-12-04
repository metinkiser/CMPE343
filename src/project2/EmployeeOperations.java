package project2;

import java.sql.*;
import java.util.Scanner;

public class EmployeeOperations {

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
                scanner.nextLine(); // Buffer temizleme

                switch (option) {
                    case 1:
                        viewPersonalInformation(username);
                        break;
                    case 2:
                        updatePersonalInformation(username);
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        return; // Logout işlemi
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
                System.out.println("\n\n"); // 2 satır boşluk
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Hatalı girişleri temizle
            }
        }
    }

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
        System.out.println("\n\n"); // 2 satır boşluk
    }

    private static void updatePersonalInformation(String username) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select the information to update:");
            System.out.println("1. Phone Number");
            System.out.println("2. Email");
            System.out.println("3. Password");
            System.out.println("4. Back to Menu");

            // Kullanıcının seçimini doğrula
            String choiceStr = validateInput(scanner, "Enter your choice: ", "[1-4]", "Invalid choice! Only numbers 1-4 are allowed.", 3);
            if (choiceStr == null) return;

            int choice = Integer.parseInt(choiceStr);
            String updateQuery = null;
            String newValue = null;

            switch (choice) {
                case 1: // Phone Number güncelleme
                    newValue = validateInput(scanner, "Enter new phone number: ", "\\d{10}", "Invalid phone number! Must be 10 digits.", 3);
                    updateQuery = "UPDATE employees SET phone_no = ? WHERE username = ?";
                    break;
                case 2: // Email güncelleme
                    newValue = validateInput(scanner, "Enter new email: ", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "Invalid email format!", 3);
                    updateQuery = "UPDATE employees SET email = ? WHERE username = ?";
                    break;
                case 3: // Password güncelleme
                    newValue = validateInput(scanner, "Enter new password: ", "^.{6,}$", "Invalid password! Must be at least 6 characters.", 3);
                    updateQuery = "UPDATE employees SET password = ? WHERE username = ?";
                    break;
                case 4: // Menüye geri dön
                    System.out.println("Returning to menu...");
                    System.out.println("\n\n"); // 2 satır boşluk
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }

            // Eğer kullanıcı geçerli bir bilgi girdi ise güncelleme sorgusunu çalıştır
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
               System.out.println("\n\n"); // 2 satır boşluk
           }
       }

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
