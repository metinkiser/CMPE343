package project2;

import java.sql.*;
import java.util.Scanner;

public class LoginScreen {
    public static String promptLogin(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=====Firm Management System=====");
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (authenticateUser(connection, username, password)) {
                System.out.println("Login successful!");
                return username;
            } else {
                System.out.println("Invalid username or password, please try again.");
            }
        }
    }

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

