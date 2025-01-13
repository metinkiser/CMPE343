package CinemaCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Represents a ticket for a movie session in the cinema system.
 * Manages ticket details including session information, seat assignment, pricing, and customer details.
 */
public class Ticket {
    private int sessionID;
    private int seatId;
    private double price;
    private boolean ageDiscountApplied;
    private String firstName;
    private String lastName;
    private String seatNumber;
    private Session session;

    /**
     * Constructs a new Ticket with the specified parameters.
     * @param sessionID The ID of the session this ticket is for
     * @param seatId The ID of the seat assigned to this ticket
     * @param price The base price of the ticket
     */
    public Ticket(int sessionID, int seatId, double price) {
        this.sessionID = sessionID;
        this.seatId = seatId;
        this.price = price;
        this.ageDiscountApplied = false;
        this.session = loadSession(sessionID);
    }

    /**
     * Loads the session details from the database for this ticket.
     * @param sessionID The ID of the session to load
     * @return The Session object containing session details
     */
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

    /**
     * @return The session ID associated with this ticket
     */
    public int getSessionID() {
        return sessionID;
    }

    /**
     * @return The seat ID assigned to this ticket
     */
    public int getSeatId() {
        return seatId;
    }

    /**
     * @return The current price of the ticket
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return The seat number assigned to this ticket
     */
    public String getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number for this ticket.
     * @param seatNumber The seat number to assign
     */
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * @return Whether an age discount has been applied to this ticket
     */
    public boolean hasAgeDiscount() {
        return ageDiscountApplied;
    }

    /**
     * Applies an age discount to the ticket price and records customer details.
     * The discount reduces the price by 20%.
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     */
    public void applyAgeDiscount(String firstName, String lastName) {
        if (!ageDiscountApplied) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.price = price * 0.8;
            this.ageDiscountApplied = true;
        }
    }

    /**
     * @return The first name of the ticket holder
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return The last name of the ticket holder
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return The Session object associated with this ticket
     */
    public Session getSession() {
        return session;
    }
}
