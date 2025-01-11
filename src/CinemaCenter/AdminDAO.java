package CinemaCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // ==================== MOVIE ====================
    public static boolean addMovie(String title, String genre, String summary, String posterPath) {
        if (movieExists(title)) {
            return false;
        }
        String query = "INSERT INTO Movies (Title, Genre, Summary, PosterPath) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setString(3, summary);
            stmt.setString(4, posterPath);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean movieExists(String title) {
        String query = "SELECT 1 FROM Movies WHERE Title = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getMovieIdByTitle(String title) {
        String query = "SELECT MovieID FROM Movies WHERE Title = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("MovieID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean updateMovieField(int movieId, String fieldName, String newValue) {
        String query = "UPDATE Movies SET " + fieldName + " = ? WHERE MovieID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newValue);
            stmt.setInt(2, movieId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================== SESSIONS ====================
    public static List<SessionData> getSessionsByMovie(int movieId) {
        List<SessionData> list = new ArrayList<>();
        String query = "SELECT SessionID, Hall, SessionDate, StartTime, EndTime FROM Sessions WHERE MovieID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int sessionId = rs.getInt("SessionID");
                String hall = rs.getString("Hall");
                LocalDate date = rs.getDate("SessionDate").toLocalDate();
                LocalTime start = rs.getTime("StartTime").toLocalTime();
                LocalTime end = rs.getTime("EndTime").toLocalTime();
                list.add(new SessionData(sessionId, hall, date, start, end));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean addSession(int movieId, LocalDate date, String hall, LocalTime start, LocalTime end) {
        String query = "INSERT INTO Sessions (MovieID, Hall, SessionDate, StartTime, EndTime, VacantSeats) VALUES (?, ?, ?, ?, ?, 0)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.setString(2, hall);
            stmt.setDate(3, java.sql.Date.valueOf(date));
            stmt.setTime(4, java.sql.Time.valueOf(start));
            stmt.setTime(5, java.sql.Time.valueOf(end));
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasSoldTickets(int sessionId) {
        String query = "SELECT 1 FROM Tickets WHERE SessionID = ? LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateSessionAllFields(int sessionId, LocalDate date, String hall, LocalTime start, LocalTime end) {
        String query = "UPDATE Sessions SET Hall=?, SessionDate=?, StartTime=?, EndTime=? WHERE SessionID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hall);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setTime(3, java.sql.Time.valueOf(start));
            stmt.setTime(4, java.sql.Time.valueOf(end));
            stmt.setInt(5, sessionId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteSession(int sessionId) {
        String query = "DELETE FROM Sessions WHERE SessionID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sessionId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================== CANCELLATIONS ====================
    public static boolean cancelTicket(int ticketId) {
        // Örnek: bilet iptali
        // Burada gerçekte Ticket var mı yok mu, Sales var mı kontrol edebilirsiniz.
        // Şimdilik sadece "true" dönüp, iptal edildiğini varsayalım.
        return true;
    }

    public static boolean cancelProduct(int productId, int quantity) {
        // Ürün iadesi: direkt stok ekliyoruz (örnek)
        String query = "UPDATE Products SET StockQuantity = StockQuantity + ? WHERE ProductID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================== PRODUCTS ====================
    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT ProductID, Name, Price, StockQuantity, Category FROM Products";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int pid = rs.getInt("ProductID");
                String name = rs.getString("Name");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("StockQuantity");
                String cat = rs.getString("Category");
                list.add(new Product(pid, name, price, stock, cat));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Ürün adından ID döndüren basit metot
     */
    public static int getProductIdByName(String productName) {
        String query = "SELECT ProductID FROM Products WHERE Name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ProductID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
