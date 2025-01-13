package cinemaCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;


/**
 * Data Access Object (DAO) for Admin operations:
 * - Movies
 * - Sessions
 * - Tickets
 * - Products
 * - Cancellations
 */
public class AdminDAO {
	
	

    // ... (the existing methods) ...

    /**
     * Example method to get all purchases (tickets + products) for demonstration.
     * This uses a UNION to combine results: each row labeled with "Ticket" or "Product".
     * itemId = ID of ticket or product, name = short info, quantity, totalPrice, etc.
     */
    public static List<CancellationItem> getAllPurchases() {
        List<CancellationItem> list = new ArrayList<>();

        // 1) Tickets
        String queryTickets =
            "SELECT 'Ticket' AS Type, TicketID AS ItemId, CONCAT('Ticket #', TicketID) AS Name, 1 AS Qty, Price AS Total "
            + "FROM Tickets";

        // 2) Products (from Sales)
        // We assume each sale row is an item that can be canceled
        String queryProducts =
            "SELECT 'Product' AS Type, SaleID AS ItemId, p.Name AS Name, s.Quantity AS Qty, s.TotalPrice AS Total "
            + "FROM Sales s "
            + "JOIN Products p ON s.ProductID = p.ProductID";

        // Simple approach: run both queries separately, add results to the list
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmtT = conn.prepareStatement(queryTickets);
             PreparedStatement stmtP = conn.prepareStatement(queryProducts)) {

            // Tickets
            ResultSet rsT = stmtT.executeQuery();
            while (rsT.next()) {
                list.add(new CancellationItem(
                    rsT.getString("Type"),
                    rsT.getInt("ItemId"),
                    rsT.getString("Name"),
                    rsT.getInt("Qty"),
                    rsT.getDouble("Total")
                ));
            }
            rsT.close();

            // Products
            ResultSet rsP = stmtP.executeQuery();
            while (rsP.next()) {
                list.add(new CancellationItem(
                    rsP.getString("Type"),
                    rsP.getInt("ItemId"),
                    rsP.getString("Name"),
                    rsP.getInt("Qty"),
                    rsP.getDouble("Total")
                ));
            }
            rsP.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    


    public static boolean refundProductFromTicket(int saleId) {
        String sql = "DELETE FROM Sales WHERE SaleID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, saleId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ... rest of the AdminDAO methods (addMovie, movieExists, hasSoldTickets, etc.) ...


	
	
	
	

    // ==================== MOVIES ====================
    /**
     * Adds a new movie record.
     * @param title The movie title
     * @param genre The genre
     * @param summary Summary/description
     * @param posterPath Full path to the poster image
     * @return true if inserted successfully, false otherwise
     */
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

    /**
     * Checks if a movie with the same title already exists.
     * @param title Movie title
     * @return true if found
     */
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

    /**
     * Returns the MovieID for a given title, or -1 if not found.
     * @param title The movie title
     * @return The MovieID or -1
     */
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

    /**
     * Updates a specific field (Title, Genre, Summary, PosterPath) for a movie.
     * @param movieId The movie ID
     * @param fieldName The column name to update
     * @param newValue The new value for that column
     * @return true if update was successful
     */
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
    /**
     * Retrieves all sessions for a given movieId, including how many tickets sold for each.
     * @param movieId The movie ID
     * @return A list of SessionData objects
     */
    public static List<SessionData> getSessionsByMovie(int movieId) {
        List<SessionData> list = new ArrayList<>();
        String query =
            "SELECT s.SessionID, s.Hall, s.SessionDate, s.StartTime, s.EndTime, " +
            "       (SELECT COUNT(*) FROM Tickets t WHERE t.SessionID = s.SessionID) AS ticketsSold " +
            "FROM Sessions s " +
            "WHERE s.MovieID = ? " +
            "ORDER BY s.SessionDate, s.StartTime";
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
                int sold = rs.getInt("ticketsSold");

                SessionData data = new SessionData(sessionId, hall, date, start, end);
                data.setTicketsSold(sold);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Adds a new session. Returns false if there's a time overlap in the same hall.
     * @param movieId The movie ID
     * @param date Session date
     * @param hall Session hall
     * @param start Start time
     * @param end End time
     * @return true if successfully inserted; false if error or overlapping
     */
    public static boolean addSession(int movieId, LocalDate date, String hall, LocalTime start, LocalTime end) {
        // Check for overlap
        if (isSessionOverlapping(hall, date, start, end)) {
            return false;
        }

        String query = "INSERT INTO Sessions (MovieID, Hall, SessionDate, StartTime, EndTime, VacantSeats) VALUES (?, ?, ?, ?, ?, 0)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.setString(2, hall);
            stmt.setDate(3, java.sql.Date.valueOf(date));
            stmt.setTime(4, Time.valueOf(start));
            stmt.setTime(5, Time.valueOf(end));
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a new session (hall, date, start-end) overlaps with existing sessions.
     * @param hall The hall
     * @param date The date
     * @param start Start time
     * @param end End time
     * @return true if overlap is found
     */
    private static boolean isSessionOverlapping(String hall, LocalDate date, LocalTime start, LocalTime end) {
        String overlapQuery =
            "SELECT 1 FROM Sessions " +
            "WHERE Hall = ? AND SessionDate = ? " +
            "  AND ( (StartTime < ? AND EndTime > ?) " +
            "     OR (StartTime < ? AND EndTime > ?) " +
            "     OR (StartTime = ? AND EndTime = ?) ) " +
            "LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(overlapQuery)) {
            stmt.setString(1, hall);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            // Checking overlap: (StartTime < newEnd) AND (EndTime > newStart)
            stmt.setTime(3, Time.valueOf(end));
            stmt.setTime(4, Time.valueOf(start));
            stmt.setTime(5, Time.valueOf(end));
            stmt.setTime(6, Time.valueOf(start));
            stmt.setTime(7, Time.valueOf(start));
            stmt.setTime(8, Time.valueOf(end));

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true; 
        }
    }

    /**
     * Checks if tickets have been sold for a given sessionId.
     * @param sessionId The session ID
     * @return true if there's at least one ticket sold
     */
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

    /**
     * Updates all fields of a session (hall, date, start, end).
     * Checks for overlap with other sessions.
     * @param sessionId The session to update
     * @param date New date
     * @param hall New hall
     * @param start New start time
     * @param end New end time
     * @return true if successful and no overlap
     */
    public static boolean updateSessionAllFields(int sessionId, LocalDate date, String hall, LocalTime start, LocalTime end) {
        // Check overlap excluding this session itself
        if (isSessionOverlappingUpdate(sessionId, hall, date, start, end)) {
            return false;
        }

        String query = "UPDATE Sessions SET Hall=?, SessionDate=?, StartTime=?, EndTime=? WHERE SessionID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hall);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(start));
            stmt.setTime(4, Time.valueOf(end));
            stmt.setInt(5, sessionId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Overlap check for session updates, ignoring the current session ID.
     */
    private static boolean isSessionOverlappingUpdate(int sessionId, String hall, LocalDate date, LocalTime start, LocalTime end) {
        String overlapQuery =
            "SELECT 1 FROM Sessions " +
            "WHERE SessionID <> ? " +
            "  AND Hall = ? AND SessionDate = ? " +
            "  AND ( (StartTime < ? AND EndTime > ?) " +
            "     OR (StartTime < ? AND EndTime > ?) " +
            "     OR (StartTime = ? AND EndTime = ?) ) " +
            "LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(overlapQuery)) {
            stmt.setInt(1, sessionId);
            stmt.setString(2, hall);
            stmt.setDate(3, java.sql.Date.valueOf(date));
            stmt.setTime(4, Time.valueOf(end));
            stmt.setTime(5, Time.valueOf(start));
            stmt.setTime(6, Time.valueOf(end));
            stmt.setTime(7, Time.valueOf(start));
            stmt.setTime(8, Time.valueOf(start));
            stmt.setTime(9, Time.valueOf(end));

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Deletes a session record. 
     * @param sessionId The ID of the session
     * @return true if deleted successfully
     */
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

    // ==================== TICKETS & CANCELLATIONS ====================
    /**
     * Cancels a ticket by deleting it from the database (example logic).
     * @param ticketId The ticket ID
     * @return true if found and deleted
     */
    public static boolean cancelTicket(int ticketId) {
        // Check if ticket exists
        String check = "SELECT TicketID FROM Tickets WHERE TicketID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmtCheck = conn.prepareStatement(check)) {
            stmtCheck.setInt(1, ticketId);
            ResultSet rs = stmtCheck.executeQuery();
            if (!rs.next()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Delete from Tickets
        String del = "DELETE FROM Tickets WHERE TicketID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmtDel = conn.prepareStatement(del)) {
            stmtDel.setInt(1, ticketId);
            int affected = stmtDel.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves detailed info about a ticket: which movie, any purchased products, etc.
     * @param ticketId The ID of the ticket
     * @return A formatted string with all info, or null if not found
     */
    public static String getTicketFullInfo(int ticketId) {
        String queryTicket =
            "SELECT t.TicketID, s.SessionID, m.Title AS MovieTitle, t.Price, t.DiscountApplied " +
            "FROM Tickets t " +
            "JOIN Sessions s ON t.SessionID = s.SessionID " +
            "JOIN Movies m   ON s.MovieID   = m.MovieID " +
            "WHERE t.TicketID = ?";

        StringBuilder sb = new StringBuilder();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryTicket)) {
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String movieTitle = rs.getString("MovieTitle");
            double price = rs.getDouble("Price");
            boolean disc = rs.getBoolean("DiscountApplied");

            sb.append("TICKET ID: ").append(ticketId)
              .append("\nMovie: ").append(movieTitle)
              .append("\nPrice: ").append(price)
              .append(disc ? " (Discount Applied)" : "")
              .append("\n\n");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String querySales =
            "SELECT p.Name AS ProductName, s.Quantity, s.TotalPrice " +
            "FROM Sales s " +
            "JOIN Products p ON s.ProductID = p.ProductID " +
            "WHERE s.TicketID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt2 = conn.prepareStatement(querySales)) {
            stmt2.setInt(1, ticketId);
            ResultSet rs2 = stmt2.executeQuery();
            if (!rs2.isBeforeFirst()) {
                sb.append("No products purchased with this ticket.\n");
            } else {
                sb.append("Products Purchased:\n");
                while (rs2.next()) {
                    sb.append("- ")
                      .append(rs2.getString("ProductName"))
                      .append(" x")
                      .append(rs2.getInt("Quantity"))
                      .append(" => ")
                      .append(rs2.getDouble("TotalPrice"))
                      .append(" TL\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    /**
     * Cancels (refunds) a product by adding back to stock.
     * @param productId The product ID
     * @param quantity The quantity to refund
     * @return true if updated
     */
    public static boolean cancelProduct(int productId, int quantity) {
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
    /**
     * Retrieves all products from the database.
     * @return A list of Product objects
     */
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
     * Gets the ProductID by product name.
     * @param productName The product name
     * @return The ProductID or -1 if not found
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
