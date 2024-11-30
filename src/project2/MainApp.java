package project2;

import java.sql.Connection;
import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) {
        while (true) { // Logout sonrası tekrar giriş için döngü
            try (Connection connection = DatabaseUtil.getConnection()) {
                // Kullanıcı giriş işlemi
                String username = LoginScreen.promptLogin(connection);

                // Kullanıcının rolünü belirleme
                String role = LoginScreen.getUserRole(connection, username);

                // Rol bazlı yönlendirme
                if ("Manager".equalsIgnoreCase(role)) {
                    ManagerOperations.showManagerMenu(username);
                } else if ("Engineer".equalsIgnoreCase(role) || "Technician".equalsIgnoreCase(role) || "Intern".equalsIgnoreCase(role)) {
                    EmployeeOperations.showEmployeeMenu(username);
                } else {
                    System.out.println("Invalid role! Exiting application.");
                    break;
                }
            } catch (SQLException e) {
                System.out.println("Database connection error: " + e.getMessage());
                break;
            }
        }
    }
}

