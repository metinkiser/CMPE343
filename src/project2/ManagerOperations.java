package project2;

import java.sql.*;
import java.util.Scanner;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Providing some  functionalities for managers, which includes hiring, firing, 
 * and updating employee information, as well as displaying and sorting employees.
 * These functions are crucial for manager role and healthy for working process.
 */
public class ManagerOperations{
	
    /**
     * Displays the manager menu and processes user actions.
     *
     * @param username the username of the logged-in manager.
     */
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
            System.out.println("7- Logout");
            System.out.print("Select an option: ");

            try {
                int option = scanner.nextInt();
                scanner.nextLine(); 

                switch (option) {
                    case 1:
                        displayAllEmployees();
                        scanner.nextLine();
                        clearScreen();
                        break;
                    case 2:
                        displayEmployeesByRole();
                        scanner.nextLine(); 
                        clearScreen();
                        break;
                    case 3:
                        hireEmployee();
                        scanner.nextLine();
                        clearScreen();
                        break;
                    case 4:
                        fireEmployee();
                        scanner.nextLine(); 
                        clearScreen();
                        break;
                    case 5:
                        updateEmployeeInfo();
                        scanner.nextLine(); 
                        clearScreen();
                        break;
                    case 6:                  
                        ManagerAlgorithms.runAlgorithmsMenu();
                        clearScreen();
                        break;
                    case 7:
                        System.out.println("Logging out...");
                        scanner.nextLine();
                        clearScreen();
                        return; 
                    default:
                        System.out.println("Invalid option, please try again.");
                        scanner.nextLine();
                        clearScreen();
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please enter a number.");
                scanner.nextLine(); 
            }
        }
    }

    /**
     * Employees are displayed with their information under the light of their employee ID.
     */
    private static void displayAllEmployees() {
        String query = "SELECT e.employee_id, e.username, e.first_name, e.last_name, e.phone_no, e.email, e.date_of_birth, e.date_of_start, r.role_name " +
                       "FROM employees e " +
                       "JOIN employee_roles er ON e.employee_id = er.employee_id " +
                       "JOIN roles r ON er.role_id = r.role_id " +
                       "ORDER BY e.employee_id"; 

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.printf("%-10s %-20s %-15s %-15s %-15s %-25s %-15s %-15s %-15s%n", 
                              "ID", "Username", "First Name", "Last Name", "Phone No", "Email", "Date of Birth", "Date of Start", "Role");

            
            while (resultSet.next()) {
                System.out.printf("%-10d %-20s %-15s %-15s %-15s %-25s %-15s %-15s %-15s%n",
                        resultSet.getInt("employee_id"),
                        resultSet.getString("username"),  
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
    }


    /**
     * Employees are filtered by their role and then display action occurs.
     */
    private static void displayEmployeesByRole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a role:");
        System.out.println("1- Manager");
        System.out.println("2- Engineer");
        System.out.println("3- Technician");
        System.out.println("4- Intern");
        System.out.print("Enter your choice: ");

        int roleOption = scanner.nextInt();
        scanner.nextLine();

        String roleName;
        switch (roleOption) {
            case 1:
                roleName = "Manager";
                break;
            case 2:
                roleName = "Engineer";
                break;
            case 3:
                roleName = "Technician";
                break;
            case 4:
                roleName = "Intern";
                break;
            default:
                System.out.println("Invalid choice. Returning to menu.");
                return;
        }
        

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


    /**
     * Details of a new employee collecting and these informations are inserted into the database.
     */
    private static void hireEmployee() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine().toLowerCase();

            if (!isUsernameUnique(username)) {
                System.out.println("Username already exists. Please choose another one.");
            } else {
                break;
            }
        }


        String password = generateRandomPassword();
        System.out.println("Generated password for the new employee: " + password);


        String phoneNumber;
        while (true) {
            System.out.print("Enter phone number (must start with 5 and be 10 digits): ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.length() == 10 && phoneNumber.matches("5\\d{9}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter a valid 10-digit phone number starting with 5.");
            }
        }


        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                break;
            } else {
                System.out.println("Invalid email address. Please enter a valid email.");
            }
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String dateOfBirth;
        while (true) {
            System.out.print("Enter date of birth (YYYY-MM-DD): ");
            dateOfBirth = scanner.nextLine();
            if (dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
                LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
                if (Period.between(birthDate, LocalDate.now()).getYears() >= 18) {
                    break;
                } else {
                    System.out.println("Employee must be at least 18 years old.");
                }
            } else {
                System.out.println("Invalid date format. Please enter a date in the format YYYY-MM-DD.");
            }
        }

        String dateOfStart;
        while (true) {
            System.out.print("Enter date of start (YYYY-MM-DD): ");
            dateOfStart = scanner.nextLine();
            if (dateOfStart.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    LocalDate startDate = LocalDate.parse(dateOfStart, formatter);
                    if (!startDate.isBefore(LocalDate.now())) {
                        break; 
                    } else {
                        System.out.println("Start date cannot be before today's date.");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter a date in the format YYYY-MM-DD.");
                }
            } else {
                System.out.println("Invalid date format. Please enter a date in the format YYYY-MM-DD.");
            }
        }


        String roleName;
        while (true) {
            System.out.print("Enter role (Manager, Engineer, Technician, Intern): ");
            roleName = scanner.nextLine();
            if (isValidRole(roleName)) {
                break;
            } else {
                System.out.println("Invalid role. Please enter a valid role.");
            }
        }

        String insertEmployeeQuery = "INSERT INTO employees (username, password, first_name, last_name, phone_no, email, date_of_birth, date_of_start) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String assignRoleQuery = "INSERT INTO employee_roles (employee_id, role_id) VALUES ((SELECT employee_id FROM employees WHERE username = ?), (SELECT role_id FROM roles WHERE role_name = ?))";

        try (Connection connection = DatabaseUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertEmployeeStmt = connection.prepareStatement(insertEmployeeQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement assignRoleStmt = connection.prepareStatement(assignRoleQuery)) {

                insertEmployeeStmt.setString(1, username);
                insertEmployeeStmt.setString(2, password);
                insertEmployeeStmt.setString(3, firstName);
                insertEmployeeStmt.setString(4, lastName);
                insertEmployeeStmt.setString(5, phoneNumber);
                insertEmployeeStmt.setString(6, email);
                insertEmployeeStmt.setString(7, dateOfBirth);
                insertEmployeeStmt.setString(8, dateOfStart);
                insertEmployeeStmt.executeUpdate();

                ResultSet generatedKeys = insertEmployeeStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    assignRoleStmt.setString(1, username);
                    assignRoleStmt.setString(2, roleName);
                    assignRoleStmt.executeUpdate();

                    connection.commit();
                    System.out.println("Employee hired successfully!");
                } else {
                    connection.rollback();
                    System.out.println("Failed to retrieve employee ID.");
                }
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Error hiring employee: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static boolean isUsernameUnique(String username) {
        String query = "SELECT COUNT(*) FROM employees WHERE LOWER(username) = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username.toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next() || resultSet.getInt(1) == 0;
        } catch (SQLException e) {
            System.out.println("Error checking username uniqueness: " + e.getMessage());
        }
        return false;
    }

    private static String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 8; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    private static boolean isValidRole(String roleName) {
        return roleName.equalsIgnoreCase("Manager") ||
               roleName.equalsIgnoreCase("Engineer") ||
               roleName.equalsIgnoreCase("Technician") ||
               roleName.equalsIgnoreCase("Intern");
    }



    /**
     * This function fires an employee by their username.
     */
    private static void fireEmployee() {
        Scanner scanner = new Scanner(System.in);

        // Kullanıcıdan oturum açmış kullanıcı adını alın
        System.out.print("Enter your username (current logged-in user): ");
        String currentUsername = scanner.nextLine().toLowerCase();

        // Çalışan listesini göster
        System.out.println("Current employees:");
        displayAllEmployees(); // Çalışanları listelemek için bu metodu tanımlayın

        // Silmek istediğiniz çalışanın kullanıcı adını alın
        System.out.print("Enter employee username to fire: ");
        String username = scanner.nextLine().toLowerCase();

        // Kendini silmeye izin verme
        if (username.equalsIgnoreCase(currentUsername)) {
            System.out.println("You cannot fire yourself.");
            return;
        }

        // SQL sorgusu: Çalışanı sil
        String deleteQuery = "DELETE FROM employees WHERE LOWER(username) = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, username);
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

    private static boolean isManagerByUsername(String username) {
        String query = "SELECT COUNT(*) FROM employee_roles er JOIN roles r ON er.role_id = r.role_id JOIN employees e ON er.employee_id = e.employee_id WHERE LOWER(e.username) = ? AND r.role_name = 'Manager'";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username.toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if employee is a manager: " + e.getMessage());
        }
        return false;
    }


    /**
     * Updates an employee's information based on user input.
     */
    private static void updateEmployeeInfo() {
    	  Scanner scanner = new Scanner(System.in);

    	  System.out.println("Current employees:");
          displayAllEmployees();

    	    System.out.print("Enter the username of the employee to update: ");
    	    String username = scanner.nextLine().toLowerCase();

    	    if (!isUsernameExists(username)) {
    	        System.out.println("Employee not found.");
    	        return;
    	    }

    	    while (true) {
    	        System.out.println("Select the information to update:");
    	        System.out.println("1. First Name");
    	        System.out.println("2. Last Name");
    	        System.out.println("3. Date of Birth (YYYY-MM-DD)");
    	        System.out.println("4. Date of Start (YYYY-MM-DD)");
    	        System.out.println("5. Back to Menu");
    	        System.out.print("Enter your choice: ");

    	        try {
    	            int choice = scanner.nextInt();
    	            scanner.nextLine(); 

    	            String updateQuery = null;
    	            String newValue = null;

    	            switch (choice) {
    	                case 1:
    	                    System.out.print("Enter new first name: ");
    	                    newValue = scanner.nextLine();
    	                    updateQuery = "UPDATE employees SET first_name = ? WHERE LOWER(username) = ?";
    	                    break;

    	                case 2:
    	                    System.out.print("Enter new last name: ");
    	                    newValue = scanner.nextLine();
    	                    updateQuery = "UPDATE employees SET last_name = ? WHERE LOWER(username) = ?";
    	                    break;

    	                case 3:
    	                    System.out.print("Enter new date of birth (YYYY-MM-DD): ");
    	                    newValue = scanner.nextLine();
    	                    updateQuery = "UPDATE employees SET date_of_birth = ? WHERE LOWER(username) = ?";
    	                    break;

    	                case 4:
    	                    System.out.print("Enter new date of start (YYYY-MM-DD): ");
    	                    newValue = scanner.nextLine();
    	                    updateQuery = "UPDATE employees SET date_of_start = ? WHERE LOWER(username) = ?";
    	                    break;

    	                case 5:
    	                    System.out.println("Returning to menu...");
    	                    return;

    	                default:
    	                    System.out.println("Invalid choice. Please try again.");
    	                    continue;
    	            }

    	            try (Connection connection = DatabaseUtil.getConnection();
    	                 PreparedStatement statement = connection.prepareStatement(updateQuery)) {

    	                statement.setString(1, newValue);
    	                statement.setString(2, username);

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
    	            scanner.nextLine(); 
    	        }
    	    }
    	}

    	private static void listAllUsernames() {
    	    String query = "SELECT username FROM employees";
    	    try (Connection connection = DatabaseUtil.getConnection();
    	         Statement statement = connection.createStatement();
    	         ResultSet resultSet = statement.executeQuery(query)) {

    	        while (resultSet.next()) {
    	            System.out.println(resultSet.getString("username"));
    	        }
    	    } catch (SQLException e) {
    	        System.out.println("Error listing usernames: " + e.getMessage());
    	    }
    	}

    	private static boolean isUsernameExists(String username) {
    	    String query = "SELECT COUNT(*) FROM employees WHERE LOWER(username) = ?";
    	    try (Connection connection = DatabaseUtil.getConnection();
    	         PreparedStatement statement = connection.prepareStatement(query)) {

    	        statement.setString(1, username);
    	        ResultSet resultSet = statement.executeQuery();
    	        if (resultSet.next() && resultSet.getInt(1) > 0) {
    	            return true;
    	        }
    	    } catch (SQLException e) {
    	        System.out.println("Error checking username existence: " + e.getMessage());
    	    }
    	    return false;
    	}
    	
    	
        /**
         * Console screens clearence is done for a better user interface.
         */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
