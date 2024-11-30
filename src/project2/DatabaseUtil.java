package project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/firm_management?useTimezone=true&serverTimezone=UTC";
    private static final String DATABASE_USER = "root"; // MySQL kullanıcı adı
    private static final String DATABASE_PASSWORD = "123qwe"; // MySQL şifre

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }
}

