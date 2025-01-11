package CinemaCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Ticket {
    private int sessionID;
    private int seatId;
    private double price;
    private boolean ageDiscountApplied;
    private String firstName;
    private String lastName;
    private String seatNumber;
    private Session session;

    public Ticket(int sessionID, int seatId, double price) {
        this.sessionID = sessionID;
        this.seatId = seatId;
        this.price = price;
        this.ageDiscountApplied = false;
        this.session = loadSession(sessionID);
    }

    private Session loadSession(int sessionID) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT s.*, m.* " +
                "FROM Sessions s " +
                "JOIN Movies m ON s.MovieID = m.MovieID " +
                "WHERE s.SessionID = ?")) {
            
            stmt.setInt(1, sessionID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Session session = new Session(
                    rs.getInt("SessionID"),
                    rs.getString("Hall"),
                    rs.getString("SessionDate"),
                    rs.getString("StartTime"),
                    rs.getString("EndTime"),
                    rs.getInt("VacantSeats")
                );
                
                // Film bilgilerini yükle
                Movie movie = new Movie(
                    rs.getInt("MovieID"),
                    rs.getString("Title"),
                    rs.getString("Genre"),
                    rs.getString("Summary"),
                    rs.getString("PosterPath")
                );
                
                session.setMovie(movie);
                return session;
            }
        } catch (SQLException e) {
            System.out.println("Error loading session: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public int getSessionID() {
        return sessionID;
    }

    public int getSeatId() {
        return seatId;
    }

    public double getPrice() {
        return price;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean hasAgeDiscount() {
        return ageDiscountApplied;
    }

    public void applyAgeDiscount(String firstName, String lastName) {
        if (!ageDiscountApplied) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.price = price * 0.8; // %20 indirim
            this.ageDiscountApplied = true;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Session getSession() {
        return session;
    }
}
