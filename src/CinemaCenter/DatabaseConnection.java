package CinemaCenter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/CinemaCenter"; // Veritabanı adı
    private static final String USER = "root"; // MySQL kullanıcı adı
    private static final String PASSWORD = "123qwe"; // MySQL şifresi

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed.");
        }
    }
}
