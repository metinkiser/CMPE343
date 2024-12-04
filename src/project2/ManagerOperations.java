package project2;

import java.sql.*;
import java.util.Scanner;

public class ManagerOperations {

	public static void showManagerMenu(String username) {
	    Scanner scanner = new Scanner(System.in);

	    while (true) {
	        System.out.printf("Welcome, %s (Manager)!%n", username);
	        System.out.println("1- Displaying all employees");
	        System.out.println("2- Displaying employees by role");
	        System.out.println("3- Hiring employee");
	        System.out.println("4- Firing employee");
	        System.out.println("5- Updating employee info");
	        System.out.println("6- Run sorting algorithms");
	        System.out.println("7- View My Information");
	        System.out.println("8- Update My Information");
	        System.out.println("9- Logout");
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
	                    fireEmployee(username);
	                    break;
	                case 5:
	                    updateEmployeeInfo("Manager");
	                    break;
	                case 6:
	                    ManagerAlgorithms.runAlgorithmsMenu();
	                    break;
	                case 7:
	                    viewMyInformation(username);
	                    break;
	                case 8:
	                    updateMyInformation(username);
	                    break;
	                case 9:
	                    System.out.println("Logging out...");
	                    return;
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

	private static void viewMyInformation(String username) {
	    String query = "SELECT username, first_name, last_name, phone_no, email, date_of_birth, date_of_start " +
	                   "FROM employees WHERE LOWER(username) = LOWER(?)";

	    try (Connection connection = DatabaseUtil.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setString(1, username);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            System.out.printf("%-15s %-15s %-15s %-15s %-25s %-15s %-15s%n",
	                    "Username", "First Name", "Last Name", "Phone No", "Email", "Date of Birth", "Date of Start");
	            System.out.printf("%-15s %-15s %-15s %-15s %-25s %-15s %-15s%n",
	                    resultSet.getString("username"),
	                    resultSet.getString("first_name"),
	                    resultSet.getString("last_name"),
	                    resultSet.getString("phone_no"),
	                    resultSet.getString("email"),
	                    resultSet.getDate("date_of_birth"),
	                    resultSet.getDate("date_of_start"));
	        } else {
	            System.out.println("Error: Your information could not be found.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error retrieving your information: " + e.getMessage());
	    }
	    System.out.println("\n\n"); // 2 satır boşluk
	}

	private static void updateMyInformation(String username) {
	    Scanner scanner = new Scanner(System.in);

	    while (true) {
	        System.out.println("Select the information to update:");
	        System.out.println("1. First Name");
	        System.out.println("2. Last Name");
	        System.out.println("3. Phone Number");
	        System.out.println("4. Email");
	        System.out.println("5. Password");
	        System.out.println("6. Back to Menu");

	        String choiceStr = validateInput(scanner, "Enter your choice: ", "[1-6]", "Invalid choice! Only numbers 1-6 are allowed.", 3);
	        if (choiceStr == null) return;

	        int choice = Integer.parseInt(choiceStr);
	        String updateQuery = null;
	        String newValue = null;

	        switch (choice) {
	            case 1: // First Name
	                newValue = validateInput(scanner, "Enter new first name: ", "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", "Invalid name! Only letters are allowed.", 3);
	                updateQuery = "UPDATE employees SET first_name = ? WHERE LOWER(username) = LOWER(?)";
	                break;
	            case 2: // Last Name
	                newValue = validateInput(scanner, "Enter new last name: ", "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", "Invalid name! Only letters are allowed.", 3);
	                updateQuery = "UPDATE employees SET last_name = ? WHERE LOWER(username) = LOWER(?)";
	                break;
	            case 3: // Phone Number
	                newValue = validateInput(scanner, "Enter new phone number: ", "\\d{10}", "Invalid phone number! Must be 10 digits.", 3);
	                updateQuery = "UPDATE employees SET phone_no = ? WHERE LOWER(username) = LOWER(?)";
	                break;
	            case 4: // Email
	                newValue = validateInput(scanner, "Enter new email: ", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "Invalid email format!", 3);
	                updateQuery = "UPDATE employees SET email = ? WHERE LOWER(username) = LOWER(?)";
	                break;
	            case 5: // Password
	                newValue = validateInput(scanner, "Enter new password: ", "^.{6,}$", "Invalid password! Must be at least 6 characters.", 3);
	                updateQuery = "UPDATE employees SET password = ? WHERE LOWER(username) = LOWER(?)";
	                break;
	            case 6: // Back to Menu
	                System.out.println("Returning to menu...");
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
	                    System.out.println("Your information has been updated successfully!");
	                } else {
	                    System.out.println("Error: No rows were updated. Please try again.");
	                }
	            } catch (SQLException e) {
	                System.out.println("Error updating your information: " + e.getMessage());
	            }
	        }
	        System.out.println("\n\n"); // 2 satır boşluk
	    }
	}



    private static void displayAllEmployees() {
        String query = "SELECT e.employee_id, e.username, e.password, e.first_name, e.last_name, e.phone_no, e.email, e.date_of_birth, e.date_of_start, r.role_name " +
                       "FROM employees e " +
                       "JOIN employee_roles er ON e.employee_id = er.employee_id " +
                       "JOIN roles r ON er.role_id = r.role_id";

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.printf("%-10s %-15s %-15s %-15s %-15s %-15s %-25s %-15s %-15s %-15s%n",
                    "ID", "Username", "Password", "First Name", "Last Name", "Phone No", "Email", "Date of Birth", "Date of Start", "Role");

            while (resultSet.next()) {
                System.out.printf("%-10d %-15s %-15s %-15s %-15s %-15s %-25s %-15s %-15s %-15s%n",
                        resultSet.getInt("employee_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("phone_no"),
                        resultSet.getString("email"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getDate("date_of_start"),
                        resultSet.getString("role_name"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving employees: " + e.getMessage());
        }
        System.out.println("\n\n"); // 2 satır boşluk
    }

    private static void displayEmployeesByRole() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select a role:");
        System.out.println("1 - Manager");
        System.out.println("2 - Engineer");
        System.out.println("3 - Technician");
        System.out.println("4 - Intern");

        String roleNumber = validateInput(scanner, "Enter the role number: ", "[1-4]", "Invalid selection! Please choose a number between 1 and 4.", 3);
        if (roleNumber == null) return;

        String roleName = getRoleNameFromNumber(roleNumber);

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
        System.out.println("\n\n"); // 2 satır boşluk
    }
    
    private static boolean isUsernameUnique(String username) {
        String query = "SELECT COUNT(*) FROM employees WHERE LOWER(username) = LOWER(?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) == 0; // Eğer 0 ise username benzersizdir
            }
        } catch (SQLException e) {
            System.out.println("Error checking username uniqueness: " + e.getMessage());
        }
        return false; // Hata durumunda varsayılan olarak false döner
    }



    private static void hireEmployee() {
        Scanner scanner = new Scanner(System.in);

        String firstName = validateInput(scanner, "Enter first name: ", "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", "Invalid name! Only letters are allowed.", 3);
        if (firstName == null) return;

        String lastName = validateInput(scanner, "Enter last name: ", "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", "Invalid name! Only letters are allowed.", 3);
        if (lastName == null) return;

        String username = validateInput(scanner, "Enter username: ", "^[A-Za-z0-9çğıöşüÇĞİÖŞÜ_]+$", "Invalid username! Only alphanumeric characters and underscores are allowed.", 3);
        if (username == null) return;

        // Benzersiz username kontrolü
        if (!isUsernameUnique(username)) {
            System.out.println("This username is already taken. Please choose a different username.");
            return;
        }

        // Varsayılan şifre
        String defaultPassword = "password123";
        System.out.println("Default password 'password123' will be set for the new employee.");

        System.out.println("Select a role:");
        System.out.println("1 - Manager");
        System.out.println("2 - Engineer");
        System.out.println("3 - Technician");
        System.out.println("4 - Intern");

        String roleNumber = validateInput(scanner, "Enter the role number: ", "[1-4]", "Invalid selection! Please choose a number between 1 and 4.", 3);
        if (roleNumber == null) return;

        String roleName = getRoleNameFromNumber(roleNumber);

        String insertEmployeeQuery = "INSERT INTO employees (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
        String assignRoleQuery = "INSERT INTO employee_roles (employee_id, role_id) VALUES (LAST_INSERT_ID(), (SELECT role_id FROM roles WHERE role_name = ?))";

        try (Connection connection = DatabaseUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertEmployeeStmt = connection.prepareStatement(insertEmployeeQuery);
                 PreparedStatement assignRoleStmt = connection.prepareStatement(assignRoleQuery)) {

                insertEmployeeStmt.setString(1, username);
                insertEmployeeStmt.setString(2, defaultPassword); // Varsayılan şifre atanıyor
                insertEmployeeStmt.setString(3, firstName);
                insertEmployeeStmt.setString(4, lastName);
                insertEmployeeStmt.executeUpdate();

                assignRoleStmt.setString(1, roleName);
                assignRoleStmt.executeUpdate();

                connection.commit();
                System.out.println("Employee hired successfully with default password: " + defaultPassword);
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Error hiring employee: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        System.out.println("\n\n"); // 2 satır boşluk
    }




    private static void fireEmployee(String loggedInUsername) {
        Scanner scanner = new Scanner(System.in);

        // Kullanıcıdan silinecek employee_id alınır
        String employeeIdStr = validateInput(scanner, "Enter employee ID to fire: ", "\\d+", "Invalid ID! Only numbers are allowed.", 3);
        if (employeeIdStr == null) return;

        int employeeId = Integer.parseInt(employeeIdStr);

        // Kullanıcı kendisini silmeye çalışıyorsa kontrol
        String queryToCheckUsername = "SELECT username FROM employees WHERE employee_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(queryToCheckUsername)) {

            checkStatement.setInt(1, employeeId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                String usernameOfEmployee = resultSet.getString("username");
                if (usernameOfEmployee.equalsIgnoreCase(loggedInUsername)) {
                    System.out.println("You cannot fire yourself! Operation canceled.");
                    System.out.println("\n\n"); // 2 satır boşluk
                    return;
                }
            } else {
                System.out.println("Employee not found.");
                System.out.println("\n\n"); // 2 satır boşluk
                return;
            }

        } catch (SQLException e) {
            System.out.println("Error while checking employee: " + e.getMessage());
            System.out.println("\n\n"); // 2 satır boşluk
            return;
        }

        // Silme sorgusu
        String deleteQuery = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {

            deleteStatement.setInt(1, employeeId);
            int rowsDeleted = deleteStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Employee fired successfully!");
            } else {
                System.out.println("Employee not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error firing employee: " + e.getMessage());
        }
        System.out.println("\n\n"); // 2 satır boşluk
    }


    private static void updateEmployeeInfo(String loggedInRole) {
        Scanner scanner = new Scanner(System.in);

        // Güncellenmek istenen kullanıcının username'ini al
        String usernameToUpdate = validateInput(scanner, "Enter username to update: ", "^[A-Za-z0-9çğıöşüÇĞİÖŞÜ_]+$", "Invalid username! Only alphanumeric characters and underscores are allowed.", 3);
        if (usernameToUpdate == null) return;

        // Güncellenmek istenen username mevcut mu kontrol et
        if (isUsernameUnique(usernameToUpdate)) {
            System.out.println("User not found! Operation canceled.");
            return;
        }

        while (true) {
            System.out.println("Select the information to update:");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Phone Number");

            // Eğer kullanıcı Manager değilse, diğer alanlara erişim izni ver
            if (!"Manager".equalsIgnoreCase(loggedInRole)) {
                System.out.println("4. Email");
                System.out.println("5. Password");
            }

            System.out.println("6. Back to Menu");

            String choiceStr = validateInput(scanner, "Enter your choice: ", loggedInRole.equalsIgnoreCase("Manager") ? "[1-3|6]" : "[1-6]", "Invalid choice! Only valid options are allowed.", 3);
            if (choiceStr == null) return;

            int choice = Integer.parseInt(choiceStr);
            String updateQuery = null;
            String newValue = null;

            switch (choice) {
                case 1: // First Name
                    newValue = validateInput(scanner, "Enter new first name: ", "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", "Invalid name! Only letters are allowed.", 3);
                    updateQuery = "UPDATE employees SET first_name = ? WHERE LOWER(username) = LOWER(?)";
                    break;
                case 2: // Last Name
                    newValue = validateInput(scanner, "Enter new last name: ", "^[A-Za-zçğıöşüÇĞİÖŞÜ]+$", "Invalid name! Only letters are allowed.", 3);
                    updateQuery = "UPDATE employees SET last_name = ? WHERE LOWER(username) = LOWER(?)";
                    break;
                case 3: // Phone Number
                    newValue = validateInput(scanner, "Enter new phone number: ", "\\d{10}", "Invalid phone number! Must be 10 digits.", 3);
                    updateQuery = "UPDATE employees SET phone_no = ? WHERE LOWER(username) = LOWER(?)";
                    break;
                case 4: // Email (sadece Manager olmayanlar için)
                    if (!"Manager".equalsIgnoreCase(loggedInRole)) {
                        newValue = validateInput(scanner, "Enter new email: ", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "Invalid email format!", 3);
                        updateQuery = "UPDATE employees SET email = ? WHERE LOWER(username) = LOWER(?)";
                    } else {
                        System.out.println("Access denied! Managers cannot update this field.");
                        continue;
                    }
                    break;
                case 5: // Password (sadece Manager olmayanlar için)
                    if (!"Manager".equalsIgnoreCase(loggedInRole)) {
                        newValue = validateInput(scanner, "Enter new password: ", "^.{6,}$", "Invalid password! Must be at least 6 characters.", 3);
                        updateQuery = "UPDATE employees SET password = ? WHERE LOWER(username) = LOWER(?)";
                    } else {
                        System.out.println("Access denied! Managers cannot update this field.");
                        continue;
                    }
                    break;
                case 6: // Back to Menu
                    System.out.println("Returning to menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }

            if (newValue != null) {
                try (Connection connection = DatabaseUtil.getConnection();
                     PreparedStatement statement = connection.prepareStatement(updateQuery)) {

                    statement.setString(1, newValue);
                    statement.setString(2, usernameToUpdate);
                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Information updated successfully!");
                    } else {
                        System.out.println("Error: No rows were updated. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error updating employee info: " + e.getMessage());
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

    private static String getRoleNameFromNumber(String roleNumber) {
        switch (roleNumber) {
            case "1":
                return "Manager";
            case "2":
                return "Engineer";
            case "3":
                return "Technician";
            case "4":
                return "Intern";
            default:
                return null;
        }
    }
}
